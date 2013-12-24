package evilcraft.api.config.elementtypeaction;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import evilcraft.EvilCraftTab;
import evilcraft.api.config.ConfigurableBlockContainer;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

public class BlockAction extends IElementTypeAction {

    @Override
    public void run(ExtendedConfig eConfig, Configuration config) {
        // Get property in config file and set comment
        Property property = config.getBlock(CATEGORIES.get(eConfig.getHolderType()), eConfig.NAME, eConfig.ID);
        property.comment = eConfig.COMMENT;
        
        // Update the ID, it could've changed
        eConfig.ID = property.getInt();
        
        // Save the config inside the correct element
        eConfig.save();
        
        Block block = (Block) eConfig.getSubInstance();
        
        // Register
        GameRegistry.registerBlock(
                block,
                eConfig.getSubUniqueName()
        );
        
        // Set creative tab
        block.setCreativeTab(EvilCraftTab.getInstance());
        
        // Add I18N
        LanguageRegistry.addName(eConfig.getSubInstance(), eConfig.NAME);
        
        // Also register tile entity
        if(eConfig.getHolderType().equals(ElementType.BLOCKCONTAINER)) {
            ConfigurableBlockContainer container = (ConfigurableBlockContainer) block;
            GameRegistry.registerTileEntity(container.getTileEntity(), eConfig.getSubUniqueName());
        }
    }

}
