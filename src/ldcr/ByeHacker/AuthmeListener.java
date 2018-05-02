package ldcr.ByeHacker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.xephi.authme.events.LoginEvent;

public class AuthmeListener implements Listener {
    @EventHandler
    public void onLogin(final LoginEvent e) {
	if (e.getPlayer().hasPermission("byehacker.bypass")) return;
	ByeHacker.instance.startCheck(e.getPlayer());
    }
}
