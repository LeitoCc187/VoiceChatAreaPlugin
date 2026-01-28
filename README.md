# 🔊 VoiceChatArea

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.5-blue.svg)](https://www.minecraft.net/)
[![PaperMC](https://img.shields.io/badge/PaperMC-Supported-blue.svg)](https://papermc.io/)
[![Simple Voice Chat](https://img.shields.io/badge/Simple%20Voice%20Chat-2.5.0-green.svg)](https://modrinth.com/plugin/simple-voice-chat)

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

1. Download the latest release from the [Releases](../../releases) tab
2. Place the `.jar` file in your server's `plugins/` folder
3. **Ensure Simple Voice Chat and WorldGuard are installed**
4. Restart your server
5. The plugin will create a `links.yml` file in `plugins/VoiceChatArea/` on first run

---

## 🎮 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/vca info` | Check which voice group you're currently in | `voicechatarea.use` |
| `/vca link <master> <region>` | Link a region to another (shares voice channel) | `voicechatarea.admin` |
| `/vca setgroup <region> <name>` | Set a custom voice group name for a region | `voicechatarea.admin` |
| `/vca unlink <region>` | Remove custom link/group assignment | `voicechatarea.admin` |
| `/vca list` | View all current region links | `voicechatarea.admin` |
| `/vca reload` | Reload configuration files | `voicechatarea.admin` |

**Aliases**: `/voicechatarea`, `/vca`

---

## 🎯 Usage Example

### Basic Setup
Create some WorldGuard regions first
/rg define spawn
/rg define shop1
/rg define shop2
/rg define secrethq
Copy

### Scenario 1: Shopping District
You have two shops that should share the same voice channel:
/vca link spawn shop1
/vca link spawn shop2
Copy
Now `shop1` and `shop2` both use the "spawn" voice group. Players in either shop can talk to each other, but not to players outside.

### Scenario 2: Custom Naming
Make your secret HQ sound cooler:
/vca setgroup secrethq Black_Ops_Channel
Copy
Players now see they've entered "Voice Area: Black_Ops_Channel"

### Scenario 3: Overlapping Regions
If a player stands in both `building` and `building_interior` (where `building_interior` is linked to `Main_Hall`), they'll join `Main_Hall` automatically due to priority linking.

---

## 📁 Configuration

The plugin stores data in `plugins/VoiceChatArea/links.yml`:

```yaml
links:
  shop1: spawn
  shop2: spawn
  secrethq: Black_Ops_Channel
```
**Note:** All configuration is managed via in-game commands. Edit links.yml manually only if you know what you're doing, then use /vca reload.

---

## 🔧 Technical Details

- **Storage:** File-based (YAML) - no database required
- **Performance: Event-driven with minimal tick overhead
- **Compatibility:** Works with existing WorldGuard regions; no region flags required
- **Cleanup:** Automatic group deletion prevents memory leaks

---

## 🐛 Troubleshooting

"Simple Voice Chat is not installed!"
Ensure you have the Bukkit/Spigot version of Simple Voice Chat (not the Fabric/Forge mod)
"Voice chat API not ready yet"
This appears if you run /vca info before the voice chat server fully initializes. Wait a few seconds after joining.
Regions not detecting?
Ensure the player actually enters the region block (move at least 1 block into the region, not just the edge)

---

## 🤝 Contributing
Contributions are welcome! Please feel free to submit a Pull Request.
Fork the repository
Create your feature branch (``git checkout -b feature/AmazingFeature``)
Commit your changes (``git commit -m 'Add some AmazingFeature'``)
Push to the branch (``git push origin feature/AmazingFeature``)
Open a Pull Request

---

## 🙏 Credits
[Simple Voice Chat](https://modrinth.com/plugin/simple-voice-chat) by Henkelmax for the amazing voice API
[WorldGuard](https://enginehub.org/worldguard) by EngineHub for region management
PaperMC team for the server software
Made with ❤️ for the Minecraft community
