package de.geborune.wallbuilder.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.geborune.wallbuilder.WallBuilderMod;
import de.geborune.wallbuilder.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.Map;

public final class WallWandRecipes {
	public static final ResourceLocation STONE_ID = WallBuilderMod.id("wall_wand_from_stone");
	public static final ResourceLocation COBBLESTONE_ID = WallBuilderMod.id("wall_wand_from_cobblestone");

	private WallWandRecipes() {
	}

	public static JsonObject createStoneRecipeJson() {
		return createRecipeJson("minecraft:stone");
	}

	public static JsonObject createCobblestoneRecipeJson() {
		return createRecipeJson("minecraft:cobblestone");
	}

	private static JsonObject createRecipeJson(String stoneItem) {
		JsonObject stone = new JsonObject();
		stone.addProperty("item", stoneItem);

		JsonObject stick = new JsonObject();
		stick.addProperty("item", "minecraft:stick");

		JsonObject key = new JsonObject();
		key.add("S", stone);
		key.add("C", stick);

		JsonArray pattern = new JsonArray();
		pattern.add("S  ");
		pattern.add(" C ");
		pattern.add("  S");

		JsonObject result = new JsonObject();
		result.addProperty("id", "wallwand:wall_wand");
		result.addProperty("count", 1);

		JsonObject recipe = new JsonObject();
		recipe.addProperty("type", "minecraft:crafting_shaped");
		recipe.addProperty("category", "equipment");
		recipe.add("pattern", pattern);
		recipe.add("key", key);
		recipe.add("result", result);
		return recipe;
	}

	public static RecipeHolder<CraftingRecipe> createDiagonalRecipe(ResourceLocation id, Item stone) {
		ShapedRecipePattern pattern = ShapedRecipePattern.of(
			Map.of(
				'S', Ingredient.of(stone),
				'C', Ingredient.of(Items.STICK)
			),
			"S  ",
			" C ",
			"  S"
		);

		ShapedRecipe recipe = new ShapedRecipe(
			"wallwand",
			CraftingBookCategory.EQUIPMENT,
			pattern,
			new ItemStack(ModItems.WALL_BUILDER),
			true
		);

		return new RecipeHolder<>(id, recipe);
	}
}
