package org.cyclops.evilcraft.core.recipe.xml;

import net.minecraft.item.crafting.Ingredient;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.xml.SuperRecipeTypeHandler;
import org.cyclops.cyclopscore.recipe.xml.XmlRecipeLoader;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Handler for blood infuser recipes.
 * @author rubensworks
 *
 */
public class EnvironmentalAccumulatorRecipeTypeHandler extends SuperRecipeTypeHandler<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> {

	@Override
	public String getCategoryId() {
		return Reference.MOD_ID + ":envir_acc_recipe";
	}

	@Override
	protected IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> handleRecipe(RecipeHandler recipeHandler, Element input, Element output, Element properties)
			throws XmlRecipeLoader.XmlRecipeException {
		Node inputItem = input.getElementsByTagName("item").item(0);
		String inputWeather = input.getElementsByTagName("weather").item(0).getTextContent();
		Node outputItem = output.getElementsByTagName("item").item(0);
		String outputWeather = output.getElementsByTagName("weather").item(0).getTextContent();
		
		int duration = -1;
		int cooldowntime = -1;
		double processingspeed = -1.0D;
		if(properties.getElementsByTagName("duration").getLength() > 0) {
			duration = Integer.parseInt(properties.getElementsByTagName("duration").item(0).getTextContent());
		}
		if(properties.getElementsByTagName("cooldowntime").getLength() > 0) {
			cooldowntime = Integer.parseInt(properties.getElementsByTagName("cooldowntime").item(0).getTextContent());
		}
		if(properties.getElementsByTagName("processingspeed").getLength() > 0) {
			processingspeed = Double.parseDouble(properties.getElementsByTagName("processingspeed").item(0).getTextContent());
		}

        Ingredient outputIngredient = getIngredient(recipeHandler, outputItem);
		return EnvironmentalAccumulator.getInstance().getRecipeRegistry().registerRecipe(
                new EnvironmentalAccumulatorRecipeComponent(
                        getIngredient(recipeHandler, inputItem),
                        getWeatherType(inputWeather)
                ),
                new EnvironmentalAccumulatorRecipeComponent(
						outputIngredient,
                        getWeatherType(outputWeather)
                ),
                new EnvironmentalAccumulatorRecipeProperties(duration, cooldowntime, processingspeed)
        );
	}
	
	private WeatherType getWeatherType(String type) throws XmlRecipeLoader.XmlRecipeException {
		WeatherType weather = WeatherType.valueOf(type);
		if(weather == null) {
			throw new XmlRecipeLoader.XmlRecipeException(String.format("Could not found the weather '%s'", type));
		}
		return weather;
	}

}
