package com.neutralplasma.holographicPlaceholders.animations.animations;

import com.neutralplasma.holographicPlaceholders.animations.AnimationData;
import eu.virtusdevelops.virtuscore.utils.HexUtil;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Wave implements AnimationData {

    private static final Map<String, String> DEFAULTS = Collections.singletonMap(
            "colors", "&c,&e,&6,&a,&9,&1,&d");

    @Override
    public String getName() {
        return "wave";
    }

    @Override
    public Map<String, String> getOptions() {
        return DEFAULTS;
    }

    @Override
    public List<String> create(String text, Map<String, String> options) {
        List<String> frames = new ArrayList<>();
        String[] colors = options.get("colors").split(",");

        String formatting = ChatColor.getLastColors(text);
        text = text.replace(formatting, "");

        int counter = 0;
        int index = 0;

        for (String ignored : colors) {
            StringBuilder currentFrame = new StringBuilder();


            for (char c : text.toCharArray()) {
                String result = colors[index] + formatting;

                index++;
                if (index >= colors.length) {
                    index = 0;
                }

                currentFrame.append(result).append(c);
            }

            index = ++counter;
            frames.add(HexUtil.colorify(currentFrame.toString()));
        }
        return frames;
    }

}
