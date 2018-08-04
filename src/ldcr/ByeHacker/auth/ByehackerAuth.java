package ldcr.ByeHacker.auth;

import lombok.Getter;

import java.util.Collections;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import ldcr.ByeHacker.ByeHacker;
import ldcr.ByeHacker.Tasks.DetectedKickThread;
import ldcr.ByeHacker.Tasks.TimeoutKickThread;
import ldcr.ByeHacker.Utils.AuthKeyGenerator;
import ldcr.ByeHacker.Utils.BookUtils;
import ldcr.lib.com.google.common.base.Joiner;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ByehackerAuth {
	private static final int AUTH_KEY_COUNT = 5;
	private static final int SPEC_KEY_COUNT = 4;
	private static final String RETRY_MESSAGE;
	static {
		RETRY_MESSAGE = ComponentSerializer.toString(TextComponent.fromLegacyText("§c§lByeHacker 作弊验证 §7>> \n\n\n\n\n   §4§l§n验证失败, 请重试"));
	}
	//private static final String[] RETRY_MESSAGE = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "§b§l作弊验证 §7>> §c验证失败, 请重试~", "", "", "", "" }; //             §7如果您使用了自定义字体, 按钮可能错位, 请以提示按钮序号为准
	private final Player player;
	private final LinkedList<ByehackerKey> keys = new LinkedList<>();
	private int pointer = -1;
	private ByehackerKey currentKey;
	private boolean hacking = false;
	private boolean lastCheck = false;
	private boolean retry = false;
	private TextComponent prefix;
	private TextComponent keypad;
	private final ItemStack book;
	private final BookMeta meta;
	@Getter
	private final TimeoutKickThread timeoutThread;

	public ByehackerAuth(final Player player) {
		this.player = player;
		timeoutThread = new TimeoutKickThread(player, this);
		book = new ItemStack(Material.WRITTEN_BOOK, 1);
		meta = (BookMeta) book.getItemMeta();
		meta.setDisplayName("§b§lByeHacker §3§l作弊验证");
		book.setItemMeta(meta);
	}

	private void generateKeys() {
		int index = 0;
		for (int i = 0; i < AUTH_KEY_COUNT; i++) {
			index++;
			keys.add(AuthKeyGenerator.generateValidKey(i, index));
		}
		for (int i = 0; i < SPEC_KEY_COUNT; i++) {
			index++;
			keys.add(AuthKeyGenerator.generateSpecKey(index));
		}

		final TextComponent space = new TextComponent("\n\n§r       ");
		final TextComponent key1 = new TextComponent("§1§l ① ");
		key1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, keys.get(0).getLegalKey()));
		final TextComponent key2 = new TextComponent("§1§l ② ");
		key2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, keys.get(1).getLegalKey()));
		final TextComponent key3 = new TextComponent("§1§l ③ ");
		key3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, keys.get(2).getLegalKey()));
		final TextComponent key4 = new TextComponent("§1§l ④ ");
		key4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, keys.get(3).getLegalKey()));
		final TextComponent key5 = new TextComponent("§1§l ⑤ ");
		key5.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, keys.get(4).getLegalKey()));
		final TextComponent key6 = new TextComponent("§1§l ⑥ ");
		key6.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, keys.get(5).getLegalKey()));
		final TextComponent key7 = new TextComponent("§1§l ⑦ ");
		key7.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, keys.get(6).getLegalKey()));
		final TextComponent key8 = new TextComponent("§1§l ⑧ ");
		key8.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, keys.get(7).getLegalKey()));
		final TextComponent key9 = new TextComponent("§1§l ⑨ ");
		key9.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, keys.get(8).getLegalKey()));
		keypad = new TextComponent(space, key1, key2, key3, space, key4, key5, key6, space, key7, key8, key9);
		Collections.shuffle(keys);
		final LinkedList<String> captha = new LinkedList<>();
		for (final ByehackerKey key : keys) {
			captha.add(String.valueOf(key.getKey()));
		}
		prefix = new TextComponent(TextComponent.fromLegacyText("§c§lByeHacker 作弊验证 §7>> \n§2请输入验证码: §1§l§n"+Joiner.on("").join(captha)+"§t\n\n\n"));
		//title = new TextComponent(new TextComponent("§b§l作弊验证 §7>> §a请打开聊天用鼠标从左到右依次点击: "), new TextComponent(question.getDisplay()));
	}

	private boolean hasNextPointer() {
		return pointer < keys.size();
	}

	private boolean matchMessage(final String message) {
		for (final ByehackerKey key : keys) {
			if (key.match(message)) return true;
		}
		return false;
	}

	private void nextPointer(final boolean mess) {
		pointer++;
		if (!hasNextPointer()) {
			timeoutThread.cancel();
			endCheck();
			return;
		}
		currentKey = keys.get(pointer);
		if (mess) {
			sendAuthMessage();
		}
		if (hacking && (Math.random() > 0.5)) {
			Bukkit.getScheduler().runTask(ByeHacker.instance, new DetectedKickThread(player));
		}
	}
	private void endCheck() {
		//sendAuthMessage();
		lastCheck = true;
		player.closeInventory();
		player.sendMessage("§b§l作弊验证 §7>> §e正在检查您的客户端, 请稍候...");
		Bukkit.getScheduler().runTaskLater(ByeHacker.instance, new Runnable() {
			@Override
			public void run() {
				if (player.isOnline()) {
					if (hacking) {
						Bukkit.getScheduler().runTask(ByeHacker.instance, new DetectedKickThread(player));
						return;
					}
					ByeHacker.passAuth(player);
				}
			}
		}, 20);
	}

	public boolean onChat(final String message) {
		if (lastCheck) return false;
		if (currentKey.match(message)) {
			if (!currentKey.isPassed(message)) {
				hacking = true;
				ByeHacker.instance.banHacker(player);
			}
			nextPointer(true);
		} else if (matchMessage(message)) {
			resetKey();
		}
		return false;
	}

	public void requestAuth() {
		generateKeys();
		nextPointer(false);
		timeoutThread.start();
		Bukkit.getScheduler().runTaskLater(ByeHacker.instance, new Runnable() {
			@Override
			public void run() {
				sendAuthMessage();
			}
		}, 20);
	}

	private void resetKey() {
		retry = true;
		keys.clear();
		pointer = -1;
		currentKey = null;
		generateKeys();
		nextPointer(true);
		Bukkit.getScheduler().runTaskLater(ByeHacker.instance, new Runnable() {
			@Override
			public void run() {
				retry = false;
				sendAuthMessage();
			}
		}, 30);
	}

	public void sendAuthMessage() {
		if (lastCheck) return;
		if (retry) {
			sendBook(player, RETRY_MESSAGE);
			return;
		}
		final StringBuilder sb = new StringBuilder("§r  §0>>  ");
		for (int i = 0, length = keys.size();i<length;i++) {
			final ByehackerKey key = keys.get(i);
			sb.append((pointer>i) ? "§1"+key.getKey() : "§5_");
			sb.append(' ');
		}
		sb.append(" §0<<\n");
		sendBook(player, ComponentSerializer.toString(prefix, new TextComponent(TextComponent.fromLegacyText(sb.toString())), keypad));
	}
	private void sendBook(final Player player, final String message) {
		meta.setPages(EMPTY_PAGE);
		BookUtils.addBookPage(meta, message);
		book.setItemMeta(meta);
		BookUtils.openBook(player, book);
	}
	private static String[] EMPTY_PAGE = new String[]{};
}
