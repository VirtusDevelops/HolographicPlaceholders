package com.neutralplasma.holographicPlaceholders.animations.animations;

import com.neutralplasma.holographicPlaceholders.animations.AnimationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pause implements AnimationData {

    private static final Map<String, String> DEFAULTS = new HashMap<String, String>() {{
        put("times", "10");
    }};

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public Map<String, String> getOptions() {
        return DEFAULTS;
    }

    @Override
    public List<String> create(String text, Map<String, String> options) {
        List<String> frames = new ArrayList<>();

        int pause = Integer.parseInt(options.get("times"));
        for (int i = 0; i < pause; i++) {
            frames.add(text);
        }

        return frames;
    }
}