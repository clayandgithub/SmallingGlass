package com.game.glassball.layers;

import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.game.glassball.basic.PublicVars;
import com.game.glassball.data.GlassType;
import com.w_game.basic.W_LayerManager;
import com.w_game.layers.W_Sprite;

public class Glass extends W_Sprite {

	private Body mJBoxBody;

	private boolean mIsBroken;

	private GlassType mGlassType;

	private int mHitCount;

	private static final float RATIO = 40f / 64f;

	public String mDebugLevel;

	public Glass(final int width, final int height, final int level,
			final Bitmap bmp, final int[] frameSeq, final GlassType glassType) {
		super(width, height, level, bmp, frameSeq);
		setGlassType(glassType);
	}

	public void createGlassInWorld(final int center_x, final int center_y) {
		setCenterPosition(center_x, center_y);
		mJBoxBody = PublicVars.mStaticPublicVars.mJ2DBoxWorld.createPolygon(
				center_x, center_y, mDstWidth * RATIO, mDstHeight * RATIO,
				0.8f, true);
		mJBoxBody.m_userData = this;
	}

	@Override
	public void tick() {
		if (!mVisible) {
			return;
		}
		super.tick();
		if (mIsBroken) {
			if (!nextFrame()) {
				mVisible = false;
			}
		}
	}

	@Override
	public void paint(final Canvas canvas, final W_LayerManager lm) {
		if (mHitCount == 1) {
			setCurFrame(1);
		}
		// mPaint.setAlpha(120);
		super.paint(canvas, lm);
	}

	public Body getJBoxBody() {
		return mJBoxBody;
	}

	public void setJBoxBody(final Body jBoxBody) {
		this.mJBoxBody = jBoxBody;
	}

	public boolean isIsBroken() {
		return mIsBroken;
	}

	public void setIsBroken(final boolean isBroken) {
		mIsBroken = isBroken;
	}

	public GlassType getGlassType() {
		return mGlassType;
	}

	public void setGlassType(final GlassType glassType) {
		mGlassType = glassType;
	}

	public int getHitCount() {
		return mHitCount;
	}

	public void setHitCount(final int hitCount) {
		mHitCount = hitCount;
	}

}
