package com.neutralplasma.holographicPlaceholders.storage;

import java.util.List;

public class AnimationConfig {
    private final String name;
    private final double refresh;
    private final List<String> frames;

    AnimationConfig(String name, double refresh, List<String> frames) {
        this.name = name;
        this.refresh = refresh;
        this.frames = frames;
    }

    public String getName() {
        return name;
    }

    public double getRefreshRate() {
        return refresh;
    }

    public List<String> getFrames() {
        return frames;
    }
}
