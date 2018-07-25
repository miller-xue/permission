package com.miller.util;

import java.util.Date;
import java.util.Random;

/**
 * Created by miller on 2018/7/25
 */
public class PasswordUtil {
    public final static String[] word = {
            "a", "b", "c", "d", "e", "f", "g",
            "h", "j", "k", "m", "n",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "M", "N",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    public final static String[] num = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };


    public static synchronized String randomPassword() {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random(System.currentTimeMillis());

        boolean falg = false;
        int length = random.nextInt(3) + 8;
        for (int i = 0 ; i < length ; i ++) {
            if (falg) {
                stringBuffer.append(num[random.nextInt(num.length)]);
            }else {
                stringBuffer.append(word[random.nextInt(word.length)]);
            }
            falg = !falg;
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(randomPassword());
    }
}
