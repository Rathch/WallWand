package de.geborune.wallbuilder.item;

public enum WallBuildMode {
	NORMAL,
	DIAGONAL,
	PALISADE,
	TOWER;

	public WallBuildMode next() {
		WallBuildMode[] values = values();
		return values[(ordinal() + 1) % values.length];
	}
}
