package com.m3z0id.informer.commands;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import com.m3z0id.informer.database.Database;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.*;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class IpsCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Lang lang = Informer.instance.lang;
        if(args.length == 0) {
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + "&cThis is a console-only command."));
            return;
        }
        Database database = Informer.instance.database;

        List<String> ips = database.getIpsByPlayer(args[0]);
        if(ips.isEmpty()) {
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getNothingFound()));
            return;
        }

        String allIps = lang.getEntryFormatting() + String.join(lang.getDividerFormatting() + ", " + lang.getEntryFormatting(), ips);
        commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getIpsMessage().replaceAll("%ips%", allIps.replaceAll("/", "")).replaceAll("%player%", args[0])));
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        return Informer.instance.database.getPlayers();
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() || sender instanceof ConsoleCommandSender || sender.hasPermission("informer.ips");
    }

    @Override
    public @Nullable String permission() {
        return "informer.ips";
    }
}
