package de.leo.conwaysGameOfLife.other.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Version implements CommandExecutor {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        String textId = "Version";
        Component message = MM.deserialize("<gray>[<gold>" + textId + "<gray>] " + "<gray>This Plugin by KeineAhnung_Leo runs on version ConwaysGameOfLife-1.0.0. " + "Additionally, this Plugin is open source - visit the GitHub repository </gray>" + "<dark_green><u>here.</u></dark_green>").clickEvent(ClickEvent.openUrl("https://github.com/KeineAhnungLeo/Conway-s-Game-of-Life-Plugin")).hoverEvent(HoverEvent.showText(MM.deserialize("<gray>GitHub</gray>")));
        commandSender.sendMessage(message);
        return false;
    }
}
