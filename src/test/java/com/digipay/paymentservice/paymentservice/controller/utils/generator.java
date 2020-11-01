package com.digipay.paymentservice.paymentservice.controller.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class generator {

    public static Long getRandomIntegerBetween(Long min, Long max) {

        return ThreadLocalRandom.current().nextLong(min, max);
    }


    public static Long generateNumber() {

        return getRandomIntegerBetween(10000L, 1000000000L);
    }


    public static Long generateNationalityCode() {

        return getRandomIntegerBetween(1000000000L, 9999999999L);
    }

    public static String generateCardNumbers() {

        String value;
        Random rd = new Random();
        if (rd.nextBoolean()) {
            value = "6037";
        } else {
            value = getRandomIntegerBetween(1000L, 9999L).toString();
        }
        value += "-" + getRandomIntegerBetween(1000L, 9999L).toString()
                + "-" + getRandomIntegerBetween(1000L, 9999L).toString()
                + "-" + getRandomIntegerBetween(1000L, 9999L).toString();
        return value;
    }

    public static Integer generateCvv2() {

        return getRandomIntegerBetween(100L, 999L).intValue();
    }

    public static String generateStatus() {
        Random rd = new Random();
        if (rd.nextBoolean()) {
            return "SUCCESS";
        }
        return "FAILED";
    }


}
