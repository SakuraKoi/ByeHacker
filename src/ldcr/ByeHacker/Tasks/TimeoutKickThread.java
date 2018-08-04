package ldcr.ByeHacker.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import ldcr.ByeHacker.ByeHacker;
import ldcr.ByeHacker.auth.ByehackerAuth;

public class TimeoutKickThread implements Runnable {
	private final Player player;
	private final ByehackerAuth auth;
	private BukkitTask task = null;
	public TimeoutKickThread(final Player player, final ByehackerAuth auth) {
		this.player = player;
		this.auth = auth;
	}
	private int timeout = 60;
	public void start() {
		if (task==null) {
			task = Bukkit.getScheduler().runTaskTimer(ByeHacker.instance, this, 20, 20);
		}
	}
	@Override
	public void run() {
		timeout--;
		auth.sendAuthMessage();
		if (timeout<0) {
			player.kickPlayer("§b§l作弊验证 §7>> §c验证超时, 请重试~");
		}
	}
	public void cancel() {
		if (task != null) {
			task.cancel();
			task = null;
		}
	}

}
