package ldcr.ByeHacker;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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

import ldcr.ByeHacker.Utils.JarScanner;
import ldcr.ByeHacker.layers.BuiltinLayer;

public class ByeHacker extends JavaPlugin implements Listener {
    public static ByeHacker instance;
    protected final ArrayList<String> whileList = new ArrayList<String>(Arrays.asList(new String[]{
	    "/login",
	    "/register",
	    "/l",
	    "/reg"
    }));

    private ProtocolManager protocolManager;
    private LinkedList<IByeHackerLayer> layers;
    private HashMap<Player,WaitingAuthPlayerLayers> waitingAuth;
    @Override
    public void onEnable() {
	instance = this;
	loadLayers(Bukkit.getConsoleSender());
	waitingAuth = new HashMap<Player,WaitingAuthPlayerLayers>();
	getCommand("byehacker").setExecutor(new ByeHackerCommand());
	Bukkit.getPluginManager().registerEvents(this, this);
	protocolManager = ProtocolLibrary.getProtocolManager();
	protocolManager.addPacketListener(new ChatListener());
    }
    public void loadLayers(final CommandSender callback) {
	loadLayersInternal(callback);
	callback.sendMessage("§b§l作弊验证 §7>> §a验证层加载完毕! 共载入了 "+layers.size()+" 种验证模式");
    }
    private void loadLayersInternal(final CommandSender callback) {
	callback.sendMessage("§b§l作弊验证 §7>> §e正在加载验证层...");
	layers = new LinkedList<IByeHackerLayer>();
	getDataFolder().mkdirs();
	final File[] layerFiles = getDataFolder().listFiles(new FilenameFilter(){
	    @Override
	    public boolean accept(final File arg0, final String arg1) {
		if (arg1.endsWith(".jar")) {
		    callback.sendMessage("§b§l作弊验证 §7>> §a找到外置验证包 "+arg1);
		    return true;
		} else return false;
	    }
	});
	if (layerFiles.length==0) {
	    callback.sendMessage("§b§l作弊验证 §7>> §c没有找到外置验证层文件, 加载内建基础验证层...");
	    layers.add(new BuiltinLayer());
	    callback.sendMessage("§b§l作弊验证 §7>> §a成功加载验证层 §d"+BuiltinLayer.class.getName());
	    return;
	}

	for (final File layerFile : layerFiles) {
	    final URLClassLoader loader;
	    try {
		loader = URLClassLoader.newInstance(new URL[]{ layerFile.toURI().toURL()}, getClassLoader());
	    } catch (final MalformedURLException e) {
		callback.sendMessage("§b§l作弊验证 §7>> §c外置验证包 "+layerFile.getName()+" 无效: MalformedURL");
		continue;
	    }
	    ArrayList<String> clses;
	    try {
		clses = JarScanner.getCrunchifyClassNamesFromJar(layerFile);
	    } catch (final IOException e) {
		callback.sendMessage("§b§l作弊验证 §7>> §c外置验证包 "+layerFile.getName()+" 加载失败: IOException");
		continue;
	    }
	    for (final String clsName : clses) {
		if (!clsName.startsWith("ldcr.ByeHacker.layers.")) {
		    continue;
		}
		if (clsName.contains("$")) {
		    continue;
		}
		final Class<?> cls;
		try {
		    cls = loader.loadClass(clsName);
		} catch (final ClassNotFoundException e) {
		    callback.sendMessage("§b§l作弊验证 §7>> §c外置验证层 §d"+clsName+" §c加载失败: ClassNotFound");
		    continue;
		}
		if (IByeHackerLayer.class.isAssignableFrom(cls)) {
		    final Class<? extends IByeHackerLayer> layerCls = cls.asSubclass(IByeHackerLayer.class);
		    try {
			final IByeHackerLayer layer = layerCls.getConstructor().newInstance();
			layers.add(layer);
			callback.sendMessage("§b§l作弊验证 §7>> §a成功加载验证层 §d"+clsName);
		    } catch (final Exception e) {
			callback.sendMessage("§b§l作弊验证 §7>> §c外置验证层 §d"+clsName+" §c加载失败: "+e.getClass().getSimpleName());
			continue;
		    }
		} else {
		    continue;
		}
	    }
	}

    }
    private LinkedList<IByeHackerLayer> getLayers() {
	final LinkedList<IByeHackerLayer> layers = new LinkedList<IByeHackerLayer>(this.layers);
	Collections.shuffle(layers);
	return layers;
    }
    @Override
    public void onDisable() {
	onReload();
    }
    public void onReload() {
	for (final Player p : waitingAuth.keySet()) {
	    p.kickPlayer("§b§l作弊验证 §7>> §c服务器重载, 请重新登录游戏!");
	}
    }
    public boolean isWaitingAuth(final Player player) {
	return waitingAuth.containsKey(player);
    }
    public void passAuth(final Player player) {
	if (hackers.contains(player)) {
	    Bukkit.getScheduler().runTask(this, new DetectedKickThread(player));
	    return;
	}
	waitingAuth.remove(player);
	player.sendMessage("§b§l作弊验证 §7>> §a验证已通过, 您可以继续游戏了~");
    }
    private final HashSet<Player> hackers = new HashSet<Player>();
    public void markHack(final Player player, final String layer) {
	hackers.add(player);
	onDetectedBan(player, layer);
    }
    public void passLayer(final Player player) {
	if (hackers.contains(player)) {
	    if (Math.random()>0.5) {
		Bukkit.getScheduler().runTask(this, new DetectedKickThread(player));
		return;
	    }
	}
	player.sendMessage("§b§l作弊验证 §7>> §a您已通过本层反作弊验证!");
	waitingAuth.get(player).passLayer();
    }
    public void onDetected(final Player player, final String layer) {
	onDetectedBan(player,layer);
	Bukkit.getScheduler().runTask(this, new DetectedKickThread(player));
    }
    public void onDetectedBan(final Player player, final String layer) {
	Bukkit.getConsoleSender().sendMessage("§b§l作弊验证 §7>> §c玩家 "+player.getName()+" 被检测到作弊客户端");
	Bukkit.getBanList(Type.IP).addBan(player.getAddress().getHostString(),
		"ByeHacker-Detected? "+
			player.getAddress().getHostString()+" "+layer
			, null, "ByeHacker-AutoDetect");
	Bukkit.getBanList(Type.NAME).addBan(player.getName(),
		"ByeHacker-Detected? "+
			player.getAddress().getHostString()+" "+layer
			, null, "ByeHacker-AutoDetect");
    }

