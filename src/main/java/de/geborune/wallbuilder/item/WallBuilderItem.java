package de.geborune.wallbuilder.item;

import de.geborune.wallbuilder.util.WallPlacer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class WallBuilderItem extends Item {
	public WallBuilderItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		Player player = context.getPlayer();
		if (player == null) {
			return InteractionResult.PASS;
		}

		ItemStack wand = context.getItemInHand();

		if (player.isShiftKeyDown()) {
			return selectMaterial(level, player, context, wand);
		}

		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.SUCCESS;
		}

		if (!WallSettings.hasMaterial(wand) && !WallSettings.isRandomMode(wand)) {
			player.displayClientMessage(Component.translatable("item.wallwand.wall_wand.no_material"), true);
			return InteractionResult.FAIL;
		}

		int placed = WallPlacer.placeWall(
			serverLevel,
			player,
			context.getClickedPos(),
			context.getClickedFace(),
			wand
		);

		if (placed == 0) {
			player.displayClientMessage(Component.translatable("item.wallwand.wall_wand.place_failed"), true);
			return InteractionResult.FAIL;
		}

		player.displayClientMessage(Component.translatable("item.wallwand.wall_wand.placed", placed), true);
		return InteractionResult.SUCCESS;
	}

	private InteractionResult selectMaterial(Level level, Player player, UseOnContext context, ItemStack wand) {
		if (!(level instanceof ServerLevel)) {
			return InteractionResult.SUCCESS;
		}

		BlockState state = level.getBlockState(context.getClickedPos());
		Block block = state.getBlock();

		if (!(block.asItem() instanceof BlockItem)) {
			player.displayClientMessage(Component.translatable("item.wallwand.wall_wand.invalid_material"), true);
			return InteractionResult.FAIL;
		}

		WallSettings.setMaterial(wand, block);
		player.setItemInHand(context.getHand(), wand);
		player.displayClientMessage(Component.translatable("item.wallwand.wall_wand.material_selected", block.getName()), true);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		Block material = WallSettings.getMaterialBlock(stack);
		if (material != null) {
			tooltip.add(Component.translatable("item.wallwand.wall_wand.tooltip.material", material.getName()));
		} else {
			tooltip.add(Component.translatable("item.wallwand.wall_wand.tooltip.no_material"));
		}

		tooltip.add(Component.translatable(
			"item.wallwand.wall_wand.tooltip.mode",
			Component.translatable("screen.wallwand.build_mode." + WallSettings.getBuildMode(stack).name().toLowerCase())
		));

		if (WallSettings.isRandomMode(stack)) {
			tooltip.add(Component.translatable("item.wallwand.wall_wand.tooltip.random_on"));
		}

		if (WallSettings.getBuildMode(stack) == WallBuildMode.TOWER) {
			tooltip.add(Component.translatable(
				"item.wallwand.wall_wand.tooltip.tower",
				WallSettings.getDiameter(stack),
				WallSettings.getHeight(stack),
				Component.translatable("screen.wallwand.tower_shape." + WallSettings.getTowerShape(stack).name().toLowerCase())
			));
		} else {
			tooltip.add(Component.translatable(
				"item.wallwand.wall_wand.tooltip",
				WallSettings.getWidth(stack),
				WallSettings.getHeight(stack),
				Component.translatable("item.wallwand.wall_wand.orientation." + WallSettings.getOrientation(stack).name().toLowerCase())
			));
		}
		tooltip.add(Component.translatable("item.wallwand.wall_wand.tooltip.select_material"));
		tooltip.add(Component.translatable("item.wallwand.wall_wand.tooltip.config"));
	}
}
