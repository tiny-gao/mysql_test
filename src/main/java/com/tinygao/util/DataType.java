package com.tinygao.util;

import com.tinygao.mysql.gendata.TypeElement;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Created by gsd on 2017/3/11.
 */
public interface  DataType {
    String getRandom(int length);
}
