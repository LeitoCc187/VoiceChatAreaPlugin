package elixius.proxychat.voiceChatArea;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import elixius.proxychat.voiceChatArea.commands.VoiceChatCommand;
import elixius.proxychat.voiceChatArea.link.RegionLinkManager;
import elixius.proxychat.voiceChatArea.listeners.PlayerQuitListener;
import elixius.proxychat.voiceChatArea.listeners.RegionMoveListener;
import elixius.proxychat.voiceChatArea.voice.VoiceGroupManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VoiceChatArea extends JavaPlugin implements VoicechatPlugin {

    private VoiceGroupManager manager;
    private VoicechatServerApi api;
    private RegionLinkManager linkManager;

    @Override
    public String getPluginId() {
        return "voicechat_area";
    }

    @Override
    public void onEnable() {
        // Initialize link manager (creates links.yml if needed)
        this.linkManager = new RegionLinkManager(this);

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

        getLogger().info("VoiceChatArea registered with Simple Voice Chat!");
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

    public VoicechatServerApi getApi() {
        return api;
    }

    public VoiceGroupManager getManager() {
        return manager;
    }

    public RegionLinkManager getLinkManager() {
        return linkManager;
    }
}