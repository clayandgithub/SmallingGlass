package com.game.glassball.scenes;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.game.glassball.basic.GameConstants;
import com.game.glassball.basic.PublicVars;
import com.game.glassball.basic.R;
import com.game.glassball.layers.SuperTextButton;
import com.w_game.basic.W_EventListener;
import com.w_game.basic.W_Scene;
import com.w_game.layers.W_SingleLineText;
import com.w_game.layers.W_Sprite;
import com.w_game.layers.W_TextButton;

public class ChooseLevelScene extends W_Scene {

	public static final int MAX_BTN_NUM_PER_PAGE = 20;

	private static final int BTN_SIZE = 50;

	private final List<SuperTextButton> mLvBtns;

	private final W_TextButton mPrePageBtn;

	private final W_TextButton mNextPageBtn;

	private W_TextButton mTextBackBtn;

	private int mCurPage;

	private final W_SingleLineText mPageInfo;

	private final W_Sprite mBadStar;

	private final W_Sprite mGoodStar;

	private final W_EventListener mLockLvEventListener;

	public ChooseLevelScene(final int sceneId, final int layerNum,
			final int viewWidth, final int viewHeight) {
		super(sceneId, layerNum, viewWidth, viewHeight);
		mLockLvEventListener = new W_EventListener() {
			@Override
			public void event() {
				PublicVars.mStaticPublicVars.mMessageBars
						.addNewMessage(PublicVars.mStaticMainContext
								.getString(R.string.level_is_locked), Color.RED);
			};
		};
		mBadStar = new W_Sprite(16, 16, 1,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_STAR_16_16_BITMAP_KEY),
				new int[] { 0 });
		mGoodStar = new W_Sprite(16, 16, 1,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_STAR_16_16_BITMAP_KEY),
				new int[] { 1 });

		mLvBtns = new ArrayList<SuperTextButton>(MAX_BTN_NUM_PER_PAGE);
		for (int i = 0; i < MAX_BTN_NUM_PER_PAGE; ++i) {
			final SuperTextButton btn = new SuperTextButton(null, BTN_SIZE,
					BTN_SIZE, 1);
			btn.getmText().setTextAttr(Color.WHITE, 20);
			mLvBtns.add(btn);
		}

		mPageInfo = new W_SingleLineText(50, 40, 1, null);
		mPageInfo.setTextAttr(Color.WHITE, 20);

		mPrePageBtn = new W_TextButton("<<", 64, 40, 1);
		mPrePageBtn.setmHasBackground(true);
		mPrePageBtn.getmText().mHasBorder = true;
		mPrePageBtn.getmText().setmIsRoundRect(true);
		mPrePageBtn.getmText().setTextAttr(Color.WHITE, 20);
		mPrePageBtn.setmEventListener(new W_EventListener() {
			@Override
			public void event() {
			    setCurPage(mCurPage - 1);
			}
		});
		mNextPageBtn = new W_TextButton(">>", 64, 40, 1);
		mNextPageBtn.setmHasBackground(true);
		mNextPageBtn.getmText().setmIsRoundRect(true);
		mNextPageBtn.getmText().mHasBorder = true;
		mNextPageBtn.getmText().setTextAttr(Color.WHITE, 20);
		mNextPageBtn.setmEventListener(new W_EventListener() {
			@Override
			public void event() {
			    setCurPage(mCurPage + 1);
			}
		});
	}

	@Override
	public void initScene() {
		mLM.removeAllLayers();
		if (mTextBackBtn == null) {
			mTextBackBtn = new W_TextButton(
					PublicVars.mStaticMainContext
							.getString(R.string.back_btn_name),
					64, 32, 1);
			mTextBackBtn.getmText().setTextAttr(Color.WHITE, 18);
			mTextBackBtn.setIsRoundRect(true);
			mTextBackBtn.setmHasBorder(true);
			mTextBackBtn.setmEventListener(new W_EventListener() {
				@Override
				public void event() {
					PublicVars.mStaticMainActivity
							.changeSceneBySceneId(GameConstants.STARTMENU_SCENE);
				}
			});
		}
		mTextBackBtn.resetBtn();
		mLM.append(mTextBackBtn);
		mPrePageBtn.resetBtn();
		mLM.append(mPrePageBtn);
		mLM.append(mPageInfo);
		mNextPageBtn.resetBtn();
		mLM.append(mNextPageBtn);
		for (int i = 0; i < mLvBtns.size(); ++i) {
		    mLvBtns.get(i).resetBtn();
			mLM.append(mLvBtns.get(i));
		}
		mLM.append(PublicVars.mStaticPublicVars.mMessageBars);

		// int startAlpha = PublicVars.mStaticPublicVars.mBlackMask.getmAlpha();
		// startAlpha = (startAlpha == 0 ? 255 : startAlpha);
		// PublicVars.mStaticPublicVars.mBlackMask.startDecreaseAlpha(startAlpha,
		// 0);
		// mLM.append(PublicVars.mStaticPublicVars.mBlackMask);

		// initPosition
		initScenePosition();
	}

	@Override
	public void releaseScene() {
	}

	public void initScenePosition() {
		int tempX = 24;
		final int tempY = 458;
		resetLvBtnPosition();
		tempX = 160 - mPageInfo.getDstWidth() - mPrePageBtn.getDstWidth();
		mPrePageBtn.setPosition(tempX, tempY);
		tempX = 160 - mPageInfo.getDstWidth() / 2;
		mPageInfo.setPosition(tempX, tempY);
		tempX = 160 + mPageInfo.getDstWidth();
		mNextPageBtn.setPosition(tempX, tempY);
		mTextBackBtn.setPosition(24, 10);
		PublicVars.mStaticPublicVars.mMessageBars.setPosition(0, 64);
	}

	public int getCurPage() {
		return this.mCurPage;
	}

	public boolean setCurPage(final int curPage) {
		final int gameMaxLevel = PublicVars.mStaticPublicVars.mPlayerInfo
				.getGameMaxLevel();
		final int playerMaxLevel = PublicVars.mStaticPublicVars.mPlayerInfo
				.getPlayerMaxLevel();
		final int maxPageNo = gameMaxLevel / MAX_BTN_NUM_PER_PAGE;
		if (curPage >= 0 && curPage <= maxPageNo) {
			if (mCurPage != curPage) {
				resetLvBtnPosition();
			}
			this.mCurPage = curPage;
			mPageInfo.setmContent(String.valueOf(mCurPage + 1) + "/"
					+ String.valueOf(maxPageNo + 1));
			final int startLevelNo = mCurPage * MAX_BTN_NUM_PER_PAGE;
			final int btnNumOnThisPage = Math.min(gameMaxLevel - startLevelNo
					+ 1, MAX_BTN_NUM_PER_PAGE);
			// set text
			for (int i = 0; i < btnNumOnThisPage; ++i) {
				mLvBtns.get(i).setVisible(true);
				if (i + startLevelNo <= playerMaxLevel) {
					mLvBtns.get(i).getmText()
							.setmContent(String.valueOf(i + startLevelNo + 1));
					bindLevelToBtn(i + startLevelNo, i);
				} else {
					mLvBtns.get(i).getmText().setmContent("?");
					bindLevelToBtn(i + startLevelNo, i);
					bindLevelToBtn(-1, i);

				}
			}
			for (int i = btnNumOnThisPage; i < MAX_BTN_NUM_PER_PAGE; ++i) {
				mLvBtns.get(i).setVisible(false);
				bindLevelToBtn(-1, i);
			}
			return true;
		}
		return false;
	}

	private void bindLevelToBtn(final int level, final int btnIndex) {
		if (level < 0) {
			mLvBtns.get(btnIndex).setmEventListener(mLockLvEventListener);
			return;
		}
		mLvBtns.get(btnIndex).setmEventListener(new W_EventListener() {
			@Override
			public void event() {
				if (PublicVars.mStaticPublicVars.mGameBoard
						.initGlassesAndBallByLevel(level)) {
					PublicVars.mStaticMainActivity
							.changeSceneBySceneId(GameConstants.GAMING_SCENE);
				}
				super.event();
			}
		});
	}

	// public W_SingleLineText getCoinNumText() {
	// return mCoinNumText;
	// }

	@Override
	public void paint(final Canvas canvas) {
		PublicVars.mStaticPublicVars.mBackground.paint(canvas, mLM);
		// paint level stars
		final int start = mCurPage * MAX_BTN_NUM_PER_PAGE;
		final int gameMaxLevel = PublicVars.mStaticPublicVars.mPlayerInfo
				.getGameMaxLevel();
		final int btnNumOnThisPage = Math.min(gameMaxLevel - start + 1,
				MAX_BTN_NUM_PER_PAGE);
		for (int i = 0; i < btnNumOnThisPage; ++i) {
			final int starnum = PublicVars.mStaticPublicVars.mPlayerInfo
					.getLevelStar(i + start);
			int tmpX = mLvBtns.get(i).getX() + 1;
			final int tmpY = mLvBtns.get(i).getY()
					+ mLvBtns.get(i).getDstHeight() + 4;
			for (int j = 0; j < starnum; ++j) {
				mGoodStar.setPosition(tmpX, tmpY);
				mGoodStar.paint(canvas, mLM);
				tmpX += 16;
			}
			for (int j = starnum; j < 3; ++j) {
				mBadStar.setPosition(tmpX, tmpY);
				mBadStar.paint(canvas, mLM);
				tmpX += 16;
			}
		}
		super.paint(canvas);

	}

	public void resetLvBtnPosition() {
		int tempX = -(2 * BTN_SIZE + 24);
		int tempY = 64;
		final int rx1 = 320;
		final int rx2 = 320 + BTN_SIZE + 24;
		final int baseSpeed = 36;
		for (int i = 0; i < mLvBtns.size(); i += 4) {
			mLvBtns.get(i).setPosition(tempX, tempY);
			mLvBtns.get(i).setmMovingSpeed(baseSpeed);
			mLvBtns.get(i + 1).setPosition(-BTN_SIZE, tempY);
			mLvBtns.get(i + 1).setmMovingSpeed(baseSpeed);
			mLvBtns.get(i + 2).setPosition(rx1, tempY);
			mLvBtns.get(i + 2).setmMovingSpeed(baseSpeed);
			mLvBtns.get(i + 3).setPosition(rx2, tempY);
			mLvBtns.get(i + 3).setmMovingSpeed(baseSpeed);
			tempY += 78;
		}
		tempX = 24;
		tempY = 64;
		for (int i = 0; i < mLvBtns.size(); ++i) {
			mLvBtns.get(i).setDstPosition(tempX, tempY);
			if (i % 4 == 3) {
				tempX = 24;
				tempY += 78;
			} else {
				tempX += 74;
			}
		}

	}
}
