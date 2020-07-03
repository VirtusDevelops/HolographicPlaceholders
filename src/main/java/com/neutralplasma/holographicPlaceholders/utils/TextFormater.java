package com.neutralplasma.holographicPlaceholders.utils;
import org.bukkit.ChatColor;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TextFormater {


    private static String formatDecimals(Long number){
        DecimalFormat dformater = new DecimalFormat("###.##");

        String formated = dformater.format(number);

        return formated;

    }

    private static String formatNumbers(Long number){
        DecimalFormat dformater = new DecimalFormat("###,###,###,###.###");

        String formated = dformater.format(number);

        return formated;

    }

    private static String formatNames(Long number){
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        double numValue = number.doubleValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,###.##").format(numValue);
        }
    }

    public static String formatValue(int format, long value){
        switch(format){
            case 1: return formatDecimals(value);
            case 2: return formatNumbers(value);
            case 3: return formatNames(value);
        }
        return "";
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

