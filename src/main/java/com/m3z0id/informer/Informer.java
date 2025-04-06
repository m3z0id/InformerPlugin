package com.m3z0id.informer;

import com.m3z0id.informer.commands.*;
import com.m3z0id.informer.config.Config;
import com.m3z0id.informer.config.Lang;
import com.m3z0id.informer.database.Database;
import com.m3z0id.informer.events.join.JoinEvent;
import com.m3z0id.informer.events.join.LeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Informer extends JavaPlugin {
    public static Informer instance;
    public Database database;
    public Map<String, String> clientBrands;

    public Lang lang;
    public Config config;

    @Override
    public void onEnable() {
        instance = this;

        load();

        clientBrands = new HashMap<>();

        getCommand("client").setExecutor(new BrandsCommand());
        getCommand("client").setTabCompleter(new BrandsCommand());

        getCommand("alts").setExecutor(new AltsCommand());
        getCommand("alts").setTabCompleter(new AltsCommand());

        getCommand("ips").setExecutor(new IpsCommand());
        getCommand("ips").setTabCompleter(new IpsCommand());

        getCommand("informer").setExecutor(new InformerCommand());
        getCommand("informer").setTabCompleter(new InformerCommand());

        getCommand("dataremove").setExecutor(new RemoveCommand());
        getCommand("dataremove").setTabCompleter(new RemoveCommand());

        Bukkit.getMessenger().registerIncomingPluginChannel(this, "minecraft:brand", new JoinEvent());
        Bukkit.getPluginManager().registerEvents(new LeaveEvent(), this);
    }

    public void load(){
        getDataFolder().mkdirs();
        database = new Database("jdbc:sqlite:" + getDataFolder() + "/database.db");

        lang = Lang.get();
        config = Config.get();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
