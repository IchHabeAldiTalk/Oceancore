package de.oceanblocks.oceancore.perms;

import de.oceanblocks.oceancore.Oceancore;
import de.oceanblocks.oceancore.util.DateUtil;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.platform.PlayerAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class LuckPermsAPIHook {

    public boolean isPlayerInGroup(final Player player, final String group) {
        return player.hasPermission("group." + group);
    }

    public String getGroupOfPlayer(Player player) {
        PlayerAdapter<Player> playerAdapter = Oceancore.getInstance().getLuckPerms().getPlayerAdapter(Player.class);
        User user = playerAdapter.getUser(player);
        Collection<Group> groups = user.getInheritedGroups(playerAdapter.getQueryOptions(player));
        String finalGroup = groups.stream().map(Group::getName).collect(Collectors.joining(";"));
        String[] groupData = finalGroup.split(";");
        return groupData[0].substring(0, 1).toUpperCase() + groupData[0].substring(1);
    }

    public Component getPlayerColor(Player player) {
        String color;
        color = "<gray>";
        if (isPlayerInGroup(player, "spieler")) color = "<#42AAFA>";
        if (isPlayerInGroup(player, "group.premium")) color = "<#edff24>";
        if (isPlayerInGroup(player, "group.premiumplus")) color = "<#ffe224>";
        if (isPlayerInGroup(player, "group.superior")) color = "<#FA424E>";
        if (isPlayerInGroup(player, "group.youtuber")) color = "<#8523c2>";
        if (isPlayerInGroup(player, "group.content")) color = "<#7CF311>";
        if (isPlayerInGroup(player, "group.developer")) color = "<#4fe4e8>";
        if (isPlayerInGroup(player, "group.moderator")) color = "<#ff2626>";
        if (isPlayerInGroup(player, "group.admin")) color = "<#cc2323>";
        return Component.text(color);
    }

    public String getPlayerColorAsString(Player player) {
        String color;
        color = "§7";
        if (isPlayerInGroup(player, "group.spieler")) color = ChatColor.of(new Color(66, 170, 250)).toString() + ChatColor.BOLD;
        if (isPlayerInGroup(player, "group.premium")) color = ChatColor.of(new Color(244, 244, 0)).toString() + ChatColor.BOLD;
        if (isPlayerInGroup(player, "group.premiumplus")) color = ChatColor.of(new Color(214, 169, 0)).toString() + ChatColor.BOLD;
        if (isPlayerInGroup(player, "group.superior")) color = ChatColor.of(new Color(250, 66, 78)).toString() + ChatColor.BOLD;
        if (isPlayerInGroup(player, "group.youtuber")) color = ChatColor.of(new Color(133, 35, 194)).toString() + ChatColor.BOLD;
        if (isPlayerInGroup(player, "group.content")) color = ChatColor.of(new Color(124, 243, 17)).toString() + ChatColor.BOLD;
        if (isPlayerInGroup(player, "group.developer")) color = ChatColor.of(new Color(79, 228, 232)).toString() + ChatColor.BOLD;
        if (isPlayerInGroup(player, "group.moderator")) color = ChatColor.of(new Color(255, 48, 48)).toString() + ChatColor.BOLD;
        if (isPlayerInGroup(player, "group.admin")) color = ChatColor.of(new Color(204, 35, 35)).toString() + ChatColor.BOLD;
        return color;
    }

    public void addTempGroup(final UUID uuid, final String groupName, final String expireDate) {
        Group group = Oceancore.getInstance().getLuckPerms().getGroupManager().getGroup(groupName);
        if (group == null) {
            System.out.println("Could not find group " + groupName);
            Bukkit.getOnlinePlayers().forEach(all -> {
                if (all.getUniqueId().equals(uuid)) {
                    all.sendMessage("§7We were unable to locate the group §3" + groupName + " §7in our database!");
                }
            });
            return;
        }
        Oceancore.getInstance().getLuckPerms().getUserManager().modifyUser(uuid, (User user) -> {
            user.data().clear(NodeType.INHERITANCE::matches);
            Node node = null;
            node = InheritanceNode.builder(group).expiry(DateUtil.parseDateDiff(expireDate, true)).build();
            user.data().add(node);
        });
        Bukkit.getOnlinePlayers().forEach(all -> {
            if (all.getUniqueId().equals(uuid)) {
                all.sendMessage("§7You got the rank " + getPlayerColorAsString(all) + groupName + " §7for §3§l" + expireDate + " §7days!");
            }
        });
    }

    public void addGroup(final UUID uuid, final String groupName) {
        Group group = Oceancore.getInstance().getLuckPerms().getGroupManager().getGroup(groupName);
        if (group == null) {
            System.out.println("Could not find group " + groupName);
            Bukkit.getOnlinePlayers().forEach(all -> {
                if (all.getUniqueId().equals(uuid)) {
                    all.sendMessage("§7We were unable to locate the group §3" + groupName + " §7in our database!");
                }
            });
            return;
        }
        Oceancore.getInstance().getLuckPerms().getUserManager().modifyUser(uuid, (User user) -> {
            user.data().clear(NodeType.INHERITANCE::matches);
            Node node = null;
            node = InheritanceNode.builder(group).build();
            user.data().add(node);
        });
        Bukkit.getOnlinePlayers().forEach(all -> {
            if (all.getUniqueId().equals(uuid)) {
                all.sendMessage("§7You got the rank " + getPlayerColorAsString(all) + groupName + " §7for §3§llifetime§7!");
            }
        });
    }

    public void removeGroup(final UUID uuid, final String groupName) {
        Group group = Oceancore.getInstance().getLuckPerms().getGroupManager().getGroup(groupName);
        if (group == null) {
            System.out.println("Could not find group " + groupName);
            Bukkit.getOnlinePlayers().forEach(all -> {
                if (all.getUniqueId().equals(uuid)) {
                    all.sendMessage("§7We were unable to locate the group §3" + groupName + " §7in our database!");
                }
            });
            return;
        }
        Oceancore.getInstance().getLuckPerms().getUserManager().modifyUser(uuid, (User user) -> {
            user.data().clear(NodeType.INHERITANCE::matches);
            Node node = null;
            node = InheritanceNode.builder(group).build();
            user.data().remove(node);

        });
    }
}
