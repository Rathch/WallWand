package de.geborune.wallbuilder.item;

import de.geborune.wallbuilder.util.WallPlacer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class WallBuilderItem extends Item {
	public WallBuilderItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		Player player = context.getPlayer();
		if (player == null || player.isShiftKeyDown()) {
			return InteractionResult.PASS;
		}

		ItemStack wand = context.getItemInHand();
		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.SUCCESS;
		}

		if (!(WallPlacer.getMaterialStack(player, context.getHand()).getItem() instanceof net.minecraft.world.item.BlockItem)) {
			player.displayClientMessage(Component.translatable("item.wallbuilder.wall_builder.no_material"), true);
			return InteractionResult.FAIL;
		}

		int placed = WallPlacer.placeWall(
			serverLevel,
			player,
			context.getClickedPos(),
			context.getClickedFace(),
			wand,
			context.getHand()
		);

		if (placed == 0) {
			player.displayClientMessage(Component.translatable("item.wallbuilder.wall_builder.place_failed"), true);
			return InteractionResult.FAIL;
		}

		player.displayClientMessage(Component.translatable("item.wallbuilder.wall_builder.placed", placed), true);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable(
			"item.wallbuilder.wall_builder.tooltip",
			WallSettings.getWidth(stack),
			WallSettings.getHeight(stack),
			Component.translatable("item.wallbuilder.wall_builder.orientation." + WallSettings.getOrientation(stack).name().toLowerCase())
		));
		tooltip.add(Component.translatable("item.wallbuilder.wall_builder.tooltip.config"));
	}
}
