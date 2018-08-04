package ldcr.ByeHacker.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public interface IBookUtils {
	public void openBook(Player player, ItemStack book);
	public void addBookPage(final BookMeta meta, final String json);

}
