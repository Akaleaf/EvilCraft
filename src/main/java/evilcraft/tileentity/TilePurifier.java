package evilcraft.tileentity;

import evilcraft.block.Purifier;
import evilcraft.block.PurifierConfig;
import evilcraft.client.particle.EntityBloodBubbleFX;
import evilcraft.client.particle.EntityMagicFinishFX;
import evilcraft.core.fluid.BloodFluidConverter;
import evilcraft.core.fluid.ImplicitFluidConversionTank;
import evilcraft.core.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import evilcraft.core.tileentity.TankInventoryTileEntity;
import evilcraft.fluid.Blood;
import evilcraft.item.BlookConfig;
import evilcraft.tileentity.tickaction.bloodchest.DamageableItemRepairAction;
import net.minecraft.client.particle.EntityEnchantmentTableParticleFX;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableEnchantment;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Tile for the {@link Purifier}..
 * @author rubensworks
 *
 */
public class TilePurifier extends TankInventoryTileEntity {
    
    private static final int PURIFY_DURATION = 60;
    
    /**
     * The amount of slots.
     */
    public static final int SLOTS = 2;
    /**
     * The purify item slot.
     */
    public static final int SLOT_PURIFY = 0;
    /**
     * The book slot.
     */
    public static final int SLOT_BOOK = 1;
    
    /**
     * Duration in ticks to show the 'poof' animation.
     */
    private static final int ANIMATION_FINISHED_DURATION = 2;
    
    @NBTPersist
    private Float randomRotation = 0F;
    
    private int tick = 0;
    
    /**
     * The allowed book instance.
     */
    public static final Item ALLOWED_BOOK = BlookConfig._instance.downCast().getItemInstance();
    
    /**
     * The fluid it uses.
     */
    public static final Fluid FLUID = Blood.getInstance();
    
    private static final int MAX_BUCKETS = 3;
    
    /**
     * Book bounce tick count.
     */
    @NBTPersist
    public Integer tickCount = 0;
    /**
     * The next book rotation.
     */
    @NBTPersist
    public Float bookRotation2 = 0F;
    /**
     * The previous book rotation.
     */
    @NBTPersist
    public Float bookRotationPrev = 0F;
    /**
     * The book rotation.
     */
    @NBTPersist
    public Float bookRotation = 0F;
    
    @NBTPersist
    private Integer finishedAnimation = 0;
    
    /**
     * Make a new instance.
     */
    public TilePurifier() {
        super(SLOTS, PurifierConfig._instance.getNamedId(), 1, FluidContainerRegistry.BUCKET_VOLUME * MAX_BUCKETS, PurifierConfig._instance.getNamedId() + "tank", FLUID);
        
        List<Integer> slots = new LinkedList<Integer>();
        slots.add(SLOT_BOOK);
        slots.add(SLOT_PURIFY);
        for(EnumFacing direction : DirectionHelpers.DIRECTIONS)
            addSlotsToSide(direction, slots);
        
        this.setSendUpdateOnInventoryChanged(true);
        this.setSendUpdateOnTankChanged(true);
    }
    
    @Override
    protected SingleUseTank newTank(String tankName, int tankSize) {
    	return new ImplicitFluidConversionTank(tankName, tankSize, this, BloodFluidConverter.getInstance());
    }
    
