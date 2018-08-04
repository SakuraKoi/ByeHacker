package ldcr.ByeHacker.listeners;

import java.util.ArrayList;
import java.util.Arrays;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import ldcr.ByeHacker.ByeHacker;

public class ChatPacketListener extends PacketAdapter {
	public ChatPacketListener() {
		super(ByeHacker.instance, ListenerPriority.NORMAL, PacketType.Play.Client.CHAT);
	}
	protected final ArrayList<String> whileList = new ArrayList<String>(Arrays.asList(new String[]{
			"/login",
			"/register",
			"/l",
			"/reg"
	}));

	@Override
	public void onPacketReceiving(final PacketEvent event) {
		if (event.getPacketType() == PacketType.Play.Client.CHAT) {
			final PacketContainer packet = event.getPacket();
			if (packet==null) return;
			final String message = packet.getStrings().read(0);
			if (message==null) return;
			if (whileList.contains(message.split(" ")[0].toLowerCase())) return;

			if (ByeHacker.isAuthing(event.getPlayer())) {
				if (!ByeHacker.instance.callAuthPlayerChat(event.getPlayer(), message)) {
					event.setCancelled(true);
				}
			}
			if (!ByeHacker.instance.callNonAuthPlayerChat(message)) {
				event.setCancelled(true);
			}
		}
	}
}