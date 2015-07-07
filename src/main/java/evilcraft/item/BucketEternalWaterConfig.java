package evilcraft.item;

import evilcraft.EvilCraft;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItemBucket;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemBucketConfig;

/**
 * Config for the Eternal Water Bucket.
 * @author rubensworks
 *
 */
public class BucketEternalWaterConfig extends ItemBucketConfig {

    /**
     * The unique instance.
     */
    public static BucketEternalWaterConfig _instance;

    /**
     * Make a new instance.
     */
    public BucketEternalWaterConfig() {
        super(
                EvilCraft._instance,
        	true,
            "bucketEternalWater",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        ConfigurableItemBucket bucket = new ConfigurableItemBucket(this, Blocks.flowing_water) {

            @Override
            public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
                MovingObjectPosition position = this.getMovingObjectPositionFromPlayer(world, player, true);
                if(position != null && position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    Block block = world.getBlockState(position.getBlockPos()).getBlock();
                    if(block == Blocks.water) {
                        world.setBlockToAir(position.getBlockPos());
                        return itemStack;
                    }
                }

                ItemStack result = super.onItemRightClick(itemStack, world, player);
                if(result != null && result.getItem() == Items.bucket) return new ItemStack(getContainerItem());

                return result;
            }

            @Override
            public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
                                          float hitX, float hitY, float hitZ) {
                TileEntity tile = world.getTileEntity(pos);
                if(tile instanceof IFluidHandler) {
                    if(!world.isRemote) {
                        ((IFluidHandler) tile).fill(side, new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME), true);
                        return true;
                    }
                }
                return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ);
            }

        };
        bucket.setContainerItem(bucket);
        bucket.setMaxStackSize(64);
        return bucket;
    }

    @Override
    public Fluid getFluidInstance() {
        return FluidRegistry.WATER;
    }

    @Override
    public Block getFluidBlockInstance() {
        return Blocks.water;
    }
    
}
