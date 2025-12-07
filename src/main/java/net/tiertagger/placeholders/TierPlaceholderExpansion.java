package net.tiertagger.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.tiertagger.TierTaggerPlugin;
import net.tiertagger.models.PlayerTierData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TierPlaceholderExpansion extends PlaceholderExpansion {

    // God forgive my code and my sins
    
    private final TierTaggerPlugin plugin;
    
    public TierPlaceholderExpansion(TierTaggerPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public @NotNull String getIdentifier() {
        return "tiertagger";
    }
    
    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }
    
    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        
        if (!plugin.getTierDisplayManager().isPlayerDisplayEnabled(player) ||
            plugin.getTierDisplayManager().isTierHidden(player) ||
            !plugin.getTierDisplayManager().isGlobalDisplayEnabled()) {
            return getDefaultPlaceholderValue(params);
        }
        
        String uuid = player.getUniqueId().toString();
        
        try {
            Optional<PlayerTierData> cachedData = plugin.getDatabaseManager().getCachedTierData(uuid).get();
            
            if (!cachedData.isPresent()) {
                return getDefaultPlaceholderValue(params);
            }
            
            PlayerTierData tierData = cachedData.get();

            return switch (params.toLowerCase()) {
                case "tier", "best_tier" -> tierData.getBestTier();
                case "tier_formatted", "best_tier_formatted" -> formatTierDisplay(tierData.getBestTier());
                case "points" -> String.valueOf(tierData.getPoints());
                case "overall", "overall_rank" -> String.valueOf(tierData.getOverall());
                case "region" -> tierData.getRegion() != null ? tierData.getRegion() : "";
                case "vanilla", "vanilla_tier" -> tierData.getTierForGamemode("vanilla");
                case "sword", "sword_tier" -> tierData.getTierForGamemode("sword");
                case "pot", "pot_tier" -> tierData.getTierForGamemode("pot");
                case "uhc", "uhc_tier" -> tierData.getTierForGamemode("uhc");
                case "axe", "axe_tier" -> tierData.getTierForGamemode("axe");
                case "nethop", "nethop_tier" -> tierData.getTierForGamemode("nethop");
                case "mace", "mace_tier" -> tierData.getTierForGamemode("mace");
                case "smp", "smp_tier" -> tierData.getTierForGamemode("smp");
                case "crystal", "crystal_tier" -> tierData.getTierForGamemode("crystal");
                case "ht1" -> tierData.getBestTier().equals("HT1") ? "HT1" : "";
                case "lt1" -> tierData.getBestTier().equals("LT1") ? "LT1" : "";
                case "ht2" -> tierData.getBestTier().equals("HT2") ? "HT2" : "";
                case "lt2" -> tierData.getBestTier().equals("LT2") ? "LT2" : "";
                case "ht3" -> tierData.getBestTier().equals("HT3") ? "HT3" : "";
                case "lt3" -> tierData.getBestTier().equals("LT3") ? "LT3" : "";
                case "ht4" -> tierData.getBestTier().equals("HT4") ? "HT4" : "";
                case "lt4" -> tierData.getBestTier().equals("LT4") ? "LT4" : "";
                case "ht5" -> tierData.getBestTier().equals("HT5") ? "HT5" : "";
                case "lt5" -> tierData.getBestTier().equals("LT5") ? "LT5" : "";
                default -> getDefaultPlaceholderValue(params);
            };
            
        } catch (Exception e) {
            return getDefaultPlaceholderValue(params);
        }
    }
    
    private String getDefaultPlaceholderValue(String params) {
        return switch (params.toLowerCase()) {
            case "tier", "best_tier", "vanilla", "sword", "pot", "uhc", "axe", "nethop", "mace", "smp", "crystal" ->
                    "UNRANKED";
            case "tier_formatted", "best_tier_formatted" -> "§fUnranked";
            case "points", "overall", "overall_rank" -> "0";
            case "region" -> "";
            case "ht1", "lt1", "ht2", "lt2", "ht3", "lt3", "ht4", "lt4", "ht5", "lt5" -> "";
            default -> "";
        };
    }
    
    private String formatTierDisplay(String tier) {
        switch (tier.toUpperCase()) {
            case "HT1": return "§c§lHigh Tier 1";
            case "LT1": return "§6§lLow Tier 1";
            case "HT2": return "§e§lHigh Tier 2";
            case "LT2": return "§a§lLow Tier 2";
            case "HT3": return "§b§lHigh Tier 3";
            case "LT3": return "§9§lLow Tier 3";
            case "HT4": return "§d§lHigh Tier 4";
            case "LT4": return "§5§lLow Tier 4";
            case "HT5": return "§7§lHigh Tier 5";
            case "LT5": return "§8§lLow Tier 5";
            default: return "§fUnranked";
        }
    }
}