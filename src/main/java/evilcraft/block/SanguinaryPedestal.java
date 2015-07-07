package evilcraft.block;

import evilcraft.core.IInformationProvider;
import evilcraft.core.block.IBlockRarityProvider;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.tileentity.TileSanguinaryPedestal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.List;

/**
 * Pedestal that can obtain blood from blood stained blocks and can optionally extract blood from mobs
 * when a blood extractor is inserted.
 * @author rubensworks
 *
 */
public class SanguinaryPedestal extends ConfigurableBlockContainer implements IInformationProvider, IBlockRarityProvider {

    @BlockProperty
    public static final PropertyInteger TIER = PropertyInteger.create("tier", 0, 1);

    private static SanguinaryPedestal _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SanguinaryPedestal getInstance() {
        return _instance;
    }

    public SanguinaryPedestal(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.iron, TileSanguinaryPedestal.class);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0.02F, 0F, 0.02F, 0.98F, 0.98F, 0.98F);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos blockPos) {
        setBlockBoundsForItemRender();
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos blockPos, IBlockState blockState, AxisAlignedBB area, List list, Entity entity) {
        setBlockBounds(0, 0, 0, 1, 1, 1);
        super.addCollisionBoxesToList(world, blockPos, blockState, area, list, entity);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for (int j = 0; j <= 1; ++j) {
            list.add(new ItemStack(item, 1, j));
        }
    }

    @Override
    public int damageDropped(IBlockState blockState) {
        return (Integer) blockState.getValue(TIER);
    }

    @Override
    public String getInfo(ItemStack itemStack) {
        if(itemStack.getItemDamage() == 1) {
            return EnumChatFormatting.GRAY + L10NHelpers.localize(this.getUnlocalizedName() + ".boost");
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack,
                                   EntityPlayer entityPlayer, List list, boolean par4) {

    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return itemStack.getItemDamage() == 1 ? EnumRarity.UNCOMMON : EnumRarity.COMMON;
    }
}
