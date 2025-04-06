package com.m3z0id.informer.commands;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class RemoveCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        Lang lang = Informer.instance.lang;
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
            if(ips == null || ips.isEmpty()){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getSuccessMessage()));
                return true;
            }
            for(String ip : ips) {
                String databaseIp = "/" + ip;
                Informer.instance.database.removePlayer(args[1], databaseIp);
            }
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getSuccessMessage()));
            return true;
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getUnknownSubcommand()));
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
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
                List<String> temp = Informer.instance.database.getIps();
                if(temp == null || temp.isEmpty()) {
                    return List.of("");
                }
                for(String ip : temp) {
                    if(ip.startsWith(args[1])) list.add(ip);
                }
            }
            if(args[0].equalsIgnoreCase("player")) {
                List<String> templist = Informer.instance.database.getPlayers();
                if(templist == null) return List.of("");
                List<String> tempSet = new ArrayList<>();
                for(String entry : templist) {
                    LinkedHashSet<String> temp = new LinkedHashSet<>(Arrays.asList(entry.split(";")));
                    for(String tempidk : temp) {
                        if(tempidk.startsWith(args[1])) tempSet.add(tempidk);
                    }
                }
                list = new ArrayList<>(new LinkedHashSet<>(tempSet));
            }
        }
        return list;
    }
}
