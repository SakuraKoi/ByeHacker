package ldcr.ByeHacker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import ldcr.ByeHacker.Utils.BookUtils;
import ldcr.ByeHacker.api.event.ByeHackerPassEvent;
import ldcr.ByeHacker.auth.ByehackerAuth;
import ldcr.ByeHacker.listeners.ActionListener;
import ldcr.ByeHacker.listeners.ChatPacketListener;
import ldcr.ByeHacker.listeners.AuthListener.AuthmeListener;
import ldcr.ByeHacker.listeners.AuthListener.BukkitListener;
import ldcr.LdcrUtils.plugin.LdcrUtils;
import ldcr.Utils.exception.ExceptionUtils;

public class ByeHacker extends JavaPlugin implements Listener {
	public static ByeHacker instance;
	private ProtocolManager protocolManager;
	@Override
	public void onEnable() {
		instance = this;
		LdcrUtils.requireVersion(32);
		authingPlayers = new HashMap<>();
		getCommand("byehacker").setExecutor(new ByeHackerCommand());
		Bukkit.getConsoleSender().sendMessage("§b§l作弊验证 §7>> §e正在加载 ByeHacker作弊检测 v"+getDescription().getVersion());
		try {
			BookUtils.loadImpl();
		} catch (final Exception e) {
			ExceptionUtils.printStacktrace(e);
			Bukkit.getConsoleSender().sendMessage("§b§l作弊验证 §7>> §c错误: 构建 BookUtils 失败");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("AuthMe")) {
			Bukkit.getPluginManager().registerEvents(new AuthmeListener(), this);
			Bukkit.getConsoleSender().sendMessage("§b§l作弊验证 §7>> §a发现Authme登录插件, 支持已启用.");
		} else {
			Bukkit.getPluginManager().registerEvents(new BukkitListener(), this);
			Bukkit.getConsoleSender().sendMessage("§b§l作弊验证 §7>> §c未发现Authme登录插件.");
		}
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new ActionListener(), this);
		protocolManager = ProtocolLibrary.getProtocolManager();
		protocolManager.addPacketListener(new ChatPacketListener());
		Bukkit.getConsoleSender().sendMessage("§b§l作弊验证 §7>> §a欢迎使用 ByeHacker作弊检测 §bBy.Ldcr");
	}

	@Override
	public void onDisable() {
		onReload();
	}

	public void onReload() {
		for (final Player p : authingPlayers.keySet()) {
			p.kickPlayer("§b§l作弊验证 §7>> §c服务器重载, 请重新登录游戏!");
		}
	}

	private static HashMap<Player, ByehackerAuth> authingPlayers;
	public static boolean isAuthing(final Player player) {
		return authingPlayers.containsKey(player);
	}

	public static void startAuth(final Player player) {
		final ByehackerAuth data = new ByehackerAuth(player);
		authingPlayers.put(player, data);
		data.requestAuth();
	}
	public static void passAuth(final Player player) {
		removeAuth(player);
		player.sendMessage(passMessage);
		Bukkit.getPluginManager().callEvent(new ByeHackerPassEvent(player));
	}
	private static void removeAuth(final Player player) {
		authingPlayers.get(player).getTimeoutThread().cancel();
		authingPlayers.remove(player);
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		if (isAuthing(event.getPlayer())) {
			removeAuth(event.getPlayer());
		}
	}

	@EventHandler
	public void onLogin(final PlayerLoginEvent e) {
		if (e.getResult()==Result.KICK_BANNED) {
			if (e.getKickMessage().contains("ByeHacker-Detected")) {
				e.setKickMessage(" java.net.ConnectException: Connection Refused: no further information; ");
				Bukkit.getConsoleSender().sendMessage("§b§l作弊验证 §7>> §c被封禁的玩家 "+e.getPlayer().getName()+" 试图进入服务器");
			}
		}
	}
	public boolean callNonAuthPlayerChat(final String message) {
		return !message.contains("§f");
	}
	public boolean callAuthPlayerChat(final Player player, final String message) {
		return authingPlayers.get(player).onChat(message);
	}

	public void banHacker(final Player player) {
		Bukkit.getConsoleSender().sendMessage("§b§l作弊验证 §7>> §c玩家 "+player.getName()+" 被检测到作弊客户端");
		final Calendar c = new GregorianCalendar();
		c.add(Calendar.DAY_OF_MONTH, 30);
		Bukkit.getBanList(Type.IP).addBan(player.getAddress().getHostString(),
		                                  "ByeHacker-Detected- "+
		                                		  player.getAddress().getHostString()+" "+player.getName()
		                                		  , c.getTime(), "ByeHacker-AutoDetect");
		Bukkit.getBanList(Type.NAME).addBan(player.getName(),
		                                    "ByeHacker-Detected- "+
		                                    		player.getAddress().getHostString()+" "+player.getName()
		                                    		, null, "ByeHacker-AutoDetect");
	}

	private static final String[] passMessage = new String[] {
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"§b§l作弊验证 §7>> §a验证已通过, 您可以继续游戏了~",
			"",
			"",
			""
	};
}
