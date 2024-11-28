package com.pucminas.commons.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {
    public static double setScale(double value) {
        return BigDecimal.valueOf(value).setScale(0, RoundingMode.CEILING).doubleValue();
    }
}
