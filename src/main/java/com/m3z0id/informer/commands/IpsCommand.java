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
import java.util.List;

public class IpsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        Database database = Informer.instance.database;
        Lang lang = Informer.instance.lang;

        List<String> ips = database.getIpsByPlayer(args[0]);
        if(ips.isEmpty()) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getNothingFound()));
            return true;
        }

        String allIps = lang.getEntryFormatting() + String.join(lang.getDividerFormatting() + ", " + lang.getEntryFormatting(), ips);
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getIpsMessage().replaceAll("%ips%", allIps.replaceAll("/", "")).replaceAll("%player%", args[0])));

        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        return null;
    }
}
