package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for the Dark Power Gem.
 * @author rubensworks
 *
 */
public class DarkPowerGemConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static DarkPowerGemConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkPowerGemConfig() {
        super(
                EvilCraft._instance,
        	true,
            "dark_power_gem",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return(ConfigurableItem) new ConfigurableItem(this).setMaxStackSize(16);
    }

    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_GEMDARKPOWER;
    }
    
}
