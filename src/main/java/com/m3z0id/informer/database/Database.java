package com.m3z0id.informer.database;

import com.m3z0id.informer.Informer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    String url;
    Connection connection;

    public Database(String url) {
        this.url = url;
        try {
            this.connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "ip TEXT UNIQUE, " +
                    "players TEXT)");
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Could not connect to database.");
        }
    }

    private @Nullable String getPlayersByIpString(@Nonnull String ip) {
        String query = "SELECT players FROM players WHERE ip = ?";
        StringBuilder result = new StringBuilder();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ip);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.append(resultSet.getString("players"));
            }
            return result.toString();
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to get players by ip. " + e.getMessage());
        }
        return null;
    }

    public @Nullable List<String> getPlayersByIp(@Nonnull String ip) {
        String players = getPlayersByIpString(ip);
        if (players == null) {
            return null;
        }
        String[] playersArray = players.split(";");
        return new ArrayList<>(Arrays.asList(playersArray));
    }

    public int addPlayer(@Nonnull String ip, @Nonnull String player) {
        List<String> playersWithIp = getPlayersByIp(ip);
        if(playersWithIp == null || playersWithIp.size() == 1 && playersWithIp.get(0).isEmpty()) {
            playersWithIp = new ArrayList<>();
        }
        if(!playersWithIp.contains(player)) {
            playersWithIp.add(player);
        }
        StringBuilder players = new StringBuilder();
        for (String playerWithIp : playersWithIp) {
            players.append(playerWithIp + ";");
        }
        removeIp(ip);
        String query = "INSERT INTO players (ip, players) VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ip);
            statement.setString(2, players.toString());
            return statement.executeUpdate();
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to add player to the database. " + e.getMessage());
            return -1;
        }
    }

    public boolean isConnected() {
        try {
            return !connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to close database." + e.getMessage());
        }
    }

    public int removeIp(@Nonnull String ip) {
        String query = "DELETE FROM players WHERE ip = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ip);
            return statement.executeUpdate();
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to remove ip from the database." + e.getMessage());
            return -1;
        }
    }

    public int removePlayer(@Nonnull String player, @Nonnull String ip) {
        List<String> players = getPlayersByIp(ip);
        if (players.contains(player)) {
            players.remove(player);
            StringBuilder newValue = new StringBuilder();
            for(String playerInList : players) {
                if(playerInList.equals(player)) continue;
                newValue.append(playerInList).append(";");
            }
            String query = "INSERT INTO players (ip, players) VALUES (?, ?)";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, ip);
                statement.setString(2, newValue.toString());
                return statement.executeUpdate();
            } catch (SQLException e) {
                Informer.instance.getLogger().severe("Unable to remove player from the database." + e.getMessage());
                return -1;
            }
        } else {
            return -1;
        }
    }

    public @Nullable List<String> getIpsByPlayer(@Nonnull String player) {
        String query = "SELECT ip FROM players WHERE players LIKE ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + player + ";%");

            ResultSet resultSet = statement.executeQuery();
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getString("ip"));
            }
            return result;
        } catch (SQLException e) {
            Informer.instance.getLogger().severe("Unable to get IPs by player from the database." + e.getMessage());
            return null;
        }
    }
}
