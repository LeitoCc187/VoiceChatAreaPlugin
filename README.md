# 🔊 VoiceChatArea

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.5-blue.svg)](https://www.minecraft.net/)
[![PaperMC](https://img.shields.io/badge/PaperMC-Supported-blue.svg)](https://papermc.io/)
[![Simple Voice Chat](https://img.shields.io/badge/Simple%20Voice%20Chat-2.5.0-green.svg)](https://modrinth.com/plugin/simple-voice-chat)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

**VoiceChatArea** is a lightweight Minecraft plugin that seamlessly integrates [Simple Voice Chat](https://modrinth.com/plugin/simple-voice-chat) with [WorldGuard](https://enginehub.org/worldguard), allowing you to create private voice chat zones within specific regions.

Perfect for RP servers, minigames, or any server wanting spatial voice separation!

---

## ✨ Features

- 🏰 **Region-Based Voice Groups** - Players automatically join private voice channels when entering WorldGuard regions
- 🔗 **Smart Region Linking** - Link multiple disconnected regions to share the same voice channel (great for complex buildings)
- 🧠 **Intelligent Priority** - When standing in overlapping regions, prefers linked/prioritized zones
- 🧹 **Auto-Cleanup** - Voice groups are automatically deleted when the last player leaves, keeping resources clean
- 📝 **Custom Group Names** - Rename voice channels for better organization (`/vca setgroup spawn Main Lobby`)
- 🎯 **Tab Completion** - Smart autocomplete for region names when typing commands
- 🔔 **Update Notifier** - Optional check for updates (notifies admins on join)
- ⚡ **Performance Optimized** - Minimal overhead, event-based movement checking with block-change optimization

---

## 📋 Requirements

- **Minecraft**: 1.21.5 (Paper/Spigot)
- **Java**: 17 or higher
- **[Simple Voice Chat](https://modrinth.com/plugin/simple-voice-chat)** (Bukkit version 2.5.0+)
- **[WorldGuard](https://enginehub.org/worldguard)** (7.0.9+)

---

## 🚀 Installation

1. Download the latest release from the [Releases](https://github.com/YOURUSERNAME/VoiceChatArea/releases) tab
2. Place the `.jar` file in your server's `plugins/` folder
3. **Ensure Simple Voice Chat and WorldGuard are installed**
4. Restart your server
5. The plugin will create a `links.yml` file in `plugins/VoiceChatArea/` on first run

---

## 🎮 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/vca info` | Check which voice group you're currently in | `voicechatarea.use` |
| `/vca link &lt;master&gt; &lt;region&gt;` | Link a region to another (shares voice channel) | `voicechatarea.admin` |
| `/vca setgroup &lt;region&gt; &lt;name&gt;` | Set a custom voice group name for a region | `voicechatarea.admin` |
| `/vca unlink &lt;region&gt;` | Remove custom link/group assignment | `voicechatarea.admin` |
| `/vca list` | View all current region links | `voicechatarea.admin` |
| `/vca reload` | Reload configuration files | `voicechatarea.admin` |

**Aliases**: `/voicechatarea`, `/vca`

---

## 🎯 Usage Example

### Basic Setup