    @Override
    public void updateTileEntity() {
    	super.updateTileEntity();
    	
        int buckets = getBucketsFloored();
        if(getPurifyItem() != null && buckets > 0) {
            tick++;
            boolean done = false;
            
            // Try removing bad enchants.
            for(ConfigurableEnchantment enchant : DamageableItemRepairAction.BAD_ENCHANTS) {
                if(!done) {
                    int enchantmentListID = EnchantmentHelpers.doesEnchantApply(getPurifyItem(), enchant.effectId);
                    if(enchantmentListID > -1) {
                        if(tick >= PURIFY_DURATION) {
                            if(!worldObj.isRemote) {
                                int level = EnchantmentHelpers.getEnchantmentLevel(getPurifyItem(), enchantmentListID);
                                EnchantmentHelpers.setEnchantmentLevel(getPurifyItem(), enchantmentListID, level - 1);
                            }
                            setBuckets(buckets - 1, getBucketsRest());
                            finishedAnimation = ANIMATION_FINISHED_DURATION;
                        }
                        if(worldObj.isRemote)
                            showEffect();
                        done = true;
                    }
                }
            }
            
            // If no bad enchants were found/removed, try disenchanting.
            if(!done && buckets == getMaxBuckets()
                    && getBookItem() != null && getBookItem().getItem() == ALLOWED_BOOK) {
                NBTTagList enchantmentList = getPurifyItem().getEnchantmentTagList();
                if(enchantmentList != null && enchantmentList.tagCount() > 0) {
                    if(tick >= PURIFY_DURATION) {
                        if(!worldObj.isRemote) {
                            // Init enchantment data.
                            int enchantmentListID = worldObj.rand.nextInt(enchantmentList.tagCount());
                            int level = EnchantmentHelpers.getEnchantmentLevel(getPurifyItem(), enchantmentListID);
                            int id = EnchantmentHelpers.getEnchantmentID(getPurifyItem(), enchantmentListID);
                            ItemStack enchantedItem = new ItemStack(Items.enchanted_book, 1);
                            
                            // Set the enchantment book.
                            Map<Integer, Integer> enchantments = new HashMap<Integer, Integer>();
                            enchantments.put(id, level);
                            EnchantmentHelper.setEnchantments(enchantments, enchantedItem);
                            
                            // Define the enchanted book level.
                            EnchantmentHelpers.setEnchantmentLevel(getPurifyItem(), enchantmentListID, 0);
                            
                            // Put the enchanted book in the book slot.
                            setBookItem(enchantedItem);
                        }
                        finishedAnimation = ANIMATION_FINISHED_DURATION;
                        setBuckets(0, getBucketsRest());
                    }
                    if(worldObj.isRemote) {
                        showEffect();
                        showEnchantingEffect();
                    }
                    done = true;
                }
            }
        } else {
            tick = 0;
        }
        
        // Animation tick/display.
        if(finishedAnimation > 0) {
            finishedAnimation--;
            if(worldObj.isRemote) {
                showEnchantedEffect();
            }
        }
        
        updateBook();
        
        if(tick >= PURIFY_DURATION)
            tick = 0;
    }
    
    /**
     * Get the amount of contained buckets.
     * @return The amount of buckets.
     */
    public int getBucketsFloored() {
        return (int) Math.floor(getTank().getFluidAmount() / (double) FluidContainerRegistry.BUCKET_VOLUME);
    }
    
    /**
     * Get the rest of the fluid that can not fit in a bucket.
     * Use this in {@link TilePurifier#setBuckets(int, int)} as rest.
     * @return The rest of the fluid.
     */
    public int getBucketsRest() {
        return getTank().getFluidAmount() % FluidContainerRegistry.BUCKET_VOLUME;
    }
    
    /**
     * Set the amount of contained buckets. This will also change the inner tank.
     * @param buckets The amount of buckets.
     * @param rest The rest of the fluid.
     */
    public void setBuckets(int buckets, int rest) {
        getTank().setFluid(new FluidStack(FLUID, FluidContainerRegistry.BUCKET_VOLUME * buckets + rest));
        sendUpdate();
    }
    
    /**
     * Set the maximum amount of contained buckets.
     * @return The maximum amount of buckets.
     */
    public int getMaxBuckets() {
        return MAX_BUCKETS;
    }
    
