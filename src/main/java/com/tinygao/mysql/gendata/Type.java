package com.tinygao.mysql.gendata;

/**
 * Created by gsd on 2017/3/11.
 */
public enum  Type {
    INT_TYPE("int"),
    VARCHAR_TYPE("varchar");

    public String type;
    private Type(String type) {
        this.type = type;
    }

    public static Type getTypeByStr(String type) {
       switch (type) {
           case "int" : return INT_TYPE;
           case "varchar": return VARCHAR_TYPE;
           default: throw new RuntimeException(String.format("类型: %s 不存在", type));
       }
    }
}
