package com.neutralplasma.holographicPlaceholders.animations.animations;

import com.neutralplasma.holographicPlaceholders.animations.AnimationData;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
    Coded by Niall
    Edited by VirtusDevelops
 */

public class Align {


    public static final AnimationData LEFT = new AnimationData() {
        @Override
        public String getName() {
            return "left";
        }

        @Override
        public Map<String, String> getOptions() {
            return Collections.singletonMap("width", "32");
        }

        @Override
        public List<String> create(String text, Map<String, String> options) {
            return Collections.singletonList(alignText(
                    text, Integer.parseInt(options.get("width")), false));
        }
    };

    public static final AnimationData RIGHT = new AnimationData() {
        @Override
        public String getName() {
            return "right";
        }

        @Override
        public Map<String, String> getOptions() {
            return Collections.singletonMap("width", "32");
        }

        @Override
        public List<String> create(String text, Map<String, String> options) {
            return Collections.singletonList(alignText(
                    text, Integer.parseInt(options.get("width")), true));
        }
    };

    private static String alignText(String text, int width, boolean right) {
        StringBuilder space = new StringBuilder();
        /*
            Calculate how many spaces to add to match the width.
         */
        for (int i = 0; i < (width - text.length()); i++) {
            space.append(" ");
        }

        return right ? (space + text) : (text + space); // add spaces to left/right depends how to align it.
    }
}
