package evilcraft;

import evilcraft.block.*;
import evilcraft.core.item.ItemBlockFluidContainer;
import evilcraft.core.recipe.ItemBlockFluidContainerCombinationRecipe;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import evilcraft.core.recipe.xml.BloodInfuserRecipeTypeHandler;
import evilcraft.core.recipe.xml.EnvironmentalAccumulatorRecipeTypeHandler;
import evilcraft.core.weather.WeatherType;
import evilcraft.enchantment.EnchantmentPoisonTip;
import evilcraft.enchantment.EnchantmentPoisonTipConfig;
import evilcraft.item.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.event.IRecipeOutputObserver;
import org.cyclops.cyclopscore.recipe.event.ObservableShapedRecipe;
import org.cyclops.cyclopscore.recipe.xml.IRecipeConditionHandler;
import org.cyclops.cyclopscore.recipe.xml.IRecipeTypeHandler;

import java.util.Map;
import java.util.Set;

/**
 * An extended recipe handler.
 * @author rubensworks
 */
public class ExtendedRecipeHandler extends RecipeHandler {

    public ExtendedRecipeHandler(ModBase mod, String... fileNames) {
        super(mod, fileNames);
    }

    @Override
    protected void registerHandlers(Map<String, IRecipeTypeHandler> recipeTypeHandlers, Map<String, IRecipeConditionHandler> recipeConditionHandlers) {
        super.registerHandlers(recipeTypeHandlers, recipeConditionHandlers);
        recipeTypeHandlers.put("evilcraft:bloodinfuser", new BloodInfuserRecipeTypeHandler());
        recipeTypeHandlers.put("evilcraft:environmentalaccumulator",
                new EnvironmentalAccumulatorRecipeTypeHandler());
    }

    @Override
    protected void registerRecipeSorters() {
        super.registerRecipeSorters();
        RecipeSorter.register(Reference.MOD_ID + "containercombination", ItemBlockFluidContainerCombinationRecipe.class,
                RecipeSorter.Category.SHAPELESS, "after:forge:shapedore");
    }

    @Override
    protected void loadPredefineds(Map<String, ItemStack> predefinedItems, Set<String> predefinedValues) {
        super.loadPredefineds(predefinedItems, predefinedValues);
        if(Configs.isEnabled(EnchantmentPoisonTipConfig.class)) {
            ItemStack poisonTipEnchant = new ItemStack(Items.enchanted_book);
            Enchantment enchant = EnchantmentPoisonTip.getInstance();
            Items.enchanted_book.addEnchantment(poisonTipEnchant, new EnchantmentData(enchant,
                    enchant.getMinLevel()));
            predefinedItems.put("evilcraft:enchanted_book_poisonTip", poisonTipEnchant);
        }

        if(Configs.isEnabled(WeatherContainerConfig.class)) {
            ItemStack lightningWeatherContainer = new ItemStack(WeatherContainer.getInstance(), 1,
                    WeatherContainer.WeatherContainerTypes.LIGHTNING.ordinal());
            predefinedItems.put("evilcraft:lightningWeatherContainer",
                    lightningWeatherContainer);
        }

        if(Configs.isEnabled(InvertedPotentiaConfig.class)) {
            ItemStack invertedPotentiaEmpowered = new ItemStack(InvertedPotentia.getInstance(),
                    1, InvertedPotentia.EMPOWERED_META);
            predefinedItems.put("evilcraft:invertedPotentiaEmpowered",
                    invertedPotentiaEmpowered);
        }

        if(Configs.isEnabled(VengeancePickaxeConfig.class)) {
            ItemStack vengeancePickaxeFortune = VengeancePickaxe.createCraftingResult();
            predefinedItems.put("evilcraft:vengeancePickaxeFortune",
                    vengeancePickaxeFortune);
        }

        if(Configs.isEnabled(BoxOfEternalClosureConfig.class)) {
            ItemStack boxOfEternalClosureFilled = new ItemStack(BoxOfEternalClosure.getInstance());
            BoxOfEternalClosure.setVengeanceSwarmContent(boxOfEternalClosureFilled);
            predefinedItems.put("evilcraft:boxOfEternalClosureFilled",
                    boxOfEternalClosureFilled);
        }

        if(Configs.isEnabled(InvertedPotentiaConfig.class)) {
            ItemStack empoweredInvertedPotentia = new ItemStack(InvertedPotentia.getInstance());
            InvertedPotentia.getInstance().empower(empoweredInvertedPotentia);
            predefinedItems.put("evilcraft:empoweredInvertedPotentia",
                    empoweredInvertedPotentia);
        }

        if(PotentiaSphereConfig.enderPearlRecipe) {
            predefinedValues.add("evilcraft:enderPearlRecipe");
        }

        if(Configs.isEnabled(VeinSwordConfig.class)) {
            ItemStack veinSwordLooting = VeinSword.createCraftingResult();
            predefinedItems.put("evilcraft:veinSwordLooting",
                    veinSwordLooting);
        }

        if(WeatherContainerConfig.shapelessRecipes) {
            predefinedValues.add("evilcraft:shapelessRecipes");
        }
    }

