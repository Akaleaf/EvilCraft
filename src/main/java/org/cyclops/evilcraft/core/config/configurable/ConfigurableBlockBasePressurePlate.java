package org.cyclops.evilcraft.core.config.configurable;

import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.BlockPropertyManagerComponent;
import org.cyclops.cyclopscore.block.property.IBlockPropertyManager;
import org.cyclops.cyclopscore.config.configurable.IConfigurableBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import javax.annotation.Nullable;

/**
 * {@link BlockBasePressurePlate} that can hold ExtendedConfigs.
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockBasePressurePlate extends BlockBasePressurePlate implements IConfigurableBlock {

    @Delegate private IBlockPropertyManager propertyManager;
    @Override protected BlockStateContainer createBlockState() {
        return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
    }

    @BlockProperty
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    @SuppressWarnings("rawtypes")
    protected BlockConfig eConfig = null;
    protected boolean hasGui = false;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockBasePressurePlate(ExtendedConfig<BlockConfig> eConfig, Material material) {
        super(material);
        this.setConfig((BlockConfig) eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
        setHardness(2F);
        setSoundType(SoundType.STONE);
    }

    @Override
    public boolean hasGui() {
        return hasGui;
    }

    @Nullable
    @Override
    public IBlockColor getBlockColorHandler() {
        return null;
    }

    @Override
    public void neighborChanged(IBlockState blockState, World world, BlockPos blockPos, Block block, BlockPos fromPos) {
        if(!canPlaceBlockAt(world, blockPos)) {
        	this.dropBlockAsItem(world, blockPos, world.getBlockState(blockPos), 0);
            world.setBlockToAir(blockPos);
        }
    }
    
    private void setConfig(@SuppressWarnings("rawtypes") BlockConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public BlockConfig getConfig() {
        return eConfig;
    }

}
