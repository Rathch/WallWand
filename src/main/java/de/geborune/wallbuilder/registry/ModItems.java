package de.geborune.wallbuilder.registry;

import de.geborune.wallbuilder.WallBuilderMod;
import de.geborune.wallbuilder.item.WallBuilderItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public final class ModItems {
	public static final Item WALL_BUILDER = new WallBuilderItem(new Item.Properties().stacksTo(1));

	private ModItems() {
	}

	public static void register() {
		Registry.register(BuiltInRegistries.ITEM, WallBuilderMod.id("wall_builder"), WALL_BUILDER);

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.accept(WALL_BUILDER));
	}
}
