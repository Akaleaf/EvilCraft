package org.cyclops.evilcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.tileentity.TilePurifier;

import java.util.List;

/**
 * Block that can remove bad enchants from items.
 * @author rubensworks
 *
 */
public class Purifier extends ConfigurableBlockContainer {

    @BlockProperty
    public static final PropertyInteger FILL = PropertyInteger.create("fill", 0, 3);

    private static Purifier _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static Purifier getInstance() {
        return _instance;
    }

    public Purifier(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.IRON, TilePurifier.class);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, EnumFacing side, float motionX, float motionY, float motionZ) {
        if(world.isRemote) {
            return true;
        } else {
            ItemStack itemStack = player.inventory.getCurrentItem();
            TilePurifier tile = (TilePurifier) world.getTileEntity(blockPos);
            if(tile != null) {
                IFluidHandler itemFluidHandler = FluidUtil.getFluidHandler(itemStack);
                SingleUseTank tank = tile.getTank();
                if (itemStack.isEmpty() && !tile.getPurifyItem().isEmpty()) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getPurifyItem());
                    tile.setPurifyItem(ItemStack.EMPTY);
                } else if (itemStack.isEmpty() && !tile.getAdditionalItem().isEmpty()) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getAdditionalItem());
                    tile.setAdditionalItem(ItemStack.EMPTY);
                } else if (itemFluidHandler != null && !tank.isFull()
                        && FluidUtil.tryEmptyContainer(itemStack, tank, Integer.MAX_VALUE, player, false).isSuccess()) {
                    ItemStack newItemStack = FluidUtil.tryEmptyContainer(itemStack, tank, Integer.MAX_VALUE, player, true).getResult();
                    InventoryHelpers.tryReAddToStack(player, itemStack, newItemStack);
                    tile.sendUpdate();
                    return true;
                } else if (itemFluidHandler != null && !tank.isEmpty() &&
                        FluidUtil.tryFillContainer(itemStack, tank, Integer.MAX_VALUE, player, false).isSuccess()) {
                    ItemStack newItemStack = FluidUtil.tryFillContainer(itemStack, tank, Integer.MAX_VALUE, player, true).getResult();
                    InventoryHelpers.tryReAddToStack(player, itemStack, newItemStack);
                    return true;
                }  else if(!itemStack.isEmpty() && tile.getActions().isItemValidForAdditionalSlot(itemStack) && tile.getAdditionalItem().isEmpty()) {
                    ItemStack copy = itemStack.copy();
                    copy.setCount(1);
                    tile.setAdditionalItem(copy);
                    itemStack.shrink(1);
                    return true;
                } else if(!itemStack.isEmpty() && tile.getActions().isItemValidForMainSlot(itemStack) && tile.getPurifyItem().isEmpty()) {
                    ItemStack copy = itemStack.copy();
                    copy.setCount(1);
                    tile.setPurifyItem(copy);
                    itemStack.shrink(1);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return super.collisionRayTrace(blockState, worldIn, pos, start, end);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos blockPos, AxisAlignedBB area, List<AxisAlignedBB> collisionBoxes, Entity entity, boolean useProvidedState) {
        float f = 0.125F;
        BlockHelpers.addCollisionBoxToList(blockPos, area, collisionBoxes, new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F));
        BlockHelpers.addCollisionBoxToList(blockPos, area, collisionBoxes, new AxisAlignedBB(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F));
        BlockHelpers.addCollisionBoxToList(blockPos, area, collisionBoxes, new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f));
        BlockHelpers.addCollisionBoxToList(blockPos, area, collisionBoxes, new AxisAlignedBB(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F));
        BlockHelpers.addCollisionBoxToList(blockPos, area, collisionBoxes, new AxisAlignedBB(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState blockState) {
        return false;
    }
    
    @Override
    public boolean hasComparatorInputOverride(IBlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getValue(FILL);
    }

}
