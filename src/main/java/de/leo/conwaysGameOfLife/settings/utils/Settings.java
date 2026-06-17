package de.leo.conwaysGameOfLife.settings.utils;

import org.bukkit.Material;

import javax.swing.text.Position;

public class Settings {
    private Position center;
    private Material canvasMat;
    private Material cellMat;
    private Float generationTime;
    private Boolean debugActionBar;
    private Boolean debugChat;
    private Boolean canvasScheduler;

    public Settings(Position center, Material canvasMat, Material cellMat, Float generationTime, Boolean debugActionBar, Boolean debugChat, Boolean canvasScheduler){
        this.center = center;
        this.canvasMat = canvasMat;
        this.cellMat = cellMat;
        this.generationTime = generationTime;
        this.debugActionBar = debugActionBar;
        this.debugChat = debugChat;
        this.canvasScheduler = canvasScheduler;
    }
}