    @EventHandler
    public void onJoin(final PlayerLoginEvent event) {
	if (event.getPlayer().hasPermission("byehacker.bypass")) return;
	final WaitingAuthPlayerLayers layer = new WaitingAuthPlayerLayers(event.getPlayer(), getLayers());
	waitingAuth.put(event.getPlayer(), layer);
    }
    private final Vector downVector = new Vector(0,0,0);
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
	if (isWaitingAuth(event.getPlayer())) {
	    event.setTo(event.getFrom().getBlock().getLocation().add(0.5, 0, 0.5).setDirection(downVector));
	    waitingAuth.get(event.getPlayer()).nextLayer().onDenyAction(event.getPlayer());
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
	    waitingAuth.get(event.getPlayer()).nextLayer().endAuth(event.getPlayer());
	    waitingAuth.remove(event.getPlayer());
	}
	if(hackers.contains(event.getPlayer())) {
	    hackers.remove(event.getPlayer());
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
    public boolean callNonAuthPlayerChat(final Player player, final String message) {
	boolean shouldCancel = false;
	for (final IByeHackerLayer layer : layers) {
	    if (layer.onNonAuthPlayerChat(player, message)) {
		shouldCancel = true;
	    }
	}
	return shouldCancel;
    }
    public boolean callAuthPlayerChat(final Player player, final String message) {
	return waitingAuth.get(player).onChat(player, message);
    }
}
