package de.geborune.wallbuilder.client.integration.emi;

import de.geborune.wallbuilder.WallBuilderMod;
import de.geborune.wallbuilder.registry.ModItems;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class WallWandEmiPlugin implements EmiPlugin {
	@Override
	public void register(EmiRegistry registry) {
		registerDiagonalRecipe(registry, Items.STONE, WallBuilderMod.id("wall_wand_from_stone"));
		registerDiagonalRecipe(registry, Items.COBBLESTONE, WallBuilderMod.id("wall_wand_from_cobblestone"));
	}

	private void registerDiagonalRecipe(EmiRegistry registry, Item stone, ResourceLocation id) {
		registry.addRecipe(new EmiCraftingRecipe(
			List.of(
				EmiStack.of(stone),
				EmiStack.EMPTY,
				EmiStack.EMPTY,
				EmiStack.EMPTY,
				EmiStack.of(Items.STICK),
				EmiStack.EMPTY,
				EmiStack.EMPTY,
				EmiStack.EMPTY,
				EmiStack.of(stone)
			),
			EmiStack.of(ModItems.WALL_BUILDER),
			id,
			true
		));
	}
}
