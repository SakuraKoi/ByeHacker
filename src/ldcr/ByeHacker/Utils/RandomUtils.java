package ldcr.ByeHacker.Utils;

import java.util.Random;

public class RandomUtils {
	private static final Random ran = new Random();
	public static String selectRandom(final String[] all) {
		return all[ran.nextInt(all.length)];
	}

	public static char selectRandom(final char[] all) {
		return all[ran.nextInt(all.length)];
	}

	public static <T> T selectRandom(final T[] all) {
		return all[ran.nextInt(all.length)];
	}

}
