package com.game.glassball.scenes;

import android.graphics.Color;

import com.game.glassball.basic.GameConstants;
import com.game.glassball.basic.PublicVars;
import com.game.glassball.basic.R;
import com.game.glassball.data.ConfirmType;
import com.w_game.basic.W_EventListener;
import com.w_game.basic.W_Scene;
import com.w_game.layers.W_SingleLineText;
import com.w_game.layers.W_Sprite;
import com.w_game.layers.W_TextButton;

public class ConfirmScene extends W_Scene {

	private static final int LENGTH = 256;

	private ConfirmType mConfirmType;

	private final W_SingleLineText mInfo;

	private final W_TextButton mYesBtn;

	private final W_TextButton mNoBtn;

	private W_Sprite mScreenShot;

	public ConfirmScene(final int sceneId, final int layerNum,
			final int viewWidth, final int viewHeight) {
		super(sceneId, layerNum, viewWidth, viewHeight);
		mInfo = new W_SingleLineText(LENGTH, 64, 1, null);
		mInfo.setTextAttr(Color.WHITE, 36);
		mYesBtn = new W_TextButton(
				PublicVars.mStaticMainContext.getString(R.string.yes_name), 64,
				64, 1);
		mYesBtn.setmHasBackground(true);
		mYesBtn.setmHasBorder(true);
		mYesBtn.getmText().setmIsRoundRect(true);
		mNoBtn = new W_TextButton(
				PublicVars.mStaticMainContext.getString(R.string.no_name), 64,
				64, 1);
		mNoBtn.setmHasBackground(true);
		mNoBtn.setmHasBorder(true);
		mNoBtn.getmText().setmIsRoundRect(true);
	}

	@Override
	public void initScene() {
		mLM.removeAllLayers();
		mScreenShot = PublicVars.mStaticMainActivity.getScreenShot();
		mLM.append(mScreenShot);
		PublicVars.mStaticPublicVars.mBlackMask.startIncreaseAlpha(0, 120);
		mLM.append(PublicVars.mStaticPublicVars.mBlackMask);
		mLM.append(mInfo);
		mYesBtn.resetBtn();
		mLM.append(mYesBtn);
		mNoBtn.resetBtn();
		mLM.append(mNoBtn);
		// initPosition
		initScenePosition();
	}

	@Override
	public void releaseScene() {
		mScreenShot = null;
	}

	public void initScenePosition() {
		int tempX = 160;
		int tempY = 220;
		mInfo.setCenterPosition(tempX, tempY);
		tempX = mInfo.getX() + 32;
		tempY = mInfo.getY() + mInfo.getDstHeight() + 64;
		mYesBtn.setPosition(tempX, tempY);
		tempX = mInfo.getX() + mInfo.getDstWidth() - 32 - mYesBtn.getDstWidth();
		mNoBtn.setPosition(tempX, tempY);
	}

	public ConfirmType getConfirmType() {
		return mConfirmType;
	}

	public void setConfirmType(final ConfirmType confirmType) {
		this.mConfirmType = confirmType;
		switch (mConfirmType) {
		case NORMAL_EXIT_GAME:
			mInfo.setmContent(PublicVars.mStaticMainContext
					.getString(R.string.confirm_exit));
			mYesBtn.setmEventListener(new W_EventListener() {
				@Override
				public void event() {
					PublicVars.mStaticPublicVars.mPlayerInfo.exitGame();
				}
			});
			mNoBtn.setmEventListener(new W_EventListener() {
				@Override
				public void event() {
					PublicVars.mStaticMainActivity
							.changeSceneBySceneId(GameConstants.GAMING_SCENE);
				}
			});
			break;
		default:
			break;
		}
	}

}
