package com.xck.bean;

/**
 * class desc:自定义和数据库字段名对应的描述信息，用于表头显示
 *
 * create time:2019-2-21 00:00:00
 * @version 1.0.0
 */
public class ColumnInfo {
    private String desc; //描述
    private int widthPercent; //宽度占比以0~100的数字表示

    public ColumnInfo(String desc, int widthPercent){
        this.desc = desc;
        this.widthPercent = widthPercent;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getWidthPercent() {
        return widthPercent;
    }

    public void setWidthPercent(int widthPercent) {
        this.widthPercent = widthPercent;
    }
}
