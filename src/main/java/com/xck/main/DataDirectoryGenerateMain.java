package com.xck.main;

import com.xck.bean.ColumnInfo;
import com.xck.bean.ColumnNameEnum;
import com.xck.bean.DataDirectory;
import com.xck.service.DataDirectoryService;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.LinkedHashMap;

public class DataDirectoryGenerateMain {
    public static void main(String[] args){
        AbstractApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        DataDirectoryService service = context.getBean(DataDirectoryService.class);

        DataDirectory dataDirectory = new DataDirectory();
        dataDirectory.setBaseFielPath("D:\\");
        dataDirectory.setDatabaseName("sqlbook");

        LinkedHashMap<ColumnNameEnum, ColumnInfo> columnInfoMap = new LinkedHashMap<ColumnNameEnum, ColumnInfo>();
        columnInfoMap.put(ColumnNameEnum.COLUMN_NAME, new ColumnInfo("字段名", 20));
        columnInfoMap.put(ColumnNameEnum.COLUMN_TYPE, new ColumnInfo("数据类型", 20));
        columnInfoMap.put(ColumnNameEnum.IS_NULLABLE, new ColumnInfo("允许为空", 20));
        columnInfoMap.put(ColumnNameEnum.COLUMN_COMMENT, new ColumnInfo("字段说明", 30));
        columnInfoMap.put(ColumnNameEnum.COLUMN_KEY, new ColumnInfo("键", 10));
        dataDirectory.setTableHeader(columnInfoMap);

        service.generateDataDirectory(dataDirectory);
    }
}