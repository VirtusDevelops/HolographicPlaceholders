package com.neutralplasma.holographicPlaceholders.animations;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimationReplacer {
    private static final Pattern ANIMATION_PATTERN = Pattern.compile("(.*)<([^ ]*)(.*?)>(.*?)</(.*?)>(.*)");
    private static AnimationRegistery animationRegistry;

    public static void setAnimationRegistry(AnimationRegistery animationRegistry) {
        AnimationReplacer.animationRegistry = animationRegistry;
    }

    public static List<String> setAnimations(List<String> frames) {
        List<String> outputFrames = new ArrayList<>();

        for (String frame : frames) {
            //frame = HexUtil.colorify(frame); //ChatColor.translateAlternateColorCodes('&', frame);
            outputFrames.addAll(setAnimations(frame));
        }

        if (frames.size() < 1) {
            outputFrames.add("Failed to load animation");
        }
        return outputFrames;
    }

    public static List<String> setAnimations(String text) {
        Matcher matcher = ANIMATION_PATTERN.matcher(text);

        if (matcher.find()) {
            String tagName = matcher.group(2);
            Animation animation = animationRegistry.getAnimation(tagName);

            if (animation != null) {
                List<String> animationFrames = new ArrayList<>();
                String options = matcher.group(3).trim();
                String value = matcher.group(4);

                if (animation instanceof AnimationData) {
                    AnimationData animationData = (AnimationData) animation;
                    Map<String, String> optionsMap = mergeMap(animationData.getOptions(), decodeOptions(options));
                    animationFrames.addAll(animationData.create(value, optionsMap));
                } else {
                    animationFrames.addAll(animation.create(value));
                }

                if (animationFrames.size() > 0) {
                    String before = matcher.group(1);
                    String after = matcher.group(6);

                    animationFrames = insertList(before, animationFrames, after);
                }

                return animationFrames;
            }
        }

        return Collections.singletonList(text);
    }

    private static Map<String, String> mergeMap(Map<String, String> defaults, Map<String, String> overrides) {
        Map<String, String> mergedMap = new HashMap<>(defaults);
        for (String key : defaults.keySet())
            if (overrides.containsKey(key)) mergedMap.put(key, overrides.get(key));

        return mergedMap;
    }

    private static List<String> insertList(String before, List<String> list, String after) {
        List<String> out = new ArrayList<>();
        for (String frame : list) {
            out.add(before + frame + after);
        }
        return out;
    }

    private static Map<String, String> decodeOptions(String text) {
        Map<String, String> options = new HashMap<>();

        if (!text.contains(" ")) {
            return parseParametersToMap(text, options);
        }

        for (String parameter : text.split(" ")) {
            parseParametersToMap(parameter, options);
        }

        return options;
    }

    private static Map<String, String> parseParametersToMap(String parameter, Map<String, String> map) {
        if (parameter.contains("=")) {
            String[] parts = parameter.split("=", 2);
            map.put(parts[0], parts[1]);
        }

        return map;
    }
}
