package elixius.proxychat.voiceChatArea;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import elixius.proxychat.voiceChatArea.commands.VoiceChatCommand;
import elixius.proxychat.voiceChatArea.link.RegionLinkManager;
import elixius.proxychat.voiceChatArea.listeners.PlayerQuitListener;
import elixius.proxychat.voiceChatArea.listeners.RegionMoveListener;
import elixius.proxychat.voiceChatArea.updates.UpdateChecker;
import elixius.proxychat.voiceChatArea.voice.VoiceGroupManager;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Callable;

public class VoiceChatArea extends JavaPlugin implements VoicechatPlugin {

    private VoiceGroupManager manager;
    private VoicechatServerApi api;
    private RegionLinkManager linkManager;

    // Configuration constants
    private static final int BSTATS_PLUGIN_ID = 21842; // CHANGE THIS! Get from bstats.org
    private static final String UPDATE_CHECK_URL = "https://api.github.com/repos/YOURUSERNAME/VoiceChatArea/releases/latest"; // CHANGE THIS!
    private static final String SPIGOT_RESOURCE_ID = "123456"; // CHANGE THIS if using Spigot

    private boolean updateAvailable = false;
    private String latestVersion = "";

    @Override
    public String getPluginId() {
        return "voicechat_area";
    }

    @Override
    public void onEnable() {
        // Initialize link manager (creates links.yml if needed)
        this.linkManager = new RegionLinkManager(this);

        // Initialize bStats Metrics
        initMetrics();

        BukkitVoicechatService service = getServer().getServicesManager()
                .load(BukkitVoicechatService.class);

        if (service == null) {
            getLogger().severe("Simple Voice Chat is not installed!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        service.registerPlugin(this);

        // Register commands
        VoiceChatCommand cmd = new VoiceChatCommand(this, linkManager);
        getCommand("voicechatarea").setExecutor(cmd);
        getCommand("voicechatarea").setTabCompleter(cmd);

        // Register listeners
        getServer().getPluginManager().registerEvents(
                new RegionMoveListener(this), this);
        getServer().getPluginManager().registerEvents(
                new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(
                new UpdateNotifyListener(), this);

        // Check for updates asynchronously
        checkForUpdates();

        getLogger().info("VoiceChatArea v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        if (manager != null) {
            manager.cleanup();
        }
    }

    @Override
    public void initialize(VoicechatApi api) {
        if (!(api instanceof VoicechatServerApi)) {
            getLogger().severe("Invalid API type received!");
            return;
        }
        this.api = (VoicechatServerApi) api;
        this.manager = new VoiceGroupManager(this.api, linkManager);
        getLogger().info("VoiceChatArea API initialized!");
    }

    public void deinitialize(VoicechatApi api) {
        if (manager != null) {
            manager.cleanup();
        }
        this.api = null;
        this.manager = null;
    }

    private void initMetrics() {
        Metrics metrics = new Metrics(this, BSTATS_PLUGIN_ID);

        // Add custom chart for linked regions count
        metrics.addCustomChart(new SimplePie("linked_regions", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return String.valueOf(linkManager.getAllLinks().size());
            }
        }));

        // Add chart for whether update is available
        metrics.addCustomChart(new SimplePie("update_available", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return updateAvailable ? "Yes" : "No";
            }
        }));

        getLogger().info("bStats metrics enabled!");
    }

    private void checkForUpdates() {
        // Run async to not block server startup
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                UpdateChecker checker = new UpdateChecker(this, UPDATE_CHECK_URL);
                if (checker.check()) {
                    latestVersion = checker.getLatestVersion();
                    String current = getDescription().getVersion();

                    if (!current.equalsIgnoreCase(latestVersion)) {
                        updateAvailable = true;
                        getLogger().warning("===============================================");
                        getLogger().warning("A new version of VoiceChatArea is available!");
                        getLogger().warning("Current: " + current);
                        getLogger().warning("Latest: " + latestVersion);
                        getLogger().warning("Download: https://github.com/LeitoCc187/VoiceChatAreaPlugin/releases");
                        getLogger().warning("===============================================");
                    } else {
                        getLogger().info("You are running the latest version!");
                    }
                }
            } catch (Exception e) {
                getLogger().warning("Could not check for updates: " + e.getMessage());
            }
        });
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public VoicechatServerApi getApi() {
        return api;
    }

    public VoiceGroupManager getManager() {
        return manager;
    }

    public RegionLinkManager getLinkManager() {
        return linkManager;
    }

    // Listener to notify admins of updates when they join
    private class UpdateNotifyListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            if (updateAvailable && player.hasPermission("voicechatarea.admin")) {
                getServer().getScheduler().runTaskLater(VoiceChatArea.this, () -> {
                    player.sendMessage("§6[VoiceChatArea] §eAn update is available! §7(" +
                            getDescription().getVersion() + " → " + latestVersion + ")");
                    player.sendMessage("§6[VoiceChatArea] §eDownload: §fhttps://github.com/YOURUSERNAME/VoiceChatArea/releases");
                }, 40L); // 2 second delay after join
            }
        }
    }
}