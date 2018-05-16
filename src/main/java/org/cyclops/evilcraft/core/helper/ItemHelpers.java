package org.cyclops.evilcraft.core.helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.fluid.Blood;

/**
 * Helpers for items.
 * @author rubensworks
 *
 */
public class ItemHelpers {

	private static final int MB_FILL_PERTICK = GeneralConfig.mbFlowRate;
    private static ItemStack bloodBucket = null;
	
	/**
     * Check if the given item is activated.
     * @param itemStack The item to check
     * @return If it is an active container.
     */
    public static boolean isActivated(ItemStack itemStack) {
        return !itemStack.isEmpty() && itemStack.getTagCompound() != null && itemStack.getTagCompound().getBoolean("enabled");
    }
    
    /**
     * Toggle activation for the given item.
     * @param itemStack The item to toggle.
     */
    public static void toggleActivation(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if(tag == null) {
            tag = new NBTTagCompound();
            itemStack.setTagCompound(tag);
        }
        tag.setBoolean("enabled", !isActivated(itemStack));
    }
    
    /**
     * Get the integer value of the given ItemStack.
     * @param itemStack The item to check.
     * @param tag The tag in NBT for storing this value.
     * @return The integer value for the given tag.
     */
    public static int getNBTInt(ItemStack itemStack, String tag) {
        if(itemStack.isEmpty() || itemStack.getTagCompound() == null) {
            return 0;
        }
        return itemStack.getTagCompound().getInteger(tag);
    }
    
    /**
     * Set the integer value of the given ItemStack for the given tag.
     * @param itemStack The item to change.
     * @param integer The new integer value.
     * @param tag The tag in NBT for storing this value.
     */
    public static void setNBTInt(ItemStack itemStack, int integer, String tag) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if(tagCompound == null) {
            tagCompound = new NBTTagCompound();
            itemStack.setTagCompound(tagCompound);
        }
        tagCompound.setInteger(tag, integer);
    }
    
    /**
     * Run an auto-fill tick for filling currently held container items from this item.
     * @param toDrain The item handler to drain from.
     * @param world The world.
     * @param entity The entity that holds this item.
     * @param fillBuckets If buckets should be filled.
     */
    public static void updateAutoFill(IFluidHandlerItem toDrain, World world, Entity entity, boolean fillBuckets) {
    	if(entity instanceof EntityPlayer && !world.isRemote) {
            FluidStack tickFluid = toDrain.drain(Integer.MAX_VALUE, false);
            if(tickFluid != null && tickFluid.amount > 0) {
                EntityPlayer player = (EntityPlayer) entity;
                for (EnumHand hand : EnumHand.values()) {
                    ItemStack held = player.getHeldItem(hand);
                    if (!held.isEmpty() && (fillBuckets || held.getItem() != Items.BUCKET)) {
                        ItemStack toFill = held.splitStack(1);
                        ItemStack filled = tryFillContainerForPlayer(toDrain, toFill, tickFluid, player);
                        if (!filled.isEmpty()) {
                            if (player.getHeldItem(hand).isEmpty()) {
                                player.setHeldItem(hand, filled);
                            } else {
                                player.addItemStackToInventory(filled);
                            }
                        } else {
                            held.grow(1);
                        }
                    }
                }
            }
        }
    }

    /**
     * Tries to fill a container item in a player inventory.
     * @param toDrain The item handler to drain from.
     * @param toFill The container to try to fill.
     * @param tickFluid The fluid to fill with.
     * @param player The player that is the owner of toFill.
     * @return The filled container
     */
    public static ItemStack tryFillContainerForPlayer(IFluidHandlerItem toDrain, ItemStack toFill, FluidStack tickFluid, EntityPlayer player) {
        int maxFill = MB_FILL_PERTICK;
        if (toFill.getItem() == Items.BUCKET) {
            maxFill = Fluid.BUCKET_VOLUME;
        }
        if(!toFill.isEmpty() && toFill != toDrain.getContainer() && FluidUtil.getFluidHandler(toFill) != null
                && player.getItemInUseCount() == 0 && FluidUtil.tryFillContainer(toFill, toDrain, Math.min(maxFill, tickFluid.amount), player, false).isSuccess()) {
            return FluidUtil.tryFillContainer(toFill, toDrain, Math.min(maxFill, tickFluid.amount), player, true).getResult();
        }
        return ItemStack.EMPTY;
    }

    /**
     * @return The filled blood bucket.
     */
    public static ItemStack getBloodBucket() {
        if (bloodBucket == null) {
            bloodBucket = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, Blood.getInstance());
        }
        return bloodBucket;
    }
	
}
