package com.m3z0id.informer.events.join;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JoinEvent implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@Nonnull String s, @Nonnull Player player, @Nonnull byte[] data) {
        Lang lang = Informer.instance.lang;
        String brand = new String(data, StandardCharsets.UTF_8).substring(1);
        Informer.instance.clientBrands.put(player.getName(), brand);

        if(player.getAddress() == null) return;
        InetAddress ip = player.getAddress().getAddress();

        Component msg = LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getBrandMessage().replaceAll("%brand%", brand).replaceAll("%player%", player.getName()));
        Bukkit.broadcast(msg, Informer.instance.config.getNotificationPermission());
        Informer.instance.getLogger().info(msg.toString());
        if(ip == null) return;

        List<String> players = Informer.instance.database.getPlayersByIp(ip.toString());
        if(players.isEmpty()) {
            msg = LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getNewPlayerMessage().replaceAll("%player%", player.getName()));
            Bukkit.broadcast(msg, Informer.instance.config.getNotificationPermission());
            Informer.instance.database.addPlayer(ip.toString(), player.getName());
            return;
        }

        String alts = lang.getEntryFormatting() + String.join(lang.getDividerFormatting() + ", " + lang.getEntryFormatting(), players);
        msg = LegacyComponentSerializer.legacySection().deserialize(lang.getServerPrefix() + lang.getAltsMessage().replaceAll("%player%", player.getName()).replaceAll("%alts%", alts));
        Bukkit.broadcast(msg, Informer.instance.config.getNotificationPermission());
        Informer.instance.getLogger().info(msg.toString());

        Informer.instance.database.addPlayer(ip.toString(), player.getName());
    }
}
