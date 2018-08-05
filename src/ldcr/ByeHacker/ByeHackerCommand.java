package ldcr.ByeHacker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
			sender.sendMessage("§b§lByeHacker §7>> §"+(sender.hasPermission("byehacker.info") ? "a" : "c")+  "/byehacker info <player>    查询ByeHacker检测");
			//sender.sendMessage("§b§lByeHacker §7>> §"+(sender.hasPermission("byehacker.info") ? "a" : "c")+  "/byehacker infoip <address> 查询ByeHacker检测");
			sender.sendMessage("§b§lByeHacker §7>> §"+(sender.hasPermission("byehacker.manage") ? "a" : "c")+"/byehacker unban <player>   解除ByeHacker封禁");
			//sender.sendMessage("§b§lByeHacker §7>> §"+(sender.hasPermission("byehacker.manage") ? "a" : "c")+"/byehacker unbanip <player> 解除ByeHacker封禁");
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
					final ByeHackerReason reason = new ByeHackerReason(banInfo.getReason());
					sender.sendMessage(new String[] {
							"§b§lByeHacker §7>> §a玩家 §e"+player.getName()+" §a被检测于 §e"+DateUtils.formatDate(banInfo.getCreated()),
							"§b§lByeHacker §7>> §a所在IP: §e"+reason.getIp()
					});

				} else {
					sender.sendMessage("§b§lByeHacker §7>> §a玩家 "+player.getName()+" 不是被ByeHacker检测封禁的");
				}
			} else {
				sender.sendMessage("§b§lByeHacker §7>> §a玩家 "+player.getName()+" 没有被ByeHacker检测封禁");
			}
			return true;
		}/*
		case "infoip":{
			if (!sender.hasPermission("byehacker.info")) {
				sender.sendMessage("§b§lByeHacker §7>> §c你没有权限执行此命令");
				return true;
			}
			if (args.length==1) {
				sender.sendMessage("§b§lByeHacker §7>> §a/byehacker infoip <address> 查询ByeHacker检测");
				return true;
			}
			final String ip = args[1];
			if (Bukkit.getBanList(Type.IP).isBanned(ip)) {
				final BanEntry banInfo = Bukkit.getBanList(Type.IP).getBanEntry(ip);
				if ("ByeHacker-AutoDetect".equals(banInfo.getSource())) {
					final ByeHackerReason reason = new ByeHackerReason(banInfo.getReason());
					sender.sendMessage(new String[] {
							"§b§lByeHacker §7>> §aIP §e"+ip+" §a被检测于 §e"+DateUtils.formatDate(banInfo.getCreated()),
							"§b§lByeHacker §7>> §a所用ID: §e"+reason.getPlayer()
					});

				} else {
					sender.sendMessage("§b§lByeHacker §7>> §aIP "+ip+" 不是被ByeHacker检测封禁的");
				}
			} else {
				sender.sendMessage("§b§lByeHacker §7>> §aIP "+ip+" 没有被ByeHacker检测封禁");
			}
			return true;
		}*/
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
					final ByeHackerReason reason = new ByeHackerReason(banInfo.getReason());
					Bukkit.getBanList(Type.NAME).pardon(reason.getPlayer());
					Bukkit.getBanList(Type.IP).pardon(reason.getIp());
					sender.sendMessage("§b§lByeHacker §7>> §e已解封玩家 "+playerName+" ["+reason.getIp()+"]");
				} else {
					sender.sendMessage("§b§lByeHacker §7>> §a玩家 "+player.getName()+" 不是被ByeHacker检测封禁的");
				}
			} else {
				sender.sendMessage("§b§lByeHacker §7>> §a玩家 "+player.getName()+" 没有被封禁");
			}
			return true;
		}/*
		case "unbanip": {
			if (!sender.hasPermission("byehacker.manage")) {
				sender.sendMessage("§b§lByeHacker §7>> §c你没有权限执行此命令");
				return true;
			}
			if (args.length==1) {
				sender.sendMessage("§b§lByeHacker §7>> §a/byehacker unbanip <player> 解除ByeHacker封禁");
				return true;
			}
			final String ip = args[1];
			if (Bukkit.getBanList(Type.IP).isBanned(ip)) {
				final BanEntry banInfo = Bukkit.getBanList(Type.IP).getBanEntry(ip);
				if ("ByeHacker-AutoDetect".equals(banInfo.getSource())) {
					final ByeHackerReason reason = new ByeHackerReason(banInfo.getReason());
					Bukkit.getBanList(Type.NAME).pardon(reason.getPlayer());
					Bukkit.getBanList(Type.IP).pardon(reason.getIp());
					sender.sendMessage("§b§lByeHacker §7>> §e已解封IP "+ip+" ["+reason.getPlayer()+"]");
				} else {
					sender.sendMessage("§b§lByeHacker §7>> §aIP "+ip+" 不是被ByeHacker检测封禁的");
				}
			} else {
				sender.sendMessage("§b§lByeHacker §7>> §aIP "+ip+" 没有被封禁");
			}
			return true;
		}*/
		case "reload": {
			if (!sender.hasPermission("byehacker.manage")) {
				sender.sendMessage("§b§lByeHacker §7>> §c你没有权限执行此命令");
				return true;
			}
			ByeHacker.instance.onReload();
			return true;
		}
		case "list":{
			if (!sender.hasPermission("byehacker.info")) {
				sender.sendMessage("§b§lByeHacker §7>> §c你没有权限执行此命令");
				return true;
			}
			final ArrayList<BanEntry> sortedList = new ArrayList<BanEntry>(Bukkit.getBanList(Type.NAME).getBanEntries());
			Collections.sort(sortedList, new Comparator<BanEntry>() {
				@Override
				public int compare(final BanEntry arg0, final BanEntry arg1) {
					return arg0.getCreated().compareTo(arg1.getCreated());
				}
			});
			int count = 0;
			for (final BanEntry banInfo : sortedList) {
				if ("ByeHacker-AutoDetect".equals(banInfo.getSource())) {
					count++;
					final ByeHackerReason reason = new ByeHackerReason(banInfo.getReason());
					sender.sendMessage(
					                   "§b§lByeHacker §7>> §a玩家 §e"+banInfo.getTarget()+
					                   " ["+reason.getIp()+"§e]"+
					                   " §a时间 §e"+DateUtils.formatDateWhitoutTime(banInfo.getCreated()));
				}
			}
			sender.sendMessage("§b§lByeHacker §7>> §b已封禁 "+count+" 个作弊者");
			return true;
		}
		default: {
			sender.sendMessage("§b§lByeHacker §7>> §c参数无效");
			return true;
		}
		}
	}

}
