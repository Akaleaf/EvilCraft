package evilcraft.block;

import evilcraft.fluid.Poison;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockFluidClassic;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * A blockState for the {@link Poison} fluid.
 * @author rubensworks
 *
 */
public class FluidBlockPoison extends ConfigurableBlockFluidClassic {

    private static FluidBlockPoison _instance = null;
    
    private static final int POISON_DURATION = 5;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static FluidBlockPoison getInstance() {
        return _instance;
    }

    public FluidBlockPoison(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Poison.getInstance(), Material.water);
        
        if (MinecraftHelpers.isClientSide())
            this.setParticleColor(0.0F, 1.0F, 0.0F);
        this.setTickRandomly(true);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, Entity entity) {
        if(entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, POISON_DURATION * 20, 1));
        }
        super.onEntityCollidedWithBlock(world, blockPos, entity);
    }

}
