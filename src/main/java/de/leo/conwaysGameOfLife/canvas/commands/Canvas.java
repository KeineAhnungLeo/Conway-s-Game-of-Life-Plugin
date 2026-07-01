package de.leo.conwaysGameOfLife.canvas.commands;

import de.leo.conwaysGameOfLife.ConwaysGameOfLife;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Canvas implements CommandExecutor, TabCompleter {
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final String textId = "Canvas";

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
            case "material": {
                if(strings.length == 1){
                    sendUsage(commandSender);
                    return false;
                }

                if(strings.length == 2){
                    if (strings[1].equalsIgnoreCase("canvas"))
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Current Block Type for Canvas is " + settings.getCanvasMat().toString().toLowerCase()));

                    else if (strings[1].equalsIgnoreCase("cells"))
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Current Block Type for Cells is " + settings.getCellMat().toString().toLowerCase()));

                    return true;
                }

                Material material = Material.matchMaterial(strings[2].toLowerCase());

                if(material == null){
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>Invalid Block Type"));
                    return false;
                }
                else if(settings.getCanvasMat() == material || settings.getCellMat() == material){
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>The two block types must not be identical"));
                    return false;
                }
                else if (strings[1].equalsIgnoreCase("canvas")){
                    if(settings.getPlaced()) {
                        if (!de.leo.conwaysGameOfLife.canvas.utils.Canvas.updateCanvasMat(material))
                            commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Some Blocks could may not be replaced during unloaded chunks."));
                    }
                    settings.setCanvasMat(material);
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully changed Canvas-Material to " + material.toString().toLowerCase()));
                }
                else if (strings[1].equalsIgnoreCase("cells")){
                    if(settings.getPlaced()) {
                        if (!de.leo.conwaysGameOfLife.canvas.utils.Canvas.updateCellMat(material))
                            commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Some Blocks could may not be replaced during unloaded chunks."));
                    }
                    settings.setCellMat(material);
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully changed Cell-Material to " + material.toString().toLowerCase()));
                }
                return true;
            }
            case "center": {
                if(strings.length == 1){
                    Location center = settings.getCenter();
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Current Center: " + center.getX() + " " + center.getY() + " " + center.getZ()));
                    return true;
                }

                if(strings.length >= 4) {

                    Location center;

                    int x;
                    int y;
                    int z;

                    if (commandSender instanceof Player player) {
                        if (strings[1].equalsIgnoreCase("~") && strings[2].equalsIgnoreCase("~") && strings[3].equalsIgnoreCase("~")){
                            x = player.getLocation().getBlockX();
                            y = player.getLocation().getBlockY();
                            z = player.getLocation().getBlockZ();
                        }
                        else{
                            x = Integer.parseInt(strings[1]);
                            y = Integer.parseInt(strings[2]);
                            z = Integer.parseInt(strings[3]);
                        }
                        center = new Location(player.getWorld(), x, y, z);
                    } else {
                        x = Integer.parseInt(strings[1]);
                        y = Integer.parseInt(strings[2]);
                        z = Integer.parseInt(strings[3]);
                        center = new Location(Bukkit.getWorld("world"), x, y, z);
                    }

                    if(settings.getPlaced()) {
                        if (!de.leo.conwaysGameOfLife.canvas.utils.Canvas.updateCenter(center))
                            commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Some Blocks could may not be placed during unloaded chunks."));
                    }
                    settings.setCenter(center);
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully changed Center-Position to " + x + " " + y + " " + z));
                    return true;
                }
                else{
                    sendUsage(commandSender);
                    return false;
                }
            }
            case "size": {
                if(strings.length == 1){
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Current Size of the Canvas: " + settings.getCanvasSize() + " Bocks"));
                    return true;
                }

                int size;
                try {
                    size = Integer.parseInt(strings[1]);
                    if(size <= -1){
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>Invalid number"));
                        return false;
                    }

                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully changed the Size of the Canvas to " + size));

                    if(size > 300)
                        commandSender.sendMessage(MM.deserialize("<gray>[<dark_red>Warn<gray>] <red>This is a high Number as a Size. Trying to place this Canvas can lead to performance Problems"));

                    if(settings.getPlaced()) {
                        if (!de.leo.conwaysGameOfLife.canvas.utils.Canvas.updateSize(size))
                            commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Some Blocks could may not be placed during unloaded chunks."));
                    }
                    settings.setCanvasSize(size);
                    return true;
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>Invalid number"));
                    return false;
                }
            }
            case "place": {
                World world = (commandSender instanceof Player player) ? player.getWorld() : Bukkit.getWorld("world");
                Boolean success = de.leo.conwaysGameOfLife.canvas.utils.Canvas.createCanvas(settings.getCenter(), settings.getCanvasSize(), settings.getCanvasMat(), world);
                Location center = settings.getCenter();
                int size = settings.getCanvasSize();
                commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully placed the Canvas at " + center.getX() + " " + center.getY() + " " + center.getY()
                    + " with a size of " + size + " Blocks."));
                if(!success){
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Some Blocks could may not be placed during unloaded chunks."));
                }
                settings.setPlaced(true);
                return true;
            }
            case "remove": {
                if (settings.getPlaced()) {
                    settings.setPlaced(false);
                    if (!de.leo.conwaysGameOfLife.canvas.utils.Canvas.deleteCanvas()) {
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Some Blocks could may not be removed during unloaded chunks."));
                    }
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully removed the Canvas"));
                    return true;
                }
                else{
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>No Canvas is placed yet"));
                }
                return false;
            }
            case "clear": {
                if (settings.getPlaced()) {
                    if (!de.leo.conwaysGameOfLife.canvas.utils.Canvas.clear()) {
                        commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <yellow>Some Blocks could may not be cleared during unloaded chunks."));
                    }
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Successfully cleared the Canvas"));
                    return true;
                }
                else{
                    commandSender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <red>No Canvas is placed yet"));
                }
                return false;
            }
        }
        sendUsage(commandSender);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        List<String> completions = new ArrayList<>();
        if(command.getName().equalsIgnoreCase("canvas")) {
            if (strings.length == 1) {
                List<String> subcommands = List.of("material", "center", "size", "place", "remove", "clear");
                for(String string : subcommands){
                    if(string.toLowerCase().startsWith(strings[0].toLowerCase())){
                        completions.add(string);
                    }
                }
            }


            else if(strings.length==2&&strings[0].equalsIgnoreCase("material")){
                List<String> subcommands = List.of("canvas", "cells");
                for(String string : subcommands){
                    if(string.toLowerCase().startsWith(strings[1].toLowerCase())){
                        completions.add(string);
                    }
                }
            }
            else if(strings.length==2&&strings[0].equalsIgnoreCase("center")&&commandSender instanceof Player player){
                Block target = player.getTargetBlockExact(100);
                if(target != null){
                    completions.add(target.getX() + " " + target.getY() + " " + target.getZ());
                }
                completions.add("~ ~ ~");
            }


            else if(strings.length==3&&(strings[1].equalsIgnoreCase("canvas") || strings[1].equalsIgnoreCase("cells"))){
                for(Material material : Material.values()){
                    if(material.isBlock() && material.name().toLowerCase().startsWith(strings[2].toLowerCase())){
                        completions.add(material.name().toLowerCase());
                    }
                }
            }
        }
        return completions;
    }

    private void sendUsage(CommandSender sender){
        sender.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] <green>Usage: <gold>/canvas <place | remove | clear>, /canvas material <canvas | cells> <material>, /canvas center <X Y Z | ~ ~ ~>, /canvas size <integer>"));
    }
}
