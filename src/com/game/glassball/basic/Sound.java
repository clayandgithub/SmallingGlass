package com.game.glassball.basic;

import com.w_game.basic.W_GameSound;

import android.content.Context;

public class Sound extends W_GameSound {

	public Sound(final Context inContex) {
		super(inContex);
		initSound(10);
	}

	@Override
	public void initSound(final int maxStreams) {
		super.initSound(maxStreams);
		addSound(R.raw.ding, 1);
		addSound(R.raw.glass, 1);
	}
}
