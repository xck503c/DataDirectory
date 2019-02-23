package com.xck.bean;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * class desc:用于描述所需创建数据字典的基础信息
 *
 * create time:2019-2-21 00:00:00
 * @version 1.0.0
 */
public class DataDirectory {
    private String baseFielPath; //基础路径不包括文件名，文件名固定
    private String columnSql;
    //数据字典信息
    private String databaseName; //目标数据库名
    private LinkedHashMap<ColumnNameEnum, ColumnInfo> tableHeader; //表头名和表名中文名以及宽度

    public String getBaseFielPath() {
        return baseFielPath;
    }

    public void setBaseFielPath(String baseFielPath) {
        if(StringUtils.isNotBlank(baseFielPath)){
            this.baseFielPath = baseFielPath;
            return;
        }
        this.baseFielPath = "." + File.pathSeparator;
    }

    public String getColumnSql() {
        return columnSql;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public LinkedHashMap<ColumnNameEnum, ColumnInfo> getTableHeader() {
        return tableHeader;
    }

    public void setTableHeader(LinkedHashMap<ColumnNameEnum, ColumnInfo> tableHeader) {
        Iterator iterator = tableHeader.entrySet().iterator();
        int percent = 0;
        StringBuffer sb = new StringBuffer("");
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            ColumnInfo columnInfo = (ColumnInfo)entry.getValue();
            int widthPercent =columnInfo.getWidthPercent();
            //遍历检查描述是否为空，或百分比数值是否正确
            if(StringUtils.isBlank(columnInfo.getDesc()) || widthPercent<=0 || widthPercent>=100){
                iterator.remove(); //移除
            }
            percent += widthPercent;
            sb.append(entry.getKey()).append(",");
        }
        this.columnSql = sb.delete(sb.length()-1, sb.length()).toString();
        if(percent > 100){
            this.tableHeader = new LinkedHashMap<ColumnNameEnum, ColumnInfo>();
            return;
        }
        this.tableHeader = tableHeader;
    }
}
