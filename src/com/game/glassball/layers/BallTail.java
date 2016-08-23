package com.game.glassball.layers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.w_game.basic.W_LayerManager;
import com.w_game.layers.W_Sprite;

public class BallTail extends W_Sprite {

	private int mAngle;
	private final Point mStartPoint;
	private final Point mEndPoint;

	public BallTail(final int width, final int height, final int level,
			final Bitmap bmp) {
		super(width, height, level, bmp);
		mStartPoint = new Point();
		mEndPoint = new Point();
		mVisible = false;
	}

	@Override
	public void paint(final Canvas canvas, final W_LayerManager lm) {
		canvas.save();
		canvas.rotate(mAngle, mX, mY + mDstHeight / 2);
		super.paint(canvas, lm);
		canvas.restore();
	}

	public void setStartPoint(final int x, final int y) {
		mStartPoint.x = x;
		mStartPoint.y = y;
		mX = x;
		mY = y - mDstHeight / 2;
	}

	public void setEndPoint(final int x, final int y) {
		mEndPoint.x = x;
		mEndPoint.y = y;
		mDstWidth = (int) Math.sqrt((x - mX) * (x - mX) + (y - mY) * (y - mY));
		if (x == mX) {
			mAngle = 90;
		}
		final int yDiff = y - mY;
		final int xDiff = x - mX;
		mAngle = (int) (Math.atan(yDiff * 1.0 / xDiff) * 180 / Math.PI);
		if (xDiff < 0) {
			mAngle += 180;
		}
		mVisible = (mWidth != 0);
	}

	public Point getStartPoint() {
		return mStartPoint;
	}

	public Point getEndPoint() {
		return mEndPoint;
	}
}
