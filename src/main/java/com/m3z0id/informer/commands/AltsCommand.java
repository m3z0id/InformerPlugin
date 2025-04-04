package com.m3z0id.informer.commands;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import com.m3z0id.informer.database.Database;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AltsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        Lang lang = Informer.instance.lang;
        if(args.length < 1) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getUnknownSubcommand()));
            return true;
        }

        Database database = Informer.instance.database;

        List<String> ips = database.getIpsByPlayer(args[0]);
        if(ips == null || ips.isEmpty()) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getNothingFound()));
            return true;
        }

        List<String> players = new ArrayList<>();
        for(String ip : ips) {
            List<String> currentEntry = database.getPlayersByIp(ip);
            assert currentEntry != null;
            players.addAll(currentEntry);
        }

        if(players.isEmpty()) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getNothingFound()));
            return true;
        }

        StringBuilder playersBuilder = new StringBuilder();
        playersBuilder.append(lang.getEntryFormatting());
        for(int i = 0; i < players.size(); i++) {
            if(i == 0 || playersBuilder.isEmpty()) {
                playersBuilder.append(lang.getEntryFormatting()).append(players.get(i));
            } else {
                playersBuilder.append(lang.getDividerFormatting() + ", " + lang.getEntryFormatting()).append(players.get(i));
            }
        }

        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getAltsMessage().replaceAll("%player%", args[0]).replaceAll("%alts%", playersBuilder.toString())));
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        return null;
    }
}