    @Override
    protected void onSendUpdate() {
        super.onSendUpdate();
        worldObj.setBlockState(getPos(), Purifier.getInstance().getDefaultState().withProperty(Purifier.FILL, getBucketsFloored()), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
    }
    
    private void updateBook() {
        this.bookRotationPrev = this.bookRotation2;
        
        this.bookRotation += 0.02F;
        
        while (this.bookRotation2 >= (float)Math.PI) {
            this.bookRotation2 -= ((float)Math.PI * 2F);
        }

        while (this.bookRotation2 < -(float)Math.PI) {
            this.bookRotation2 += ((float)Math.PI * 2F);
        }

        while (this.bookRotation >= (float)Math.PI) {
            this.bookRotation -= ((float)Math.PI * 2F);
        }

        while (this.bookRotation < -(float)Math.PI) {
            this.bookRotation += ((float)Math.PI * 2F);
        }

        float baseNextRotation;

        for (baseNextRotation = this.bookRotation - this.bookRotation2; baseNextRotation >= (float)Math.PI; baseNextRotation -= ((float)Math.PI * 2F)) { }

        while (baseNextRotation < -(float)Math.PI) {
            baseNextRotation += ((float)Math.PI * 2F);
        }

        this.bookRotation2 += baseNextRotation * 0.4F;

        ++this.tickCount;
    }
    
    /**
     * Get the purify item.
     * @return The purify item.
     */
    public ItemStack getPurifyItem() {
        return getStackInSlot(SLOT_PURIFY);
    }
    
    /**
     * Set the purify item.
     * @param itemStack The purify item.
     */
    public void setPurifyItem(ItemStack itemStack) {
        this.randomRotation = worldObj.rand.nextFloat() * 360;
        setInventorySlotContents(SLOT_PURIFY, itemStack);
    }
    
    /**
     * Get the book item.
     * @return The book item.
     */
    public ItemStack getBookItem() {
        return getStackInSlot(SLOT_BOOK);
    }
    
    /**
     * Set the book item.
     * @param itemStack The book item.
     */
    public void setBookItem(ItemStack itemStack) {
        setInventorySlotContents(SLOT_BOOK, itemStack);
    }
    
    @SideOnly(Side.CLIENT)
    private void showEffect() {
        for (int i=0; i < 1; i++) {                
            double particleX = getPos().getX() + 0.2 + worldObj.rand.nextDouble() * 0.6;
            double particleY = getPos().getY() + 0.2 + worldObj.rand.nextDouble() * 0.6;
            double particleZ = getPos().getZ() + 0.2 + worldObj.rand.nextDouble() * 0.6;

            float particleMotionX = -0.01F + worldObj.rand.nextFloat() * 0.02F;
            float particleMotionY = 0.01F;
            float particleMotionZ = -0.01F + worldObj.rand.nextFloat() * 0.02F;

            FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                    new EntityBloodBubbleFX(worldObj, particleX, particleY, particleZ,
                            particleMotionX, particleMotionY, particleMotionZ)
                    );
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void showEnchantingEffect() {
        if(worldObj.rand.nextInt(10) == 0) {
            for (int i=0; i < 1; i++) {                
                double particleX = getPos().getX() + 0.45 + worldObj.rand.nextDouble() * 0.1;
                double particleY = getPos().getY() + 1.45 + worldObj.rand.nextDouble() * 0.1;
                double particleZ = getPos().getZ() + 0.45 + worldObj.rand.nextDouble() * 0.1;
                
                float particleMotionX = -0.4F + worldObj.rand.nextFloat() * 0.8F;
                float particleMotionY = -worldObj.rand.nextFloat();
                float particleMotionZ = -0.4F + worldObj.rand.nextFloat() * 0.8F;
    
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                        new EntityEnchantmentTableParticleFX.EnchantmentTable().getEntityFX(0, worldObj, particleX, particleY, particleZ,
                                particleMotionX, particleMotionY, particleMotionZ)
                        );
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void showEnchantedEffect() {
        for (int i=0; i < 100; i++) {                
            double particleX = getPos().getX() + 0.45 + worldObj.rand.nextDouble() * 0.1;
            double particleY = getPos().getY() + 1.45 + worldObj.rand.nextDouble() * 0.1;
            double particleZ = getPos().getZ() + 0.45 + worldObj.rand.nextDouble() * 0.1;
            
            float particleMotionX = -0.4F + worldObj.rand.nextFloat() * 0.8F;
            float particleMotionY = -0.4F + worldObj.rand.nextFloat() * 0.8F;
            float particleMotionZ = -0.4F + worldObj.rand.nextFloat() * 0.8F;

            FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                    new EntityMagicFinishFX(worldObj, particleX, particleY, particleZ,
                            particleMotionX, particleMotionY, particleMotionZ)
                    );
        }
    }

    /**
     * Get the random rotation for displaying the item.
     * @return The random rotation.
     */
    public float getRandomRotation() {
        return randomRotation;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        if(i == 0) {
            return itemStack.stackSize == 1;
        } else if(i == 1) {
            return itemStack.stackSize == 1 && itemStack.getItem() == ALLOWED_BOOK;
        }
        return false;
    }

}
