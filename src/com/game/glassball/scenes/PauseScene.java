package com.game.glassball.scenes;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;

import com.game.glassball.basic.GameConstants;
import com.game.glassball.basic.PublicVars;
import com.game.glassball.basic.R;
import com.w_game.basic.W_EventListener;
import com.w_game.basic.W_Scene;
import com.w_game.layers.W_SingleLineText;
import com.w_game.layers.W_Sprite;
import com.w_game.layers.W_TextButton;

public class PauseScene extends W_Scene {

	private static final int LENGTH = 256;

	private boolean mIsSurvivalMode;

	private final W_SingleLineText mGameStatusInfo;

	private final List<W_SingleLineText> mPs;

	private final W_TextButton mResumeBtn;

	private final W_TextButton mNextBtn;

	private final W_TextButton mMainMenuBtn;

	private final W_TextButton mExitGameBtn;

	private W_Sprite mScreenShot;

	public PauseScene(final int sceneId, final int layerNum,
			final int viewWidth, final int viewHeight) {
		super(sceneId, layerNum, viewWidth, viewHeight);
		mGameStatusInfo = new W_SingleLineText(LENGTH, 64, 1, null);
		mGameStatusInfo.setTextAttr(Color.WHITE, 36);
		mPs = new LinkedList<W_SingleLineText>();
		mPs.add(new W_SingleLineText(LENGTH, 32, 1,
				PublicVars.mStaticMainContext.getString(R.string.ps_info1)));
		mPs.add(new W_SingleLineText(LENGTH, 32, 1,
				PublicVars.mStaticMainContext.getString(R.string.ps_info2)));
		for (final W_SingleLineText text : mPs) {
			text.setTextAttr(Color.YELLOW, 16);
		}
		mResumeBtn = new W_TextButton(
				PublicVars.mStaticMainContext.getString(R.string.back_to_game),
				LENGTH, 64, 1);
		mResumeBtn.setmHasBackground(true);
		mResumeBtn.setmHasBorder(true);
		mResumeBtn.getmText().setmIsRoundRect(true);
		mResumeBtn.setmEventListener(new W_EventListener() {
			@Override
			public void event() {
				if (!mIsSurvivalMode) {
					PublicVars.mStaticMainActivity
							.changeSceneBySceneId(GameConstants.GAMING_SCENE);
				} else {
					// TODO
				}
			}
		});
		mNextBtn = new W_TextButton(
				PublicVars.mStaticMainContext.getString(R.string.next_level),
				LENGTH, 64, 1);
		mNextBtn.setmHasBackground(true);
		mNextBtn.setmHasBorder(true);
		mNextBtn.getmText().setmIsRoundRect(true);
		mNextBtn.setmEventListener(new W_EventListener() {
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
		mMainMenuBtn = new W_TextButton(
				PublicVars.mStaticMainContext.getString(R.string.choose_level),
				LENGTH, 64, 1);
		mMainMenuBtn.setmHasBackground(true);
		mMainMenuBtn.setmHasBorder(true);
		mMainMenuBtn.getmText().setmIsRoundRect(true);
		mMainMenuBtn.setmEventListener(new W_EventListener() {
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
		mExitGameBtn = new W_TextButton(
				PublicVars.mStaticMainContext.getString(R.string.exit_game),
				LENGTH, 64, 1);
		mExitGameBtn.setmHasBackground(true);
		mExitGameBtn.setmHasBorder(true);
		mExitGameBtn.getmText().setmIsRoundRect(true);
		mExitGameBtn.setmEventListener(new W_EventListener() {
			@Override
			public void event() {
				PublicVars.mStaticPublicVars.mPlayerInfo.exitGame();
			}
		});
	}

	@Override
	public void initScene() {
		mLM.removeAllLayers();
		mScreenShot = PublicVars.mStaticMainActivity.getScreenShot();
		mLM.append(mScreenShot);
		PublicVars.mStaticPublicVars.mBlackMask.setNoChangeAlpha(120);
		mLM.append(PublicVars.mStaticPublicVars.mBlackMask);
		mLM.append(mGameStatusInfo);
		// final Random rand = new Random();
		// if (rand.nextInt(100) <
		// (PublicVars.mStaticPublicVars.mPlayerInfo.getPlayerMaxLevel() / 2)) {
		// for(final W_SingleLineText text : mPs) {
		// mLM.append(text);
		// }
		// }
		mResumeBtn.resetBtn();
		mLM.append(mResumeBtn);
		mNextBtn.resetBtn();
		mLM.append(mNextBtn);
		mMainMenuBtn.resetBtn();
		mLM.append(mMainMenuBtn);
		mExitGameBtn.resetBtn();
		mLM.append(mExitGameBtn);
		// initPosition
		initScenePosition();
	}

	@Override
	public void releaseScene() {
		mScreenShot = null;
	}

	public void initScenePosition() {
		final int tempX = 160;
		int tempY = 96;
		mGameStatusInfo.setCenterPosition(tempX, tempY);
		tempY += 80;
		mResumeBtn.setCenterPosition(tempX, tempY);
		tempY += 80;
		mNextBtn.setCenterPosition(tempX, tempY);
		tempY += 80;
		mMainMenuBtn.setCenterPosition(tempX, tempY);
		tempY += 80;
		mExitGameBtn.setCenterPosition(tempX, tempY);
		tempY += 64;
		for (final W_SingleLineText text : mPs) {
			text.setCenterPosition(tempX, tempY);
			tempY += 32;
		}
	}

	public boolean isIsSurvivalMode() {
		return mIsSurvivalMode;
	}

	public void setIsSurvivalMode(final boolean isSurvivalMode) {
		this.mIsSurvivalMode = isSurvivalMode;
	}

	public W_SingleLineText getGameStatusInfo() {
		return this.mGameStatusInfo;
	}
}
