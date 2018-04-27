package ldcr.ByeHacker;

import org.bukkit.entity.Player;

public interface IByeHackerLayer {
    public String getLayerName();
    public void requestAuth(Player player);
    public void endAuth(Player player);
    public void onDenyAction(Player player);
    public boolean onChat(Player player, String message);
    public boolean onNonAuthPlayerChat(Player player, String message);
}
