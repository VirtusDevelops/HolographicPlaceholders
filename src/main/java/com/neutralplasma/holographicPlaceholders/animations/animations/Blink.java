package com.neutralplasma.holographicPlaceholders.animations.animations;

import com.neutralplasma.holographicPlaceholders.animations.Animation;

import java.util.Arrays;
import java.util.List;

public class Blink implements Animation {

    @Override
    public String getName() {
        return "blink";
    }

    @Override
    public List<String> create(String text) {
        return Arrays.asList(text, "");
    }
}