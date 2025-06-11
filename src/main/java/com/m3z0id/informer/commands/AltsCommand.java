package com.m3z0id.informer.commands;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import com.m3z0id.informer.database.Database;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class AltsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        Lang lang = Informer.instance.lang;
        if(args.length < 1) {
            commandSender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getUnknownSubcommand()));
            return true;
        }

        Database database = Informer.instance.database;

        List<String> ips = database.getIpsByPlayer(args[0]);
        if(ips.isEmpty()) {
            commandSender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getNothingFound()));
            return true;
        }

        LinkedHashSet<String> players = new LinkedHashSet<>();
        ips.forEach(ip -> {
            List<String> currentEntry = database.getPlayersByIp(ip);
            players.addAll(currentEntry);
        });

        if(players.isEmpty()) {
            commandSender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getNothingFound()));
            return true;
        }

        List<String> playersList = new ArrayList<>(players);

        String alts = lang.getEntryFormatting() + String.join(lang.getDividerFormatting() + ", " + lang.getEntryFormatting(), playersList);

        commandSender.sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getAltsMessage().replaceAll("%player%", args[0]).replaceAll("%alts%", alts)));
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        return null;
    }
}
