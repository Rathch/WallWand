package de.geborune.wallbuilder.network;

import de.geborune.wallbuilder.WallBuilderMod;
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

				try {
					WallSettings.setOrientation(stack, WallOrientation.valueOf(payload.orientation()));
				} catch (IllegalArgumentException ignored) {
					WallSettings.setOrientation(stack, WallOrientation.PLAYER_RELATIVE);
				}

				context.player().setItemInHand(hand, stack);
			});
		});
	}

	public record UpdateSettingsPayload(int width, int height, String orientation, int handOrdinal) implements CustomPacketPayload {
		public static final StreamCodec<RegistryFriendlyByteBuf, UpdateSettingsPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT,
			UpdateSettingsPayload::width,
			ByteBufCodecs.VAR_INT,
			UpdateSettingsPayload::height,
			ByteBufCodecs.STRING_UTF8,
			UpdateSettingsPayload::orientation,
			ByteBufCodecs.VAR_INT,
			UpdateSettingsPayload::handOrdinal,
			UpdateSettingsPayload::new
		);

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return UPDATE_SETTINGS_TYPE;
		}
	}
}
