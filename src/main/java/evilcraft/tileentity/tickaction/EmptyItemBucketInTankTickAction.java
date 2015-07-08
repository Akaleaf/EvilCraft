package evilcraft.tileentity.tickaction;

import org.cyclops.cyclopscore.fluid.SingleUseTank;
import evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import evilcraft.core.tileentity.tickaction.ITickAction;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * {@link ITickAction} that can empty buckets in tanks.
 * @author rubensworks
 *
 * @param <T> Extension of {@link TickingTankInventoryTileEntity} that has a tank.
 */
public class EmptyItemBucketInTankTickAction<T extends TickingTankInventoryTileEntity<T>> extends EmptyInTankTickAction<T> {

    @Override
    public void onTick(T tile, ItemStack itemStack, int slot, int tick) {
        if(tick >= getRequiredTicks(tile, slot)) {
            ItemStack infuseStack = itemStack;
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(infuseStack);
            SingleUseTank tank = tile.getTank();
            if(fluidStack != null && tank.canTankAccept(fluidStack.getFluid())
            		&& tank.canCompletelyFill(fluidStack)) {
                tank.fill(fluidStack, true);
                tile.getInventory().setInventorySlotContents(slot, FluidContainerRegistry.EMPTY_BUCKET.copy());
            }
        }
    }
    
    @Override
    public boolean canTick(T tile, ItemStack itemStack, int slot, int tick) {
        ItemStack containerStack = tile.getInventory().getStackInSlot(slot);
        return super.canTick(tile, itemStack, slot, tick) && containerStack.getItem() != Items.bucket;
    }
    
    @Override
    public int getRequiredTicks(T tile, int slot) {
        return FluidContainerRegistry.BUCKET_VOLUME / MB_PER_TICK;
    }

}
