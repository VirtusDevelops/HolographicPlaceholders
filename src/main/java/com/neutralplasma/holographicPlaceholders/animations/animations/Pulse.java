package com.neutralplasma.holographicPlaceholders.animations.animations;

import com.neutralplasma.holographicPlaceholders.animations.AnimationData;
import eu.virtusdevelops.virtuscore.utils.HexUtil;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Pulse implements AnimationData {

    private static final Map<String, String> DEFAULTS = new HashMap<String, String>() {{
        put("pause", "1");
        //put("colors", "#FFFFFF:#000000");
        put("from", "#FFFFFF");
        put("to", "#000000");

        put("steps", "10");
    }};

    @Override
    public String getName() {
        return "pulse";
    }

    @Override
    public Map<String, String> getOptions() {
        return DEFAULTS;
    }

    @Override
    public List<String> create(String text, Map<String, String> options) {

        int pause = Integer.parseInt(options.get("pause"));
        int steps = Integer.parseInt(options.get("steps"));
        List<Color> hexSteps = new ArrayList<>();
        hexSteps.add(Color.decode(options.get("from")));
        hexSteps.add(Color.decode(options.get("to")));

        HexUtil.Gradient gradient = new HexUtil.Gradient(hexSteps, 10);


        List<String> pulseFrames = new ArrayList<>();

        for(int i = 0; i < steps; i++){
            pulseFrames.add(HexUtil.translateHex(gradient.next()) + text);
        }

        ArrayList<String> frames = new ArrayList<>(pulseFrames);
        for (int i = 0; i < pause; i++) {
            frames.add(HexUtil.translateHex(hexSteps.get(1)) + text);
        }
        Collections.reverse(pulseFrames);
        frames.addAll(pulseFrames);



        return frames;
    }
}
