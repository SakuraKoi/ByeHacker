package ldcr.ByeHacker.auth;

import lombok.Getter;

public class ByehackerKey {
	@Getter private final String legalKey;
	@Getter private final String hackKey;
	@Getter private final int key;
	public ByehackerKey(final String legalKey, final String hackKey, final int index) {
		this.legalKey = legalKey;
		this.hackKey = hackKey;
		key = index;
	}
	public boolean match(final String message) {
		if (message.equals(legalKey)) return true;
		return message.equals(hackKey);
	}
	public boolean isPassed(final String message) {
		return message.equals(legalKey);
	}
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof String) {
			if (obj.equals(legalKey)) return true;
			return obj.equals(hackKey);
		}
		return super.equals(obj);
	}
}
