package com.game.glassball.scenes;

import android.graphics.Bitmap;

import com.game.glassball.basic.GameConstants;
import com.game.glassball.basic.PublicVars;
import com.game.glassball.basic.R;
import com.w_game.basic.W_EventListener;
import com.w_game.basic.W_MainActivity;
import com.w_game.basic.W_Scene;
import com.w_game.layers.W_Button;
import com.w_game.layers.W_Sprite;

public class GamingScene extends W_Scene {

	private final W_Sprite mBackground;
	private final W_Sprite mExample;
	private final W_Button mPauseBtn;
	private final W_Button mRefreshBtn;

	public GamingScene(final int sceneId, final int layerNum,
			final int viewWidth, final int viewHeight) {
		super(sceneId, layerNum, viewWidth, viewHeight);
		mBackground = new W_Sprite(320, 560, 0,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_BACKGROUND_BITMAP_KEY));
		mExample = new W_Sprite(150, 150, 0, W_MainActivity.myDecodeResource(
				PublicVars.mStaticMainActivity.mResources, R.drawable.example));
		mPauseBtn = new W_Button(64, 64, 1,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_MENU_BTNS_64_64_BITMAP_KEY),
				new int[] { 6, 7 });
		mPauseBtn.setDstSize(32, 32);
		mPauseBtn.setmEventListener(new W_EventListener() {
			@Override
			public void event() {
				final PauseScene scene = (PauseScene) PublicVars.mStaticScenes
						.get(GameConstants.PAUSE_SCENE);
				scene.setIsSurvivalMode(false);
				scene.getGameStatusInfo()
						.setmContent(
								PublicVars.mStaticMainContext
										.getString(R.string.level_info)
										.replace(
												"LEVEL_NUM",
												String.valueOf(PublicVars.mStaticPublicVars.mGameBoard
														.getCurLevel() + 1)));
				PublicVars.mStaticMainActivity
						.changeSceneBySceneId(GameConstants.PAUSE_SCENE);
			}
		});
		mRefreshBtn = new W_Button(64, 64, 1,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_MENU_BTNS_64_64_BITMAP_KEY),
				new int[] { 2, 3 });
		mRefreshBtn.setDstSize(32, 32);
		mRefreshBtn.setmEventListener(new W_EventListener() {
			@Override
			public void event() {
				PublicVars.mStaticPublicVars.mBlackMask.startDecreaseAlpha(255,
						0);
				if (!PublicVars.mStaticPublicVars.mGameBoard.restartCurLevet()) {
					PublicVars.mStaticMainActivity
							.changeSceneBySceneId(GameConstants.STARTMENU_SCENE);
				}
			}
		});
	}

	@Override
	public void initScene() {
		mLM.removeAllLayers();
		// TODO PublicVars.mStaticPublicVars.mMessageBars.clear();
		mLM.append(PublicVars.mStaticPublicVars.mJ2DBoxWorld);
		mLM.append(mBackground);
		mLM.append(mExample);
		mLM.append(PublicVars.mStaticPublicVars.mGameBoard);
		mPauseBtn.resetBtn();
		mLM.append(mPauseBtn);
		mRefreshBtn.resetBtn();
		mLM.append(mRefreshBtn);

		int startAlpha = PublicVars.mStaticPublicVars.mBlackMask.getmAlpha();
		startAlpha = (startAlpha == 0 ? 255 : startAlpha);
		PublicVars.mStaticPublicVars.mBlackMask.startDecreaseAlpha(startAlpha,
				0);
		mLM.append(PublicVars.mStaticPublicVars.mBlackMask);
		mLM.append(PublicVars.mStaticPublicVars.mMessageBars);

		// initPosition
		initScenePosition();
	}

	@Override
	public void releaseScene() {
	}

	private void initScenePosition() {
		mBackground.setPosition(0, 0);
		mExample.setCenterPosition(160, 440);
		PublicVars.mStaticPublicVars.mJ2DBoxWorld.setPosition(0, 0);
		PublicVars.mStaticPublicVars.mGameBoard.setPosition(0, 0);
		mPauseBtn.setPosition(320 - mPauseBtn.getDstWidth() - 10, 10);
		mRefreshBtn.setPosition(mPauseBtn.getX() - 60, 10);
		PublicVars.mStaticPublicVars.mMessageBars.setPosition(0, 64);
	}
}