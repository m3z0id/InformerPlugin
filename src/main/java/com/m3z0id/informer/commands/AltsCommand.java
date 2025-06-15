package com.m3z0id.informer.commands;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import com.m3z0id.informer.database.Database;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class AltsCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Lang lang = Informer.instance.lang;
        if(args.length < 1) {
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getUnknownSubcommand()).decoration(TextDecoration.ITALIC, false));
            return;
        }

        Database database = Informer.instance.database;

        List<String> ips = database.getIpsByPlayer(args[0]);
        if(ips.isEmpty()) {
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getNothingFound()).decoration(TextDecoration.ITALIC, false));
            return;
        }

        LinkedHashSet<String> players = new LinkedHashSet<>();
        ips.forEach(ip -> {
            List<String> currentEntry = database.getPlayersByIp(ip);
            players.addAll(currentEntry);
        });

        if(players.isEmpty()) {
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getNothingFound()).decoration(TextDecoration.ITALIC, false));
            return;
        }

        List<String> playersList = new ArrayList<>(players);

        String alts = lang.getEntryFormatting() + String.join(lang.getDividerFormatting() + ", " + lang.getEntryFormatting(), playersList);

        commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getAltsMessage().replaceAll("%player%", args[0]).replaceAll("%alts%", alts)).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        // TODO: Suggest players from the database instead of online players
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() || sender instanceof ConsoleCommandSender || sender.hasPermission("informer.alts");
    }

    @Override
    public @Nullable String permission() {
        return "informer.alts";
    }
}
