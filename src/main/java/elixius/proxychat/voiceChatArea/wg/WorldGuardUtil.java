package elixius.proxychat.voiceChatArea.wg;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Location;


import java.util.Set;
import java.util.stream.Collectors;


public class WorldGuardUtil {


    public static Set<String> getRegionsAt(Location location) {
        RegionManager manager = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(BukkitAdapter.adapt(location.getWorld()));


        if (manager == null) return Set.of();


        ApplicableRegionSet regions = manager.getApplicableRegions(
                BukkitAdapter.asBlockVector(location)
        );


        return regions.getRegions()
                .stream()
                .map(r -> r.getId())
                .collect(Collectors.toSet());
    }
}
