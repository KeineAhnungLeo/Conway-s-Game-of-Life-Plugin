package de.leo.conwaysGameOfLife.datamanager;

import de.leo.conwaysGameOfLife.ConwaysGameOfLife;
import de.leo.conwaysGameOfLife.settings.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class SettingsManager {
    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration configuration;

    public SettingsManager(JavaPlugin plugin){
        this.plugin = plugin;
        createFile();
    }

    private void createFile(){
        file = new File(plugin.getDataFolder(), "Settings.yml");
        if(!file.exists()){
            plugin.getDataFolder().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e){
                ConwaysGameOfLife.getInstance().getLogger().log(Level.SEVERE, "Failed to create Settings.yml file", e);
            }
        }

        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void save(Settings settings){
        configuration.set("settings.center", settings.getCenter());
        configuration.set("settings.canvasMat", settings.getCanvasMat().name());
        configuration.set("settings.cellMat", settings.getCellMat().name());
        configuration.set("settings.generationTime", settings.getGenerationTime());
        configuration.set("settings.debugActionBar", settings.getDebugActionBar());
        configuration.set("settings.debugChat", settings.getDebugChat());
        configuration.set("settings.ruleType", settings.getRuleType());
        configuration.set("settings.canvasSize", settings.getCanvasSize());
        configuration.set("settings.placed", settings.getPlaced());
        try {
            configuration.save(file);
        } catch (IOException e) {
            ConwaysGameOfLife.getInstance().getLogger().log(Level.SEVERE, "Failed to save Settings", e);
        }
    }

    public Settings load() {
        Location defaultCenter = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        Location center = configuration.getLocation("settings.center", defaultCenter);
        Material canvasMat = Material.matchMaterial(configuration.getString("settings.canvasMat", Material.WHITE_CONCRETE.name()));
        if (canvasMat == null) canvasMat = Material.WHITE_CONCRETE;
        Material cellMat = Material.matchMaterial(configuration.getString("settings.cellMat", Material.BLACK_CONCRETE.name()));
        if (cellMat == null) cellMat = Material.BLACK_CONCRETE;
        int generationTime = configuration.getInt("settings.generationTime", 20);
        boolean debugActionBar = configuration.getBoolean("settings.debugActionBar", false);
        boolean debugChat = configuration.getBoolean("settings.debugChat", false);
        boolean ruleType = configuration.getBoolean("settings.ruleType", true);
        int canvasSize = configuration.getInt("settings.canvasSize", 20);
        boolean placed = configuration.getBoolean("settings.placed", false);

        return new Settings(center, canvasMat, cellMat, generationTime, debugActionBar, debugChat, ruleType, canvasSize, placed);
    }
}
