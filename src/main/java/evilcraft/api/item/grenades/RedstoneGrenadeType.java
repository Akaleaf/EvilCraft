package evilcraft.api.item.grenades;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import evilcraft.Configs;
import evilcraft.blocks.InvisibleRedstoneBlock;
import evilcraft.blocks.InvisibleRedstoneBlockConfig;
import evilcraft.entities.item.EntityGrenade;

/**
 * Implements functionality for a redstone grenade; When it hits a block
 * it sends out a redstone pulse.
 * @author immortaleeb
 *
 */
public class RedstoneGrenadeType extends AbstractGrenadeType {

    private static RedstoneGrenadeType _instance = null;

    /**
     * @return Returns the unique instance of this class.
     */
    public static RedstoneGrenadeType getInstance() {
        if (_instance == null)
            _instance = new RedstoneGrenadeType();

        return _instance;
    }

    private RedstoneGrenadeType() { }

    /**
     * Maps a side number to the offset of the X, Y and Z coordinates where the
     * redstone block will be spawned when the redstone grenade hits the ground
     * Bottom = 0, Top = 1, East = 2, West = 3, North = 4, South = 5.
     */
    private static final int[] sideXOffsets = { 0, 0,  0, 0, -1, 1};
    private static final int[] sideYOffsets = {-1, 1,  0, 0,  0, 0};
    private static final int[] sideZOffsets = { 0, 0, -1, 1,  0, 0};

    @Override
    public boolean onImpact(World world, EntityLivingBase thrower, EntityGrenade grenade,
                            MovingObjectPosition pos, Random random) {

        int dx = sideXOffsets[pos.sideHit];
        int dy = sideYOffsets[pos.sideHit];
        int dz = sideZOffsets[pos.sideHit];

        if (world.isAirBlock(pos.blockX + dx, pos.blockY + dy, pos.blockZ + dz)) {
            if(Configs.isEnabled(InvisibleRedstoneBlockConfig.class)) {
                world.setBlock(
                        pos.blockX + dx,
                        pos.blockY + dy,
                        pos.blockZ + dz,
                        InvisibleRedstoneBlock.getInstance());
            }

            if (world.isRemote) {
                double x = dx + ((dx >= 0) ? 0.5 : 0.9) + ((dx == 1) ? -0.5 : 0);
                double y = dy + ((dy >= 0) ? 0.5 : 0.9) + ((dy == 1) ? -0.5 : 0);
                double z = dz + ((dz >= 0) ? 0.5 : 0.9) + ((dz == 1) ? -0.5 : 0);

                world.spawnParticle("reddust",
                        pos.blockX + x,
                        pos.blockY + y,
                        pos.blockZ + z, 1, 0, 0);
            }
        }

        return false;
    }

    @Override
    public String getName() {
        return "redstoneGrenade";
    }

    @Override
    public int getId() {
        return 2;
    }

	@Override
	public void addInformation(List list) {
		list.add(EnumChatFormatting.RED + "Sends redstone pulse to the side");
		list.add(EnumChatFormatting.RED + "of a block it hits.");
	}
}
