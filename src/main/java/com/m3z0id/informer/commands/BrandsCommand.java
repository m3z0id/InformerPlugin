package com.m3z0id.informer.commands;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public class BrandsCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        Lang lang = Informer.instance.lang;
        if (player == null) {
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getNothingFound()));
            return;
        }
        String brand = Informer.instance.clientBrands.getOrDefault(player.getName(), null);
        if (brand == null) {
            commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getNothingFound()));
            return;
        }
        commandSourceStack.getSender().sendMessage(LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getBrandMessage().replaceAll("%brand%", brand).replaceAll("%player%", player.getName())));
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() || sender instanceof ConsoleCommandSender || sender.hasPermission("informer.brands");
    }

    @Override
    public @Nullable String permission() {
        return "informer.brands";
    }
}
