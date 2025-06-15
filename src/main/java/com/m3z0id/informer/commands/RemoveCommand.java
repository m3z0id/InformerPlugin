package com.m3z0id.informer.commands;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.*;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RemoveCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Lang lang = Informer.instance.lang;
        if(args.length == 0) {
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + "&cThis is a console-only command."));
            return;
        }
        if(args.length != 2) {
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getUnknownSubcommand()));
            return;
        }
        if(args[0].equalsIgnoreCase("ip")) {
            String ip = "/" + args[1];
            Informer.instance.database.removeIp(ip);
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getSuccessMessage()));
        }
        else if(args[0].equalsIgnoreCase("player")) {
            List<String> ips = Informer.instance.database.getIps();
            if(ips.isEmpty()){
                commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getSuccessMessage()));
                return;
            }
            ips.forEach(ip -> Informer.instance.database.removePlayer(args[1], "/" + ip));
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getSuccessMessage()));
        } else {
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getUnknownSubcommand()));
        }
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
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

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() || sender instanceof ConsoleCommandSender || sender.hasPermission("informer.remove");
    }

    @Override
    public @Nullable String permission() {
        return "informer.remove";
    }
}
