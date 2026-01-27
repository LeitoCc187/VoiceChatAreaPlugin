package elixius.proxychat.voiceChatArea.commands;

import elixius.proxychat.voiceChatArea.VoiceChatArea;
import elixius.proxychat.voiceChatArea.link.RegionLinkManager;
import elixius.proxychat.voiceChatArea.voice.VoiceGroupManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VoiceChatCommand implements CommandExecutor, TabCompleter {

    private final VoiceChatArea plugin;
    private final RegionLinkManager linkManager;

    public VoiceChatCommand(VoiceChatArea plugin, RegionLinkManager linkManager) {
        this.plugin = plugin;
        this.linkManager = linkManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "link":
                if (!sender.hasPermission("voicechatarea.admin")) {
                    sender.sendMessage("§cNo permission");
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage("§cUsage: /vca link <region1> <region2>");
                    return true;
                }
                linkManager.linkRegions(args[1], args[2]);
                sender.sendMessage("§aLinked region '" + args[2] + "' to '" + args[1] + "'");
                return true;

            case "setgroup":
                if (!sender.hasPermission("voicechatarea.admin")) {
                    sender.sendMessage("§cNo permission");
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage("§cUsage: /vca setgroup <region> <groupname>");
                    return true;
                }
                linkManager.setGroup(args[1], args[2]);
                sender.sendMessage("§aSet region '" + args[1] + "' to use voice group '" + args[2] + "'");
                return true;

            case "unlink":
                if (!sender.hasPermission("voicechatarea.admin")) {
                    sender.sendMessage("§cNo permission");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /vca unlink <region>");
                    return true;
                }
                linkManager.removeLink(args[1]);
                sender.sendMessage("§aRemoved custom link for region '" + args[1] + "'");
                return true;

            case "info":
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§cPlayer only command");
                    return true;
                }
                VoiceGroupManager mgr = plugin.getManager();
                if (mgr == null) {
                    player.sendMessage("§cVoice chat API not ready yet");
                    return true;
                }
                String currentGroup = mgr.getPlayerGroup(player);
                if (currentGroup == null) {
                    player.sendMessage("§7You are not in a voice area");
                } else {
                    player.sendMessage("§aCurrent voice group: " + currentGroup);
                }
                return true;

            case "list":
                if (!sender.hasPermission("voicechatarea.admin")) {
                    sender.sendMessage("§cNo permission");
                    return true;
                }
                sender.sendMessage("§6Region Links:");
                var links = linkManager.getAllLinks();
                if (links.isEmpty()) {
                    sender.sendMessage("§7No custom links set");
                } else {
                    links.forEach((region, group) ->
                            sender.sendMessage("§7- " + region + " → " + group)
                    );
                }
                return true;

            case "reload":
                if (!sender.hasPermission("voicechatarea.admin")) {
                    sender.sendMessage("§cNo permission");
                    return true;
                }
                linkManager.loadLinks();
                sender.sendMessage("§aReloaded region links");
                return true;

            default:
                sendHelp(sender);
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6=== VoiceChatArea Commands ===");
        sender.sendMessage("§a/vca info §f- Check your current voice group");
        if (sender.hasPermission("voicechatarea.admin")) {
            sender.sendMessage("§a/vca link <region> <master> §f- Link region to master");
            sender.sendMessage("§a/vca setgroup <region> <group> §f- Set custom group name");
            sender.sendMessage("§a/vca unlink <region> §f- Remove custom link");
            sender.sendMessage("§a/vca list §f- List all links");
            sender.sendMessage("§a/vca reload §f- Reload config");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("info");
            if (sender.hasPermission("voicechatarea.admin")) {
                completions.add("link");
                completions.add("setgroup");
                completions.add("unlink");
                completions.add("list");
                completions.add("reload");
            }
        }

        return completions;
    }
}