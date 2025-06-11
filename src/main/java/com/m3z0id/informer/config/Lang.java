package com.m3z0id.informer.config;

import com.google.gson.Gson;
import com.m3z0id.informer.Informer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.Objects;

public class Lang {
    private String serverPrefix;
    private String brandMessage;
    private String newPlayerMessage;
    private String altsMessage;
    private String badSyntax;
    private String nothingFound;
    private String entryFormatting;
    private String dividerFormatting;
    private String ipsMessage;
    private String successMessage;

    public String getServerPrefix() {
        return serverPrefix;
    }
    public String getBrandMessage() {
        return brandMessage;
    }
    public String getNewPlayerMessage() {
        return newPlayerMessage;
    }
    public String getAltsMessage() {
        return altsMessage;
    }
    public String getUnknownSubcommand() {
        return badSyntax;
    }
    public String getNothingFound() {
        return nothingFound;
    }
    public String getEntryFormatting() {
        return entryFormatting;
    }
    public String getDividerFormatting() {
        return dividerFormatting;
    }

    public String getIpsMessage() {
        return ipsMessage;
    }
    public String getSuccessMessage() {
        return successMessage;
    }

    public static Lang get(){
        File langPath = new File(Informer.instance.getDataFolder(), "lang.json");
        if (!langPath.exists()){
            try {
                Files.copy(Objects.requireNonNull(Informer.class.getResourceAsStream("/lang.json")), langPath.toPath());
            } catch (IOException e) {
                Informer.instance.getLogger().severe("Failed to drop lang file.");
                return null;
            }
        }
        try (Reader reader = new FileReader(langPath)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Lang.class);
        } catch (IOException e){
            Informer.instance.getLogger().severe("Failed to load lang file.");
            return null;
        }
    }
}
