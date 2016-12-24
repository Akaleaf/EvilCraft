package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link PoisonBottle}.
 * @author rubensworks
 *
 */
public class PoisonBottleConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static PoisonBottleConfig _instance;

    /**
     * Make a new instance.
     */
    public PoisonBottleConfig() {
        super(
                EvilCraft._instance,
        	true,
            "poison_bottle",
            null,
            PoisonBottle.class
        );
    }
}
