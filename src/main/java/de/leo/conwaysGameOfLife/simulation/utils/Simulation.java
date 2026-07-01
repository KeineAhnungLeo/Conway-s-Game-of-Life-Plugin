package de.leo.conwaysGameOfLife.simulation.utils;

import de.leo.conwaysGameOfLife.ConwaysGameOfLife;
import de.leo.conwaysGameOfLife.settings.utils.Settings;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class Simulation {
    private static final HashSet<Location> isAlive = new HashSet<>();
    private static final HashSet<Location> isDead = new HashSet<>();
    public static BukkitRunnable task;
    private static BukkitRunnable task1;
    public static boolean stopScheduler = false;
    private static Integer tick = 0;
    private static final MiniMessage MM = MiniMessage.miniMessage();

    public static Integer generationCount = 0;
    private static Integer aliveCells = 0;
    private static Integer previousAliveCells = 0;
    private static Integer deadCells = 0;
    private static Integer bornCells = 0;
    private static Integer diedCells = 0;
    private static Integer calculatedCells = 0;
    private static Integer growing = 0;
    private static Double calculationSpeed = 0.0;

    public static void simulateStep(){

        long start = System.nanoTime();

        Settings settings = ConwaysGameOfLife.getInstance().getSettings();
        int size = settings.getCanvasSize();
        Location center = settings.getCenter();
        Material cellMaterial = settings.getCellMat();
        Material canvasMaterial = settings.getCanvasMat();

        previousAliveCells = aliveCells;
        calculatedCells = 0;
        aliveCells = 0;
        deadCells = 0;
        bornCells = 0;
        diedCells = 0;

        for (int dx = -size; dx <= size; dx++) {
            for (int dz = -size; dz <= size; dz++) {

                Location checkedLocation = new Location(center.getWorld(), center.getBlockX() + dx, center.getBlockY(), center.getBlockZ() + dz);
                int aliveNeighborsCount = 0;
                boolean alive = checkedLocation.getBlock().getType() == cellMaterial;

                calculatedCells++;

                if(alive)
                    aliveCells++;
                else
                    deadCells++;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if(j == 0 && i == 0)
                            continue;
                        Location checkedNeighborLocation = checkedLocation.clone().add(i, 0, j);

                        int x = checkedNeighborLocation.getBlockX();
                        int z = checkedNeighborLocation.getBlockZ();
                        int minX = center.getBlockX() - size;
                        int maxX = center.getBlockX() + size;
                        int minZ = center.getBlockZ() - size;
                        int maxZ = center.getBlockZ() + size;

                        if (x > maxX) x = minX;
                        else if (x < minX) x = maxX;

                        if (z > maxZ) z = minZ;
                        else if (z < minZ) z = maxZ;
                        checkedNeighborLocation = new Location(center.getWorld(), x, center.getBlockY(), z);

                        if (checkedNeighborLocation.getBlock().getType() == cellMaterial) {
                            aliveNeighborsCount++;
                        }
                    }
                }
                if(settings.getRuleType()) {
                    if (alive && (aliveNeighborsCount < 2 || aliveNeighborsCount > 3))
                        isDead.add(checkedLocation);
                    else if (!alive && aliveNeighborsCount == 3)
                        isAlive.add(checkedLocation);
                }
                else{
                    if (aliveNeighborsCount == 0 || aliveNeighborsCount == 2 || aliveNeighborsCount == 4 || aliveNeighborsCount == 6 || aliveNeighborsCount == 8){
                        isDead.add(checkedLocation);
                    }
                    else{
                        isAlive.add(checkedLocation);
                    }
                }
            }
        }

        for(Location location : isAlive){
            location.getBlock().setType(cellMaterial, false);
            bornCells++;
        }

        for(Location location : isDead){
            location.getBlock().setType(canvasMaterial, false);
            diedCells++;
        }

        isAlive.clear();
        isDead.clear();

        long end = System.nanoTime();
        calculationSpeed = (end - start) / 1_000_000.0;

        calculateDebugInfo(previousAliveCells, aliveCells);

        if(settings.getDebugChat())
            sendDebugInfoInChat();
    }

    public static void startSimulationScheduler(){
        if (task != null) return;
        task = new BukkitRunnable() {
            @Override
            public void run() {
                Settings settings = ConwaysGameOfLife.getInstance().getSettings();
                if(!settings.getPlaced() || stopScheduler) {
                    stopScheduler = false;
                    cancel();
                    task = null;
                    return;
                }
                tick++;
                if(tick >= ConwaysGameOfLife.getInstance().getSettings().getGenerationTime()) {
                    simulateStep();
                    tick = 0;
                    generationCount++;
                }
            }
        };
        task.runTaskTimer(ConwaysGameOfLife.getInstance(), 0L, 1L);
    }

    public static void showDebugInfoInActionbar(){
        if (task1 != null) return;
        task1 = new BukkitRunnable() {
            @Override
            public void run() {
                Settings settings = ConwaysGameOfLife.getInstance().getSettings();
                if(!settings.getDebugActionBar()) {
                    cancel();
                    task1 = null;
                    return;
                }
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.sendActionBar(MM.deserialize("<green>Alive: " + aliveCells + "<gray>," + " <green>Dead: " + deadCells + "<gray>," + " <green>Growing: " + ((growing == 0) ? "<red>False" : (growing == 1) ? "<yellow>Neutral" : "<green>True") + "<gray>," + " <green>CalculationSpeed: " + String.format("<yellow>%.2f ms", calculationSpeed)));
                }
            }
        };
        task1.runTaskTimer(ConwaysGameOfLife.getInstance(), 0L, 1L);
    }

    private static void calculateDebugInfo(Integer previousAliveCells, Integer aliveCells){
        if(aliveCells < previousAliveCells)
            growing = 0;
        else if (aliveCells == previousAliveCells)
            growing = 2;
        else
            growing = 1;
    }

    private static void sendDebugInfoInChat(){
        for(Player player : Bukkit.getOnlinePlayers()){
            String textId = "Simulation - DebugInfo";
            player.sendMessage(MM.deserialize("<gray>[<gold>" + textId + "<gray>] " + ((task != null) ? ("<green> Generation:" + generationCount + "<gray>, ") : "") + "<green>Calculated: " + calculatedCells + "<gray>," + " <green>Alive: " + aliveCells + "<gray>," + " <green>Dead: " + deadCells + "<gray>," + " <green>Born: " + bornCells + "<gray>," + " <green>Died: " + diedCells + "<gray>," + " <green>Growing: " + ((growing == 0) ? "<red>False" : (growing == 1) ? "<yellow>Neutral" : "<green>True") + "<gray>," + " <green>CalculationSpeed: " + String.format("<yellow>%.2f ms", calculationSpeed)));
        }
    }

    public static void resetDebugInfo(){
         generationCount = 0;
         aliveCells = 0;
         previousAliveCells = 0;
         deadCells = 0;
         bornCells = 0;
         diedCells = 0;
         calculatedCells = 0;
         growing = 0;
         calculationSpeed = 0.0;
    }
}
