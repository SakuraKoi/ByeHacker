package ldcr.ByeHacker;

import java.util.LinkedList;

import org.bukkit.entity.Player;

public class WaitingAuthPlayerLayers {
    private final Player player;
    private final LinkedList<IByeHackerLayer> layers;
    public WaitingAuthPlayerLayers(final Player player, final LinkedList<IByeHackerLayer> layers) {
	this.player = player;
	this.layers = layers;
	requestAuth();
    }
    public Player getPlayer() {
	return player;
    }
    public LinkedList<IByeHackerLayer> getLayers() {
	return layers;
    }
    public IByeHackerLayer nextLayer() {
	return layers.get(0);
    }
    public void passLayer() {
	nextLayer().endAuth(player);
	layers.remove(0);
	if (layers.isEmpty()) {
	    ByeHacker.instance.passAuth(player);
	} else {
	    nextLayer().requestAuth(player);
	}
    }
    public void requestAuth() {
	nextLayer().requestAuth(player);
    }
    public void endAuth() {
	nextLayer().endAuth(player);
    }
    public void onDenyAction() {
	nextLayer().onDenyAction(player);
    }
    public boolean onChat(final String message) {
	return nextLayer().onChat(player, message);
    }
    public void endAuthIfAvaliable() {
	if (!layers.isEmpty()) {
	    endAuth();
	}
    }
}
