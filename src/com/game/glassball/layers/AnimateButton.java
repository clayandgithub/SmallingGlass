package com.game.glassball.layers;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.w_game.basic.W_LayerManager;
import com.w_game.layers.W_Button;
import com.w_game.layers.W_SingleLineText;

public class AnimateButton extends W_Button {

	protected int mDstX;
	protected int mDstY;
	protected int mMovingSpeed = 16;
	private boolean mIsMoving;
	private W_SingleLineText mText;

	public AnimateButton(int fWidth, int fHeight, int level, Bitmap bmp,
			int[] frameSeq, String text) {
		super(fWidth, fHeight, level, bmp, frameSeq);
		mText = new W_SingleLineText(fWidth, fHeight, level, text);
	}

	@Override
	public void tick() {
		if (mIsMoving) {
			if (mDstX != mX) {
				int flag = (mDstX - mX > 0) ? 1 : -1;
				if (Math.abs(mX - mDstX) < mMovingSpeed) {
					mX = mDstX;
				} else {
					mX += (flag * mMovingSpeed);
				}
			}
			if (mDstY != mY) {
				int flag = (mDstY - mY > 0) ? 1 : -1;
				if (Math.abs(mY - mDstY) < mMovingSpeed) {
					mY = mDstY;
				} else {
					mY += (flag * mMovingSpeed);
				}
			}
			if (mX == mDstX && mY == mDstY) {
				mIsMoving = false;
			}
		}
	}

	@Override
	public void paint(Canvas canvas, W_LayerManager lm) {
		super.paint(canvas, lm);
		mText.setPosition(mX, mY);
		mText.paint(canvas, lm);
	}

	@Override
	public void setPosition(int x, int y) {
		super.setPosition(x, y);
		mIsMoving = false;
		mDstX = x;
		mDstY = y;
	}

	public void setDstPosition(int dstX, int dstY) {
		if (mX != dstX || mY != dstY) {
			mIsMoving = true;
			mDstX = dstX;
			mDstY = dstY;
		}
	}

	public int getmMovingSpeed() {
		return mMovingSpeed;
	}

	public void setmMovingSpeed(int mMovingSpeed) {
		this.mMovingSpeed = mMovingSpeed;
	}

	public W_SingleLineText getText() {
		return mText;
	}

	public void setText(W_SingleLineText text) {
		mText = text;
	}

	public boolean ismIsMoving() {
		return mIsMoving;
	}

	@Override
	public boolean myDownEvent(int pointerID, int x, int y) {
		if (mIsMoving) {
			return false;
		}
		return super.myDownEvent(pointerID, x, y);
	}

	@Override
	public boolean myUpEvent(int pointerID, int x, int y) {
		if (mIsMoving) {
			return false;
		}
		return super.myUpEvent(pointerID, x, y);
	}
}
