package com.m3z0id.informer.events.join;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JoinEvent implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@Nonnull String s, @Nonnull Player player, @Nonnull byte[] data) {
        Lang lang = Informer.instance.lang;
        String brand = new String(data, StandardCharsets.UTF_8).substring(1);
        Informer.instance.clientBrands.put(player.getName(), brand);
        InetAddress ip = getIp(player);

        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getBrandMessage().replaceAll("%brand%", brand).replaceAll("%player%", player.getName())), Informer.instance.config.getNotificationPermission());
        if(ip == null) return;

        List<String> players = Informer.instance.database.getPlayersByIp(ip.toString());
        if(players == null || players.isEmpty()) {
            Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getNewPlayerMessage().replaceAll("%player%", player.getName())), Informer.instance.config.getNotificationPermission());
            Informer.instance.database.addPlayer(ip.toString(), player.getName());
            return;
        }

        StringBuilder playersBuilder = new StringBuilder();
        playersBuilder.append(lang.getEntryFormatting());
        for (int i = 0; i < players.size(); i++) {
            if(i == 0 || playersBuilder.isEmpty()) {
                playersBuilder.append(lang.getEntryFormatting()).append(players.get(i));
            } else {
                playersBuilder.append(lang.getDividerFormatting() + ", " + lang.getEntryFormatting()).append(players.get(i));
            }
        }
        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getAltsMessage().replaceAll("%player%", player.getName()).replaceAll("%alts%", playersBuilder.toString())), Informer.instance.config.getNotificationPermission());

        Informer.instance.database.addPlayer(ip.toString(), player.getName());
    }

    public static @Nullable InetAddress getIp(Player player) {
        if(player.getAddress() != null) {
            return player.getAddress().getAddress();
        }
        return null;
    }
}
