package com.neutralplasma.holographicPlaceholders.utils;

import java.text.DecimalFormat;

public class BalanceFormater {

    public String formatDecimals(Double number){
        DecimalFormat dformater = new DecimalFormat("###.##");

        String formated = dformater.format(number);

        return formated;

    }

    public String formatNumbers(Double number){
        DecimalFormat dformater = new DecimalFormat("###,###,###,###.###");

        String formated = dformater.format(number);

        return formated;

    }

    public String formatNames(Double number){
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

    public String formatDecimalsLong(Long number){
        DecimalFormat dformater = new DecimalFormat("###.##");

        String formated = dformater.format(number);

        return formated;

    }
}
