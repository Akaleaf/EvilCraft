package evilcraft.block;

import evilcraft.Configs;
import evilcraft.EvilCraft;
import evilcraft.entity.monster.VengeanceSpiritConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.ItemBlockNBT;

/**
 * Config for the {@link BoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class BoxOfEternalClosureConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static BoxOfEternalClosureConfig _instance;

    /**
     * Make a new instance.
     */
    public BoxOfEternalClosureConfig() {
        super(
            EvilCraft._instance,
        	true,
            "boxOfEternalClosure",
            null,
            BoxOfEternalClosure.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }
    
    @Override
    public void onRegistered() {
        if (MinecraftHelpers.isClientSide()) {
        	/*ModelBase model = new ModelBoxOfEternalClosure();
        	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "box.png");
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileBoxOfEternalClosure.class,
            		new RenderTileEntityBoxOfEternalClosure(model, texture));*/
            // TODO
            //ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(BoxOfEternalClosure.getInstance()),
            //		new RenderItemBoxOfEternalClosure(model, texture));
        }
        
        ItemStack spiritStack = new ItemStack(Item.getItemFromBlock(BoxOfEternalClosure.getInstance()));
        BoxOfEternalClosure.setVengeanceSwarmContent(spiritStack);
        for(String chestCategory : MinecraftHelpers.CHESTGENCATEGORIES) {
            ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(
            		spiritStack, 1, 1, 4));
        }
    }
    
    @Override
    public boolean isHardDisabled() {
        // Hard dependency on vengeance spirits.
        return !Configs.isEnabled(VengeanceSpiritConfig.class);
    }

}
