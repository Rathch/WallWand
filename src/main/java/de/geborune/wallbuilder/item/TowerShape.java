package de.geborune.wallbuilder.item;

public enum TowerShape {
	ROUND,
	SQUARE,
	DIAMOND;

	public TowerShape next() {
		TowerShape[] values = values();
		return values[(ordinal() + 1) % values.length];
	}
}
