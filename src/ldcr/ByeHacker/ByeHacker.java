package ldcr.ByeHacker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ByeHacker extends JavaPlugin implements Listener {
    protected final ArrayList<String> whileList = new ArrayList<String>(Arrays.asList(new String[]{
	    "/login",
	    "/register",
	    "/l",
	    "/reg"
    }));

    protected final String authPrefix = ".say .§fAntiHackedClient_";
    protected final String hackPrefix = ".§fAntiHackedClient_";

    private final int authLength = 64 - authPrefix.length();
    private final Random random = new Random();
    private ProtocolManager protocolManager;

    protected HashMap<Player, String> layer1Auth;
    protected HashMap<Player, Boolean> layer2Auth;
    @Override
    public void onEnable() {
	//dotwaitingAuth = new ArrayList<Player>();
	layer1Auth = new HashMap<Player, String>();
	layer2Auth = new HashMap<Player, Boolean>();
	getCommand("byehacker").setExecutor(new ByeHackerCommand());
	Bukkit.getPluginManager().registerEvents(this, this);
	protocolManager = ProtocolLibrary.getProtocolManager();
	protocolManager.addPacketListener(new ChatListener(this));
    }
    @Override
    public void onDisable() {
	for (final Player p : layer1Auth.keySet()) {
	    p.kickPlayer("§b§l作弊验证 §7>> §c服务器重载, 请重新登录游戏!");
	}
	for (final Player p : layer2Auth.keySet()) {
	    p.kickPlayer("§b§l作弊验证 §7>> §c服务器重载, 请重新登录游戏!");
	}
    }

    boolean isWaitingAuth(final Player player) {
	if (layer1Auth.containsKey(player)) return true;
	if (layer2Auth.containsKey(player)) return true;
	return false;
    }

    void sendAuthMessage(final Player player) {
	final String authKey = layer1Auth.get(player);
	if (authKey==null) return;
	final TextComponent clickAuth = new TextComponent("§b§l作弊验证 §7>> §a请打开聊天用鼠标点击此处完成认证 §6(先登录再认证)");
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

    @EventHandler
    public void onJoin(final PlayerLoginEvent event) {
	if (event.getPlayer().hasPermission("byehacker.bypass")) return;
	Bukkit.getScheduler().runTaskLater(this, new Runnable() {
	    @Override
	    public void run() {
		sendAuthMessage(event.getPlayer());
	    }
	}, 10);
	layer1Auth.put(event.getPlayer(), generateAuthKey());
    }
    private final Vector downVector = new Vector(0,0,0);
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
	if (isWaitingAuth(event.getPlayer())) {
	    event.setTo(event.getFrom().getBlock().getLocation().add(0.5, 0, 0.5).setDirection(downVector));
	    sendAuthMessage(event.getPlayer());
	}
    }

    @EventHandler
    public void onOpen(final InventoryOpenEvent event) {
	if (isWaitingAuth((Player) event.getPlayer())) {
	    event.setCancelled(true);
	}
    }

    @EventHandler
    public void onPlace(final PlayerInteractEvent event) {
	if (isWaitingAuth(event.getPlayer())) {
	    event.setCancelled(true);
	}
    }

    @EventHandler
    public void onInteractEntity(final PlayerInteractEntityEvent event) {
	if (isWaitingAuth(event.getPlayer())) {
	    event.setCancelled(true);
	}
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
	if (isWaitingAuth(event.getPlayer())) {
	    layer1Auth.remove(event.getPlayer());
	    layer2Auth.remove(event.getPlayer());
	}
    }

    @EventHandler
    public void onLogin(final PlayerLoginEvent e) {
	if (e.getResult()==Result.KICK_BANNED) {
	    if (e.getKickMessage().contains("ByeHacker-Detected")) {
		e.setKickMessage(" java.net.ConnectException: Connection Refused: no further information: ");
		Bukkit.getConsoleSender().sendMessage("§b§l作弊验证 §7>> §c被封禁的玩家 "+e.getPlayer().getName()+" 试图进入服务器");
	    }
	}
    }
}
