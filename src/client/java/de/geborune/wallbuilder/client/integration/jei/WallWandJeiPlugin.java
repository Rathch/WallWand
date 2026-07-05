package de.geborune.wallbuilder.client.integration.jei;

import de.geborune.wallbuilder.WallBuilderMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

@JeiPlugin
public class WallWandJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return WallBuilderMod.id("jei");
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null) {
			return;
		}

		List<RecipeHolder<CraftingRecipe>> recipes = minecraft.level.getRecipeManager()
			.getAllRecipesFor(RecipeType.CRAFTING)
			.stream()
			.filter(holder -> holder.id().getNamespace().equals(WallBuilderMod.MOD_ID))
			.toList();

		if (!recipes.isEmpty()) {
			registration.addRecipes(RecipeTypes.CRAFTING, recipes);
		}
	}
}
