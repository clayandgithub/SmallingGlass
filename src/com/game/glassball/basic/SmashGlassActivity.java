package com.game.glassball.basic;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.game.glassball.scenes.HideLevelScene;
import com.game.glassball.scenes.PauseScene;
import com.w_game.basic.W_MainActivity;

public class SmashGlassActivity extends W_MainActivity {

	private SpotDialogListener mSpotDialogListener;

	@Override
	protected void initSettings() {
		super.initSettings();// 务必在此位置调用super方法
		setWelcomView(R.layout.activity_basic);// 设置需要显示的欢迎画面
	}

	@Override
	protected void changeToGameView() {
		// TODO Auto-generated method stub
		addContentView(mGameView, new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	@Override
	protected void initDuringWelcome() {
		// 在显示欢迎画面的同时, 后台做一些耗时的初始化工作
		PublicVars.InitializePublicVars(this);
		initYouMiAd();
		changeSceneBySceneId(GameConstants.STARTMENU_SCENE);
	}

	public void initYouMiAd() {
		AdManager.getInstance(this).init("40d9f20e4140b47f",
				"bc6c712661a64f7b", false);
		// 加载插播资源
		SpotManager.getInstance(this).loadSpotAds();
		SpotManager.getInstance(this).setShowInterval(10);// 设置10秒的显示时间间隔
		SpotManager.getInstance(this).setSpotOrientation(
				SpotManager.ORIENTATION_PORTRAIT);
		mSpotDialogListener = new SpotDialogListener() {
            @Override
            public void onShowSuccess() {
            }

            @Override
            public void onShowFailed() {
                PublicVars.mStaticPublicVars.mMessageBars
                        .addNewMessage(PublicVars.mStaticMainContext
                                .getString(R.string.check_internet),
                                Color.RED);
            }

            @Override
            public void onSpotClosed() {
                if (PublicVars.mStaticPublicVars.mPlayerInfo
                        .addViewAdCount()) {
                    final HideLevelScene scene = (HideLevelScene) PublicVars.mStaticScenes.get(GameConstants.HIDE_LEVEL_SCENE);
                    scene.updateLeftHideLvText();
                    PublicVars.mStaticPublicVars.mMessageBars.addNewMessage(
                            PublicVars.mStaticMainContext
                                    .getString(R.string.get_hide_lv_success),
                            Color.GREEN);
                }
            }
        };
	}

	public void showYouMiSpotAd() {
		// SpotManager.getInstance(this).showSpotAds(this);
		// 展示插播广告，可以不调用loadSpot独立使用
		SpotManager.getInstance(this).showSpotAds(this, mSpotDialogListener);
	}

	public void hideYouMiSpotAd() {
		SpotManager.getInstance(this).disMiss(true);
	}

	@Override
	public void onBackPressed() {
		switch (getmCurSceneId()) {
		case GameConstants.STARTMENU_SCENE:
			PublicVars.mStaticPublicVars.mPlayerInfo.exitGame();
			break;
		case GameConstants.CHOOSE_LEVEL_SCENE:
			changeSceneBySceneId(GameConstants.STARTMENU_SCENE);
			break;
		case GameConstants.ABOUT_US_SCENE:
			changeSceneBySceneId(GameConstants.STARTMENU_SCENE);
			break;
		case GameConstants.HIDE_LEVEL_SCENE:
			PublicVars.mStaticMainActivity.hideYouMiSpotAd();
			changeSceneBySceneId(GameConstants.STARTMENU_SCENE);
			break;
		case GameConstants.GAMING_SCENE:
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
			changeSceneBySceneId(GameConstants.PAUSE_SCENE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		// 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
		SpotManager.getInstance(this).disMiss(false);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).unregisterSceenReceiver();
		super.onDestroy();
	}
}