package ldcr.ByeHacker.Tasks;

import org.bukkit.entity.Player;

public class FakeKickThread implements Runnable {
	private final Player player;
	public FakeKickThread(final Player player) {
		this.player = player;
	}
	@Override
	public void run() {
		player.kickPlayer("§b§l作弊验证 §7>> §c验证失败, 请重试~");
	}

}