    @Override
    protected void registerCustomRecipes() {
        super.registerCustomRecipes();
        // Entangled Chalice unique id
        if(Configs.isEnabled(EntangledChaliceConfig.class)
                && Configs.isEnabled(DarkGemConfig.class)
                && Configs.isEnabled(CorruptedTearConfig.class)) {
            Item tear = CorruptedTearConfig._instance.getItemInstance();
            GameRegistry.addRecipe(new ObservableShapedRecipe(3, 3, new ItemStack[]{
                    new ItemStack(Items.gold_ingot), new ItemStack(tear), new ItemStack(Items.gold_ingot),
                    new ItemStack(DarkGem.getInstance()), new ItemStack(Items.gold_ingot), new ItemStack(DarkGem.getInstance()),
                    new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot)
            }, new ItemStack(Item.getItemFromBlock(EntangledChalice.getInstance()), 2), new IRecipeOutputObserver() {
                @Override
                public ItemStack getRecipeOutput(InventoryCrafting craftingGrid, ItemStack output) {
                    ItemStack newStack = output.copy();
                    EntangledChaliceItem item = (EntangledChaliceItem) Item.getItemFromBlock(EntangledChalice.getInstance());
                    item.setNextTankID(newStack);
                    return newStack;
                }
            }));

            GameRegistry.addRecipe(new ObservableShapedRecipe(3, 3, new ItemStack[]{
                    new ItemStack(Items.gold_ingot), new ItemStack(tear), new ItemStack(Items.gold_ingot),
                    new ItemStack(DarkGem.getInstance()), new ItemStack(Item.getItemFromBlock(EntangledChalice.getInstance()), 1, OreDictionary.WILDCARD_VALUE), new ItemStack(DarkGem.getInstance()),
                    new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot)
            }, new ItemStack(Item.getItemFromBlock(EntangledChalice.getInstance()), 2), new IRecipeOutputObserver() {
                @Override
                public ItemStack getRecipeOutput(InventoryCrafting craftingGrid, ItemStack output) {
                    ItemStack newStack = output.copy();
                    EntangledChaliceItem item = (EntangledChaliceItem) Item.getItemFromBlock(EntangledChalice.getInstance());
                    String tankID = item.getTankID(craftingGrid.getStackInSlot(4));
                    item.setTankID(newStack, tankID);
                    return newStack;
                }
            }));
        }

        // Dark tank upgrades
        if(Configs.isEnabled(DarkTankConfig.class)) {
            for(int i = 1; i < 9; i++) {
                ItemBlockFluidContainer tankItem = (ItemBlockFluidContainer) Item.getItemFromBlock(DarkTank.getInstance());
                GameRegistry.addRecipe(new ItemBlockFluidContainerCombinationRecipe(i, tankItem));
            }
        }

        if (Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
            ItemStack outputStack = null;
            String recipeName = null;

            // Add the different weather container recipes
            if (Configs.isEnabled(WeatherContainerConfig.class)) {
                ItemStack emptyContainer = WeatherContainer.createItemStack(WeatherContainer.WeatherContainerTypes.EMPTY, 1);
                WeatherType[] weatherInputs = {WeatherType.CLEAR, WeatherType.LIGHTNING, WeatherType.RAIN};
                WeatherType[] weatherOutputs = {WeatherType.RAIN, WeatherType.RAIN, WeatherType.CLEAR};

                for (int i=0; i < weatherInputs.length; ++i) {
                    recipeName = "WeatherContainer" + weatherInputs[i].getClass().getSimpleName();
                    outputStack = WeatherContainer.createItemStack(
                            WeatherContainer.WeatherContainerTypes.getWeatherContainerType(weatherInputs[i]), 1);

                    EnvironmentalAccumulator.getInstance().getRecipeRegistry().registerRecipe(
                            recipeName,
                            new EnvironmentalAccumulatorRecipeComponent(
                                    emptyContainer,
                                    weatherInputs[i]
                            ),
                            new EnvironmentalAccumulatorRecipeComponent(
                                    outputStack,
                                    weatherOutputs[i]
                            ),
                            new EnvironmentalAccumulatorRecipeProperties()
                    );
                }
            }
        }
    }

}