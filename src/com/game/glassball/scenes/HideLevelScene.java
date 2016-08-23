package com.game.glassball.scenes;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.graphics.Color;

import com.game.glassball.basic.GameConstants;
import com.game.glassball.basic.PublicVars;
import com.game.glassball.basic.R;
import com.game.glassball.layers.SuperTextButton;
import com.w_game.basic.W_EventListener;
import com.w_game.basic.W_Scene;
import com.w_game.layers.W_SingleLineText;
import com.w_game.layers.W_TextButton;

public class HideLevelScene extends W_Scene {

    private W_SingleLineText mHideLvTitle;
    private W_SingleLineText mHideLvInfo;
	private List<W_SingleLineText> mTexts;
	private W_TextButton mTextBackBtn;
	private SuperTextButton mShowSpotAdBtn;
	private final Random mRandom;

	public HideLevelScene(final int sceneId, final int layerNum,
			final int viewWidth, final int viewHeight) {
		super(sceneId, layerNum, viewWidth, viewHeight);
		mRandom = new Random(System.currentTimeMillis());
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void initScene() {
		mLM.removeAllLayers();
		mLM.append(PublicVars.mStaticPublicVars.mBackground);
		if (mHideLvTitle == null) {
		    mHideLvTitle = new W_SingleLineText(240, 64, 1, PublicVars.mStaticMainContext
                    .getString(R.string.hide_level_title));
		    mHideLvTitle.setTextAttr(Color.WHITE, 32);
		}
		mLM.append(mHideLvTitle);
		if (mHideLvInfo == null) {
			mHideLvInfo = new W_SingleLineText(240, 32, 1, PublicVars.mStaticMainContext
                    .getString(R.string.hide_level_info0));
			mHideLvInfo.setTextAttr(Color.WHITE, 20);
		}
		updateLeftHideLvText();
		mLM.append(mHideLvInfo);
		if (mTexts == null) {
			mTexts = new LinkedList<W_SingleLineText>();
			mTexts.add(new W_SingleLineText(240, 32, 1,
					PublicVars.mStaticMainContext
							.getString(R.string.hide_level_info1)));
			mTexts.add(new W_SingleLineText(240, 32, 1,
					PublicVars.mStaticMainContext
							.getString(R.string.hide_level_info2)));
			mTexts.add(new W_SingleLineText(240, 32, 1,
					PublicVars.mStaticMainContext
							.getString(R.string.hide_level_info3)));
			mTexts.add(new W_SingleLineText(240, 32, 1,
					PublicVars.mStaticMainContext
							.getString(R.string.hide_level_info4)));
			for (final W_SingleLineText text : mTexts) {
					text.setTextAttr(Color.WHITE, 20);
					text.mAlignKind = W_SingleLineText.LEFT;
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
			mTextBackBtn.getmText().setTextAttr(Color.WHITE, 18);
			mTextBackBtn.setIsRoundRect(true);
			mTextBackBtn.setmHasBackground(true);
			mTextBackBtn.setmEventListener(new W_EventListener() {
				@Override
				public void event() {
					PublicVars.mStaticMainActivity.hideYouMiSpotAd();
					PublicVars.mStaticMainActivity
							.changeSceneBySceneId(GameConstants.STARTMENU_SCENE);
				}
			});
		}
		mTextBackBtn.resetBtn();
		mLM.append(mTextBackBtn);
		if (mShowSpotAdBtn == null) {
			mShowSpotAdBtn = new SuperTextButton(
					PublicVars.mStaticMainContext
							.getString(R.string.view_spot_ad_btn),
					256, 64, 1);
			mShowSpotAdBtn.getmText().setTextAttr(Color.YELLOW, 20);
			mShowSpotAdBtn.setmEventListener(new W_EventListener() {
				@Override
				public void event() {
					if (mShowSpotAdBtn.getCurCD() == 0) {
					    if (PublicVars.mStaticPublicVars.mPlayerInfo
                                .getLeftHideLevel() == 0) {
					        PublicVars.mStaticPublicVars.mMessageBars
                            .addNewMessage(PublicVars.mStaticMainContext
                                    .getString(R.string.already_get_all_lv),
                                    Color.GREEN);
					        return;
					    }
					    if (mRandom.nextInt(100) > 20) {
					        mShowSpotAdBtn.setCD(200);
					        PublicVars.mStaticMainActivity.showYouMiSpotAd();
					    }
					    else {
					        mShowSpotAdBtn.setCD(60);
					        PublicVars.mStaticPublicVars.mMessageBars
                            .addNewMessage(PublicVars.mStaticMainContext
                                    .getString(R.string.try_again),
                                    Color.LTGRAY);
					    }
					} else {
						PublicVars.mStaticPublicVars.mMessageBars
								.addNewMessage(PublicVars.mStaticMainContext
										.getString(R.string.wait_a_second),
										Color.RED);
					}
				}
			});
		}
		mShowSpotAdBtn.resetBtn();
		mLM.append(mShowSpotAdBtn);
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
		PublicVars.mStaticPublicVars.mBackground.setPosition(0, 0);
		final int tempX = 160;
		int tempY = 100;
		mHideLvTitle.setCenterPosition(tempX, tempY);
		tempY += 80;
		mHideLvInfo.setCenterPosition(tempX, tempY);
		tempY += 60;
		mShowSpotAdBtn.setCenterPosition(tempX, tempY);
		tempY += 80;
		for (int i = 0; i < mTexts.size(); ++i) {
			mTexts.get(i).setCenterPosition(tempX, tempY);
			tempY += 40;
		}
		mTextBackBtn.setPosition(24, 10);
		PublicVars.mStaticPublicVars.mMessageBars.setPosition(0, 64);
	}

	public void updateLeftHideLvText() {
		mHideLvInfo.setmContent(
	                PublicVars.mStaticMainContext
	                        .getString(R.string.hide_level_info0)
	                        + PublicVars.mStaticPublicVars.mPlayerInfo
	                                .getLeftHideLevel());
	}
}
