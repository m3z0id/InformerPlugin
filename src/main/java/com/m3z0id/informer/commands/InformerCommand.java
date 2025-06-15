package com.m3z0id.informer.commands;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import com.m3z0id.informer.database.EssentialsImport;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.*;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InformerCommand implements BasicCommand {

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Lang lang = Informer.instance.lang;
        if(args.length == 0) {
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getUnknownSubcommand()));
            return;
        }
        if(args.length == 1) {
            if(args[0].equals("reload")) {
                Informer.instance.load();
                commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getSuccessMessage()));
            }
            else if(args[0].equals("importEssX")) {
                if(!(commandSourceStack.getSender() instanceof ConsoleCommandSender)){
                    commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + "&cThis is a console-only command."));
                    return;
                }
                EssentialsImport.run();
                commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getSuccessMessage()));
            }
            else {
                commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getUnknownSubcommand()));
            }
        }
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
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
        return List.of();
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() || sender instanceof ConsoleCommandSender || sender.hasPermission("informer.command");
    }

    @Override
    public @Nullable String permission() {
        return "informer.command";
    }
}
