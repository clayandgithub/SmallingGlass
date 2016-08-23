package com.game.glassball.scenes;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;

import com.game.glassball.basic.GameConstants;
import com.game.glassball.basic.PublicVars;
import com.game.glassball.basic.R;
import com.w_game.basic.W_EventListener;
import com.w_game.basic.W_MainActivity;
import com.w_game.basic.W_Scene;
import com.w_game.layers.W_SingleLineText;
import com.w_game.layers.W_Sprite;
import com.w_game.layers.W_TextButton;

public class AboutUsScene extends W_Scene {

	private List<W_SingleLineText> mTexts;
	private W_TextButton mTextBackBtn;
	private W_Sprite mStudioPic;

	public AboutUsScene(final int sceneId, final int layerNum,
			final int viewWidth, final int viewHeight) {
		super(sceneId, layerNum, viewWidth, viewHeight);
	}

	@Override
	public void initScene() {
		mLM.removeAllLayers();
		if (mTexts == null) {
			mTexts = new LinkedList<W_SingleLineText>();
			mTexts.add(new W_SingleLineText(240, 32, 1,
					PublicVars.mStaticMainContext
							.getString(R.string.email_info)));
			mTexts.add(new W_SingleLineText(240, 32, 1,
					PublicVars.mStaticMainContext
							.getString(R.string.notice_info_1)));
			mTexts.add(new W_SingleLineText(240, 32, 1,
					PublicVars.mStaticMainContext
							.getString(R.string.notice_info_2)));
			mTexts.add(new W_SingleLineText(240, 32, 1,
					PublicVars.mStaticMainContext
							.getString(R.string.notice_info_3)));
			for (final W_SingleLineText text : mTexts) {
				if (text != null) {
					text.setTextAttr(Color.BLACK, 20);
					text.mAlignKind = W_SingleLineText.LEFT;
					// text.getPaint().setFakeBoldText(true);
				}
			}
		}
		for (final W_SingleLineText text : mTexts) {
			mLM.append(text);
		}
		if (mTextBackBtn == null) {
			mTextBackBtn = new W_TextButton(
					PublicVars.mStaticMainContext
							.getString(R.string.back_btn_name),
					64, 32, 1);
			mTextBackBtn.setmHasBorder(true);
			mTextBackBtn.setmBorderColor(Color.BLACK);
			mTextBackBtn.getmText().setTextAttr(Color.BLACK, 18);
			mTextBackBtn.setIsRoundRect(true);
			mTextBackBtn.setmHasBackground(true);
			mTextBackBtn.setNotOnBackGroundColor(Color.WHITE);
			mTextBackBtn.setmEventListener(new W_EventListener() {
				@Override
				public void event() {
					PublicVars.mStaticMainActivity
							.changeSceneBySceneId(GameConstants.STARTMENU_SCENE);
				}
			});
			// mTextBackBtn.setmOnBackGroundColor(Color.GRAY);
		}
		mTextBackBtn.resetBtn();
		mLM.append(mTextBackBtn);
		if (mStudioPic == null) {
			mStudioPic = new W_Sprite(320, 320, 1,
					W_MainActivity.myDecodeResource(
							PublicVars.mStaticMainActivity.mResources,
							R.drawable.mullet_studio));
		}
		mLM.append(mStudioPic);
		int startAlpha = PublicVars.mStaticPublicVars.mBlackMask.getmAlpha();
		startAlpha = (startAlpha == 0 ? 255 : startAlpha);
		PublicVars.mStaticPublicVars.mBlackMask.startDecreaseAlpha(startAlpha,
				0);
		mLM.append(PublicVars.mStaticPublicVars.mBlackMask);

		// initPosition
		initScenePosition();
	}

	@Override
	public void releaseScene() {
	}

	public void initScenePosition() {
		mStudioPic.setPosition(0, 0);
		final int tempX = 160;
		int tempY = 240;
		mTexts.get(0).setCenterPosition(tempX, tempY);
		tempY += 60;
		for (int i = 1; i < mTexts.size(); ++i) {
			mTexts.get(i).setCenterPosition(tempX, tempY);
			tempY += 40;
		}
		mTextBackBtn.setPosition(24, 10);
	}

	@Override
	public void paint(final Canvas canvas) {
		// canvas.drawARGB(255, 255, 255, 255);
		canvas.drawARGB(255, 250, 250, 250);
		// canvas.drawRGB(255,255,255);
		super.paint(canvas);
	}
}
