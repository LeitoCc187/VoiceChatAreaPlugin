package elixius.proxychat.voiceChatArea.listeners;

import elixius.proxychat.voiceChatArea.VoiceChatArea;
import elixius.proxychat.voiceChatArea.voice.VoiceGroupManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final VoiceChatArea plugin;

    public PlayerQuitListener(VoiceChatArea plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        VoiceGroupManager manager = plugin.getManager();
        if (manager != null) {
            manager.removePlayer(event.getPlayer());
        }
    }
}