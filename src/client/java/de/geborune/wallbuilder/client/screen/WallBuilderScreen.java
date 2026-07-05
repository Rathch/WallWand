package de.geborune.wallbuilder.client.screen;

import de.geborune.wallbuilder.item.WallOrientation;
import de.geborune.wallbuilder.item.WallSettings;
import de.geborune.wallbuilder.network.WallBuilderNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class WallBuilderScreen extends Screen {
	private final ItemStack wand;
	private final InteractionHand hand;

	private int widthValue;
	private int heightValue;
	private WallOrientation orientation;

	public WallBuilderScreen(ItemStack wand, InteractionHand hand) {
		super(Component.translatable("screen.wallbuilder.title"));
		this.wand = wand;
		this.hand = hand;
		this.widthValue = WallSettings.getWidth(wand);
		this.heightValue = WallSettings.getHeight(wand);
		this.orientation = WallSettings.getOrientation(wand);
	}

	@Override
	protected void init() {
		int centerX = this.width / 2;
		int centerY = this.height / 2;

		addRenderableWidget(Button.builder(Component.literal("-"), button -> changeWidth(-1))
			.bounds(centerX - 110, centerY - 40, 20, 20)
			.build());
		addRenderableWidget(Button.builder(Component.literal("+"), button -> changeWidth(1))
			.bounds(centerX + 90, centerY - 40, 20, 20)
			.build());

		addRenderableWidget(Button.builder(Component.literal("-"), button -> changeHeight(-1))
			.bounds(centerX - 110, centerY - 10, 20, 20)
			.build());
		addRenderableWidget(Button.builder(Component.literal("+"), button -> changeHeight(1))
			.bounds(centerX + 90, centerY - 10, 20, 20)
			.build());

		addRenderableWidget(Button.builder(Component.translatable("screen.wallbuilder.orientation"), button -> cycleOrientation())
			.bounds(centerX - 100, centerY + 25, 200, 20)
			.build());

		addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
			.bounds(centerX - 50, centerY + 60, 100, 20)
			.build());
	}

	@Override
	public void onClose() {
		saveSettings();
		super.onClose();
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
		int centerX = this.width / 2;
		int centerY = this.height / 2;

		graphics.drawCenteredString(this.font, this.title, centerX, centerY - 70, 0xFFFFFF);
		graphics.drawCenteredString(
			this.font,
			Component.translatable("screen.wallbuilder.width", widthValue),
			centerX,
			centerY - 35,
			0xFFFFFF
		);
		graphics.drawCenteredString(
			this.font,
			Component.translatable("screen.wallbuilder.height", heightValue),
			centerX,
			centerY - 5,
			0xFFFFFF
		);
		graphics.drawCenteredString(
			this.font,
			Component.translatable(
				"screen.wallbuilder.orientation_value",
				Component.translatable("item.wallbuilder.wall_builder.orientation." + orientation.name().toLowerCase())
			),
			centerX,
			centerY + 50,
			0xA0A0A0
		);
	}

	private void changeWidth(int delta) {
		widthValue = Math.max(WallSettings.MIN_SIZE, Math.min(WallSettings.MAX_SIZE, widthValue + delta));
	}

	private void changeHeight(int delta) {
		heightValue = Math.max(WallSettings.MIN_SIZE, Math.min(WallSettings.MAX_SIZE, heightValue + delta));
	}

	private void cycleOrientation() {
		orientation = orientation.next();
	}

	private void saveSettings() {
		WallSettings.setWidth(wand, widthValue);
		WallSettings.setHeight(wand, heightValue);
		WallSettings.setOrientation(wand, orientation);

		if (minecraft != null && minecraft.player != null) {
			minecraft.player.setItemInHand(hand, wand);
			ClientPlayNetworking.send(new WallBuilderNetworking.UpdateSettingsPayload(
				widthValue,
				heightValue,
				orientation.name(),
				hand.ordinal()
			));
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
