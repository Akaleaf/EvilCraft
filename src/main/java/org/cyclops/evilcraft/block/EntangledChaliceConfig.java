package org.cyclops.evilcraft.block;

import net.minecraft.item.ItemBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntangledChalice}.
 * @author rubensworks
 *
 */
public class EntangledChaliceConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static EntangledChaliceConfig _instance;

    /**
     * Make a new instance.
     */
    public EntangledChaliceConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entangledChalice",
            null,
            EntangledChalice.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return EntangledChaliceItem.class;
    }
    
    @Override
    public void onRegistered() {
        if(MinecraftHelpers.isClientSide()) {
        	/*ResourceLocation textureGem = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "gem.png");
        	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "chalice.png");
        	ModelGem gem = new ModelGem(textureGem);
        	ModelChalice model = new ModelChalice(texture, gem);
        	*/
            // TODO
            //ClientProxy.TILE_ENTITY_RENDERERS.put(TileEntangledChalice.class,
            //		new RenderTileEntityEntangledChalice(model, texture));
            // TODO
            //ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(EntangledChalice.getInstance()),
            //		new RenderItemEntangledChalice(model, texture));
        }
    }
    
}
