package com.game.glassball.data;

import java.security.Key;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

import com.game.glassball.basic.PublicVars;
import com.game.glassball.basic.R;
import com.game.glassball.util.StringUtil;
import com.w_game.utils.W_DESUtil;
import com.w_game.utils.W_FileUtil;
import com.w_game.utils.W_PreferenceUtil;
import com.w_game.utils.W_StringUtil;

public class PlayerInfo {

	public static final int REAL_GAME_MAX_LEVEL = 99;

	public static final int DEFAULT_GAME_MAX_LEVEL = 79;

	public static final int DEFAULT_PLAYER_MAX_LEVEL = 0;

	private String mStarOfLevels;

	private int mDiamondNum;

	private int mPlayerMaxLevel;

	private int mGameMaxLevel;

	private int mHighestScore;

	private int mViewAdCount;

	private Key mRealKey;
	
	private boolean mIsEncrypted = true;

	public PlayerInfo() {
		if(mIsEncrypted) {
			mRealKey = getDESKey();
			if (mRealKey == null) {
				mIsEncrypted = false;
			}
		}
	}

	public int getHighestScore() {
		return mHighestScore;
	}

	public void setHighestScore(final int highestScore) {
		this.mHighestScore = highestScore;
	}

	public int getPlayerMaxLevel() {
		return mPlayerMaxLevel;
	}

	public void setPlayerMaxLevel(final int playerMaxLevel) {
		if (playerMaxLevel >= 0 && playerMaxLevel <= mGameMaxLevel) {
			mPlayerMaxLevel = playerMaxLevel;
		}
	}

	public int getGameMaxLevel() {
		return mGameMaxLevel;
	}

	public void setGameMaxLevel(final int gameMaxLevel) {
		mGameMaxLevel = gameMaxLevel;
	}

	public int getDiamondNum() {
		return mDiamondNum;
	}

	public void setDiamondNum(final int diamondNum) {
		this.mDiamondNum = diamondNum;
	}

	public String getStarOfLevels() {
		return mStarOfLevels;
	}

	public void setStarOfevels(final String starOfLevels) {
		mStarOfLevels = starOfLevels;
	}

	public boolean hasSaveFile() {
		return W_FileUtil.isFileExist(PublicVars.mStaticMainContext
				.getString(R.string.save_file_name));
	}

	public Key getDESKey() {
		Key retKey;
		// 1. get setting string from preference(if not exist, then create)
		String desKeyStr = W_PreferenceUtil.quickReadPreference(
				PublicVars.mStaticMainContext
						.getString(R.string.preference_file_name),
				PublicVars.mStaticMainActivity, PublicVars.mStaticMainContext
						.getString(R.string.yek_sed_name), null);
		if (desKeyStr == null) {
			W_FileUtil.deleteFileIfExist(PublicVars.mStaticMainContext
					.getString(R.string.save_file_name));
			// 2. generate des key from randStr
			String randstr = StringUtil.genRandomString(40);
			retKey = W_DESUtil.genKey(randstr);
			// 3. save desKeyStr to preference
			desKeyStr = W_DESUtil.wrapDESKeyToStr(retKey);
			W_PreferenceUtil.quickWritePreference(PublicVars.mStaticMainContext
					.getString(R.string.preference_file_name),
					PublicVars.mStaticMainActivity,
					PublicVars.mStaticMainContext
							.getString(R.string.yek_sed_name), desKeyStr);
		} else {
			retKey = W_DESUtil.unWrapStrToDESKey(desKeyStr);
		}
		return retKey;
	}

	public boolean saveData() {
		try {
			final JSONObject sav = new JSONObject();
			sav.put("D", mDiamondNum);
			sav.put("G", mGameMaxLevel);
			sav.put("P", mPlayerMaxLevel);
			sav.put("H", mHighestScore);
			sav.put("S", mStarOfLevels);
			sav.put("V", mViewAdCount);
			String savStr = sav.toString();
			if (mIsEncrypted) {
				savStr = W_DESUtil.encryptString(savStr, mRealKey);
				if (savStr == null) {
					PublicVars.mStaticPublicVars.mMessageBars.addNewMessage(
							PublicVars.mStaticMainContext
							.getString(R.string.failed_in_save), Color.RED);
					return false;
				}
			}
			W_FileUtil.writeFile(PublicVars.mStaticMainContext
					.getString(R.string.save_file_name), savStr);
		} catch (final JSONException e) {
			PublicVars.mStaticPublicVars.mMessageBars.addNewMessage(
					PublicVars.mStaticMainContext
					.getString(R.string.failed_in_save), Color.RED);
			return false;
		}
		return true;
	}

