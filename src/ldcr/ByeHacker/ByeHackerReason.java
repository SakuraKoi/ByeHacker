package ldcr.ByeHacker;

public class ByeHackerReason {
	private final String ip;
	private final String player;
	public ByeHackerReason(final String reason) {
		final String[] info = reason.split(" ");
		if (info.length>1) {
			ip = info[1];
			if (info.length>2) {
				player = info[2];
			} else {
				player = "§c??";
			}
		} else { // oldest byehacker
			ip = "§c??";
			player = "§c??";
		}
	}
	public String getIp() {
		return ip;
	}
	public String getPlayer() {
		return player;
	}

}
