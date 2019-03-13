package org.cyclops.evilcraft.infobook;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.pageelement.SectionAppendix;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.block.BloodInfuser;
import org.cyclops.evilcraft.block.BloodInfuserConfig;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.block.EnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.IngredientFluidStackAndTierRecipeComponent;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.infobook.pageelement.BloodInfuserRecipeAppendix;
import org.cyclops.evilcraft.infobook.pageelement.BroomModifierRecipeAppendix;
import org.cyclops.evilcraft.infobook.pageelement.EnvironmentalAccumulatorRecipeAppendix;
import org.cyclops.evilcraft.item.BroomConfig;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Map;

/**
 * Infobook class for the Origins of Darkness.
 * @author rubensworks
 */
public class OriginsOfDarknessBook extends InfoBook {

    private static OriginsOfDarknessBook _instance = null;

    static {
        if(Configs.isEnabled(BloodInfuserConfig.class)) {
            InfoBookParser.registerFactory(Reference.MOD_ID + ":blood_infuser_recipe", new InfoBookParser.IAppendixFactory() {

                @Override
                public SectionAppendix create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException {
                    ItemStack itemStack = InfoBookParser.createStack(node, infoBook.getMod().getRecipeHandler());
                    List<IRecipe<IngredientFluidStackAndTierRecipeComponent, IngredientRecipeComponent, DurationXpRecipeProperties>>
                            recipes = BloodInfuser.getInstance().getRecipeRegistry().
                            findRecipesByOutput(new IngredientRecipeComponent(itemStack));
                    int index = InfoBookParser.getIndex(node);
                    if (index >= recipes.size())
                        throw new InfoBookParser.InvalidAppendixException("Could not find Blood Infuser recipe for " +
                                itemStack.getItem().getTranslationKey() + "with index " + index);
                    return new BloodInfuserRecipeAppendix(infoBook, recipes.get(index));
                }

            });
        } else {
            InfoBookParser.registerIgnoredFactory(Reference.MOD_ID + ":blood_infuser_recipe");
        }

        if(Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
            InfoBookParser.registerFactory(Reference.MOD_ID + ":envir_acc_recipe", new InfoBookParser.IAppendixFactory() {

                @Override
                public SectionAppendix create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException {
                    ItemStack itemStack = InfoBookParser.createStack(node, infoBook.getMod().getRecipeHandler());
                    List<IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>>
                            recipes = EnvironmentalAccumulator.getInstance().getRecipeRegistry().
                            findRecipesByOutput(new EnvironmentalAccumulatorRecipeComponent(itemStack, WeatherType.ANY));
                    int index = InfoBookParser.getIndex(node);
                    if (index >= recipes.size())
                        throw new InfoBookParser.InvalidAppendixException("Could not find Environmental Accumulator recipe for " +
                                itemStack.getItem().getTranslationKey() + "with index " + index);
                    return new EnvironmentalAccumulatorRecipeAppendix(infoBook, recipes.get(index));
                }

            });
        } else {
            InfoBookParser.registerIgnoredFactory(Reference.MOD_ID + ":envirAccRecipe");
        }

        if(Configs.isEnabled(BloodInfuserConfig.class)) {
            InfoBookParser.registerFactory(Reference.MOD_ID + ":blood_infuser_recipe", new InfoBookParser.IAppendixItemFactory<IngredientFluidStackAndTierRecipeComponent, IngredientRecipeComponent, DurationXpRecipeProperties>() {

                @Override
                public SectionAppendix create(IInfoBook infoBook, IRecipe<IngredientFluidStackAndTierRecipeComponent, IngredientRecipeComponent, DurationXpRecipeProperties> recipe) throws InfoBookParser.InvalidAppendixException {
                    return new BloodInfuserRecipeAppendix(infoBook, recipe);
                }

            });
        } else {
            InfoBookParser.registerIgnoredFactory(Reference.MOD_ID + ":bloodInfuserRecipe");
        }

        if(Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
            InfoBookParser.registerFactory(Reference.MOD_ID + ":envir_acc_recipe", new InfoBookParser.IAppendixItemFactory<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>() {

                @Override
                public SectionAppendix create(IInfoBook infoBook, IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) throws InfoBookParser.InvalidAppendixException {
                    return new EnvironmentalAccumulatorRecipeAppendix(infoBook, recipe);
                }

            });
        } else {
            InfoBookParser.registerIgnoredFactory(Reference.MOD_ID + ":envir_acc_recipe");
        }

        if(Configs.isEnabled(BroomConfig.class)) {
            InfoBookParser.registerFactory(Reference.MOD_ID + ":broom_modifier", new InfoBookParser.IAppendixFactory() {

                @Override
                public SectionAppendix create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException {
                    String id = node.getTextContent();
                    Map<ItemStack, Float> values = Maps.newHashMap();
                    BroomModifier finalModifier = null;
                    for (BroomModifier modifier : BroomModifiers.REGISTRY.getModifiers()) {
                        if (modifier.getId().toString().equals(id)) {
                            finalModifier = modifier;
                            values.putAll(BroomModifiers.REGISTRY.getItemsFromModifier(modifier));
                        }
                    }
                    if (finalModifier == null) {
                        throw new InfoBookParser.InvalidAppendixException("Could not find the broom modifier " + id);
                    }
                    if (values.isEmpty()) {
                        throw new InfoBookParser.InvalidAppendixException("The broom modifier " + id + " has no valid items");
                    }
                    return new BroomModifierRecipeAppendix(infoBook, finalModifier, values);
                }

            });
        } else {
            InfoBookParser.registerIgnoredFactory(Reference.MOD_ID + ":broom_modifier");
        }
    }

    private OriginsOfDarknessBook() {
        super(EvilCraft._instance, 2);
    }

    public static OriginsOfDarknessBook getInstance() {
        if(_instance == null) {
            _instance = new OriginsOfDarknessBook();
        }
        return _instance;
    }
}
