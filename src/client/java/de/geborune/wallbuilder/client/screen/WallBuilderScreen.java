package de.geborune.wallbuilder.client.screen;

import de.geborune.wallbuilder.item.TowerShape;
import de.geborune.wallbuilder.item.WallBuildMode;
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

	private WallBuildMode buildMode;
	private boolean randomMode;
	private int widthValue;
	private int heightValue;
	private int diameterValue;
	private WallOrientation orientation;
	private TowerShape towerShape;

	public WallBuilderScreen(ItemStack wand, InteractionHand hand) {
		super(Component.translatable("screen.wallwand.title"));
		this.wand = wand;
		this.hand = hand;
		this.buildMode = WallSettings.getBuildMode(wand);
		this.randomMode = WallSettings.isRandomMode(wand);
		this.widthValue = WallSettings.getWidth(wand);
		this.heightValue = WallSettings.getHeight(wand);
		this.diameterValue = WallSettings.getDiameter(wand);
		this.orientation = WallSettings.getOrientation(wand);
		this.towerShape = WallSettings.getTowerShape(wand);
	}

	@Override
	protected void init() {
		int centerX = this.width / 2;
		int startY = this.height / 2 - 95;

		addRenderableWidget(Button.builder(Component.translatable("screen.wallwand.mode"), button -> {
			buildMode = buildMode.next();
			rebuildScreen();
		}).bounds(centerX - 100, startY, 200, 20).build());

		addRenderableWidget(Button.builder(
			Component.translatable(randomMode ? "screen.wallwand.random.on" : "screen.wallwand.random.off"),
			button -> {
				randomMode = !randomMode;
				rebuildScreen();
			}
		).bounds(centerX - 100, startY + 25, 200, 20).build());

		if (buildMode == WallBuildMode.TOWER) {
			addSizeControls(centerX, startY + 55, diameterValue, delta -> {
				diameterValue = clamp(diameterValue + delta);
			});
			addSizeControls(centerX, startY + 85, heightValue, delta -> {
				heightValue = clamp(heightValue + delta);
			});
			addRenderableWidget(Button.builder(Component.translatable("screen.wallwand.tower_shape"), button -> {
				towerShape = towerShape.next();
				rebuildScreen();
			}).bounds(centerX - 100, startY + 115, 200, 20).build());
		} else {
			addSizeControls(centerX, startY + 55, widthValue, delta -> {
				widthValue = clamp(widthValue + delta);
			});
			addSizeControls(centerX, startY + 85, heightValue, delta -> {
				heightValue = clamp(heightValue + delta);
			});
			addRenderableWidget(Button.builder(Component.translatable("screen.wallwand.orientation"), button -> {
				orientation = orientation.next();
				rebuildScreen();
			}).bounds(centerX - 100, startY + 115, 200, 20).build());
		}

		addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
			.bounds(centerX - 50, startY + 150, 100, 20)
			.build());
	}

	private void addSizeControls(int centerX, int y, int value, java.util.function.IntConsumer changer) {
		addRenderableWidget(Button.builder(Component.literal("-"), button -> changer.accept(-1))
			.bounds(centerX - 110, y, 20, 20)
			.build());
		addRenderableWidget(Button.builder(Component.literal("+"), button -> changer.accept(1))
			.bounds(centerX + 90, y, 20, 20)
			.build());
	}

	private void rebuildScreen() {
		this.clearWidgets();
		this.init();
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
		int startY = this.height / 2 - 95;

		graphics.drawCenteredString(this.font, this.title, centerX, startY - 20, 0xFFFFFF);
		graphics.drawCenteredString(
			this.font,
			Component.translatable(
				"screen.wallwand.mode_value",
				Component.translatable("screen.wallwand.build_mode." + buildMode.name().toLowerCase())
			),
			centerX,
			startY + 48,
			0xFFFFFF
		);

		int firstRowLabelY = startY + 61;
		int secondRowLabelY = startY + 91;

		if (buildMode == WallBuildMode.TOWER) {
			graphics.drawCenteredString(this.font, Component.translatable("screen.wallwand.diameter", diameterValue), centerX, firstRowLabelY, 0xFFFFFF);
			graphics.drawCenteredString(this.font, Component.translatable("screen.wallwand.height", heightValue), centerX, secondRowLabelY, 0xFFFFFF);
			graphics.drawCenteredString(
				this.font,
				Component.translatable(
					"screen.wallwand.tower_shape_value",
					Component.translatable("screen.wallwand.tower_shape." + towerShape.name().toLowerCase())
				),
				centerX,
				startY + 121,
				0xA0A0A0
			);
		} else {
			graphics.drawCenteredString(this.font, Component.translatable("screen.wallwand.width", widthValue), centerX, firstRowLabelY, 0xFFFFFF);
			graphics.drawCenteredString(this.font, Component.translatable("screen.wallwand.height", heightValue), centerX, secondRowLabelY, 0xFFFFFF);
			graphics.drawCenteredString(
				this.font,
				Component.translatable(
					"screen.wallwand.orientation_value",
					Component.translatable("item.wallwand.wall_wand.orientation." + orientation.name().toLowerCase())
				),
				centerX,
				startY + 121,
				0xA0A0A0
			);
		}
	}

	private int clamp(int value) {
		return Math.max(WallSettings.MIN_SIZE, Math.min(WallSettings.MAX_SIZE, value));
	}

	private void saveSettings() {
		WallSettings.setBuildMode(wand, buildMode);
		WallSettings.setRandomMode(wand, randomMode);
		WallSettings.setWidth(wand, widthValue);
		WallSettings.setHeight(wand, heightValue);
		WallSettings.setDiameter(wand, diameterValue);
		WallSettings.setOrientation(wand, orientation);
		WallSettings.setTowerShape(wand, towerShape);

		if (minecraft != null && minecraft.player != null) {
			minecraft.player.setItemInHand(hand, wand);
			ClientPlayNetworking.send(new WallBuilderNetworking.UpdateSettingsPayload(
				widthValue,
				heightValue,
				diameterValue,
				orientation.name(),
				buildMode.name(),
				randomMode,
				towerShape.name(),
				hand.ordinal()
			));
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
