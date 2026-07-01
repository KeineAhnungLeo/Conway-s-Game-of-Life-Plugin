package de.leo.conwaysGameOfLife.simulation.commands;

import de.leo.conwaysGameOfLife.ConwaysGameOfLife;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static de.leo.conwaysGameOfLife.simulation.utils.Simulation.resetDebugInfo;
import static de.leo.conwaysGameOfLife.simulation.utils.Simulation.showDebugInfoInActionbar;

public class Simulation implements CommandExecutor, TabCompleter {
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final String textId = "Simulation";

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

        de.leo.conwaysGameOfLife.settings.utils.Settings settings = ConwaysGameOfLife.getInstance().getSettings();
        switch (strings[0].toLowerCase()) {
            case "step": {
                if (settings.getPlaced()) {
                    de.leo.conwaysGameOfLife.simulation.utils.Simulation.simulateStep();
                    return true;
                } else {
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>No Canvas is placed yet"));
                }
            }
            case "start": {
                if (settings.getPlaced()) {
                    de.leo.conwaysGameOfLife.simulation.utils.Simulation.startSimulationScheduler();
                    de.leo.conwaysGameOfLife.simulation.utils.Simulation.generationCount = 0;
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Started Simulation for Canvas"));
                    return true;
                } else {
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>No Canvas is placed yet"));
                }
            }
            case "stop": {
                if (settings.getPlaced()) {
                    de.leo.conwaysGameOfLife.simulation.utils.Simulation.stopScheduler = true;
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Stopped Simulation for Canvas"));
                    return true;
                }
            }
            case "speed": {
                if (strings.length == 1) {
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Current Speed of the Simulation: " + settings.getGenerationTime() + " Ticks"));
                    return true;
                }

                int speed;
                try {
                    speed = Integer.parseInt(strings[1]);
                    if (speed <= 0) {
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>Invalid number"));
                        return false;
                    }

                    if (speed > 1200) {
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>Invalid number"));
                        return false;
                    }

                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully changed the Speed of the Simulation to " + speed + " Ticks"));

                    settings.setGenerationTime(speed);
                    return true;
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>Invalid number"));
                    return false;
                }
            }
            case "ruletype": {
                if (strings.length == 1) {
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Current Ruletype of the Simulation: " + ((settings.getRuleType()) ? "Standard" : "Copyworld")));
                    return true;
                }

                if (strings.length == 2) {
                    if (strings[1].equalsIgnoreCase("standard")) {
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully changed the Ruletype of the Simulation to Standard"));
                        settings.setRuleType(true);
                    } else if (strings[1].equalsIgnoreCase("copyworld")) {
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully changed the Ruletype of the Simulation to Copyworld"));
                        settings.setRuleType(false);
                    }
                    return true;
                }
            }
            case "debug": {
                if (strings.length == 1) {
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Actionbar: " + ((settings.getDebugActionBar()) ? "<green>On" : "<red>Off")) + " <yellow>Chat: " + ((settings.getDebugChat()) ? "<green>On" : "<red>Off"));
                    return true;
                }

                if (strings.length == 2) {
                    if (strings[1].equalsIgnoreCase("actionbar")) {
                        settings.setDebugActionBar(!settings.getDebugActionBar());
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Switched Debug-Info for Actionbar to " + ((settings.getDebugActionBar()) ? "<green>On" : "<red>Off")));
                        if(settings.getDebugActionBar())
                            showDebugInfoInActionbar();
                    } else if (strings[1].equalsIgnoreCase("chat")) {
                        settings.setDebugChat(!settings.getDebugChat());
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Switched Debug-Info for Chat to " + ((settings.getDebugChat()) ? "<green>On" : "<red>Off")));
                    } else if (strings[1].equalsIgnoreCase("reset")) {
                        resetDebugInfo();
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully reset Debug-Info"));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        List<String> completions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("simulation")) {
            if (strings.length == 1) {
                List<String> subcommands = List.of("start", "stop", "step", "speed", "debug", "ruletype");
                for (String string : subcommands) {
                    if (string.toLowerCase().startsWith(strings[0].toLowerCase())) {
                        completions.add(string);
                    }
                }
            }
            else if(strings.length==2&&strings[0].equalsIgnoreCase("ruletype")){
                List<String> subcommands = List.of("standard", "copyworld");
                for(String string : subcommands){
                    if(string.toLowerCase().startsWith(strings[1].toLowerCase())){
                        completions.add(string);
                    }
                }
            }
            else if(strings.length==2&&strings[0].equalsIgnoreCase("debug")){
                List<String> subcommands = List.of("actionbar", "chat", "reset");
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
        sender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Usage: <gold>/simulation <start | stop | step>, /simulation speed <integer>, /simulation debug <chat | actionbar>, /simulation ruletype <standard | copyworld>"));
    }
}
