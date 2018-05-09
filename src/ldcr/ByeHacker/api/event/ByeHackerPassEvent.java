package ldcr.ByeHacker.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ByeHackerPassEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {
	return handlers;
    }
    private final Player player;
    public ByeHackerPassEvent(final Player player) {
	this.player = player;
    }
    @Override
    public HandlerList getHandlers() {
	return handlers;
    }
    public Player getPlayer() {
	return player;
    }

}
