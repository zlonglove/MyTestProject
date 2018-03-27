package com.ISHello.utils;

import java.util.Random;

/**
 * math util clss
 *
 * @author zhanglong
 * @version 0.0.2
 * @category 2012-3-15
 */
public class ISMath {
    private static ISMath math;

    private ISMath() {

    }

    public static ISMath getInstance() {
        if (math == null) {
            math = new ISMath();
        }
        return math;
    }

    /**
     * 获取一个范围内的随机数
     *
     * @param min--min number
     * @param max--max number
     * @return
     */
    public int getintFromRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

}
