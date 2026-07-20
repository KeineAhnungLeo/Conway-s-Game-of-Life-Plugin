package de.leo.conwaysGameOfLife;

import de.leo.conwaysGameOfLife.canvas.commands.Canvas;
import de.leo.conwaysGameOfLife.datamanager.SettingsManager;
import de.leo.conwaysGameOfLife.other.commands.Reset;
import de.leo.conwaysGameOfLife.other.commands.Version;
import de.leo.conwaysGameOfLife.settings.utils.Settings;
import de.leo.conwaysGameOfLife.simulation.commands.Simulation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ConwaysGameOfLife extends JavaPlugin {

    private static ConwaysGameOfLife instance;

    private static Settings settings;

    private SettingsManager settingsManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        //Commands
        Objects.requireNonNull(getCommand("canvas")).setExecutor(new Canvas());
        Objects.requireNonNull(getCommand("simulation")).setExecutor(new Simulation());
        Objects.requireNonNull(getCommand("version")).setExecutor(new Version());
        Objects.requireNonNull(getCommand("reset")).setExecutor(new Reset());
        //Tab Completer
        Objects.requireNonNull(getCommand("canvas")).setTabCompleter(new Canvas());
        Objects.requireNonNull(getCommand("simulation")).setTabCompleter(new Simulation());
        Objects.requireNonNull(getCommand("reset")).setTabCompleter(new Reset());
        //Init Settings
        settingsManager = new SettingsManager(this);
        settings = settingsManager.load();
        //Other
        getLogger().info("Successfully loaded ConwaysGameOfLifePlugin-1.0.0");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        settingsManager.save(settings);
    }

    public static ConwaysGameOfLife getInstance(){
        return instance;
    }

    public de.leo.conwaysGameOfLife.settings.utils.Settings getSettings() { return  settings; }
}
