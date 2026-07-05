package de.geborune.wallbuilder.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;

public final class WallSettings {
	public static final int MIN_SIZE = 1;
	public static final int MAX_SIZE = 32;
	public static final int DEFAULT_WIDTH = 5;
	public static final int DEFAULT_HEIGHT = 4;
	public static final int DEFAULT_DIAMETER = 5;

	private static final String WIDTH_KEY = "Width";
	private static final String HEIGHT_KEY = "Height";
	private static final String DIAMETER_KEY = "Diameter";
	private static final String ORIENTATION_KEY = "Orientation";
	private static final String MATERIAL_KEY = "Material";
	private static final String BUILD_MODE_KEY = "BuildMode";
	private static final String RANDOM_MODE_KEY = "RandomMode";
	private static final String TOWER_SHAPE_KEY = "TowerShape";

	private WallSettings() {
	}

	public static int getWidth(ItemStack stack) {
		return readInt(stack, WIDTH_KEY, DEFAULT_WIDTH);
	}

	public static int getHeight(ItemStack stack) {
		return readInt(stack, HEIGHT_KEY, DEFAULT_HEIGHT);
	}

	public static int getDiameter(ItemStack stack) {
		return readInt(stack, DIAMETER_KEY, DEFAULT_DIAMETER);
	}

	public static WallOrientation getOrientation(ItemStack stack) {
		return readEnum(stack, ORIENTATION_KEY, WallOrientation.PLAYER_RELATIVE, WallOrientation.class);
	}

	public static WallBuildMode getBuildMode(ItemStack stack) {
		return readEnum(stack, BUILD_MODE_KEY, WallBuildMode.NORMAL, WallBuildMode.class);
	}

	public static TowerShape getTowerShape(ItemStack stack) {
		return readEnum(stack, TOWER_SHAPE_KEY, TowerShape.ROUND, TowerShape.class);
	}

	public static boolean isRandomMode(ItemStack stack) {
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		if (customData == null) {
			return false;
		}

		return customData.copyTag().getBoolean(RANDOM_MODE_KEY);
	}

	public static void setWidth(ItemStack stack, int width) {
		writeInt(stack, WIDTH_KEY, clamp(width));
	}

	public static void setHeight(ItemStack stack, int height) {
		writeInt(stack, HEIGHT_KEY, clamp(height));
	}

	public static void setDiameter(ItemStack stack, int diameter) {
		writeInt(stack, DIAMETER_KEY, clamp(diameter));
	}

	public static void setOrientation(ItemStack stack, WallOrientation orientation) {
		writeString(stack, ORIENTATION_KEY, orientation.name());
	}

	public static void setBuildMode(ItemStack stack, WallBuildMode buildMode) {
		writeString(stack, BUILD_MODE_KEY, buildMode.name());
	}

	public static void setTowerShape(ItemStack stack, TowerShape towerShape) {
		writeString(stack, TOWER_SHAPE_KEY, towerShape.name());
	}

	public static void setRandomMode(ItemStack stack, boolean randomMode) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(RANDOM_MODE_KEY, randomMode));
	}

	public static Block getMaterialBlock(ItemStack stack) {
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		if (customData == null) {
			return null;
		}

		CompoundTag tag = customData.copyTag();
		if (!tag.contains(MATERIAL_KEY)) {
			return null;
		}

		ResourceLocation id = ResourceLocation.tryParse(tag.getString(MATERIAL_KEY));
		if (id == null) {
			return null;
		}

		return BuiltInRegistries.BLOCK.get(id);
	}

	public static void setMaterial(ItemStack stack, Block block) {
		ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(MATERIAL_KEY, id.toString()));
	}

	public static boolean hasMaterial(ItemStack stack) {
		return getMaterialBlock(stack) != null;
	}

	private static <T extends Enum<T>> T readEnum(ItemStack stack, String key, T defaultValue, Class<T> type) {
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		if (customData == null) {
			return defaultValue;
		}

		CompoundTag tag = customData.copyTag();
		if (!tag.contains(key)) {
			return defaultValue;
		}

		try {
			return Enum.valueOf(type, tag.getString(key));
		} catch (IllegalArgumentException ignored) {
			return defaultValue;
		}
	}

	private static int readInt(ItemStack stack, String key, int defaultValue) {
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		if (customData == null) {
			return defaultValue;
		}

		CompoundTag tag = customData.copyTag();
		return tag.contains(key) ? clamp(tag.getInt(key)) : defaultValue;
	}

	private static void writeInt(ItemStack stack, String key, int value) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(key, value));
	}

	private static void writeString(ItemStack stack, String key, String value) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(key, value));
	}

	private static int clamp(int value) {
		return Mth.clamp(value, MIN_SIZE, MAX_SIZE);
	}
}
