package ru.optimus.handlers;

import net.minecraft.client.audio.Sound;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import ru.optimus.MainRadioCar;

import java.util.ArrayList;
import java.util.List;

public class VehicleRadio {

    private int xPos;
    private int yPos;
    private int zPos;
    private List<SoundEvent> url;
    private String nameCar;
    private float volume;
    private boolean active;
    private Entity entity;
    private int currentIndex;
    private SoundEvent activeUrl;

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getzPos() {
        return zPos;
    }

    public List<SoundEvent> getUrls() {
        return url;
    }

    public String getNameCar() {
        return nameCar;
    }

    public float getVolume() {
        return volume;
    }

    public boolean isActive() {
        return active;
    }

    public Entity getEntity() {
        return entity;
    }

    public VehicleRadio(Entity entity, int xPos, int yPos, int zPos, String nameCar) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.url = new ArrayList<>();
        this.nameCar = nameCar;
        this.entity = entity;
        NBTTagCompound nbtTagCompound = entity.getEntityData();
        nbtTagCompound.setBoolean("active", true);
        nbtTagCompound.setFloat("volume", 1.0F);
        this.active = nbtTagCompound.getBoolean("active");
        this.volume = nbtTagCompound.getFloat("volume");
        this.currentIndex = 0;
        url.addAll(MainRadioCar.sounds);
        activeUrl = SoundsHandler.FIRST_SOUND;
    }


    public int getCurrentIndex() {
        return currentIndex;
    }

    public SoundEvent getActiveUrl() {
        return activeUrl;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public void setzPos(int zPos) {
        this.zPos = zPos;
    }

    public void nextSound() {
        currentIndex++;
        if (currentIndex >= getUrls().size()) {
            currentIndex = 0;
        }
        activeUrl = getUrls().get(currentIndex);
    }

    public void setUrl(List<SoundEvent> url) {
        this.url = url;
    }

    public void setNameCar(String nameCar) {
        this.nameCar = nameCar;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
