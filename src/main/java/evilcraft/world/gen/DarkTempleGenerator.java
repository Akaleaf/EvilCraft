package evilcraft.world.gen;

import evilcraft.Configs;
import evilcraft.EvilCraft;
import evilcraft.GeneralConfig;
import evilcraft.block.EnvironmentalAccumulatorConfig;
import evilcraft.world.gen.structure.DarkTempleStructure;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * World generator for Dark Temples.
 * @author immortaleeb
 *
 */
public class DarkTempleGenerator implements IWorldGenerator {

	@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.getDimensionId() != 0 || !Configs.isEnabled(EnvironmentalAccumulatorConfig.class))
			return;

		// Add some randomness to spawning
		if (random.nextInt(GeneralConfig.darkTempleRarity) == 0) {
            BlockPos blockPos = new BlockPos(chunkX * 16 + random.nextInt(16), 0, chunkZ * 16 + random.nextInt(16));

			// Check if there is no dark temple in the neighbourhood
			if (!EvilCraft.darkTempleData.isStructureInRange(blockPos, GeneralConfig.darkTempleMinDistance)) {
				// Generate the dark temple if possible (height checks are performed inside generate)
			    DarkTempleStructure.getInstance().generate(world, random, blockPos);
			}
		}
    }
}
