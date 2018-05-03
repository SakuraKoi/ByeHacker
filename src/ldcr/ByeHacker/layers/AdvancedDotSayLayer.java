package ldcr.ByeHacker.layers;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ldcr.ByeHacker.ByeHacker;
import ldcr.ByeHacker.IByeHackerLayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class AdvancedDotSayLayer implements IByeHackerLayer {
    protected final String authPrefix = ".say .§fAntiHackedClient（_";
    protected final String hackPrefix = ".§fAntiHackedClient（_";
    private final int authLength = 64 - authPrefix.length();
    private final Random random = new Random();
    private final HashMap<Player, String> authKeyMap = new HashMap<Player, String>();
    @Override
    public void requestAuth(final Player player) {
	Bukkit.getScheduler().runTaskLater(ByeHacker.instance, new Runnable() {
	    @Override
	    public void run() {
		onDenyAction(player);
	    }
	}, 10);
	authKeyMap.put(player, generateAuthKey());
    }

    @Override
    public boolean onChat(final Player player, final String message) {
	if (message.startsWith(authPrefix)) {
	    if (message.equals(authKeyMap.get(player))) {
		ByeHacker.instance.passLayer(player);
	    }
	} else if (message.startsWith(hackPrefix)) {
	    final String authKey = authKeyMap.get(player);
	    if (message.equals(authKey.substring(5, authKey.length()))) {
		ByeHacker.instance.markHack(player, getLayerName());
		ByeHacker.instance.passLayer(player);
	    }
	}
	return false;
    }

    @Override
    public boolean onNonAuthPlayerChat(final Player player, final String message) {
	if (message.startsWith(authPrefix)) return false;
	if (message.startsWith(hackPrefix)) return false;
	return true;
    }

    @Override
    public void onDenyAction(final Player player) {
	final String authKey = authKeyMap.get(player);
	if (authKey==null) return;
	final TextComponent clickAuth = new TextComponent("§b§l作弊验证 §7>> §a请打开聊天用鼠标点击此处完成认证");
	clickAuth.setColor(ChatColor.YELLOW);
	clickAuth.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, authKey));
	clickAuth.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a点击完成认证").create()));
	player.spigot().sendMessage(clickAuth);
    }

    private final char[] obfKey = "il1I".toCharArray();
    private String generateAuthKey() {
	final StringBuilder builder = new StringBuilder();
	builder.append(authPrefix);
	for (int i = 0; i<authLength; i++) {
	    builder.append(obfKey[random.nextInt(obfKey.length)]);
	}
	return builder.toString();
    }

    @Override
    public void endAuth(final Player player) {
	authKeyMap.remove(player);
    }

    @Override
    public String getLayerName() {
	return "AdvancedDotSay";
    }

}
