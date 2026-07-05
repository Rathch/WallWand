package de.geborune.wallbuilder.network;

import de.geborune.wallbuilder.WallBuilderMod;
import de.geborune.wallbuilder.item.TowerShape;
import de.geborune.wallbuilder.item.WallBuildMode;
import de.geborune.wallbuilder.item.WallOrientation;
import de.geborune.wallbuilder.item.WallSettings;
import de.geborune.wallbuilder.item.WallBuilderItem;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class WallBuilderNetworking {
	public static final CustomPacketPayload.Type<UpdateSettingsPayload> UPDATE_SETTINGS_TYPE =
		new CustomPacketPayload.Type<>(WallBuilderMod.id("update_settings"));

	private WallBuilderNetworking() {
	}

	public static void register() {
		PayloadTypeRegistry.playC2S().register(UPDATE_SETTINGS_TYPE, UpdateSettingsPayload.STREAM_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(UPDATE_SETTINGS_TYPE, (payload, context) -> {
			context.server().execute(() -> {
				InteractionHand hand = InteractionHand.values()[payload.handOrdinal()];
				ItemStack stack = context.player().getItemInHand(hand);

				if (!(stack.getItem() instanceof WallBuilderItem)) {
					return;
				}

				WallSettings.setWidth(stack, payload.width());
				WallSettings.setHeight(stack, payload.height());
				WallSettings.setDiameter(stack, payload.diameter());
				WallSettings.setRandomMode(stack, payload.randomMode());
				WallSettings.setOrientation(stack, parseEnum(payload.orientation(), WallOrientation.PLAYER_RELATIVE, WallOrientation.class));
				WallSettings.setBuildMode(stack, parseEnum(payload.buildMode(), WallBuildMode.NORMAL, WallBuildMode.class));
				WallSettings.setTowerShape(stack, parseEnum(payload.towerShape(), TowerShape.ROUND, TowerShape.class));
				context.player().setItemInHand(hand, stack);
			});
		});
	}

	private static <T extends Enum<T>> T parseEnum(String value, T defaultValue, Class<T> type) {
		try {
			return Enum.valueOf(type, value);
		} catch (IllegalArgumentException | NullPointerException ignored) {
			return defaultValue;
		}
	}

	public record UpdateSettingsPayload(
		int width,
		int height,
		int diameter,
		String orientation,
		String buildMode,
		boolean randomMode,
		String towerShape,
		int handOrdinal
	) implements CustomPacketPayload {
		private record SettingsBody(int width, int height, int diameter, String orientation, String buildMode, boolean randomMode) {
		}

		private static final StreamCodec<RegistryFriendlyByteBuf, SettingsBody> BODY_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT,
			SettingsBody::width,
			ByteBufCodecs.VAR_INT,
			SettingsBody::height,
			ByteBufCodecs.VAR_INT,
			SettingsBody::diameter,
			ByteBufCodecs.STRING_UTF8,
			SettingsBody::orientation,
			ByteBufCodecs.STRING_UTF8,
			SettingsBody::buildMode,
			ByteBufCodecs.BOOL,
			SettingsBody::randomMode,
			SettingsBody::new
		);

		public static final StreamCodec<RegistryFriendlyByteBuf, UpdateSettingsPayload> STREAM_CODEC = StreamCodec.composite(
			BODY_CODEC,
			payload -> new SettingsBody(payload.width(), payload.height(), payload.diameter(), payload.orientation(), payload.buildMode(), payload.randomMode()),
			ByteBufCodecs.STRING_UTF8,
			UpdateSettingsPayload::towerShape,
			ByteBufCodecs.VAR_INT,
			UpdateSettingsPayload::handOrdinal,
			(body, towerShape, handOrdinal) -> new UpdateSettingsPayload(
				body.width(),
				body.height(),
				body.diameter(),
				body.orientation(),
				body.buildMode(),
				body.randomMode(),
				towerShape,
				handOrdinal
			)
		);

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return UPDATE_SETTINGS_TYPE;
		}
	}
}