	public boolean readData() {
		if (!hasSaveFile()) {
			PublicVars.mStaticPublicVars.mMessageBars.addNewMessage(
					PublicVars.mStaticMainContext
					.getString(R.string.failed_in_read), Color.RED);
			return false;
		}
		String readStr = W_FileUtil
				.readFile(PublicVars.mStaticMainContext
						.getString(R.string.save_file_name));
		if (readStr == null) {
			PublicVars.mStaticPublicVars.mMessageBars.addNewMessage(
					PublicVars.mStaticMainContext
					.getString(R.string.failed_in_read), Color.RED);
			return false;
		}
		if (mIsEncrypted) {
			readStr = W_DESUtil.decryptString(readStr, mRealKey);
		}
		try {
			final JSONObject read = new JSONObject(readStr);
			mDiamondNum = read.getInt("D");
			mGameMaxLevel = read.getInt("G");
			mPlayerMaxLevel = read.getInt("P");
			mHighestScore = read.getInt("H");
			mStarOfLevels = read.getString("S");
			mViewAdCount = read.getInt("V");
		} catch (final JSONException e) {
			PublicVars.mStaticPublicVars.mMessageBars.addNewMessage(
					PublicVars.mStaticMainContext
					.getString(R.string.failed_in_read), Color.RED);
			return false;
		}
		return true;
	}

	public void exitGame() {
		saveData();
		System.exit(0);
	}

	public void setLevelStar(final int level, int starNum) {
		if (level < 0) {
			return;
		}
		if (starNum > 3) {
			starNum = 3;
		}
		while (mStarOfLevels.length() <= level) {
			mStarOfLevels = mStarOfLevels + "0";
		}
		final char[] charArray = mStarOfLevels.toCharArray();
		charArray[level] = (char) ('0' + starNum);
		mStarOfLevels = String.valueOf(charArray);
	}

	public int getLevelStar(final int level) {
		if (level < mStarOfLevels.length()) {
			return mStarOfLevels.charAt(level) - '0';
		}
		return 0;
	}

	public void addDiamond(final int num) {
		mDiamondNum += num;
	}

	public boolean initGlassesAndBallByLevel(final int level) {
		saveData();
		final String lvDataString = getLvDataString(level);
		if (lvDataString != null && lvDataString.length() != 0) {
			final List<Integer> dataIntArray = W_StringUtil.stringToIntList(
					lvDataString, ",");
			// add balls
			final int ballNum = dataIntArray.get(0);
			for (int i = 0; i < ballNum; ++i) {
				PublicVars.mStaticPublicVars.mGameBoard.addBall();
			}
			// add glasses
			for (int i = 1; i < dataIntArray.size(); ++i) {
				final int type = dataIntArray.get(i) / 100;
				final int position = dataIntArray.get(i) % 100;
				final int center_x = (position / 10) * 40 + 20;
				final int center_y = (position % 10) * 40 + 20;
				PublicVars.mStaticPublicVars.mGameBoard.addGlasses(center_x,
						center_y, GlassType.toEnum(type), level);
			}
			return true;
		}
		return false;
	}

	private String getLvDataString(final int level) {
		// TODO decrypt?
		final String string = W_FileUtil.readFile(PublicVars.mStaticMainContext
				.getString(R.string.game_data_file_name));
		try {
			final JSONObject read = new JSONObject(string);
			return read.getString(String.valueOf(level));
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getViewAdCount() {
		return mViewAdCount;
	}

	public void setViewAdCount(final int viewAdCount) {
		this.mViewAdCount = viewAdCount;
	}

	public boolean addViewAdCount() {
		++mViewAdCount;
		if (mGameMaxLevel < REAL_GAME_MAX_LEVEL) {
			++mGameMaxLevel;
			saveData();
			return true;
		}
		return false;
	}

	public int getLeftHideLevel() {
		final int allHideLevelNum = REAL_GAME_MAX_LEVEL
				- DEFAULT_GAME_MAX_LEVEL;
		return Math.max(allHideLevelNum - mViewAdCount, 0);
	}
}
