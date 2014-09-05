package evilcraft.proxy;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.core.BucketHandler;
import evilcraft.event.BonemealEventHook;
import evilcraft.event.ConfigChangedEventHook;
import evilcraft.event.EntityStruckByLightningEventHook;
import evilcraft.event.ItemCraftedEventHook;
import evilcraft.event.LivingAttackEventHook;
import evilcraft.event.LivingDeathEventHook;
import evilcraft.event.PlayerInteractEventHook;
import evilcraft.event.PlayerRingOfFire;
import evilcraft.network.PacketHandler;
import evilcraft.network.packet.DetectionListenerPacket;
import evilcraft.network.packet.FartPacket;
import evilcraft.network.packet.RingOfFirePacket;
import evilcraft.network.packet.SoundPacket;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy {
	
	protected static final String DEFAULT_RESOURCELOCATION_MOD = "minecraft";
    
    /**
     * Register renderers.
     */
    public void registerRenderers() {
        // Nothing here as the server doesn't render graphics!
    }
    
    /**
     * Register key bindings.
     */
    public void registerKeyBindings() {
    }
 
    /**
     * Register packet handlers.
     */
    public void registerPacketHandlers() {
    	PacketHandler.init();
    	
    	// Register packets.
    	PacketHandler.register(FartPacket.class);
    	PacketHandler.register(RingOfFirePacket.class);
    	PacketHandler.register(DetectionListenerPacket.class);
    	PacketHandler.register(SoundPacket.class);
    	
        EvilCraft.log("Registered packet handler.");
    }
    
    /**
     * Register tick handlers.
     */
    public void registerTickHandlers() {
        
    }
    
    /**
     * Register the event hooks
     */
    public void registerEventHooks() {
    	MinecraftForge.EVENT_BUS.register(BucketHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(new LivingDeathEventHook());
        MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHook());
        MinecraftForge.EVENT_BUS.register(new LivingAttackEventHook());
        MinecraftForge.EVENT_BUS.register(new BonemealEventHook());
        MinecraftForge.EVENT_BUS.register(new EntityStruckByLightningEventHook());
        
        FMLCommonHandler.instance().bus().register(new ConfigChangedEventHook());
        FMLCommonHandler.instance().bus().register(new PlayerRingOfFire());
        FMLCommonHandler.instance().bus().register(new ItemCraftedEventHook());
    }
    
    /**
     * Play a minecraft sound, will do nothing serverside, use {@link CommonProxy#sendSound(double,
     * double, double, String, float, float, String)} for this.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void playSoundMinecraft(double x, double y, double z, String sound, float volume, float frequency) {
    	playSound(x, y, z, sound, volume, frequency, DEFAULT_RESOURCELOCATION_MOD);
    }
    
    /**
     * Play a sound, will do nothing serverside, use {@link CommonProxy#sendSound(double,
     * double, double, String, float, float, String)} for this.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     * @param mod The mod that has this sound.
     */
    public void playSound(double x, double y, double z, String sound, float volume, float frequency,
    		String mod) {
    	// No implementation server-side.
    }
    
    /**
     * Play an evilcraft sound, will do nothing serverside, use {@link CommonProxy#sendSound(double,
     * double, double, String, float, float, String)} for this.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void playSound(double x, double y, double z, String sound, float volume, float frequency) {
    	playSound(x, y, z, sound, volume, frequency, Reference.MOD_ID);
    }
    
    /**
     * Send a minecraft sound packet.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void sendSoundMinecraft(double x, double y, double z, String sound, float volume, float frequency) {
		sendSound(x, y, z, sound, volume, frequency, DEFAULT_RESOURCELOCATION_MOD);
    }
    
    /**
     * Send a sound packet.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     * @param mod The mod id that has this sound.
     */
    public void sendSound(double x, double y, double z, String sound, float volume, float frequency,
    		String mod) {
    	SoundPacket packet = new SoundPacket(x, y, z, sound, volume, frequency, mod);
		PacketHandler.sendToServer(packet);
    }
    
    /**
     * Send an evilcraft sound packet.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void sendSound(double x, double y, double z, String sound, float volume, float frequency) {
    	sendSound(x, y, z, sound, volume, frequency, Reference.MOD_ID);
    }
}