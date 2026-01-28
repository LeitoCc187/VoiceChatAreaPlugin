package elixius.proxychat.voiceChatArea.voice;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import elixius.proxychat.voiceChatArea.link.RegionLinkManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VoiceGroupManager {

    private final VoicechatServerApi voiceApi;
    private final RegionLinkManager linkManager;

    // voice group ID -> voice group
    private final Map<String, Group> regionGroups = new HashMap<>();

    // player UUID -> region ID (the actual WorldGuard region they're in)
    private final Map<UUID, String> playerRegions = new HashMap<>();

    public VoiceGroupManager(VoicechatServerApi voiceApi, RegionLinkManager linkManager) {
        this.voiceApi = voiceApi;
        this.linkManager = linkManager;
    }

    public void enterRegion(Player player, String regionId) {
        VoicechatConnection connection = voiceApi.getConnectionOf(player.getUniqueId());
        if (connection == null) return;

        // Already in this region
        if (regionId.equals(playerRegions.get(player.getUniqueId()))) return;

        leaveRegion(player);

        String groupId = linkManager.getGroupForRegion(regionId);

        // Get or create the group for this linked ID
        Group group = regionGroups.computeIfAbsent(groupId,
                id -> voiceApi.createGroup(id, "Voice Area: " + id));

        connection.setGroup(group);
        playerRegions.put(player.getUniqueId(), regionId);
    }

    public void leaveRegion(Player player) {
        UUID uuid = player.getUniqueId();
        VoicechatConnection connection = voiceApi.getConnectionOf(uuid);
        if (connection == null) return;

        String oldRegion = playerRegions.remove(uuid);
        if (oldRegion == null) return;

        connection.setGroup(null);

        // Check if the linked group is still in use by anyone else
        String oldGroupId = linkManager.getGroupForRegion(oldRegion);
        boolean stillUsed = playerRegions.values().stream()
                .anyMatch(region -> linkManager.getGroupForRegion(region).equals(oldGroupId));

        if (!stillUsed) {
            Group group = regionGroups.get(oldGroupId);
            if (group != null) {
                voiceApi.removeGroup(group.getId());
                regionGroups.remove(oldGroupId);
            }
        }
    }

    public void removePlayer(Player player) {
        leaveRegion(player);
    }

    public void cleanup() {
        for (Group group : regionGroups.values()) {
            voiceApi.removeGroup(group.getId());
        }
        regionGroups.clear();
        playerRegions.clear();
    }

    public String getPlayerGroup(Player player) {
        String region = playerRegions.get(player.getUniqueId());
        if (region == null) return null;
        return linkManager.getGroupForRegion(region);
    }
}