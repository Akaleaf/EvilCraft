package evilcraft.item;

import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.core.helper.RenderHelpers;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * A bowl of promises.
 * @author rubensworks
 *
 */
public class BowlOfPromises extends ConfigurableItem {

    private static BowlOfPromises _instance = null;
    public static final int ACTIVE_META = 2;

    private TextureAtlasSprite dusted;
    private TextureAtlasSprite empty;
    private TextureAtlasSprite active_overlay;
    private TextureAtlasSprite active;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BowlOfPromises getInstance() {
        return _instance;
    }

    public BowlOfPromises(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        if(itemStack.getItemDamage() >= ACTIVE_META) {
            return new ItemStack(this, 1, 1);
        }
        return super.getContainerItem(itemStack);
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return itemStack.getItemDamage() >= ACTIVE_META;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return itemStack.getItemDamage() >= ACTIVE_META;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
        if(itemStack.getItemDamage() > 1 && renderPass == 0) {
            float division = (((float) ((((BowlOfPromisesConfig) eConfig).getTiers() -
                    (itemStack.getItemDamage() - 2)) - 1) / 3) + 1);
            int channel = (int) (255 / division);
            return RenderHelpers.RGBToInt(channel, channel, channel);
        }
        return super.getColorFromItemStack(itemStack, renderPass);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for(int i = 0; i < ACTIVE_META + ((BowlOfPromisesConfig) eConfig).getTiers(); i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        String suffix = "active";
        if(itemStack.getItemDamage() == 0) suffix = "dusted";
        if(itemStack.getItemDamage() == 1) suffix = "empty";
        return super.getUnlocalizedName(itemStack) + "." + suffix;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        if(itemStack.getItemDamage() >= ACTIVE_META) {
            int tier = itemStack.getItemDamage() - ACTIVE_META;
            list.add(L10NHelpers.localize(super.getUnlocalizedName(itemStack) + ".strength") + " " +
                    (tier == 0 ? 0 : L10NHelpers.localize("enchantment.level." + tier)));
        }
    }

}
