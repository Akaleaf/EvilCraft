package evilcraft.item;

import evilcraft.EvilCraft;
import evilcraft.block.FluidBlockPoison;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockFluidClassic;
import org.cyclops.cyclopscore.config.configurable.ConfigurableFluid;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItemBucket;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import evilcraft.fluid.Poison;
import org.cyclops.cyclopscore.config.extendedconfig.ItemBucketConfig;

/**
 * Config for the Poison Bucket.
 * @author rubensworks
 *
 */
public class BucketPoisonConfig extends ItemBucketConfig {
    
    /**
     * The unique instance.
     */
    public static BucketPoisonConfig _instance;

    /**
     * Make a new instance.
     */
    public BucketPoisonConfig() {
        super(
                EvilCraft._instance,
        	true,
            "bucketPoison",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItemBucket(this, FluidBlockPoison.getInstance());
    }

    @Override
    public ConfigurableFluid getFluidInstance() {
        return Poison.getInstance();
    }

    @Override
    public ConfigurableBlockFluidClassic getFluidBlockInstance() {
        return FluidBlockPoison.getInstance();
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
