package com.game.glassball.scenes;

import android.graphics.Color;

import com.game.glassball.basic.GameConstants;
import com.game.glassball.basic.PublicVars;
import com.game.glassball.basic.R;
import com.game.glassball.layers.SuperTextButton;
import com.w_game.basic.W_EventListener;
import com.w_game.basic.W_MainActivity;
import com.w_game.basic.W_Scene;
import com.w_game.layers.W_Sprite;

public class StartMenuScene extends W_Scene {

	private W_Sprite mGameTitle;
	private SuperTextButton mNormalGameBtn;
	private SuperTextButton mHideLevelBtn;
	private SuperTextButton mAboutUsBtn;
	private SuperTextButton mExitGameBtn;

	public StartMenuScene(final int sceneId, final int layerNum,
			final int viewWidth, final int viewHeight) {
		super(sceneId, layerNum, viewWidth, viewHeight);
	}

	@Override
	public void initScene() {
		mLM.removeAllLayers();
		mLM.append(PublicVars.mStaticPublicVars.mBackground);
		mGameTitle = new W_Sprite(240, 100, 1, W_MainActivity.myDecodeResource(
				PublicVars.mStaticMainActivity.mResources, R.drawable.title));
		mLM.append(mGameTitle);

		mNormalGameBtn = new SuperTextButton(
				PublicVars.mStaticMainContext.getString(R.string.start_game),
				180, 50, 1);
		mNormalGameBtn.getText().setTextAttr(Color.WHITE, 20);
		mNormalGameBtn.setmEventListener(new W_EventListener() {
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
		mNormalGameBtn.resetBtn();
		mLM.append(mNormalGameBtn);

		mHideLevelBtn = new SuperTextButton(
				PublicVars.mStaticMainContext.getString(R.string.hide_level),
				180, 50, 1);
		mHideLevelBtn.getText().setTextAttr(Color.WHITE, 20);
		mHideLevelBtn.setmEventListener(new W_EventListener() {
			@Override
			public void event() {
				PublicVars.mStaticMainActivity
						.changeSceneBySceneId(GameConstants.HIDE_LEVEL_SCENE);
			}
		});
		mHideLevelBtn.resetBtn();
		mLM.append(mHideLevelBtn);

		mAboutUsBtn = new SuperTextButton(
				PublicVars.mStaticMainContext.getString(R.string.about_us),
				180, 50, 1);
		mAboutUsBtn.getText().setTextAttr(Color.WHITE, 20);
		mAboutUsBtn.setmEventListener(new W_EventListener() {
			@Override
			public void event() {
				PublicVars.mStaticMainActivity
						.changeSceneBySceneId(GameConstants.ABOUT_US_SCENE);
			}
		});
		mAboutUsBtn.resetBtn();
		mLM.append(mAboutUsBtn);

		mExitGameBtn = new SuperTextButton(
				PublicVars.mStaticMainContext.getString(R.string.exit_game),
				180, 50, 1);
		mExitGameBtn.getText().setTextAttr(Color.WHITE, 20);
		mExitGameBtn.setmEventListener(new W_EventListener() {
			@Override
			public void event() {
				PublicVars.mStaticPublicVars.mPlayerInfo.exitGame();
			}
		});
		mExitGameBtn.resetBtn();
		mLM.append(mExitGameBtn);
		mLM.append(PublicVars.mStaticPublicVars.mMessageBars);

		// int startAlpha = PublicVars.mStaticPublicVars.mBlackMask.getmAlpha();
		// startAlpha = (startAlpha == 0 ? 120 : startAlpha);
		// startAlpha = 120;
		// PublicVars.mStaticPublicVars.mBlackMask.setNoChangeAlpha(startAlpha);
		// PublicVars.mStaticPublicVars.mBlackMask.startDecreaseAlpha(startAlpha,
		// 0);
		// mLM.append(PublicVars.mStaticPublicVars.mBlackMask);

		// initPosition
		initScenePosition();
	}

	@Override
	public void releaseScene() {
		mGameTitle = null;
		mNormalGameBtn = null;
		mHideLevelBtn = null;
		mAboutUsBtn = null;
		mExitGameBtn = null;
	}

	public void initScenePosition() {
		mGameTitle.setCenterPosition(160, 100);

		final int diffY = 70;

		final int tempX = 160 - mNormalGameBtn.getDstWidth() / 2;
		int tempY = mGameTitle.getY() + mGameTitle.getDstHeight() + 70;

		mNormalGameBtn.setPosition(-mNormalGameBtn.getDstWidth(), tempY);
		mNormalGameBtn.setDstPosition(tempX, tempY);
		mNormalGameBtn.setmMovingSpeed(48);

		tempY += diffY;
		mHideLevelBtn.setPosition(320, tempY);
		mHideLevelBtn.setDstPosition(tempX, tempY);
		mHideLevelBtn.setmMovingSpeed(48);

		tempY += diffY;
		mAboutUsBtn.setPosition(-mAboutUsBtn.getDstWidth(), tempY);
		mAboutUsBtn.setDstPosition(tempX, tempY);
		mAboutUsBtn.setmMovingSpeed(48);

		tempY += diffY;
		mExitGameBtn.setPosition(320, tempY);
		mExitGameBtn.setDstPosition(tempX, tempY);
		mExitGameBtn.setmMovingSpeed(48);

		PublicVars.mStaticPublicVars.mMessageBars.setPosition(0, 64);

		PublicVars.mStaticPublicVars.mBlackMask.setPosition(0, 0);
	}
}
