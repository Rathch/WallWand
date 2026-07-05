package de.geborune.wallbuilder.client;

import de.geborune.wallbuilder.client.screen.WallBuilderScreen;
import de.geborune.wallbuilder.item.WallBuilderItem;
import de.geborune.wallbuilder.network.WallBuilderNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;

public class WallBuilderModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack stack = player.getItemInHand(hand);
			if (!(stack.getItem() instanceof WallBuilderItem) || !player.isShiftKeyDown()) {
				return InteractionResultHolder.pass(stack);
			}

			if (world.isClientSide()) {
				Minecraft minecraft = Minecraft.getInstance();
				ItemStack screenStack = stack.copy();
				minecraft.execute(() -> minecraft.setScreen(new WallBuilderScreen(screenStack, hand)));
			}

			return InteractionResultHolder.success(stack);
		});
	}
}
