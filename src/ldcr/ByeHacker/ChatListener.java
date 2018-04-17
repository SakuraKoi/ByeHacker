package ldcr.ByeHacker;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

class ChatListener extends PacketAdapter {
	private final ByeHacker instance;
	public ChatListener(final ByeHacker byeHacker) {
		super(byeHacker, ListenerPriority.NORMAL, PacketType.Play.Client.CHAT);
		instance = byeHacker;
	}

	@Override
	public void onPacketReceiving(final PacketEvent event) {
		if (event.getPacketType() == PacketType.Play.Client.CHAT) {
			final PacketContainer packet = event.getPacket();
			if (packet==null) return;
			final String message = packet.getStrings().read(0);
			if (message==null) return;
			if (message.startsWith(instance.authPrefix)) {
				event.setCancelled(true);
				if (instance.isWaitingAuth(event.getPlayer())) {
					if (message.equals(instance.layer1Auth.get(event.getPlayer()))) {
						event.getPlayer().sendMessage("§b§l作弊验证 §7>> §a您已通过第一层反作弊验证!");
						instance.layer1Auth.remove(event.getPlayer());
						instance.layer2Auth.put(event.getPlayer(), false);
						new Layer2Check(instance, event.getPlayer(), false);
					}
				}
			} else if (message.startsWith(instance.hackPrefix)) {
				event.setCancelled(true);
				if (instance.isWaitingAuth(event.getPlayer())) {
					final String authKey = instance.layer1Auth.get(event.getPlayer());
					System.out.println(authKey.substring(5, authKey.length()));
					if (message.equals(authKey.substring(5, authKey.length()))) {
						event.getPlayer().sendMessage("§b§l作弊验证 §7>> §a您已通过第一层反作弊验证!");
						instance.layer1Auth.remove(event.getPlayer());
						instance.layer2Auth.put(event.getPlayer(), true);
						new Layer2Check(instance, event.getPlayer(), true);
					}
				}
			} else if (instance.whileList.contains(message.split(" ")[0].toLowerCase()))
				return;
			else if (instance.isWaitingAuth(event.getPlayer())) {
				instance.sendAuthMessage(event.getPlayer());
				event.setCancelled(true);
			}
		}
	}
}