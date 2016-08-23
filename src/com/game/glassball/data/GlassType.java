package com.game.glassball.data;

public enum GlassType {
	IRON, NORMAL_GLASS, SUPER_GLASS, GLASS_WITH_ONE_BALL, GLASS_WITH_TWO_BALL, GLASS_WITH_ONE_DIAMOND;

	public int value() {
		return this.ordinal() + 1;
	}

	public static GlassType toEnum(final int intValue) {
		// default ret is EXIT_GAME
		GlassType ret = IRON;
		for (final GlassType confirmType : GlassType.values()) {
			if (confirmType.value() == intValue) {
				ret = confirmType;
				break;
			}
		}
		return ret;
	}
}
