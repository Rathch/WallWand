package de.geborune.wallbuilder.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class WallSettings {
	public static final int MIN_SIZE = 1;
	public static final int MAX_SIZE = 32;
	public static final int DEFAULT_WIDTH = 5;
	public static final int DEFAULT_HEIGHT = 4;

	private static final String WIDTH_KEY = "Width";
	private static final String HEIGHT_KEY = "Height";
	private static final String ORIENTATION_KEY = "Orientation";

	private WallSettings() {
	}

	public static int getWidth(ItemStack stack) {
		return readInt(stack, WIDTH_KEY, DEFAULT_WIDTH);
	}

	public static int getHeight(ItemStack stack) {
		return readInt(stack, HEIGHT_KEY, DEFAULT_HEIGHT);
	}

	public static WallOrientation getOrientation(ItemStack stack) {
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		if (customData == null) {
			return WallOrientation.PLAYER_RELATIVE;
		}

		CompoundTag tag = customData.copyTag();
		if (!tag.contains(ORIENTATION_KEY)) {
			return WallOrientation.PLAYER_RELATIVE;
		}

		try {
			return WallOrientation.valueOf(tag.getString(ORIENTATION_KEY));
		} catch (IllegalArgumentException ignored) {
			return WallOrientation.PLAYER_RELATIVE;
		}
	}

	public static void setWidth(ItemStack stack, int width) {
		writeInt(stack, WIDTH_KEY, clamp(width));
	}

	public static void setHeight(ItemStack stack, int height) {
		writeInt(stack, HEIGHT_KEY, clamp(height));
	}

	public static void setOrientation(ItemStack stack, WallOrientation orientation) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(ORIENTATION_KEY, orientation.name()));
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

	private static int clamp(int value) {
		return Mth.clamp(value, MIN_SIZE, MAX_SIZE);
	}
}
