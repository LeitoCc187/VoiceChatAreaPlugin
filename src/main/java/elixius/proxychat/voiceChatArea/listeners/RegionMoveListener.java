package elixius.proxychat.voiceChatArea.listeners;

import elixius.proxychat.voiceChatArea.VoiceChatArea;
import elixius.proxychat.voiceChatArea.voice.VoiceGroupManager;
import elixius.proxychat.voiceChatArea.wg.WorldGuardUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

public class RegionMoveListener implements Listener {

    private final VoiceChatArea plugin;

    public RegionMoveListener(VoiceChatArea plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Ignore head movement (only check block changes)
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        VoiceGroupManager manager = plugin.getManager();
        if (manager == null) return; // Voice chat API not ready yet

        Player player = event.getPlayer();
        Set<String> regions = WorldGuardUtil.getRegionsAt(event.getTo());

        if (regions.isEmpty()) {
            // Player left all regions - leave voice chat area
            manager.leaveRegion(player);
        } else {
            // Prefer regions with custom links over unlinked ones
            String targetRegion = regions.stream()
                    .filter(r -> plugin.getLinkManager().hasLink(r))
                    .findFirst()
                    .orElse(regions.iterator().next());

            manager.enterRegion(player, targetRegion);
        }
    }
}