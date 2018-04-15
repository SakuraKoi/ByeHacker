package ldcr.ByeHacker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.BanList.Type;
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
import org.bukkit.scheduler.BukkitTask;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ByeHacker extends JavaPlugin implements Listener {
    private final ArrayList<String> whileList = new ArrayList<String>(Arrays.asList(new String[]{"/login","/register","/l","/reg"}));
    private final String authPrefix = ".say .§fAntiHackedClient_";
    private final String hackPrefix = ".§fAntiHackedClient_";
    private final int authLength = 64 - authPrefix.length();
    private final Random random = new Random();
    private ProtocolManager protocolManager;

    private HashMap<Player, String> layer1Auth;
    private HashMap<Player, Boolean> layer2Auth;
    @Override
    public void onEnable() {
	//dotwaitingAuth = new ArrayList<Player>();
	layer1Auth = new HashMap<Player, String>();
	layer2Auth = new HashMap<Player, Boolean>();
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

    private boolean isWaitingAuth(final Player player) {
	if (layer1Auth.containsKey(player)) return true;
	if (layer2Auth.containsKey(player)) return true;
	return false;
    }

    private void sendAuthMessage(final Player player) {
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
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
	if (isWaitingAuth(event.getPlayer())) {
	    event.setTo(event.getFrom());
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
		e.setKickMessage("java.net.ConnectException: Connection Refused: no further information;");
	    }
	}
    }

    class ChatListener extends PacketAdapter {
	ByeHacker instance;
	public ChatListener(final ByeHacker plugin) {
	    super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.CHAT);
	    instance = plugin;
	}

	@Override
	public void onPacketReceiving(final PacketEvent event) {
	    if (event.getPacketType() == PacketType.Play.Client.CHAT) {
		final PacketContainer packet = event.getPacket();
		if (packet==null) return;
		final String message = packet.getStrings().read(0);
		if (message==null) return;
		if (message.startsWith(authPrefix)) {
		    event.setCancelled(true);
		    if (isWaitingAuth(event.getPlayer())) {
			if (message.equals(layer1Auth.get(event.getPlayer()))) {
			    event.getPlayer().sendMessage("§b§l作弊验证 §7>> §a您已通过第一层反作弊验证!");
			    layer1Auth.remove(event.getPlayer());
			    layer2Auth.put(event.getPlayer(), false);
			    new Layer2Check(event.getPlayer(), false);
			}
		    }
		} else if (message.startsWith(hackPrefix)) {
		    event.setCancelled(true);
		    if (isWaitingAuth(event.getPlayer())) {
			final String authKey = layer1Auth.get(event.getPlayer());
			System.out.println(authKey.substring(5, authKey.length()));
			if (message.equals(authKey.substring(5, authKey.length()))) {
			    event.getPlayer().sendMessage("§b§l作弊验证 §7>> §a您已通过第一层反作弊验证!");
			    layer1Auth.remove(event.getPlayer());
			    layer2Auth.put(event.getPlayer(), true);
			    new Layer2Check(event.getPlayer(), true);
			}
		    }
		} else if (whileList.contains(message.split(" ")[0].toLowerCase()))
		    return;
		else if (isWaitingAuth(event.getPlayer())) {
		    sendAuthMessage(event.getPlayer());
		    event.setCancelled(true);
		}
	    }
	}
    }
    class Layer2Check implements Runnable {
	Player player;
	boolean isHacker;

	BukkitTask task;
	public Layer2Check(final Player player, final boolean isHacker) {
	    this.player = player;
	    this.isHacker = isHacker;
	    player.sendMessage("§b§l作弊验证 §7>> §e正在检查您的客户端, 请稍等...");
	    task = Bukkit.getScheduler().runTaskLater(ByeHacker.this, this, 100);
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
			"ByeHacker-Detected-"+player.getName()+" ["+player.getAddress().getHostString()+"]"
			, null, "ByeHacker-AutoDetect");
		Bukkit.getBanList(Type.NAME).addBan(player.getName(),
			"ByeHacker-Detected-"+player.getName()+" ["+player.getAddress().getHostString()+"]"
			, null, "ByeHacker-AutoDetect");
	    } else {
		player.sendMessage("§b§l作弊验证 §7>> §a数据包发送完毕, 确认您的游戏没有崩溃. ");
		player.sendMessage("§b§l作弊验证 §7>> §a验证已通过, 您可以继续游戏了~");
		layer2Auth.remove(player);
	    }
	    task.cancel();
	    return;

	}
    }
}
