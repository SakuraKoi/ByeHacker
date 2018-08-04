package ldcr.ByeHacker.impl;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketDataSerializer;
import net.minecraft.server.v1_9_R1.PacketPlayOutCustomPayload;

public class BookUtilsImpl2 implements IBookUtils
{
	@Override
	public void openBook(final Player player, final ItemStack book) {
		final int slot = player.getInventory().getHeldItemSlot();
		final ItemStack old = player.getInventory().getItem(slot);
		player.getInventory().setItem(slot, book);
		final PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(Unpooled.buffer()));
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
		player.getInventory().setItem(slot, old);
	}

	@Override
	public void addBookPage(final BookMeta meta, final String json) {
		final CraftMetaBook craftMetaBook = (CraftMetaBook)meta;
		final IChatBaseComponent component = IChatBaseComponent.ChatSerializer.a(json);
		craftMetaBook.pages.add(component);
	}
}