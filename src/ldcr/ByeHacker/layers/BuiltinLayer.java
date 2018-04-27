package ldcr.ByeHacker.layers;

import java.util.HashSet;

import org.bukkit.entity.Player;

import ldcr.ByeHacker.ByeHacker;
import ldcr.ByeHacker.IByeHackerLayer;

public class BuiltinLayer implements IByeHackerLayer {
    private final HashSet<Player> waitingAuth = new HashSet<Player>();
    @Override
    public String getLayerName() {
	return "BuiltinLayer";
    }

    @Override
    public void requestAuth(final Player player) {
	waitingAuth.add(player);
    }

    @Override
    public void endAuth(final Player player) {
	waitingAuth.remove(player);
    }

    @Override
    public void onDenyAction(final Player player) {
	player.sendMessage("§b§l作弊验证 §7>> §e请在聊天发送 .auth 以进行验证!");
    }

    @Override
    public boolean onChat(final Player player, final String message) {
	if (message.equals(".auth")) {
	    ByeHacker.instance.passLayer(player);
	}
	return false;
    }

    @Override
    public boolean onNonAuthPlayerChat(final Player player, final String message) {
	if (message.equals(".auth")) return false;
	return true;
    }

}
