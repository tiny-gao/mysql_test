package com.tinygao.util;

import com.tinygao.mysql.gendata.Type;

/**
 * Created by gsd on 2017/3/11.
 */
public class DataTypeFactory {
    public static DataType genType(Type type) {
        switch (type) {
            case INT_TYPE:
                return new IntStrType();
            case VARCHAR_TYPE:
                return new VarcharType();
            default: throw  new RuntimeException(String.format("Type  %s is no existed",type));
        }
    }
}
