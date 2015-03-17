package evilcraft;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.modcompat.versionchecker.VersionCheckerModCompat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

/**
 * This will execute the version checker.
 * @author rubensworks
 *
 */
public class VersionStats {
	
	private static VersionStats VERSION_STATS = null;
	
	private static boolean CHECKED = false;
	
	/**
	 * Latest mod version ID.
	 */
	public String mod_version;
	/**
	 * Download URL of latest mod version.
	 */
	public String update_link;
	
	private VersionStats() {
		
	}

	/**
	 * Get the version of this mod.
	 * @return The latest version.
	 */
	public static String getVersion() {
		return L10NHelpers.localize("general.versionCurrent", Reference.MOD_VERSION, Reference.MOD_MC_VERSION);
	}
	
	/**
	 * Fetch the latest version. Make sure this method is only loaded once!
	 */
	public static synchronized void load() {
		new Thread(new Runnable() {
	
	        @Override
	        public void run() {
	        	VersionStats versionStats = getVersionStats();
	        	if(needsUpdate(versionStats)) {
	        		VersionCheckerModCompat.sendIMCOutdatedMessage(versionStats);
	        	}
			}
		}).start();
	}
	
	/**
	 * Check the latest version.
	 * @param event The tick event.
	 */
	public static synchronized void check(final PlayerTickEvent event) {
		if(!CHECKED) {
			CHECKED = true;
			new Thread(new Runnable() {
		
		        @Override
		        public void run() {
					EntityPlayer player = event.player;
					
					VersionStats versionStats = getVersionStats();
					if(GeneralConfig.versionChecker && needsUpdate(versionStats)) {
                        sendMessage(player, L10NHelpers.localize("general.versionUpdate", versionStats.mod_version, Reference.MOD_NAME, Reference.MOD_VERSION, versionStats.update_link));
					}
			}
		    
			}).start();
		}
	}
	
	private static boolean needsUpdate(VersionStats versionStats) {
		if(versionStats != null) {
			if(!Reference.MOD_VERSION.equals(versionStats.mod_version))
				return true;
		}
		return false;
	}
	
	private static void sendMessage(EntityPlayer player, String message) {
		player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + message));
	}
	
	private static VersionStats fetchVersionStats() {
		VersionStats versionStats = null;
		try {
			Gson gson = new Gson();
			String location = Reference.URL_VERSIONSTATS
					+ "?mc_version=" + Reference.MOD_MC_VERSION + "&mod_version=" + Reference.MOD_VERSION;
			versionStats = gson.fromJson(IOUtils.toString(new URL(location)), VersionStats.class);
		} catch (JsonSyntaxException e) {
		    EvilCraft.log("The version stats server returned an invalid answer.");
		} catch (IOException e) {
			EvilCraft.log("Can't connect to version stats server");
		}
		return versionStats;
	}
	
	private static synchronized VersionStats getVersionStats() {
		if(VERSION_STATS == null) {
			VERSION_STATS = fetchVersionStats();
		}
		return VERSION_STATS;
	}
	
}
