package com.neutralplasma.holographicPlaceholders.utils;
import org.bukkit.ChatColor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TextFormater {

    public static String sFormatText(String message){
        String formated = ChatColor.translateAlternateColorCodes('&', message);
        return formated;
    }

    public static String ColorFormat(String text){
        String formated = ChatColor.translateAlternateColorCodes('&', text);
        return formated;
    }
    public static String formatTime(long time) {
        time /= 1000L;
        final int days = (int)(time / 86400L);
        time -= 86400 * days;
        final int hours = (int)(time / 3600L);
        time -= 3600 * hours;
        final int minutes = (int)(time / 60L);
        time -= 60 * minutes;
        final int seconds = (int)time;
        final StringBuilder sb = new StringBuilder();

            if (days != 0) {
                sb.append(days).append("d ");
            }
            if (hours != 0) {
                sb.append(hours).append("h ");
            }
            if (minutes != 0) {
                sb.append(minutes).append("m ");
            }
            sb.append(seconds).append("s ");


        return sb.toString().trim();
    }
}

