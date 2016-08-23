package com.game.glassball.data;

public enum ConfirmType {
	NORMAL_EXIT_GAME, SURVIVAL_EXIT_GAME;

	public int value() {
		return this.ordinal();
	}

	public static ConfirmType toEnum(final int intValue) {
		// default ret is EXIT_GAME
		ConfirmType ret = NORMAL_EXIT_GAME;
		for (final ConfirmType confirmType : ConfirmType.values()) {
			if (confirmType.value() == intValue) {
				ret = confirmType;
				break;
			}
		}
		return ret;
	}
}
