package de.geborune.wallbuilder.util;

import de.geborune.wallbuilder.item.TowerShape;
import de.geborune.wallbuilder.item.WallBuildMode;
import de.geborune.wallbuilder.item.WallOrientation;
import de.geborune.wallbuilder.item.WallSettings;
import de.geborune.wallbuilder.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class WallPlacer {
	private WallPlacer() {
	}

	public static int placeWall(ServerLevel level, Player player, BlockPos clickedPos, Direction clickedFace, ItemStack wand) {
		if (!WallSettings.isRandomMode(wand) && WallSettings.getMaterialBlock(wand) == null) {
			return 0;
		}

		if (WallSettings.isRandomMode(wand) && getHotbarBlocks(player).isEmpty() && WallSettings.getMaterialBlock(wand) == null) {
			return 0;
		}

		WallBuildMode buildMode = WallSettings.getBuildMode(wand);
		Set<BlockPos> positions = new LinkedHashSet<>(switch (buildMode) {
			case NORMAL -> collectNormalPositions(level, player, clickedPos, clickedFace, wand);
			case DIAGONAL -> collectDiagonalPositions(level, player, clickedPos, clickedFace, wand);
			case PALISADE -> collectPalisadePositions(level, player, clickedPos, clickedFace, wand);
			case TOWER -> collectTowerPositions(level, player, clickedPos, clickedFace, wand);
		});

		return placePositions(level, player, wand, positions);
	}

	private static List<BlockPos> collectNormalPositions(ServerLevel level, Player player, BlockPos clickedPos, Direction clickedFace, ItemStack wand) {
		WallPlacementContext context = WallPlacementContext.from(level, player, clickedPos, clickedFace, wand);
		List<BlockPos> positions = new ArrayList<>(context.width * context.height);

		for (int h = 0; h < context.height; h++) {
			for (int w = 0; w < context.width; w++) {
				addIfPlaceable(level, player, positions, context.anchor.relative(context.widthDirection, w).above(h), wand);
			}
		}

		return positions;
	}

	private static List<BlockPos> collectDiagonalPositions(ServerLevel level, Player player, BlockPos clickedPos, Direction clickedFace, ItemStack wand) {
		WallPlacementContext context = WallPlacementContext.from(level, player, clickedPos, clickedFace, wand);
		List<BlockPos> positions = new ArrayList<>(context.width * context.height);

		for (int h = 0; h < context.height; h++) {
			for (int w = 0; w < context.width; w++) {
				BlockPos pos = context.anchor.relative(context.widthDirection, w + h).above(h);
				addIfPlaceable(level, player, positions, pos, wand);
			}
		}

		return positions;
	}

	private static List<BlockPos> collectPalisadePositions(ServerLevel level, Player player, BlockPos clickedPos, Direction clickedFace, ItemStack wand) {
		WallPlacementContext context = WallPlacementContext.from(level, player, clickedPos, clickedFace, wand);
		List<BlockPos> positions = new ArrayList<>(context.width * context.height);

		for (int w = 0; w < context.width; w++) {
			int depth = w % 2;
			for (int h = 0; h < context.height; h++) {
				BlockPos pos = context.anchor.relative(context.widthDirection, w).relative(context.wallFace, depth).above(h);
				addIfPlaceable(level, player, positions, pos, wand);
			}
		}

		return positions;
	}

	private static List<BlockPos> collectTowerPositions(ServerLevel level, Player player, BlockPos clickedPos, Direction clickedFace, ItemStack wand) {
		int diameter = WallSettings.getDiameter(wand);
		int height = WallSettings.getHeight(wand);
		TowerShape shape = WallSettings.getTowerShape(wand);
		BlockPos anchor = clickedPos.relative(clickedFace);
		int radius = diameter / 2;
		List<BlockPos> positions = new ArrayList<>();

		for (int y = 0; y < height; y++) {
			for (int dx = -radius; dx <= radius; dx++) {
				for (int dz = -radius; dz <= radius; dz++) {
					if (!isTowerShell(shape, dx, dz, radius)) {
						continue;
					}

					addIfPlaceable(level, player, positions, anchor.offset(dx, y, dz), wand);
				}
			}
		}

		return positions;
	}

	private static boolean isTowerShell(TowerShape shape, int dx, int dz, int radius) {
		return switch (shape) {
			case SQUARE -> (Math.abs(dx) == radius || Math.abs(dz) == radius)
				&& Math.abs(dx) <= radius
				&& Math.abs(dz) <= radius;
			case ROUND -> {
				int distSq = dx * dx + dz * dz;
				int outer = radius * radius + radius;
				if (distSq > outer) {
					yield false;
				}
				if (radius <= 1) {
					yield true;
				}
				int inner = (radius - 1) * (radius - 1);
				yield distSq >= inner;
			}
			case DIAMOND -> Math.abs(dx) + Math.abs(dz) == radius;
		};
	}

	private static int placePositions(ServerLevel level, Player player, ItemStack wand, Set<BlockPos> positions) {
		RandomSource random = level.getRandom();
		int placed = 0;

		for (BlockPos pos : positions) {
			Block block = resolveBlock(player, wand, random);
			if (block == null || !hasBlockInInventory(player, block)) {
				continue;
			}

			BlockState state = block.defaultBlockState();
			if (level.setBlock(pos, state, Block.UPDATE_ALL)) {
				consumeOneBlock(player, block);
				placed++;
			}
		}

		return placed;
	}

	private static void addIfPlaceable(ServerLevel level, Player player, List<BlockPos> positions, BlockPos pos, ItemStack wand) {
		Block block = WallSettings.getMaterialBlock(wand);
		if (block == null && WallSettings.isRandomMode(wand)) {
			List<Block> hotbarBlocks = getHotbarBlocks(player);
			if (!hotbarBlocks.isEmpty()) {
				block = hotbarBlocks.getFirst();
			}
		}

		if (block != null && canPlace(level, player, pos, block)) {
			positions.add(pos);
		} else if (block == null && WallSettings.isRandomMode(wand)) {
			for (Block hotbarBlock : getHotbarBlocks(player)) {
				if (canPlace(level, player, pos, hotbarBlock)) {
					positions.add(pos);
					break;
				}
			}
		}
	}

	private static Block resolveBlock(Player player, ItemStack wand, RandomSource random) {
		if (WallSettings.isRandomMode(wand)) {
			List<Block> hotbarBlocks = getHotbarBlocks(player);
			if (!hotbarBlocks.isEmpty()) {
				return hotbarBlocks.get(random.nextInt(hotbarBlocks.size()));
			}
		}

		return WallSettings.getMaterialBlock(wand);
	}

	private static List<Block> getHotbarBlocks(Player player) {
		List<Block> blocks = new ArrayList<>();

		for (int slot = 0; slot < 9; slot++) {
			ItemStack stack = player.getInventory().getItem(slot);
			if (stack.getItem() instanceof BlockItem blockItem && stack.getItem() != ModItems.WALL_BUILDER) {
				Block block = blockItem.getBlock();
				if (!blocks.contains(block)) {
					blocks.add(block);
				}
			}
		}

		return blocks;
	}

	private static boolean canPlace(ServerLevel level, Player player, BlockPos pos, Block block) {
		BlockState existing = level.getBlockState(pos);
		if (!existing.canBeReplaced()) {
			return false;
		}

		BlockState newState = block.defaultBlockState();
		return newState.canSurvive(level, pos) && level.isUnobstructed(newState, pos, CollisionContext.of(player));
	}

	private static boolean hasBlockInInventory(Player player, Block block) {
		return countAvailableBlocks(player, block) > 0;
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

	private record WallPlacementContext(BlockPos anchor, Direction wallFace, Direction widthDirection, int width, int height) {
		private static WallPlacementContext from(ServerLevel level, Player player, BlockPos clickedPos, Direction clickedFace, ItemStack wand) {
			Direction wallFace = clickedFace;
			BlockPos anchor = clickedPos.relative(clickedFace);

			if (clickedFace.getAxis() == Direction.Axis.Y) {
				wallFace = player.getDirection();
				anchor = clickedPos.relative(Direction.UP);
			}

			return new WallPlacementContext(
				anchor,
				wallFace,
				getWidthDirection(wallFace, WallSettings.getOrientation(wand), player),
				WallSettings.getWidth(wand),
				WallSettings.getHeight(wand)
			);
		}
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
}
