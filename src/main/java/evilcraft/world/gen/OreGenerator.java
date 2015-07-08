package evilcraft.world.gen;

import com.google.common.collect.Lists;
import evilcraft.Configs;
import evilcraft.EvilCraft;
import evilcraft.GeneralConfig;
import evilcraft.block.DarkOre;
import evilcraft.block.DarkOreConfig;
import evilcraft.block.NetherfishSpawnConfig;
import evilcraft.entity.monster.NetherfishConfig;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.init.Blocks;
import org.cyclops.cyclopscore.world.gen.SimpleMinableWorldGenerator;
import org.cyclops.cyclopscore.world.gen.WorldGenMinableExtended;

import java.util.List;

/**
 * Ore generator.
 * @author rubensworks
 *
 */
public class OreGenerator extends SimpleMinableWorldGenerator {
    
    /**
     * Make new instance.
     */
    public OreGenerator() {
		super(EvilCraft._instance, getOreGenerators());
    }
    
    private static List<WorldGenMinableExtended> getOreGenerators() {
    	List<WorldGenMinableExtended> oreGenerators = Lists.newLinkedList();
        if(Configs.isEnabled(DarkOreConfig.class) && DarkOreConfig.blocksPerVein > 0 && DarkOreConfig.veinsPerChunk > 0) {
        	oreGenerators.add(new WorldGenMinableExtended(DarkOre.getInstance().getDefaultState(), DarkOreConfig.blocksPerVein,
        			DarkOreConfig.veinsPerChunk, DarkOreConfig.startY, DarkOreConfig.endY, Blocks.stone));
		}
		if(GeneralConfig.extraSilverfish && GeneralConfig.silverfish_BlocksPerVein > 0 && GeneralConfig.silverfish_VeinsPerChunk > 0) {
			oreGenerators.add(new WorldGenMinableExtended(
                    Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE),
                    GeneralConfig.silverfish_BlocksPerVein, GeneralConfig.silverfish_VeinsPerChunk,
                    GeneralConfig.silverfish_StartY, GeneralConfig.silverfish_EndY,
					Blocks.stone));
		}
        if(NetherfishSpawnConfig.veinsPerChunk > 0 && Configs.isEnabled(NetherfishSpawnConfig.class) && Configs.isEnabled(NetherfishConfig.class)) {
        	oreGenerators.add(new NetherfishSpawnGenerator());
        }
        return oreGenerators;
    }
}
