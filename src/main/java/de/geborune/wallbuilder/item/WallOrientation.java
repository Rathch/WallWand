package de.geborune.wallbuilder.item;

public enum WallOrientation {
	PLAYER_RELATIVE,
	NORTH_SOUTH,
	EAST_WEST;

	public WallOrientation next() {
		WallOrientation[] values = values();
		return values[(ordinal() + 1) % values.length];
	}
}
