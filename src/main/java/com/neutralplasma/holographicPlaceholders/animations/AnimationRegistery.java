package com.neutralplasma.holographicPlaceholders.animations;

import java.util.ArrayList;
import java.util.List;

public class AnimationRegistery {
    private final List<Animation> registeredAnimations;

    public AnimationRegistery() {
        registeredAnimations = new ArrayList<>();
        registerDefaultAnimations();
    }

    private void registerDefaultAnimations() {
        for (DefaultAnimation anim : DefaultAnimation.values()) {
            registerAnimation(anim.getAnimation());
        }
    }

    /**
     * Register a custom animation.
     *
     * @param animation Animation
     */
    public void registerAnimation(Animation animation) {
        registeredAnimations.add(animation);
    }

    public void reload(){
        registeredAnimations.clear();
        registerDefaultAnimations();
    }

    public Animation getAnimation(String name) {
        for (Animation animation : registeredAnimations) {
            if (animation.getName().equalsIgnoreCase(name)) return animation;
        }

        return null;
    }
}
