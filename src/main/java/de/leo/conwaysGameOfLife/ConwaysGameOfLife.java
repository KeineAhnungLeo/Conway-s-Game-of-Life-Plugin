package de.leo.conwaysGameOfLife;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConwaysGameOfLife extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        //Events
        Bukkit.getPluginManager().registerEvents(new reload(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
