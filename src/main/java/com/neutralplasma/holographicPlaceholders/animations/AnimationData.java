package com.neutralplasma.holographicPlaceholders.animations;

import com.neutralplasma.holographicPlaceholders.animations.Animation;

import java.util.List;
import java.util.Map;

/*
    Coded by Niall
    Edited by VirtusDevelops
 */

public interface AnimationData extends Animation {
    /**
     * Get the available options for this animation.
     *
     * @return Map of option in format key - default value.
     */
    Map<String, String> getOptions();

    /**
     * Create animation
     *
     * @param text    Text to create animation from
     * @param options Provided options to override defaults.
     * @return Created animation frames from settings.
     */
    List<String> create(String text, Map<String, String> options);

    /**
     * Create animation from default settings.
     *
     * @param text Text to create animation from.
     * @return Created animation frames.
     */
    default List<String> create(String text) {
        return create(text, null);
    }
}
