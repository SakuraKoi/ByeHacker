package ldcr.ByeHacker.layers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ldcr.ByeHacker.ByeHacker;
import ldcr.ByeHacker.IByeHackerLayer;

public class LastCheckLayer implements IByeHackerLayer {
    @Override
    public String getLayerName() {
	return "LastCheck";
    }

    @Override
    public void requestAuth(final Player player) {
	player.sendMessage("§b§l作弊验证 §7>> §e正在检查您的客户端, 请稍候...");
	Bukkit.getScheduler().runTaskLater(ByeHacker.instance, new Runnable() {
	    @Override
	    public void run() {
		if (player.isOnline()) {
		    ByeHacker.instance.passLayer(player);
		}
	    }
	}, 20);
    }

    @Override
    public void endAuth(final Player player) {}

    @Override
    public void onDenyAction(final Player player) {}

    @Override
    public boolean onChat(final Player player, final String message) {
	return false;
    }

    @Override
    public boolean onNonAuthPlayerChat(final Player player, final String message) {
	return true;
    }

}
