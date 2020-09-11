package com.neutralplasma.holographicPlaceholders.animations;


import java.util.List;

public class PlaceholderReplacer implements com.neutralplasma.holographicPlaceholders.placeholder.PlaceholderReplacer {
    private final List<String> frames;
    private int currentIndex = 0;

    public PlaceholderReplacer(List<String> frames) {
        this.frames = frames;
    }

    @Override
    public String update() {
        String currentFrame = frames.get(currentIndex);

        if (currentIndex == frames.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        return currentFrame;
    }
}
