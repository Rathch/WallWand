package de.geborune.wallbuilder.client.integration.jei;

import de.geborune.wallbuilder.util.WallWandRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.List;

@JeiPlugin
public class WallWandJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.fromNamespaceAndPath("wallwand", "jei");
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(RecipeTypes.CRAFTING, List.of(
			WallWandRecipes.createDiagonalRecipe(WallWandRecipes.STONE_ID, Items.STONE),
			WallWandRecipes.createDiagonalRecipe(WallWandRecipes.COBBLESTONE_ID, Items.COBBLESTONE)
		));
	}
}
