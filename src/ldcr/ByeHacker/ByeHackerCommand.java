package ldcr.ByeHacker;

import org.bukkit.BanEntry;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ldcr.ByeHacker.Utils.DateUtils;


public class ByeHackerCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(final CommandSender sender, final Command arg1, final String arg2,
	    final String[] args) {
	if (args.length==0) {
	    sender.sendMessage("§b§lByeHacker §7>> §"+(sender.hasPermission("byehacker.info") ? "a" : "c")+  "/byehacker info <player>   查询ByeHacker检测");
	    sender.sendMessage("§b§lByeHacker §7>> §"+(sender.hasPermission("byehacker.manage") ? "a" : "c")+"/byehacker unban <player>  解除ByeHacker封禁");
	    return true;
	}
	switch (args[0].toLowerCase()) {
	case "info":{
	    if (!sender.hasPermission("byehacker.info")) {
		sender.sendMessage("§b§lByeHacker §7>> §c你没有权限执行此命令");
		return true;
	    }
	    if (args.length==1) {
		sender.sendMessage("§b§lByeHacker §7>> §a/byehacker info <player>   查询ByeHacker检测");
		return true;
	    }
	    final String playerName = args[1];
	    final OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
	    if (player==null) {
		sender.sendMessage("§b§lByeHacker §7>> §c玩家 "+playerName+" 不存在");
		return true;
	    }
	    if (Bukkit.getBanList(Type.NAME).isBanned(player.getName())) {
		final BanEntry banInfo = Bukkit.getBanList(Type.NAME).getBanEntry(player.getName());
		if ("ByeHacker-AutoDetect".equals(banInfo.getSource())) {
		    if (banInfo.getReason().startsWith("ByeHacker-Detected? ")) {
			final String[] infos = banInfo.getReason().split(" ");
			sender.sendMessage(new String[] {
				"§b§lByeHacker §7>> §a玩家 §e"+player.getName()+" §a被检测于 §e"+DateUtils.formatDate(banInfo.getCreated()),
				"§b§lByeHacker §7>> §a 所在IP: §e"+infos[1]+" §a匹配规则: §e"+infos[2]
			});
		    } else {
			sender.sendMessage("§b§lByeHacker §7>> §c玩家 "+player.getName()+" 被旧版ByeHacker检测封禁, 无法查询信息");
		    }
		} else {
		    sender.sendMessage("§b§lByeHacker §7>> §a玩家 "+player.getName()+" 不是被ByeHacker检测封禁的");
		}
	    } else {
		sender.sendMessage("§b§lByeHacker §7>> §a玩家 "+player.getName()+" 没有被ByeHacker检测封禁");
	    }
	    return true;
	}
	case "unban": {
	    if (!sender.hasPermission("byehacker.manage")) {
		sender.sendMessage("§b§lByeHacker §7>> §c你没有权限执行此命令");
		return true;
	    }
	    if (args.length==1) {
		sender.sendMessage("§b§lByeHacker §7>> §a/byehacker unban <player>  解除ByeHacker封禁");
		return true;
	    }
	    final String playerName = args[1];
	    final OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
	    if (player==null) {
		sender.sendMessage("§b§lByeHacker §7>> §c玩家 "+playerName+" 不存在");
		return true;
	    }
	    if (Bukkit.getBanList(Type.NAME).isBanned(player.getName())) {
		final BanEntry banInfo = Bukkit.getBanList(Type.NAME).getBanEntry(player.getName());
		if ("ByeHacker-AutoDetect".equals(banInfo.getSource())) {
		    if (banInfo.getReason().startsWith("ByeHacker-Detected? ")) {
			final String[] infos = banInfo.getReason().split(" ");
			Bukkit.getBanList(Type.NAME).pardon(player.getName());
			Bukkit.getBanList(Type.IP).pardon(infos[1]);
			sender.sendMessage("§b§lByeHacker §7>> §e已解封玩家 "+playerName+" ["+infos[1]+"]");
		    } else {
			sender.sendMessage("§b§lByeHacker §7>> §c玩家 "+player.getName()+" 被旧版ByeHacker检测封禁, 无法解封");
		    }
		} else {
		    sender.sendMessage("§b§lByeHacker §7>> §a玩家 "+player.getName()+" 不是被ByeHacker检测封禁的");
		}
	    } else {
		sender.sendMessage("§b§lByeHacker §7>> §a玩家 "+player.getName()+" 没有被ByeHacker检测封禁");
	    }
	    return true;
	}
	case "reload": {
	    if (!sender.hasPermission("byehacker.manage")) {
		sender.sendMessage("§b§lByeHacker §7>> §c你没有权限执行此命令");
		return true;
	    }
	    ByeHacker.instance.loadLayers(sender);
	    ByeHacker.instance.onReload();
	    return true;
	}
	default: {
	    sender.sendMessage("§b§lByeHacker §7>> §c参数无效");
	    return true;
	}
	}
    }

}
