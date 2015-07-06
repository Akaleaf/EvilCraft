package evilcraft.item;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

/**
 * A dark stick.
 * @author rubensworks
 *
 */
public class DarkStick extends ConfigurableItem {
    
    private static DarkStick _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new DarkStick(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkStick getInstance() {
        return _instance;
    }

    private DarkStick(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
