package ldcr.ByeHacker;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

class ChatListener extends PacketAdapter {
    public ChatListener() {
	super(ByeHacker.instance, ListenerPriority.NORMAL, PacketType.Play.Client.CHAT);
    }

    @Override
    public void onPacketReceiving(final PacketEvent event) {
	if (event.getPacketType() == PacketType.Play.Client.CHAT) {
	    final PacketContainer packet = event.getPacket();
	    if (packet==null) return;
	    final String message = packet.getStrings().read(0);
	    if (message==null) return;
	    if (ByeHacker.instance.whileList.contains(message.split(" ")[0].toLowerCase())) return;

	    if (ByeHacker.instance.isWaitingAuth(event.getPlayer())) {
		if (!ByeHacker.instance.callAuthPlayerChat(event.getPlayer(), message)) {
		    event.setCancelled(true);
		}
	    }
	    if (!ByeHacker.instance.callNonAuthPlayerChat(event.getPlayer(), message)) {
		event.setCancelled(true);
	    }
	}
    }
}