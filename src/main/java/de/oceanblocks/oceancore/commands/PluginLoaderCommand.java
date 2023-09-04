package de.oceanblocks.oceancore.commands;

import com.google.common.base.Joiner;
import de.oceanblocks.oceancore.util.PluginUtil;
import de.oceanblocks.oceancore.util.StringUtil;
import de.oceanblocks.oceancore.util.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PluginLoaderCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        MiniMessage mm = MiniMessage.miniMessage();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(mm.deserialize("<!i>Dieser Command ist nicht für die Konsole bestimmt."));
            return true;
        }

        if (player.hasPermission("rang.admin")) {
            if (args.length <= 1) {
                player.sendMessage(Utils.Prefix + "§7Nutze: /coreplug [enable / disable / load / unload / info] [name / all]");
            }
            if (args.length == 2) {
                switch (args[0]) {
                    case "enable":
                        if (args[1].equalsIgnoreCase("all")) {
                            PluginUtil.enableAll();
                            player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><green>Aktiviere <gray>alle Plugins.")));
                        }
                        Plugin targetEnable = PluginUtil.getPluginByName(args, 1);
                        if (targetEnable == null) {
                            player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><red>Dieses Plugin ist nicht im Plugins Ordner.")));
                            return true;
                        }
                        if (targetEnable.isEnabled()) {
                            player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><red>Dieses Plugin ist bereits aktiviert.")));
                            return true;
                        }
                        PluginUtil.enable(targetEnable);
                        player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><gray>Aktiviere <green>" + targetEnable.getName())));
                    case "disable":
                        if (args[1].equalsIgnoreCase("all")) {
                            PluginUtil.disableAll();
                            player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><red>Deaktiviere <gray>alle Plugins.")));
                        }
                        Plugin targetDisable = PluginUtil.getPluginByName(args, 1);
                        if (targetDisable == null) {
                            player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><red>Dieses Plugin ist nicht im Plugins Ordner.")));
                            return true;
                        }
                        if (!targetDisable.isEnabled()) {
                            player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><red>Dieses Plugin ist bereits deaktiviert.")));
                            return true;
                        }
                        PluginUtil.disable(targetDisable);
                        player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><gray>Deaktiviere <green>" + targetDisable.getName())));
                    case "load":
                        Plugin targetLoad = PluginUtil.getPluginByName(args, 1);
                        if (targetLoad != null) {
                            player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><green>" + targetLoad.getName() + " <gray>ist bereits geladen.")));
                            return true;
                        }
                        String load = StringUtil.consolidateStrings(args, 1);
                        player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><gray>" + PluginUtil.load(load))));
                    case "unload":
                        Plugin targetUnload = PluginUtil.getPluginByName(args, 1);
                        if (targetUnload == null) {
                            player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><gray>Das Plugin konnte nicht gefunden werden.")));
                            return true;
                        }
                        player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><gray>" + PluginUtil.unload(targetUnload))));
                    case "info":
                        Plugin target = PluginUtil.getPluginByName(args, 1);
                        if (target == null) {
                            player.sendMessage(Utils.Prefix + "§7Dieses Plugin konnte nicht gefunden werden.");
                            return true;
                        }
                        Component name = mm.deserialize(target.getName());
                        Component version = mm.deserialize(target.getDescription().getVersion());
                        Component authors = mm.deserialize(Joiner.on(", ").join(target.getDescription().getAuthors()));
                        Component status = mm.deserialize(target.isEnabled() ? "<!i><green>Aktiviert" : "<!i><red>Deaktiviert");
                        List<String> dependList = target.getDescription().getDepend();
                        List<String> softDependList = target.getDescription().getSoftDepend();
                        player.sendMessage(Utils.Prefix.append(mm.deserialize("<!i><gray>Informationen zu <green>" + name + "<dark_gray>: \n +" +
                                "<gray>Version: <green>" + version + "\n" +
                                "<gray>Authors: <green>" + authors + "\n" +
                                "<gray>Status: " + status)));
                        if (!dependList.isEmpty()) player.sendMessage(Joiner.on(", ").join(dependList));
                        if (!softDependList.isEmpty()) player.sendMessage(Joiner.on(", ").join(softDependList));
                        break;
                }
            }
        }

        return false;
    }
}
