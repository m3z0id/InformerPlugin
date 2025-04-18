package com.m3z0id.informer.events.join;

import com.m3z0id.informer.Informer;
import com.m3z0id.informer.config.Lang;
import net.md_5.bungee.api.ChatColor;
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
        InetAddress ip = player.getAddress().getAddress();

        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getBrandMessage().replaceAll("%brand%", brand).replaceAll("%player%", player.getName())), Informer.instance.config.getNotificationPermission());
        if(ip == null) return;

        List<String> players = Informer.instance.database.getPlayersByIp(ip.toString());
        if(players.isEmpty()) {
            Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getNewPlayerMessage().replaceAll("%player%", player.getName())), Informer.instance.config.getNotificationPermission());
            Informer.instance.database.addPlayer(ip.toString(), player.getName());
            return;
        }

        String alts = lang.getEntryFormatting() + String.join(lang.getDividerFormatting() + ", " + lang.getEntryFormatting(), players);
        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', lang.getServerPrefix() + lang.getAltsMessage().replaceAll("%player%", player.getName()).replaceAll("%alts%", alts)), Informer.instance.config.getNotificationPermission());

        Informer.instance.database.addPlayer(ip.toString(), player.getName());
    }
}
