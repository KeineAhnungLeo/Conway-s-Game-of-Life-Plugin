package de.leo.conwaysGameOfLife.canvas.utils;

import de.leo.conwaysGameOfLife.ConwaysGameOfLife;
import de.leo.conwaysGameOfLife.settings.utils.Settings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Canvas {

    public static Boolean createCanvas(Location center, Integer size, Material canvasMat, World world) {
        boolean success = true;
        for (int dx = -size; dx <= size; dx++) {
            for (int dz = -size; dz <= size; dz++) {
                Location location = new Location(world, center.getBlockX() + dx, center.getBlockY(), center.getBlockZ() + dz);
                if (!location.isChunkLoaded())
                    success = false;
                world.getBlockAt(location).setType(canvasMat, false);
            }
        }
        world.getBlockAt(center.clone().add(0, 1, 0)).setType(Material.GOLD_BLOCK, false);
        return success;
    }


    public static Boolean deleteCanvas() {
        boolean success = true;

        Settings settings = ConwaysGameOfLife.getInstance().getSettings();
        int size = settings.getCanvasSize();
        Location center = settings.getCenter();
        World world = center.getWorld();

        for (int dx = -size; dx <= size; dx++) {
            for (int dz = -size; dz <= size; dz++) {
                Location location = new Location(world, center.getBlockX() + dx, center.getBlockY(), center.getBlockZ() + dz);
                if (!location.isChunkLoaded())
                    success = false;
                world.getBlockAt(location).setType(Material.AIR, false);
            }
        }
        world.getBlockAt(center.clone().add(0, 1, 0)).setType(Material.AIR, false);
        return success;
    }


    public static Boolean updateCanvasMat(Material material) {
        boolean success = true;

        Settings settings = ConwaysGameOfLife.getInstance().getSettings();
        int size = settings.getCanvasSize();
        Location center = settings.getCenter();
        World world = center.getWorld();

        for (int dx = -size; dx <= size; dx++) {
            for (int dz = -size; dz <= size; dz++) {
                Location location = new Location(world, center.getBlockX() + dx, center.getBlockY(), center.getBlockZ() + dz);
                if (!location.isChunkLoaded())
                    success = false;
                else if(world.getBlockAt(location).getType() == settings.getCanvasMat())
                    world.getBlockAt(location).setType(material, false);
            }
        }
        return success;
    }


    public static Boolean updateCellMat(Material material) {
        boolean success = true;

        Settings settings = ConwaysGameOfLife.getInstance().getSettings();
        int size = settings.getCanvasSize();
        Location center = settings.getCenter();
        World world = center.getWorld();

        for (int dx = -size; dx <= size; dx++) {
            for (int dz = -size; dz <= size; dz++) {
                Location location = new Location(world, center.getBlockX() + dx, center.getBlockY(), center.getBlockZ() + dz);
                if (!location.isChunkLoaded())
                    success = false;
                else if(world.getBlockAt(location).getType() == settings.getCellMat())
                    world.getBlockAt(location).setType(material, false);
            }
        }
        return success;
    }


    public static Boolean updateCenter(Location center) {
        deleteCanvas();

        boolean success = true;

        Settings settings = ConwaysGameOfLife.getInstance().getSettings();
        int size = settings.getCanvasSize();
        World world = center.getWorld();
        Material canvasMat = settings.getCanvasMat();

        for (int dx = -size; dx <= size; dx++) {
            for (int dz = -size; dz <= size; dz++) {
                Location location = new Location(world, center.getBlockX() + dx, center.getBlockY(), center.getBlockZ() + dz);
                if (!location.isChunkLoaded())
                    success = false;
                world.getBlockAt(location).setType(canvasMat, false);
            }
        }
        world.getBlockAt(center.clone().add(0, 1, 0)).setType(Material.GOLD_BLOCK, false);
        return success;
    }

    public static Boolean updateSize(Integer size) {
        deleteCanvas();

        boolean success = true;

        Settings settings = ConwaysGameOfLife.getInstance().getSettings();
        Location center = settings.getCenter();
        World world = center.getWorld();
        Material canvasMat = settings.getCanvasMat();

        for (int dx = -size; dx <= size; dx++) {
            for (int dz = -size; dz <= size; dz++) {
                Location location = new Location(world, center.getBlockX() + dx, center.getBlockY(), center.getBlockZ() + dz);
                if (!location.isChunkLoaded())
                    success = false;
                world.getBlockAt(location).setType(canvasMat, false);
            }
        }
        world.getBlockAt(center.clone().add(0, 1, 0)).setType(Material.GOLD_BLOCK, false);
        return success;
    }

    public static Boolean clear() {
        deleteCanvas();

        boolean success = true;

        Settings settings = ConwaysGameOfLife.getInstance().getSettings();
        Location center = settings.getCenter();
        World world = center.getWorld();
        Material canvasMat = settings.getCanvasMat();
        Integer size = settings.getCanvasSize();

        for (int dx = -size; dx <= size; dx++) {
            for (int dz = -size; dz <= size; dz++) {
                Location location = new Location(world, center.getBlockX() + dx, center.getBlockY(), center.getBlockZ() + dz);
                if (!location.isChunkLoaded())
                    success = false;
                world.getBlockAt(location).setType(canvasMat, false);
            }
        }
        world.getBlockAt(center.clone().add(0, 1, 0)).setType(Material.GOLD_BLOCK, false);
        return success;
    }
}
