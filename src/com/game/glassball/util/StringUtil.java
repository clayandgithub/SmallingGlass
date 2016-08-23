package com.game.glassball.util;

import java.util.Random;

import com.w_game.utils.W_SHA1Util;

public class StringUtil {
	private static final String RANDOM_STRING_BASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

	public StringUtil() {
	}

	public static String genRandomString(final int length) {
		final Random random = new Random();
		final StringBuilder retSb = new StringBuilder();
		final int baseLength = RANDOM_STRING_BASE.length();
		for (int i = 0; i < length; ++i) {

			retSb.append(RANDOM_STRING_BASE.charAt(random.nextInt(baseLength)));
		}
		return retSb.toString();
	}

	public static String getUniqueStringFromCurrentTime() {
		return W_SHA1Util.encryptString(String.valueOf(System
				.currentTimeMillis()));
	}

	public static String genCertainStrByTwoStr(final String str1,
			final String str2) {
		final String seed1 = (str1 == null ? "seed1" : str1);
		final String seed2 = (str2 == null ? "2dees" : str2);
		final StringBuilder plainSb = new StringBuilder();
		boolean flag = true;
		for (int i = 0; i < seed1.length() && i < seed2.length(); ++i) {
			if (flag) {
				plainSb.append(seed1.charAt(i));
			} else {
				plainSb.append(seed2.charAt(i));
			}
			flag = !flag;
		}
		return W_SHA1Util.encryptString(plainSb.toString());
	}
}