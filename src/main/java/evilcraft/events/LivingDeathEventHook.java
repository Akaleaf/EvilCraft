package evilcraft.events;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.api.HotbarIterator;
import evilcraft.blocks.BloodStainedBlock;
import evilcraft.entities.monster.VengeanceSpirit;
import evilcraft.fluids.Blood;
import evilcraft.items.BloodExtractor;
import evilcraft.render.particle.EntityBloodSplashFX;

/**
 * Event for {@link LivingDeathEvent}.
 * @author rubensworks
 *
 */
public class LivingDeathEventHook {

    /**
     * When a living death event is received.
     * @param event The received event.
     */
	@SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingDeath(LivingDeathEvent event) {
        bloodObtainEvent(event);
        bloodStainedBlockEvent(event);
        vengeanceEvent(event);
    }

	private void bloodObtainEvent(LivingDeathEvent event) {
        Entity e = event.source.getEntity();
        if(e != null && e instanceof EntityPlayerMP && !e.worldObj.isRemote && event.entityLiving != null) {
            EntityPlayerMP player = (EntityPlayerMP) e;
           
            int health = MathHelper.floor_float(event.entityLiving.getMaxHealth());
            int toFill = health * 10 + (new Random()).nextInt(health * 90);
            
            HotbarIterator it = new HotbarIterator(player);
            while(it.hasNext() && toFill > 0) {
                ItemStack itemStack = it.next();
                if(itemStack != null && itemStack.getItem() == BloodExtractor.getInstance()) {
                    ItemFluidContainer container = (ItemFluidContainer) itemStack.getItem();
                    toFill -= container.fill(itemStack, new FluidStack(Blood.getInstance(), toFill), true);
                }
            }
        }
    }
    
    private void bloodStainedBlockEvent(LivingDeathEvent event) {
        if(event.source.damageType == DamageSource.fall.damageType) {
            int x = MathHelper.floor_double(event.entity.posX);
            int y = MathHelper.floor_double(event.entity.posY - event.entity.getYOffset() - 1);
            int z = MathHelper.floor_double(event.entity.posZ);
            Block block = event.entity.worldObj.getBlock(x, y, z);
            int meta = BloodStainedBlock.getInstance().getMetadataFromBlock(block);
            if(meta > -1) {
                if (!event.entity.worldObj.isRemote) {
                    // Transform block into blood stained version
                    event.entity.worldObj.setBlock(x, y, z, BloodStainedBlock.getInstance());
                    event.entity.worldObj.setBlockMetadataWithNotify(x, y, z, meta, 2);
                } else {
                    // Init particles
                    Random random = new Random();
                    EntityBloodSplashFX.spawnParticles(event.entity.worldObj, x, y + 1, z, ((int)event.entityLiving.getMaxHealth()) + random.nextInt(15), 5 + random.nextInt(5));
                }
            }
        }
    }
    
	private void vengeanceEvent(LivingDeathEvent event) {
		if(event.entityLiving != null) {
			World world = event.entityLiving.worldObj;
			if(!world.isRemote && VengeanceSpirit.canSustain(event.entityLiving)) {
				VengeanceSpirit spirit = new VengeanceSpirit(world);
				spirit.setInnerEntity(event.entityLiving);
				spirit.copyLocationAndAnglesFrom(event.entityLiving);
				spirit.onSpawnWithEgg((IEntityLivingData)null);
				world.spawnEntityInWorld(spirit);
			}
		}
	}
    
}
