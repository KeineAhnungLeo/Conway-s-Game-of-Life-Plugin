package de.leo.conwaysGameOfLife.other.commands;

import de.leo.conwaysGameOfLife.ConwaysGameOfLife;
import de.leo.conwaysGameOfLife.canvas.utils.Canvas;
import de.leo.conwaysGameOfLife.simulation.utils.Simulation;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Reset implements CommandExecutor, TabCompleter {
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final String textId = "Reset";

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>You have no permission to execute this command"));
            return false;
        }
        if (strings.length == 0) {
            sendUsage(commandSender);
            return false;
        }
        if (strings.length >= 2 && strings[1].equalsIgnoreCase("confirm")) {
            de.leo.conwaysGameOfLife.settings.utils.Settings settings = ConwaysGameOfLife.getInstance().getSettings();
            switch (strings[0].toLowerCase()) {
                case "canvas": {
                    settings.setCanvasMat(Material.WHITE_CONCRETE);
                    settings.setCellMat(Material.BLACK_CONCRETE);
                    settings.setCanvasSize(0);
                    if (settings.getPlaced()) {
                        Canvas.deleteCanvas();
                        settings.setPlaced(false);
                    }
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully reset the Canvas"));
                    return true;
                }
                case "simulation": {
                    settings.setDebugChat(false);
                    settings.setDebugActionBar(false);
                    settings.setGenerationTime(20);
                    settings.setRuleType(true);
                    if (Simulation.task != null)
                        Simulation.stopScheduler = true;
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully reset the Simulation"));
                    return true;
                }
                case "all": {
                    if (Simulation.task != null)
                        Simulation.stopScheduler = true;
                    if (settings.getPlaced()) {
                        Canvas.deleteCanvas();
                        settings.setPlaced(false);
                    }
                    settings.setCanvasMat(Material.WHITE_CONCRETE);
                    settings.setCellMat(Material.BLACK_CONCRETE);
                    settings.setCanvasSize(0);
                    settings.setDebugChat(false);
                    settings.setDebugActionBar(false);
                    settings.setRuleType(true);
                    settings.setGenerationTime(20);
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully reset the Everything"));
                    return true;
                }

            }
        }
        sendUsage(commandSender);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        List<String> completions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("reset")) {
            if (strings.length == 1) {
                List<String> subcommands = List.of("canvas", "simulation", "all");
                for (String string : subcommands) {
                    if (string.toLowerCase().startsWith(strings[0].toLowerCase())) {
                        completions.add(string);
                    }
                }
            }

            else if(strings.length==2&&(strings[0].equalsIgnoreCase("canvas") || strings[0].equalsIgnoreCase("simulation") || strings[0].equalsIgnoreCase("all"))){
                List<String> subcommands = List.of("confirm");
                for(String string : subcommands){
                    if(string.toLowerCase().startsWith(strings[1].toLowerCase())){
                        completions.add(string);
                    }
                }
            }
        }
        return completions;
    }

    private void sendUsage(CommandSender sender){
        sender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Usage: <gold>/reset <canvas | simulation | all> confirm"));
    }
}
