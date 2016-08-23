package com.game.glassball.layers;

import android.graphics.Canvas;
import android.graphics.Color;

import com.w_game.basic.W_Layer;
import com.w_game.basic.W_LayerManager;
import com.w_game.layers.W_SingleLineText;

public class MessageBars extends W_Layer {
	private static final int MAX_SHOW_TIME = 40;
	private final W_SingleLineText[] mMessages;
	private final int[] mShowTime;

	public MessageBars(final int width, final int height, final int level,
			final int maxMessageNum) {
		super(width, height, level);
		mMessages = new W_SingleLineText[maxMessageNum];
		mShowTime = new int[maxMessageNum];
		for (int i = 0; i < maxMessageNum; ++i) {
			mMessages[i] = new W_SingleLineText(width - 64, height, level, null);
			mMessages[i].setmHasBackGround(true);
			mMessages[i].setmBackGroundColor(Color.DKGRAY);
			mMessages[i].setmIsRoundRect(true);
			mMessages[i].mHasBorder = true;
			mShowTime[i] = 0;
		}
	}

	@Override
	public void paint(final Canvas canvas, final W_LayerManager lm) {
		for (int i = 0; i < mShowTime.length; ++i) {
			if (mShowTime[i] > 0) {
				mMessages[i].paint(canvas, lm);
			}
		}
	}

	@Override
	public void tick() {
		for (int i = 0; i < mShowTime.length; ++i) {
			if (mShowTime[i] > 0) {
				// //前1秒alpha没有变化,后一秒慢慢消退
				if (mShowTime[i] < MAX_SHOW_TIME / 2) {
					mMessages[i]
							.setmAlpha((int) (mShowTime[i] * 255.0f / (MAX_SHOW_TIME / 2)));
				}
				--mShowTime[i];
			}
		}
	}

	@Override
	public void setPosition(final int x, final int y) {
		super.setPosition(x, y);
		initContentPosition();
	}

	public void initContentPosition() {
		final int tempX = mX + 32;
		int tempY = mY + 40;
		for (int i = 0; i < mMessages.length; ++i) {
			mMessages[i].setPosition(tempX, tempY);
			tempY += 32;
		}
	}

	// 向信息条中添加一个新的信息
	public void addNewMessage(final String newMSG, final int MSGcolor) {
		for (int i = mShowTime.length - 1; i > 0; --i) {
			if (mShowTime[i - 1] != 0) {
				mMessages[i].setmContent(mMessages[i - 1].getContent());
				mMessages[i].setmTextColor(mMessages[i - 1].mTextColor);
				mMessages[i].setmAlpha(mMessages[i - 1].getmAlpha());
				mShowTime[i] = mShowTime[i - 1];
			}
		}
		mMessages[0].setmContent(newMSG);
		mMessages[0].setmTextColor(MSGcolor);
		mMessages[0].setmAlpha(255);
		mShowTime[0] = MAX_SHOW_TIME;
	}

	public void clear() {
		for (int i = 0; i < mShowTime.length; ++i) {
			mShowTime[i] = 0;
		}
	}
}
