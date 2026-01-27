package elixius.proxychat.voiceChatArea.link;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegionLinkManager {

    private final JavaPlugin plugin;
    private final File configFile;
    private FileConfiguration config;

    // region ID -> voice group ID (if null/empty, uses region ID)
    private Map<String, String> regionLinks = new HashMap<>();

    public RegionLinkManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "links.yml");
        loadLinks();
    }

    public void loadLinks() {
        // Create the plugin folder and file if they don't exist
        if (!configFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs(); // Create plugins/VoiceChatArea/ folder
                configFile.createNewFile(); // Create empty links.yml
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create links.yml: " + e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        if (config.contains("links")) {
            for (String key : config.getConfigurationSection("links").getKeys(false)) {
                regionLinks.put(key, config.getString("links." + key));
            }
        }
        plugin.getLogger().info("Loaded " + regionLinks.size() + " region links");
    }

    public void saveLinks() {
        config.set("links", null); // Clear old

        for (Map.Entry<String, String> entry : regionLinks.entrySet()) {
            config.set("links." + entry.getKey(), entry.getValue());
        }

        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save links.yml: " + e.getMessage());
        }
    }

    /**
     * Link a region to a specific voice group
     * @param regionId The WorldGuard region ID
     * @param groupId The voice group ID (null to remove/unlink)
     */
    public void setGroup(String regionId, String groupId) {
        if (groupId == null || groupId.isEmpty()) {
            regionLinks.remove(regionId);
        } else {
            regionLinks.put(regionId, groupId);
        }
        saveLinks();
    }

    /**
     * Get the voice group ID for a region
     * @param regionId WorldGuard region ID
     * @return The linked group ID, or the region ID itself if no link exists
     */
    public String getGroupForRegion(String regionId) {
        return regionLinks.getOrDefault(regionId, regionId);
    }

    /**
     * Check if a region has a custom link
     */
    public boolean hasLink(String regionId) {
        return regionLinks.containsKey(regionId);
    }

    /**
     * Remove a link (region uses its own name as group)
     */
    public void removeLink(String regionId) {
        regionLinks.remove(regionId);
        saveLinks();
    }

    /**
     * Link two regions together (both will use region1's group)
     */
    public void linkRegions(String region1, String region2) {
        String groupId = getGroupForRegion(region1);
        setGroup(region2, groupId);
    }

    public Map<String, String> getAllLinks() {
        return new HashMap<>(regionLinks);
    }
}