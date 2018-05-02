package ldcr.ByeHacker;

import org.bukkit.entity.Player;

public class DetectedKickThread implements Runnable {
    private final Player player;
    public DetectedKickThread(final Player player) {
	this.player = player;
    }
    @Override
    public void run() {
	ByeHacker.instance.removeAuth(player);
	player.kickPlayer("Internal Exception: io.netty.handler.codec.DecoderException: java.lang.RuntimeException: An internal error occured.");
    }

}
