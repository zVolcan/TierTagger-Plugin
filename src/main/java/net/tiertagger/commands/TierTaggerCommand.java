package net.tiertagger.commands;

import net.tiertagger.TierTaggerPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TierTaggerCommand implements CommandExecutor {
    
    private final TierTaggerPlugin plugin;
    
    public TierTaggerCommand(TierTaggerPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload" -> handleReload(sender);
            case "help" -> sendHelpMessage(sender);
            default -> sender.sendMessage(plugin.getLanguageManager().getMessage("commands.unknown"));
        }

        return true;
    }
    
    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("tiertagger.reload")) {
            sender.sendMessage(plugin.getLanguageManager().getMessage("commands.no_permission"));
            return;
        }
        
        try {
            plugin.reloadConfiguration();
            plugin.getDatabaseManager().clearAllCache();
            
            sender.sendMessage(plugin.getLanguageManager().getMessage("commands.reload.success"));
            sender.sendMessage(plugin.getLanguageManager().getMessage("commands.reload.cache_cleared"));
        } catch (Exception e) {
            sender.sendMessage(plugin.getLanguageManager().getMessage("commands.reload.error", "error", e.getMessage()));
        }
    }
    
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(plugin.getLanguageManager().getMessage("commands.help.header"));
        sender.sendMessage(plugin.getLanguageManager().getMessage("commands.help.reload"));
        sender.sendMessage(plugin.getLanguageManager().getMessage("commands.help.tier"));
        sender.sendMessage(plugin.getLanguageManager().getMessage("commands.help.display"));
        sender.sendMessage(plugin.getLanguageManager().getMessage("commands.help.tieroff"));
    }
}