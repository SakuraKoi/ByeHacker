package ldcr.ByeHacker.Utils;

import ldcr.ByeHacker.auth.ByehackerKey;

public class AuthKeyGenerator {
	private static final char[] OBF_KEY_CHAR = "il1I".toCharArray();
	private static final int AUTH_KEY_LENGTH = 32;
	private static final String[] SPEC_KEYS = new String[]{
			".killaura range 0.1 §f",
			".bind killaura none §f"
	};
	private static final String[] AUTH_PREFIXS = new String[] {
			".say .",
			"-say -"
	};
	public static ByehackerKey generateValidKey(int index, final int index2) {
		index = index % AUTH_PREFIXS.length;
		final StringBuilder builder = new StringBuilder();
		builder.append("§fAntiHackedClient（_");
		for (int i = 0; i<AUTH_KEY_LENGTH; i++) {
			builder.append(RandomUtils.selectRandom(OBF_KEY_CHAR));
		}
		final String key = builder.toString();
		return new ByehackerKey(AUTH_PREFIXS[index] + key, key, index2);
	}
	public static ByehackerKey generateSpecKey(final int index) {
		return new ByehackerKey(RandomUtils.selectRandom(SPEC_KEYS), null, index);
	}
}
