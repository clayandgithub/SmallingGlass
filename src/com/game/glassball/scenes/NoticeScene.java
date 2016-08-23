package com.game.glassball.scenes;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.game.glassball.basic.GameConstants;
import com.game.glassball.basic.PublicVars;
import com.game.glassball.basic.R;
import com.game.glassball.data.NoticeType;
import com.w_game.basic.W_EventListener;
import com.w_game.basic.W_Scene;
import com.w_game.layers.W_Button;
import com.w_game.layers.W_SingleLineText;
import com.w_game.layers.W_Sprite;

public class NoticeScene extends W_Scene {

	private static final int DIS_BETWEEN_STAR = 32;

	private final W_SingleLineText mTitle;

	private List<W_Button> mOptBtns;

	private NoticeType noticeType;

	private W_Sprite mScreenShot;

	private final W_Sprite mBadStar;

	private final W_Sprite mGoodStar;

	private int mCurStarNum;

	public NoticeScene(final int sceneId, final int layerNum,
			final int viewWidth, final int viewHeight) {
		super(sceneId, layerNum, viewWidth, viewHeight);
		mOptBtns = new ArrayList<W_Button>(3);
		mTitle = new W_SingleLineText(viewWidth, 32, 1, null);
		mBadStar = new W_Sprite(32, 32, 1,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_STAR_32_32_BITMAP_KEY),
				new int[] { 0 });
		mGoodStar = new W_Sprite(32, 32, 1,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_STAR_32_32_BITMAP_KEY),
				new int[] { 1 });
	}

	@Override
	public void initScene() {
		mLM.removeAllLayers();
		mScreenShot = PublicVars.mStaticMainActivity.getScreenShot();
		mLM.append(mScreenShot);
		PublicVars.mStaticPublicVars.mBlackMask.setNoChangeAlpha(120);
		mLM.append(PublicVars.mStaticPublicVars.mBlackMask);
		mLM.append(mTitle);
		mLM.append(PublicVars.mStaticPublicVars.mMessageBars);
		for (final W_Button btn : mOptBtns) {
		    btn.resetBtn();
			mLM.append(btn);
		}
		// initPosition
		initScenePosition();
	}

	@Override
	public void releaseScene() {
		mScreenShot = null;
	}

	public void initScenePosition() {
		mTitle.setPosition(0, 126);
		final int toLeft = 40;
		final int diffX = (128 - 2 * toLeft) / 2;
		int tempX = toLeft;
		final int tempY = 356;
		for (final W_Button btn : mOptBtns) {
			btn.setPosition(tempX, tempY);
			tempX += (diffX + btn.getDstWidth());
		}
		PublicVars.mStaticPublicVars.mMessageBars.setPosition(0, 64);
	}

	public NoticeType getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(final NoticeType noticeType) {
		mOptBtns.clear();
		switch (noticeType) {
		case NORMAL_GAME_FAIL:
			mTitle.setmContent(PublicVars.mStaticMainContext
					.getString(R.string.game_over));
			mTitle.setTextAttr(Color.RED, 36);
			initNormalGameNotice();
			break;
		case NORMAL_GAME_SUCCESS:
			mTitle.setmContent(PublicVars.mStaticMainContext
					.getString(R.string.level_clear));
			mTitle.setTextAttr(Color.GREEN, 36);
			initNormalGameNotice();
			break;
		case SURVIVAL_EXIT_GAME_FAIL:
			break;
		}
		this.noticeType = noticeType;
	}

	private void initNormalGameNotice() {
		// menu
		W_Button btn = new W_Button(64, 64, 1,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_MENU_BTNS_64_64_BITMAP_KEY),
				new int[] { 0, 1 });
		btn.setmEventListener(new W_EventListener() {
			@Override
			public void event() {
				final ChooseLevelScene scene = (ChooseLevelScene) PublicVars.mStaticScenes
						.get(GameConstants.CHOOSE_LEVEL_SCENE);
				scene.setCurPage(PublicVars.mStaticPublicVars.mGameBoard
						.getCurLevel() / ChooseLevelScene.MAX_BTN_NUM_PER_PAGE);
				PublicVars.mStaticMainActivity
						.changeSceneBySceneId(GameConstants.CHOOSE_LEVEL_SCENE);
			}
		});
		mOptBtns.add(btn);
		// restart
		btn = new W_Button(64, 64, 1,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_MENU_BTNS_64_64_BITMAP_KEY),
				new int[] { 2, 3 });
		btn.setmEventListener(new W_EventListener() {
			@Override
			public void event() {
				if (PublicVars.mStaticPublicVars.mGameBoard.restartCurLevet()) {
					PublicVars.mStaticMainActivity
							.changeSceneBySceneId(GameConstants.GAMING_SCENE);
				} else {
					final ChooseLevelScene scene = (ChooseLevelScene) PublicVars.mStaticScenes
							.get(GameConstants.CHOOSE_LEVEL_SCENE);
					scene.setCurPage(PublicVars.mStaticPublicVars.mGameBoard
							.getCurLevel()
							/ ChooseLevelScene.MAX_BTN_NUM_PER_PAGE);
					PublicVars.mStaticMainActivity
							.changeSceneBySceneId(GameConstants.CHOOSE_LEVEL_SCENE);
				}
			}
		});
		mOptBtns.add(btn);
		if (PublicVars.mStaticPublicVars.mGameBoard.getCurLevel() < PublicVars.mStaticPublicVars.mPlayerInfo
				.getPlayerMaxLevel()) {
			// next
			btn = new W_Button(
					64,
					64,
					1,
					(Bitmap) PublicVars.mStaticCache
							.get(GameConstants.CACHE_MENU_BTNS_64_64_BITMAP_KEY),
					new int[] { 4, 5 });
			btn.setmEventListener(new W_EventListener() {
				@Override
				public void event() {
					if (PublicVars.mStaticPublicVars.mGameBoard.initNextLevet()) {
						PublicVars.mStaticMainActivity
								.changeSceneBySceneId(GameConstants.GAMING_SCENE);
					} else {
						final ChooseLevelScene scene = (ChooseLevelScene) PublicVars.mStaticScenes
								.get(GameConstants.CHOOSE_LEVEL_SCENE);
						scene.setCurPage(PublicVars.mStaticPublicVars.mGameBoard
								.getCurLevel()
								/ ChooseLevelScene.MAX_BTN_NUM_PER_PAGE);
						PublicVars.mStaticMainActivity
								.changeSceneBySceneId(GameConstants.CHOOSE_LEVEL_SCENE);
					}
				}
			});
			mOptBtns.add(btn);
		}
	}

	public List<W_Button> getOptBtns() {
		return mOptBtns;
	}

	public void setOptBtns(final List<W_Button> optBtns) {
		mOptBtns = optBtns;
	}

	public int getCurStarNum() {
		return mCurStarNum;
	}

	public void setCurStarNum(int curStarNum) {
		if (curStarNum > 3) {
			curStarNum = 3;
		}
		mCurStarNum = curStarNum;
	}

	@Override
	public void paint(final Canvas canvas) {
		super.paint(canvas);
		if (noticeType.equals(NoticeType.NORMAL_GAME_SUCCESS)
				|| noticeType.equals(NoticeType.NORMAL_GAME_FAIL)) {
			final int starnum = mCurStarNum;
			int tmpX = 160 - mGoodStar.getDstWidth() / 2 - DIS_BETWEEN_STAR
					- mGoodStar.getDstWidth();
			final int tmpY = mTitle.getY() + mTitle.getDstHeight() + 90;
			for (int j = 0; j < starnum; ++j) {
				mGoodStar.setPosition(tmpX, tmpY);
				mGoodStar.paint(canvas, mLM);
				tmpX += (mGoodStar.getDstWidth() + DIS_BETWEEN_STAR);
			}
			for (int j = starnum; j < 3; ++j) {
				mBadStar.setPosition(tmpX, tmpY);
				mBadStar.paint(canvas, mLM);
				tmpX += (mGoodStar.getDstWidth() + DIS_BETWEEN_STAR);
			}
		}
	}
}
