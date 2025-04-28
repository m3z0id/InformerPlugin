package com.m3z0id.informer.commands;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RemoveCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        Lang lang = Informer.instance.lang;
        if(args.length == 0) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + ChatColor.RED + "This is a console-only command."));
            return true;
        }
        if(args.length != 2) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getUnknownSubcommand()));
            return true;
        }
        if(args[0].equalsIgnoreCase("ip")) {
            String ip = "/" + args[1];
            Informer.instance.database.removeIp(ip);
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getSuccessMessage()));
            return true;
        }
        else if(args[0].equalsIgnoreCase("player")) {
            List<String> ips = Informer.instance.database.getIps();
            if(ips.isEmpty()){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getSuccessMessage()));
                return true;
            }
            ips.forEach(ip -> Informer.instance.database.removePlayer(args[1], "/" + ip));
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getSuccessMessage()));
            return true;
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getUnknownSubcommand()));
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        if(!(commandSender instanceof ConsoleCommandSender)){
            return List.of();
        }
        List<String> list = new ArrayList<>();
        if(args.length == 1) {
            if("ip".startsWith(args[0])) {
                list.add("ip");
            }
            if("player".startsWith(args[0])) {
                list.add("player");
            }
        }
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("ip")) {
                list = Informer.instance.database.getIps().stream()
                        .filter(entry -> entry.startsWith(args[1]))
                        .toList();
            }
            if(args[0].equalsIgnoreCase("player")) {
                list = Informer.instance.database.getPlayers().stream()
                        .filter(entry -> entry.startsWith(args[1]))
                        .toList();
            }
        }
        return list;
    }
}
