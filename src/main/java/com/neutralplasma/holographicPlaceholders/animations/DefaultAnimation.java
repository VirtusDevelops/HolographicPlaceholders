package com.neutralplasma.holographicPlaceholders.animations;

import com.neutralplasma.holographicPlaceholders.animations.animations.*;

public enum DefaultAnimation {
    ALIGN_LEFT(Align.LEFT),
    ALIGN_RIGHT(Align.RIGHT),
    FADE_IN(Fade.IN),
    FADE_OUT(Fade.OUT),
    BLINK(new Blink()),
    ERASE(new Erase()),
    GLOW(new Glow()),
    PAUSE(new Pause()),
    PULSE(new Pulse()),
    SCROLLER(new Scroller()),
    TYPEWRITER(new TypeWriter()),
    WAVE(new Wave());


    private final Animation animation;

    DefaultAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }
}
