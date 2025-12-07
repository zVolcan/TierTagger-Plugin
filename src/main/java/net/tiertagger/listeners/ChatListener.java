package net.tiertagger.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tiertagger.TierTaggerPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    
    private final TierTaggerPlugin plugin;
    
    public ChatListener(TierTaggerPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncChatEvent event) {
        if (!plugin.getConfigurationManager().isDisplayEnabled() ||
            !plugin.getTierDisplayManager().isGlobalDisplayEnabled() ||
            !plugin.getConfigurationManager().isShowInChat()) {
            return;
        }
        
        Player player = event.getPlayer();
        
        if (!plugin.getTierDisplayManager().isPlayerDisplayEnabled(player)) {
            return;
        }
        
        String tier = plugin.getTierDisplayManager().getPlayerTier(player);
        
        if (tier != null && !tier.equals("UNRANKED")) {
            String coloredTier = getColoredTier(tier);
            String displayName = coloredTier + " <gray>| <white>" + player.getName();
            
            event.message(MiniMessage.miniMessage().deserialize(displayName + "<white>: %2$s"));
        }
    }
    
    private String getColoredTier(String tier) {
        return switch (tier.toUpperCase()) {
            case "HT1" -> "§c" + tier;
            case "LT1" -> "§6" + tier;
            case "HT2" -> "§e" + tier;
            case "LT2" -> "§a" + tier;
            case "HT3" -> "§b" + tier;
            case "LT3" -> "§9" + tier;
            case "HT4" -> "§d" + tier;
            case "LT4" -> "§5" + tier;
            case "HT5" -> "§7" + tier;
            case "LT5" -> "§8" + tier;
            default -> "§f" + tier;
        };
    }
}