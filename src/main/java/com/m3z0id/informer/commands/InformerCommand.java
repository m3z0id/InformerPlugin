package com.m3z0id.informer.commands;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import com.m3z0id.informer.database.EssentialsImport;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class InformerCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        Lang lang = Informer.instance.lang;
        if(args.length == 0) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getUnknownSubcommand()));
            return true;
        }
        if(args.length == 1) {
            if(args[0].equals("reload")) {
                Informer.instance.load();
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getSuccessMessage()));
            }
            else if(args[0].equals("importEssX")) {
                if(!(commandSender instanceof ConsoleCommandSender)){
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + ChatColor.RED + "This is a console-only command."));
                    return true;
                }
                EssentialsImport.run();
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getSuccessMessage()));
            }
            else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getUnknownSubcommand()));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        List<String> choices = new ArrayList<>();
        if(args.length == 1) {
            if("reload".startsWith(args[0])) {
                choices.add("reload");
            }
            if("importEssX".startsWith(args[0])) {
                choices.add("importEssX");
            }
            return choices;
        }
        return null;
    }
}
