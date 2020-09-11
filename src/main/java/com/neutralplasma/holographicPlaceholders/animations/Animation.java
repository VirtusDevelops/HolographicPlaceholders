package com.neutralplasma.holographicPlaceholders.animations;

import java.util.List;

public interface Animation {

    /*
        Name of animation
     */
    String getName();


    /*
        Create the animation, returns list of frames of animation
     */
    List<String> create(String text);
}
