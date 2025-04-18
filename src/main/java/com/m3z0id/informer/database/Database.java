package com.m3z0id.informer.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m3z0id.informer.Informer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.*;

public class Database {
    private Connection connection;
    private Gson gson;

    public Database(String url) {
        try {
            this.connection = DriverManager.getConnection(url);
            this.gson = new Gson();
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "ip TEXT UNIQUE NOT NULL, " +
                    "players TEXT NOT NULL CHECK(json_valid(players)))");
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Could not connect to database.");
        }
    }

    public List<String> getPlayersByIp(@Nonnull String ip) {
        String query = "SELECT players FROM players WHERE ip = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ip);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String json = resultSet.getString("players");
                return new ArrayList<>(new HashSet<>(gson.fromJson(json, new TypeToken<List<String>>() {}.getType())));
            }
            return List.of();
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to get players by ip. " + e.getMessage());
        }
        return List.of();
    }

    public void addPlayer(@Nonnull String ip, @Nonnull String player) {
        List<String> playersWithIp = getPlayersByIp(ip);
        if(playersWithIp.isEmpty()) {
            playersWithIp = new ArrayList<>();
        }
        if(!playersWithIp.contains(player)) {
            playersWithIp.add(player);
        }

        String query = "INSERT INTO players (ip, players) VALUES (?, ?) ON CONFLICT(ip) DO UPDATE SET players = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ip);
            statement.setString(2, gson.toJson(playersWithIp));
            statement.setString(3, gson.toJson(playersWithIp));
            statement.executeUpdate();
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to add player to the database. " + e.getMessage());
        }
    }

    public void removeIp(@Nonnull String ip) {
        String query = "DELETE FROM players WHERE ip = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ip);
            statement.executeUpdate();
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to remove ip from the database." + e.getMessage());
        }
    }

    public void removePlayer(@Nonnull String player, @Nonnull String ip) {
        List<String> players = getPlayersByIp(ip);
        if(players.isEmpty() || !players.contains(player)) {
            return;
        }

        players.remove(player);

        String query = "INSERT INTO players (ip, players) VALUES (?, ?) ON CONFLICT(ip) DO UPDATE SET players = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ip);
            statement.setString(2, gson.toJson(players));
            statement.setString(3, gson.toJson(players));
            statement.executeUpdate();
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to remove player from the database." + e.getMessage());
        }
    }

    public List<String> getIpsByPlayer(@Nonnull String player) {
        String query = "SELECT ip FROM players, json_each(players) WHERE json_each.value = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player);

            ResultSet resultSet = statement.executeQuery();
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getString("ip"));
            }
            return result;
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to get IPs by player from the database." + e.getMessage());
            return List.of();
        }
    }

    public List<String> getIps(){
        String query = "SELECT ip FROM players";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getString("ip").replaceAll("/", ""));
            }
            return result;
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to get IPs by player from the database." + e.getMessage());
            return List.of();
        }
    }

    public List<String> getPlayers() {
        String query = "SELECT players FROM players";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            HashSet<String> result = new HashSet<>();
            while (resultSet.next()) {
                result.addAll(gson.fromJson(resultSet.getString("players"), new TypeToken<HashSet<String>>() {}.getType()));
            }
            return new ArrayList<>(result);
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to get players by player from the database." + e.getMessage());
            return List.of();
        }
    }
}
