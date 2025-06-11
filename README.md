# InformerPlugin

InformerPlugin is a Minecraft server plugin for Paper that provides information about players, such as their alternate accounts (alts), IP addresses, and client brands. It is designed to help server administrators monitor player activity and manage their server more effectively.

## Features

- View the client brand a player is using.
- List all alternate accounts (alts) associated with a player.
- List all IP addresses used by a player.
- Remove player or IP data from the database.
- Import user data from EssentialsX.
- Configurable messages and permissions.

## Commands

| Command                | Description                                 | Permission           |
|------------------------|---------------------------------------------|----------------------|
| `/client <player>`     | Shows the client brand of a player          | `informer.client`    |
| `/alts [player]`       | Lists all alts for a player                 | `informer.alts`      |
| `/ips [player]`        | Lists all IPs used by a player              | `informer.ips`       |
| `/informer reload`     | Reloads the plugin configuration            | `informer.info`      |
| `/informer importEssX` | Imports data from EssentialsX (console only)| `informer.info`      |
| `/dataremove ip <ip>`  | Removes all data for an IP (console only)   | `informer.remove`    |
| `/dataremove player <player>` | Removes all data for a player (console only) | `informer.remove` |

## Permissions

- `informer.*` (default: OP) — Grants all permissions.

## Installation

1. Download the plugin JAR and place it in your server's `plugins` folder.
2. Start the server to generate the configuration files.
3. Edit `config.json` and `lang.json` in the plugin's data folder to customize settings and messages.
4. (Optional) Use `/informer importEssX` to import data from EssentialsX.

## Configuration

- `config.json`: Set the notification permission node.
- `lang.json`: Customize all plugin messages.

## Requirements

- PaperMC 1.18.2 or newer
- Java 17 or newer

## License

MIT

---

**Author:** [M3z0id](https://github.com/m3z0id)