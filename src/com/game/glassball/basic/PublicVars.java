package com.game.glassball.basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.game.glassball.data.PlayerInfo;
import com.game.glassball.layers.GameBoard;
import com.game.glassball.layers.J2DBoxWorld;
import com.game.glassball.layers.MessageBars;
import com.game.glassball.scenes.AboutUsScene;
import com.game.glassball.scenes.ChooseLevelScene;
import com.game.glassball.scenes.ConfirmScene;
import com.game.glassball.scenes.GamingScene;
import com.game.glassball.scenes.HideLevelScene;
import com.game.glassball.scenes.NoticeScene;
import com.game.glassball.scenes.PauseScene;
import com.game.glassball.scenes.StartMenuScene;
import com.w_game.basic.W_MainActivity;
import com.w_game.basic.W_Scene;
import com.w_game.layers.W_BlackMask;
import com.w_game.layers.W_Sprite;
import com.w_game.utils.W_FileUtil;

public class PublicVars {
	/* 注释*******************************基本变量******************************* */
	public static PublicVars mStaticPublicVars;// 公共变量
	public static Context mStaticMainContext;
	public static SmashGlassActivity mStaticMainActivity;// 主Activity
	public static SparseArray<W_Scene> mStaticScenes;// TODO
	public static SparseArray<Object> mStaticCache;
	public static String mStaticDeviceId;
	public static Sound mStaticSound;
	/* 注释*******************************基本变量******************************* */

	/* 注释*******************************常量******************************* */
	/* 注释*******************************常量******************************* */

	/* 注释*******************************models******************************* */
	/* 注释*******************************layers******************************* */

	public boolean mIsDebug = false;

	public W_BlackMask mBlackMask = null;

	public MessageBars mMessageBars = null;

	public GameBoard mGameBoard = null;

	public J2DBoxWorld mJ2DBoxWorld = null;

	public PlayerInfo mPlayerInfo = null;

	public W_Sprite mBackground;

	private PublicVars(final SmashGlassActivity mainActivity) {
		mStaticMainContext = mainActivity;
		mStaticMainActivity = mainActivity;
		mStaticScenes = new SparseArray<W_Scene>(20);
		mStaticCache = new SparseArray<Object>(20);
		mStaticSound = new Sound(mStaticMainContext);
		// final TelephonyManager tm = (TelephonyManager)
		// PublicVars.mStaticMainContext
		// .getSystemService(Context.TELEPHONY_SERVICE);
		// mStaticDeviceId = (tm == null ? null : tm.getDeviceId());
	}

	// 利用单例模式生成一个PublicVars实例(在mainActivity中的Thread中调用)
	public static void InitializePublicVars(final SmashGlassActivity mainActivity) {
		if (mStaticPublicVars == null) {
			mStaticPublicVars = new PublicVars(mainActivity);
			mStaticPublicVars.firstInit();
		}
	}

	// 第一次的初始化工作，必须且只能调用一次
	public void firstInit() {
		W_FileUtil.setActivity(mStaticMainActivity);
		// 0. create cache data
		mStaticCache.put(GameConstants.CACHE_BACKGROUND_BITMAP_KEY,
				W_MainActivity.myDecodeResource(
						PublicVars.mStaticMainActivity.mResources,
						R.drawable.background));
		mStaticCache.put(GameConstants.CACHE_BALL_BITMAP_KEY, W_MainActivity
				.myDecodeResource(PublicVars.mStaticMainActivity.mResources,
						R.drawable.ball));
		mStaticCache.put(GameConstants.CACHE_IRON_64_64_BITMAP_KEY,
				W_MainActivity.myDecodeResource(
						PublicVars.mStaticMainActivity.mResources,
						R.drawable.ironblock_64_64));
		mStaticCache.put(GameConstants.CACHE_NORMAL_GLASS_64_64_BITMAP_KEY,
				W_MainActivity.myDecodeResource(
						PublicVars.mStaticMainActivity.mResources,
						R.drawable.glasses_64_64));
		mStaticCache.put(GameConstants.CACHE_SUPER_GLASS_64_64_BITMAP_KEY,
				W_MainActivity.myDecodeResource(
						PublicVars.mStaticMainActivity.mResources,
						R.drawable.superglasses_64_64));
		mStaticCache.put(GameConstants.CACHE_GLASS_4_64_64_BITMAP_KEY,
				W_MainActivity.myDecodeResource(
						PublicVars.mStaticMainActivity.mResources,
						R.drawable.glasses4_64_64));
		mStaticCache.put(GameConstants.CACHE_GLASS_5_64_64_BITMAP_KEY,
				W_MainActivity.myDecodeResource(
						PublicVars.mStaticMainActivity.mResources,
						R.drawable.glasses5_64_64));
		mStaticCache.put(GameConstants.CACHE_GLASS_6_64_64_BITMAP_KEY,
				W_MainActivity.myDecodeResource(
						PublicVars.mStaticMainActivity.mResources,
						R.drawable.glasses6_64_64));
		mStaticCache.put(GameConstants.CACHE_STAR_32_32_BITMAP_KEY,
				W_MainActivity.myDecodeResource(
						PublicVars.mStaticMainActivity.mResources,
						R.drawable.stars_32_32));
		mStaticCache.put(GameConstants.CACHE_STAR_16_16_BITMAP_KEY,
				W_MainActivity.myDecodeResource(
						PublicVars.mStaticMainActivity.mResources,
						R.drawable.stars_16_16));
		mStaticCache.put(GameConstants.CACHE_MENU_BTNS_64_64_BITMAP_KEY,
				W_MainActivity.myDecodeResource(
						PublicVars.mStaticMainActivity.mResources,
						R.drawable.menu_btns));
		// 1. define all scenes
		initAllScenes();

		// 2. define all public variables
		mPlayerInfo = new PlayerInfo();

		mBlackMask = new W_BlackMask(
				mStaticMainActivity.getmVirtualScreenWidth(),
				mStaticMainActivity.getmVirtualScreenHeight(), 1);

		mMessageBars = new MessageBars(
				mStaticMainActivity.getmVirtualScreenWidth(), 24, 1, 4);

		mBackground = new W_Sprite(320, 560, 0,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_BACKGROUND_BITMAP_KEY));

