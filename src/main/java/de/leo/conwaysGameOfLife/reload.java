package de.leo.conwaysGameOfLife;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class reload implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(event.getMessage().toLowerCase().startsWith("/reload")){
            event.setCancelled(true);
            for(Player player1: Bukkit.getOnlinePlayers()){
                player1.sendMessage("§c§lDer Server wird neugeladen.");
                Bukkit.getServer().reload();
                player1.sendMessage("§aFertsch!");
            }
        }
    }
}
