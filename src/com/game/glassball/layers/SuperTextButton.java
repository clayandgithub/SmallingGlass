package com.game.glassball.layers;

import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import com.w_game.basic.W_LayerManager;
import com.w_game.layers.W_TextButton;

public class SuperTextButton extends W_TextButton {

	protected int mMaxCD = 0;// ���CDʱ��(֡��)
	protected int mCurCD = 0;// ��ǰʣ��CDʱ��(֡��)

	protected int mDstX;// Ŀ�ĵ�x���
	protected int mDstY;// Ŀ�ĵ�y���
	protected int mMovingSpeed = 16;
	private boolean mIsMoving;

	protected RectF mDstRectF;

	public SuperTextButton(final String text, final int fWidth, final int fHeight, final int level) {
		super(text, fWidth, fHeight, level);
		mDstRectF = new RectF();
		setmHasBackground(false);
		setIsRoundRect(true);
		setmHasBorder(true);
	}

	@Override
	public void tick() {
		if (mCurCD > 0) {
			--mCurCD;
		}
		if (mIsMoving) {
			if (mDstX != mX) {
				final int flag = (mDstX - mX > 0) ? 1 : -1;
				if (Math.abs(mX - mDstX) < mMovingSpeed) {
					mX = mDstX;
				} else {
					mX += (flag * mMovingSpeed);
				}
			}
			if (mDstY != mY) {
				final int flag = (mDstY - mY > 0) ? 1 : -1;
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
	public void setPosition(final int x, final int y) {
		super.setPosition(x, y);
		mIsMoving = false;
		mDstX = x;
		mDstY = y;
	}

	public void setDstPosition(final int dstX, final int dstY) {
		if (mX != dstX || mY != dstY) {
			mIsMoving = true;
			mDstX = dstX;
			mDstY = dstY;
		}
	}

	public int getmMovingSpeed() {
		return mMovingSpeed;
	}

	public void setmMovingSpeed(final int mMovingSpeed) {
		this.mMovingSpeed = mMovingSpeed;
	}

	@Override
	public void paint(final Canvas canvas, final W_LayerManager lm) {
		if (isOutOfView(lm)) {
			return;
		}
		mDstRectF.left = mX;
		mDstRectF.top = mY;
		if (mStill) {
			mDstRectF.left = mX + lm.mViewX;
			mDstRectF.top = mY + lm.mViewY;
		}
		mDstRectF.right = mDstRectF.left + mDstWidth;
		mDstRectF.bottom = mDstRectF.top + mDstHeight;
		if (mOn && mCurCD == 0) {
			mPaint.setColor(mOnBackGroundColor);
		} else {
			mPaint.setColor(mNotOnBackGroundColor);
		}
		mPaint.setStyle(Style.FILL);
		if (mText.ismIsRoundRect()) {
			canvas.drawRoundRect(mDstRectF, 4, 4, mPaint);
		} else {
			canvas.drawRect(mDstRectF, mPaint);
		}

		if (mCurCD > 0 && mMaxCD > 0) {
			mDstRectF.right = mDstRectF.left + mDstRectF.width()
					* (mCurCD * 1.0f / mMaxCD);
			mPaint.setColor(mOnBackGroundColor);
			mPaint.setStyle(Style.FILL);
			if (mText.ismIsRoundRect()) {
				canvas.drawRoundRect(mDstRectF, 4, 4, mPaint);
			} else {
				canvas.drawRect(mDstRectF, mPaint);
			}
		}
		mText.setPosition(mX, mY);
		mText.paint(canvas, lm);
	}

	public boolean ismIsMoving() {
		return mIsMoving;
	}

	@Override
	public boolean myDownEvent(final int pointerID, final int x, final int y) {
		if (mIsMoving) {
			return false;
		}
		return super.myDownEvent(pointerID, x, y);
	}

	@Override
	public boolean myUpEvent(final int pointerID, final int x, final int y) {
		if (mIsMoving) {
			return false;
		}
		return super.myUpEvent(pointerID, x, y);
	}

	public void setCurCD(final int curCd) {
		if (mMaxCD >= curCd) {
			this.mCurCD = curCd;
		}
	}

	public void setCD(final int max_cd) {
		if (max_cd >= 0) {
			this.mCurCD = max_cd;
			this.mMaxCD = max_cd;
		}
	}

	public int getCurCD() {
		return mCurCD;
	}
}
