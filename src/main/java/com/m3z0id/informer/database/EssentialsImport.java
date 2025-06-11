package com.m3z0id.informer.database;

import com.m3z0id.informer.Informer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class EssentialsImport {
    public static boolean run() {
        Database database = Informer.instance.database;
        Plugin essentialsX = Bukkit.getPluginManager().getPlugin("Essentials");
        if (essentialsX == null) return false;

        Path usersPath = new File(essentialsX.getDataFolder(), "userdata").toPath();
        List<File> users = Stream.of(Objects.requireNonNull(usersPath.toFile().listFiles()))
                .filter(File::isFile)
                .filter(file -> file.toString().endsWith(".yml"))
                .toList();

        for (File file : users) {
            Yaml yaml = new Yaml();
            try {
                Map<String, Object> data = yaml.load(new FileReader(file));
                String ip = "/" + data.get("ip-address");
                String username = (String) data.get("last-account-name");

                database.addPlayer(ip, username);
            } catch (FileNotFoundException ignored) {}
        }

        return true;
    }
}
