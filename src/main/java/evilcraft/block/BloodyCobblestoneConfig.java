package evilcraft.block;

import evilcraft.EvilCraft;
import evilcraft.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

/**
 * Config for the Bloody Cobblestone.
 * @author rubensworks
 *
 */
public class BloodyCobblestoneConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static BloodyCobblestoneConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodyCobblestoneConfig() {
        super(
            EvilCraft._instance,
        	true,
            "bloodyCobblestone",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        Block block = new ConfigurableBlock(this, Material.rock).setHardness(1.5F).setResistance(10.0F).
                setStepSound(Block.soundTypeStone);
        block.setHarvestLevel("pickaxe", 0);
        return (ConfigurableBlock) block;
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_BLOCKSTONE;
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
