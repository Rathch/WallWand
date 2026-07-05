package de.geborune.wallbuilder.util;

import de.geborune.wallbuilder.item.WallOrientation;
import de.geborune.wallbuilder.item.WallSettings;
import de.geborune.wallbuilder.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.ArrayList;
import java.util.List;

public final class WallPlacer {
	private WallPlacer() {
	}

	public static int placeWall(ServerLevel level, Player player, BlockPos clickedPos, Direction clickedFace, ItemStack wand, InteractionHand wandHand) {
		ItemStack materialStack = getMaterialStack(player, wandHand);
		if (!(materialStack.getItem() instanceof BlockItem blockItem)) {
			return 0;
		}

		Block block = blockItem.getBlock();
		int width = WallSettings.getWidth(wand);
		int height = WallSettings.getHeight(wand);
		WallOrientation orientation = WallSettings.getOrientation(wand);

		Direction wallFace = clickedFace;
		BlockPos anchor = clickedPos.relative(clickedFace);

		if (clickedFace.getAxis() == Direction.Axis.Y) {
			wallFace = player.getDirection();
			anchor = clickedPos.relative(Direction.UP);
		}

		Direction widthDirection = getWidthDirection(wallFace, orientation, player);
		List<BlockPos> positions = new ArrayList<>(width * height);

		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				BlockPos pos = anchor.relative(widthDirection, w).above(h);
				if (canPlace(level, player, pos, block)) {
					positions.add(pos);
				}
			}
		}

		int available = countAvailableBlocks(player, block);
		int placed = 0;

		for (BlockPos pos : positions) {
			if (placed >= available) {
				break;
			}

			BlockState state = block.defaultBlockState();
			if (level.setBlock(pos, state, Block.UPDATE_ALL)) {
				consumeOneBlock(player, block);
				placed++;
			}
		}

		return placed;
	}

	public static ItemStack getMaterialStack(Player player, InteractionHand wandHand) {
		InteractionHand otherHand = wandHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		ItemStack otherStack = player.getItemInHand(otherHand);

		if (otherStack.getItem() instanceof BlockItem && otherStack.getItem() != ModItems.WALL_BUILDER) {
			return otherStack;
		}

		return ItemStack.EMPTY;
	}

	private static Direction getWidthDirection(Direction wallFace, WallOrientation orientation, Player player) {
		return switch (orientation) {
			case NORTH_SOUTH -> Direction.SOUTH;
			case EAST_WEST -> Direction.EAST;
			case PLAYER_RELATIVE -> wallFace.getAxis() == Direction.Axis.Y
				? player.getDirection().getClockWise()
				: wallFace.getClockWise();
		};
	}

	private static boolean canPlace(ServerLevel level, Player player, BlockPos pos, Block block) {
		BlockState existing = level.getBlockState(pos);
		if (!existing.canBeReplaced()) {
			return false;
		}

		BlockState newState = block.defaultBlockState();
		return newState.canSurvive(level, pos) && level.isUnobstructed(newState, pos, CollisionContext.of(player));
	}

	private static int countAvailableBlocks(Player player, Block block) {
		int count = 0;

		for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
			ItemStack stack = player.getInventory().getItem(slot);
			if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() == block) {
				count += stack.getCount();
			}
		}

		return count;
	}

	private static void consumeOneBlock(Player player, Block block) {
		for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
			ItemStack stack = player.getInventory().getItem(slot);
			if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() == block) {
				stack.shrink(1);
				return;
			}
		}
	}
}
