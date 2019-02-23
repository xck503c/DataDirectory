package com.xck.bean;

/**
 * class desc:主要用于限定数据库的字段名
 *
 * create time:2019-2-21 00:00:00
 * @version 1.0.0
 */
public enum  ColumnNameEnum {
    COLUMN_NAME("column_name"), COLUMN_TYPE("column_type"), IS_NULLABLE("is_nullable"),
    COLUMN_COMMENT("column_comment"), COLUMN_KEY("column_key");

    private String name;
    private ColumnNameEnum(String name){
        this.name = name;
    }
}
