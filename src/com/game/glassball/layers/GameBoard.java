package com.game.glassball.layers;

import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint.Style;
import android.graphics.Path;

import com.game.glassball.basic.GameConstants;
import com.game.glassball.basic.PublicVars;
import com.game.glassball.basic.R;
import com.game.glassball.data.GlassType;
import com.game.glassball.data.NoticeType;
import com.game.glassball.scenes.NoticeScene;
import com.w_game.basic.W_LayerManager;
import com.w_game.basic.W_MainActivity;
import com.w_game.layers.W_SingleLineText;
import com.w_game.layers.W_Sprite;

public class GameBoard extends W_Sprite {
	public static final int BORDER_Y = 320;

	private int mCurPointerID = -1;// 当前对应的手指触屏ID,-1代表没有

	private final BallTail mBallTail;

	public Vector<Ball> mBalls;

	public Vector<Glass> mGlasses;

	private int mCurLevel;

	private Ball mCurBall;

	private int mBallLeftNum;

	private int mGlassLeftNum;

	private final W_Sprite mBallLeft;

	private final W_SingleLineText mBallLeftText;

	public GameBoard(final int width, final int height, final int level,
			final Bitmap bmp) {
		super(width, height, level, bmp);
		mBallTail = new BallTail(128, 16, 1, W_MainActivity.myDecodeResource(
				PublicVars.mStaticMainActivity.mResources,
				R.drawable.preparetail));
		mBalls = new Vector<Ball>(3);
		mGlasses = new Vector<Glass>(6);
		mBallLeft = new W_Sprite(32, 32, 1,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_BALL_BITMAP_KEY));
		mBallLeftText = new W_SingleLineText(64, 32, 1, null);
		mBallLeftText.setTextAttr(Color.WHITE, 20);
		mBallLeftText.setmAlignKind(W_SingleLineText.LEFT);
	}

	@Override
	public void paint(final Canvas canvas, final W_LayerManager lm) {
		synchronized (this) {
			// 画分界虚线
			mPaint.setStyle(Style.STROKE);
			mPaint.setColor(Color.GRAY);
			mPaint.setStrokeWidth(2);
			final Path path = new Path();
			path.moveTo(mX, BORDER_Y);
			path.lineTo(mX + mDstWidth, BORDER_Y);
			mPaint.setPathEffect(new DashPathEffect(new float[] { 12, 4 }, 0));
			canvas.drawPath(path, mPaint);

			for (final Glass glass : mGlasses) {
				glass.paint(canvas, lm);
			}
			mBallTail.paint(canvas, lm);
			for (final Ball ball : mBalls) {
				ball.paint(canvas, lm);
			}
			mBallLeft.paint(canvas, lm);
			mBallLeftText.paint(canvas, lm);
		}
	}

	@Override
	public void tick() {
		synchronized (this) {
			if (mGlassLeftNum <= 0) {
				if (allGlassesInvisible()) {
					levelClearEvents();
				}
			} else if (mBallLeftNum <= 0) {
				if (allBallsInvisible()) {
					gameOverEvents();
				}
			}
			for (final Glass glass : mGlasses) {
				glass.tick();
			}
			for (final Ball ball : mBalls) {
				ball.tick();
			}
		}
	}

	private boolean allGlassesInvisible() {
		for (final Glass glass : mGlasses) {
			if (glass.isVisible()
					&& !glass.getGlassType().equals(GlassType.IRON)) {
				return false;
			}
		}
		return true;
	}

	private boolean allBallsInvisible() {
		for (final Ball ball : mBalls) {
			if (ball.isVisible()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean myDownEvent(final int pointerID, final int x, final int y) {
		if (mCurPointerID != -1) {
			return false;
		}
		mCurPointerID = pointerID;
		if (x >= 0 && x <= mDstWidth && y >= BORDER_Y && y <= mDstHeight) {
			int fixedX = x;
			int fixedY = y;
			mCurBall = findFirstUnUsedBall();
			if (mCurBall != null) {
				if (x < mCurBall.getDstWidth() / 2) {
					fixedX = mCurBall.getDstWidth() / 2;
				} else if (x > mDstWidth - mCurBall.getDstWidth() / 2) {
					fixedX = mDstWidth - mCurBall.getDstWidth() / 2;
				}
				if (y < BORDER_Y + mCurBall.getDstHeight() / 2) {
					fixedY = BORDER_Y + mCurBall.getDstHeight() / 2;
				} else if (y > mDstHeight - mCurBall.getDstHeight() / 2) {
					fixedY = mDstHeight - mCurBall.getDstHeight() / 2;
				}
				mCurBall.setStartPoint(fixedX, fixedY);
				mBallTail.setStartPoint(fixedX, fixedY);
			}
		}
		return false;
	}

	@Override
	public boolean myUpEvent(final int pointerID, final int x, final int y) {
		if (mCurPointerID != pointerID || pointerID == -1) {
			return false;
		}
		final int fixedX = x;
		int fixedY = y;
		if (mCurBall != null) {
			if (y < BORDER_Y) {
				fixedY = BORDER_Y;
			} else if (y > mDstHeight) {
				fixedY = mDstHeight;
			}
			mCurBall.releaseBall(fixedX, fixedY);
			mBallTail.setVisible(false);
			mCurBall = null;
			--mBallLeftNum;
			mBallLeftText.setmContent(" x " + mBallLeftNum);
		}
		mCurPointerID = -1;
		return false;
	}

	@Override
	public boolean myMoveEvent(final int pointerID, final int x, final int y) {
		if (mCurPointerID != pointerID || pointerID == -1) {
			return false;
		}
		final int fixedX = x;
		int fixedY = y;
		if (mCurBall != null) {
			if (y < BORDER_Y) {
				fixedY = BORDER_Y;
			} else if (y > mDstHeight) {
				fixedY = mDstHeight;
			}
			mBallTail.setEndPoint(fixedX, fixedY);
		}
		return true;
	}

	@Override
	public void setPosition(final int x, final int y) {
		super.setPosition(x, y);
		mBallLeft.setPosition(10, 10);
		mBallLeftText.setPosition(mBallLeft.getX() + mBallLeft.getDstWidth(),
				mBallLeft.getY());
	}

	private Ball findFirstUnUsedBall() {
		for (final Ball ball : mBalls) {
			if (!ball.isIsUsed()) {
				return ball;
			}
		}
		return null;
	}

	public void addBall() {
		final Ball ball = new Ball(32, 32, 1,
				(Bitmap) PublicVars.mStaticCache
						.get(GameConstants.CACHE_BALL_BITMAP_KEY));
		mBalls.add(ball);
		++mBallLeftNum;
		mBallLeftText.setmContent(" x " + mBallLeftNum);
	}

	public void addGlasses(final int center_x, final int center_y,
			final GlassType glassType, final int level) {
		Glass glass = null;
		switch (glassType) {
		case IRON:
			glass = new Glass(64, 64, 1,
					(Bitmap) PublicVars.mStaticCache
							.get(GameConstants.CACHE_IRON_64_64_BITMAP_KEY),
					new int[] { 0 }, glassType);
			break;
		case NORMAL_GLASS:
			glass = new Glass(
					64,
					64,
					1,
					(Bitmap) PublicVars.mStaticCache
							.get(GameConstants.CACHE_NORMAL_GLASS_64_64_BITMAP_KEY),
					new int[] { 0, 1, 2, 3, 4, 5 }, glassType);
			++mGlassLeftNum;
			break;
		case SUPER_GLASS:
			glass = new Glass(
					64,
					64,
					1,
					(Bitmap) PublicVars.mStaticCache
							.get(GameConstants.CACHE_SUPER_GLASS_64_64_BITMAP_KEY),
					new int[] { 0, 1, 2, 3, 4, 5 }, glassType);
			++mGlassLeftNum;
			break;
		case GLASS_WITH_ONE_BALL:
			glass = new Glass(64, 64, 1,
					(Bitmap) PublicVars.mStaticCache
							.get(GameConstants.CACHE_GLASS_4_64_64_BITMAP_KEY),
					new int[] { 0, 1, 2, 3, 4, 5 }, glassType);
			++mGlassLeftNum;
			break;
		case GLASS_WITH_TWO_BALL:
			glass = new Glass(64, 64, 1,
					(Bitmap) PublicVars.mStaticCache
							.get(GameConstants.CACHE_GLASS_5_64_64_BITMAP_KEY),
					new int[] { 0, 1, 2, 3, 4, 5 }, glassType);
			++mGlassLeftNum;
			break;
		case GLASS_WITH_ONE_DIAMOND:
			glass = new Glass(64, 64, 1,
					(Bitmap) PublicVars.mStaticCache
							.get(GameConstants.CACHE_GLASS_6_64_64_BITMAP_KEY),
					new int[] { 0, 1, 2, 3, 4, 5 }, glassType);
			++mGlassLeftNum;
			break;
		}
		glass.createGlassInWorld(center_x, center_y);// TODO type
		glass.mDebugLevel = "" + (level + 1);
		mGlasses.add(glass);
	}

	public boolean destoryGlass(final Glass glass) {
		switch (glass.getGlassType()) {
		case IRON:
			return false;
		case NORMAL_GLASS:
			--mGlassLeftNum;
			glass.setJBoxBody(null);
			glass.setIsBroken(true);
			return true;
		case SUPER_GLASS:
			if (glass.getHitCount() == 0) {
				glass.setHitCount(glass.getHitCount() + 1);
				return false;
			} else {
				glass.setHitCount(glass.getHitCount() + 1);
				--mGlassLeftNum;
				glass.setJBoxBody(null);
				glass.setIsBroken(true);
				return true;
			}
		case GLASS_WITH_ONE_BALL:
			--mGlassLeftNum;
			glass.setJBoxBody(null);
			glass.setIsBroken(true);
			addBall();
			return true;
		case GLASS_WITH_TWO_BALL:
			--mGlassLeftNum;
			glass.setJBoxBody(null);
			glass.setIsBroken(true);
			addBall();
			addBall();
			return true;
		case GLASS_WITH_ONE_DIAMOND:
			--mGlassLeftNum;
			glass.setJBoxBody(null);
			glass.setIsBroken(true);
			addDiamond(1);
			return true;
		}
		return false;
	}

	private void addDiamond(final int i) {
		PublicVars.mStaticPublicVars.mPlayerInfo.addDiamond(1);
	}

	private void gameOverEvents() {
		PublicVars.mStaticPublicVars.mMessageBars.clear();
		final NoticeScene scene = (NoticeScene) PublicVars.mStaticScenes
				.get(GameConstants.NOTICE_SCENE);
		scene.setNoticeType(NoticeType.NORMAL_GAME_FAIL);
		scene.setCurStarNum(0);
		PublicVars.mStaticMainActivity
				.changeSceneBySceneId(GameConstants.NOTICE_SCENE);
	}

	private void levelClearEvents() {
		PublicVars.mStaticPublicVars.mMessageBars.clear();
		if (mCurLevel + 1 > PublicVars.mStaticPublicVars.mPlayerInfo
				.getPlayerMaxLevel()) {
			PublicVars.mStaticPublicVars.mPlayerInfo
					.setPlayerMaxLevel(mCurLevel + 1);
		}
		if (mBallLeftNum + 1 > PublicVars.mStaticPublicVars.mPlayerInfo
				.getLevelStar(mCurLevel)) {
			PublicVars.mStaticPublicVars.mPlayerInfo.setLevelStar(mCurLevel,
					mBallLeftNum + 1);
		}

		final NoticeScene scene = (NoticeScene) PublicVars.mStaticScenes
				.get(GameConstants.NOTICE_SCENE);
		scene.setNoticeType(NoticeType.NORMAL_GAME_SUCCESS);
		scene.setCurStarNum(mBallLeftNum + 1);
		PublicVars.mStaticMainActivity
				.changeSceneBySceneId(GameConstants.NOTICE_SCENE);
	}

	public boolean initGlassesAndBallByLevel(final int level) {
		mBallLeftNum = 0;
		mGlassLeftNum = 0;
		setCurBall(null);
		resetCurPointerID();
		synchronized (this) {
			PublicVars.mStaticPublicVars.mJ2DBoxWorld.clearWorld();
			mBallTail.setVisible(false);
			mBalls.clear();
			mGlasses.clear();
			if (PublicVars.mStaticPublicVars.mPlayerInfo
					.initGlassesAndBallByLevel(level)) {
				mCurLevel = level;
				return true;
			} else {
				return false;
			}
		}
	}

	public int getCurLevel() {
		return mCurLevel;
	}

	public void setCurLevel(final int curLevel) {
		this.mCurLevel = curLevel;
	}

	public boolean initNextLevet() {
		if (mCurLevel < PublicVars.mStaticPublicVars.mPlayerInfo
				.getPlayerMaxLevel()) {
			if (initGlassesAndBallByLevel(mCurLevel + 1)) {
				return true;
			} else {
				PublicVars.mStaticMainActivity
						.changeSceneBySceneId(GameConstants.STARTMENU_SCENE);
			}
		}
		return false;
	}

	public boolean restartCurLevet() {
		return initGlassesAndBallByLevel(mCurLevel);
	}

	public void resetCurPointerID() {
		mCurPointerID = -1;
	}

	public Ball getCurBall() {
		return this.mCurBall;
	}

	public void setCurBall(final Ball curBall) {
		this.mCurBall = curBall;
	}
}
