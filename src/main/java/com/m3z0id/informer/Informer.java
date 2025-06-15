package com.m3z0id.informer;

import com.m3z0id.informer.commands.*;
import com.m3z0id.informer.config.Config;
import com.m3z0id.informer.config.Lang;
import com.m3z0id.informer.database.Database;
import com.m3z0id.informer.events.JoinEvent;
import com.m3z0id.informer.events.LeaveEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("client", new BrandsCommand());
            commands.registrar().register("alts", new AltsCommand());
            commands.registrar().register("ips", new IpsCommand());
            commands.registrar().register("informer", new InformerCommand());
            commands.registrar().register("dataremove", new RemoveCommand());
        });

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
