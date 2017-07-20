package com.dzd.phonebook.util;

import java.util.Random;

/**
 * Created by CHENCHAO on 2017/6/20.
 */
public class RandomUtil {


    /**
     * 获取1~10000的随机数
     *
     * @return
     */
    public static int getRandomTenThousand() {
        int max = 9999;
        int min = 1000;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }
}
