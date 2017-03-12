package com.tinygao.util;

import java.util.Random;

/**
 * Created by gsd on 2017/3/11.
 */
public class IntStrType implements DataType {
    Random random = new Random();
    public  String getRandom(int length) {
        String base = "123456789";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
