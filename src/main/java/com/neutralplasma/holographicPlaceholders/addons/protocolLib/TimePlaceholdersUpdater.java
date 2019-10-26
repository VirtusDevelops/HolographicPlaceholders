package com.neutralplasma.holographicPlaceholders.addons.protocolLib;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import org.bukkit.Bukkit;

import java.util.List;

public class TimePlaceholdersUpdater implements PlaceholderReplacer {

    private int indexCurrent;
    private List<String> updater;

    public TimePlaceholdersUpdater(final List<String> frames){
        this.indexCurrent = 0;
        this.updater = frames;
    }

    public String update() {
        final String currentFrame = this.updater.get(this.indexCurrent);
        if (this.indexCurrent == this.updater.size() - 1) {
            this.indexCurrent = 0;
        }
        else {
            this.indexCurrent++;
        }
        return currentFrame;
    }
}
