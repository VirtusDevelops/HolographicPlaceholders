package com.neutralplasma.holographicPlaceholders.storage;

import org.bukkit.Location;

public class SignLocation {
    private final int x;
    private final int y;
    private final int z;
    private final String world;

    public SignLocation(int x, int y, int z, String world){
        this.x = x; this.y = y; this.z = z;
        this.world = world;
    }

    public SignLocation(Location location){
        this.x = location.getBlockX(); this.y = location.getBlockY(); this.z = location.getBlockZ();
        this.world = location.getWorld().getName();
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString(){
        return world + ":" + x + ":" + y + ":" + z;
    }

}
