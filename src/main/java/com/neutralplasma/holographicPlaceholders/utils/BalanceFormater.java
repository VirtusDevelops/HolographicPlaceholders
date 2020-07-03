package com.neutralplasma.holographicPlaceholders.utils;

import java.text.DecimalFormat;

public class BalanceFormater {

    public static String formatDecimals(Double number){
        DecimalFormat dformater = new DecimalFormat("###.##");

        String formated = dformater.format(number);

        return formated;

    }

    public static String formatNumbers(Double number){
        DecimalFormat dformater = new DecimalFormat("###,###,###,###.###");

        String formated = dformater.format(number);

        return formated;

    }

    public static String formatNames(Double number){
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

    public static String formatValue(int format, double value){
        switch(format){
            case 1: return formatDecimals(value);
            case 2: return formatNumbers(value);
            case 3: return formatNames(value);
        }
        return "";
    }

    public String formatDecimalsLong(Long number){
        DecimalFormat dformater = new DecimalFormat("###.##");

        String formated = dformater.format(number);

        return formated;

    }
}
