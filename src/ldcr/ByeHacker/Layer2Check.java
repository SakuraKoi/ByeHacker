package ldcr.ByeHacker;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

class Layer2Check implements Runnable {

	private final ByeHacker instance;
	private final Player player;
	private final boolean isHacker;

	BukkitTask task;
	public Layer2Check(final ByeHacker byeHacker, final Player player, final boolean isHacker) {
		instance = byeHacker;
		this.player = player;
		this.isHacker = isHacker;
		player.sendMessage("§b§l作弊验证 §7>> §e正在检查您的客户端, 请稍等...");
		task = Bukkit.getScheduler().runTaskLater(instance, this, 100);
	}
	@Override
	public void run() {
		if (!player.isOnline()) {
			task.cancel();
			return;
		}
		if (isHacker) {
			player.kickPlayer("Internal Exception: io.netty.handler.codec.DecoderException: java.lang.RuntimeException: An internal error occured.");
			Bukkit.getBanList(Type.IP).addBan(player.getAddress().getHostString(),
					"ByeHacker-Detected| "+
					player.getAddress().getHostString()
				, null, "ByeHacker-AutoDetect");
			Bukkit.getBanList(Type.NAME).addBan(player.getName(),
					"ByeHacker-Detected| "+
					player.getAddress().getHostString()
				, null, "ByeHacker-AutoDetect");
		} else {
			player.sendMessage("§b§l作弊验证 §7>> §a数据包发送完毕, 确认您的游戏没有崩溃. ");
			player.sendMessage("§b§l作弊验证 §7>> §a验证已通过, 您可以继续游戏了~");
			instance.layer2Auth.remove(player);
		}
		task.cancel();
		return;

	}
}