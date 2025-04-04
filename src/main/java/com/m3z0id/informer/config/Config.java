package com.m3z0id.informer.config;

import com.google.gson.Gson;
import com.m3z0id.informer.Informer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.Objects;

public class Config {
    private String notificationPermission;

    public String getNotificationPermission() {
        return notificationPermission;
    }

    public static Config get(){
        File configPath = new File(Informer.instance.getDataFolder(), "config.json");
        if (!configPath.exists()) {
            try {
                Files.copy(Objects.requireNonNull(Informer.class.getResourceAsStream("/config.json")), configPath.toPath());
            } catch (IOException e) {
                Informer.instance.getLogger().severe("Failed to drop config.");
                return null;
            }
        }
        try (Reader reader = new FileReader(configPath)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Config.class);
        } catch (IOException e){
            Informer.instance.getLogger().severe("Failed to load config.");
            return null;
        }
    }
}
