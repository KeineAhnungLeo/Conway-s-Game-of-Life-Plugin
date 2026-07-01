package de.leo.conwaysGameOfLife.settings.utils;

import org.bukkit.Location;
import org.bukkit.Material;

public class Settings {
    private Location center;
    private Material canvasMat;
    private Material cellMat;
    private Integer generationTime;
    private Boolean debugActionBar;
    private Boolean debugChat;
    private Boolean ruleType;
    private Integer canvasSize;
    private Boolean placed;

    public Settings(Location center, Material canvasMat, Material cellMat, Integer generationTime, Boolean debugActionBar, Boolean debugChat, Boolean ruleType, Integer canvasSize, Boolean placed){
        this.center = center;
        this.canvasMat = canvasMat;
        this.cellMat = cellMat;
        this.generationTime = generationTime;
        this.debugActionBar = debugActionBar;
        this.debugChat = debugChat;
        this.ruleType = ruleType;
        this.canvasSize = canvasSize;
        this.placed = placed;
    }

    //Getter

    public Location getCenter() {
        return center;
    }

    public Material getCanvasMat() {
        return canvasMat;
    }

    public Material getCellMat() {
        return cellMat;
    }

    public Integer getGenerationTime() {
        return generationTime;
    }

    public Boolean getDebugActionBar() {
        return debugActionBar;
    }

    public Boolean getDebugChat() {
        return debugChat;
    }

    public Boolean getRuleType() {
        return ruleType;
    }

    public Integer getCanvasSize() {
        return canvasSize;
    }

    public Boolean getPlaced() {
        return placed;
    }

    //Setter

    public void setCenter(Location center) {
        this.center = center;
    }

    public void setCanvasMat(Material canvasMat) {
        this.canvasMat = canvasMat;
    }

    public void setCellMat(Material cellMat) {
        this.cellMat = cellMat;
    }

    public void setGenerationTime(Integer generationTime) {
        this.generationTime = generationTime;
    }

    public void setDebugActionBar(Boolean debugActionBar) {
        this.debugActionBar = debugActionBar;
    }

    public void setDebugChat(Boolean debugChat) {
        this.debugChat = debugChat;
    }

    public void setRuleType(Boolean ruleType) {
        this.ruleType = ruleType;
    }

    public void setCanvasSize(Integer canvasSize) {
        this.canvasSize = canvasSize;
    }

    public void setPlaced(Boolean placed) {
        this.placed = placed;
    }
}
