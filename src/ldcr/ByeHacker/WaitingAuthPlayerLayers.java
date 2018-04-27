package ldcr.ByeHacker;

import java.util.LinkedList;

import org.bukkit.entity.Player;

public class WaitingAuthPlayerLayers {
    private final Player player;
    private final LinkedList<IByeHackerLayer> layers;
    public WaitingAuthPlayerLayers(final Player player, final LinkedList<IByeHackerLayer> layers) {
	this.player = player;
	this.layers = layers;
	requestAuth(player);
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
    public void requestAuth(final Player player) {
	nextLayer().requestAuth(player);
    }
    public void endAuth(final Player player) {
	nextLayer().endAuth(player);
    }
    public void onDenyAction(final Player player) {
	nextLayer().onDenyAction(player);
    }
    public boolean onChat(final Player player, final String message) {
	return nextLayer().onChat(player, message);
    }
}
