package org.cyclops.evilcraft.core.config.configurable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;

import java.util.List;

/**
 * Item pickaxe that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableItemArmor extends ItemArmor implements IConfigurable {

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    /**
     * Make a new bucket instance.
     * @param eConfig Config for this blockState.
     * @param material The material of the tool.
     * @param equipmentSlot The slot
     */
    @SuppressWarnings({ "rawtypes" })
    protected ConfigurableItemArmor(ExtendedConfig eConfig, ItemArmor.ArmorMaterial material, EntityEquipmentSlot equipmentSlot) {
        super(material, 0, equipmentSlot);
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }
    
    @SuppressWarnings("rawtypes")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        L10NHelpers.addOptionalInfo(list, getUnlocalizedName());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        ItemStack existingStack = playerIn.getItemStackFromSlot(armorType);
        if (existingStack.isEmpty()) {
            playerIn.setItemStackToSlot(armorType, itemStackIn.copy());
            itemStackIn.shrink(1);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
}