		mJ2DBoxWorld = new J2DBoxWorld(
				mStaticMainActivity.getmVirtualScreenWidth(),
				mStaticMainActivity.getmVirtualScreenHeight(), 0);

		mGameBoard = new GameBoard(
				mStaticMainActivity.getmVirtualScreenWidth(),
				mStaticMainActivity.getmVirtualScreenHeight(), 1, null);

		// 3. initialize data or read data from save file
		initOrReadDataFromFile();
	}

	private void addScene(final W_Scene scene) {
		final int sceneId = scene.getmSceneId();
		mStaticScenes.put(sceneId, scene);
		mStaticMainActivity.getmGameView().addGameScene(scene);
	}

	private void initAllScenes() {

		addScene(new StartMenuScene(GameConstants.STARTMENU_SCENE, 10,
				mStaticMainActivity.getmVirtualScreenWidth(),
				mStaticMainActivity.getmVirtualScreenHeight()));

		addScene(new ChooseLevelScene(GameConstants.CHOOSE_LEVEL_SCENE, 10,
				mStaticMainActivity.getmVirtualScreenWidth(),
				mStaticMainActivity.getmVirtualScreenHeight()));

		addScene(new GamingScene(GameConstants.GAMING_SCENE, 10,
				mStaticMainActivity.getmVirtualScreenWidth(),
				mStaticMainActivity.getmVirtualScreenHeight()));

		addScene(new PauseScene(GameConstants.PAUSE_SCENE, 10,
				mStaticMainActivity.getmVirtualScreenWidth(),
				mStaticMainActivity.getmVirtualScreenHeight()));

		addScene(new ConfirmScene(GameConstants.CONFIRM_SCENE, 10,
				mStaticMainActivity.getmVirtualScreenWidth(),
				mStaticMainActivity.getmVirtualScreenHeight()));

		addScene(new NoticeScene(GameConstants.NOTICE_SCENE, 10,
				mStaticMainActivity.getmVirtualScreenWidth(),
				mStaticMainActivity.getmVirtualScreenHeight()));

		addScene(new AboutUsScene(GameConstants.ABOUT_US_SCENE, 10,
				mStaticMainActivity.getmVirtualScreenWidth(),
				mStaticMainActivity.getmVirtualScreenHeight()));

		addScene(new HideLevelScene(GameConstants.HIDE_LEVEL_SCENE, 10,
				mStaticMainActivity.getmVirtualScreenWidth(),
				mStaticMainActivity.getmVirtualScreenHeight()));
		// mStaticMainActivity.changeSceneBySceneId(GameConstants.STARTMENU_SCENE);
	}

	private void initOrReadDataFromFile() {
		if (mIsDebug
				|| !W_FileUtil.isFileExist(PublicVars.mStaticMainContext
						.getString(R.string.game_data_file_name))) {
			W_FileUtil.copyAssetGenralFileToPhone(PublicVars.mStaticMainContext
					.getString(R.string.game_data_file_name),
					PublicVars.mStaticMainContext
							.getString(R.string.game_data_file_name));
		}
		if (mPlayerInfo.hasSaveFile() && mPlayerInfo.readData()) {
			// read all data from file and initialize game data
		} else {
			// manually initialize game data
			mPlayerInfo.setDiamondNum(0);
			mPlayerInfo.setGameMaxLevel(PlayerInfo.DEFAULT_GAME_MAX_LEVEL);
			mPlayerInfo.setPlayerMaxLevel(PlayerInfo.DEFAULT_PLAYER_MAX_LEVEL);
			mPlayerInfo.setHighestScore(0);
			mPlayerInfo.setStarOfevels("0");
		}
		// final ChooseLevelScene scene = (ChooseLevelScene) mStaticScenes
		// .get(GameConstants.CHOOSE_LEVEL_SCENE);
		// scene.setCurPage(0);
	}
}
