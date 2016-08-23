package com.game.glassball.data;

public enum NoticeType {
	NORMAL_GAME_SUCCESS, NORMAL_GAME_FAIL, SURVIVAL_EXIT_GAME_FAIL;

	public int value() {
		return this.ordinal();
	}

	public static NoticeType toEnum(final int intValue) {
		// default ret is NORMAL_GAME_SUCCESS
		NoticeType ret = NORMAL_GAME_SUCCESS;
		for (final NoticeType confirmType : NoticeType.values()) {
			if (confirmType.value() == intValue) {
				ret = confirmType;
				break;
			}
		}
		return ret;
	}
}
