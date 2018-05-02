package ldcr.ByeHacker;

public class ByeHackerReason {
    private final String ip;
    private final String layer;
    private final String player;
    public ByeHackerReason(final String reason) {
	final String[] info = reason.split(" ");
	if (info.length>1) {
	    ip = info[1];
	    if (info.length>2) {
		layer = info[2];
		if (info.length>3) {
		    player = info[3];
		} else {
		    player = "§c??";
		}
	    } else {
		layer = "§c??";
		player = "§c??";
	    }
	} else { // oldest byehacker
	    ip = "§c??";
	    layer = "§c??";
	    player = "§c??";
	}
    }
    public String getIp() {
	return ip;
    }
    public String getLayer() {
	return layer;
    }
    public String getPlayer() {
	return player;
    }

}
