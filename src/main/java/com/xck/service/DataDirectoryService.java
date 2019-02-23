package com.xck.service;

import com.xck.bean.ColumnInfo;
import com.xck.bean.DataDirectory;
import com.xck.dao.DataDirectoryDAO;
import com.xck.util.POIWordUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class DataDirectoryService {

    private static Logger log = Logger.getLogger(DataDirectoryService.class);

    @Resource
    DataDirectoryDAO dataDirectoryDAO;

    /**
     * 生成word版本数据字典的调用入口
     *
     * @param dataDirectory
     */
    public void generateDataDirectory(DataDirectory dataDirectory){
        if(dataDirectory.getTableHeader().size() == 0){
            return;
        }

        Map<String, String> tableCommentMap = dataDirectoryDAO.getTableComment(dataDirectory.getDatabaseName());
        Map<String, List<String>> tableColumnsMap = dataDirectoryDAO.getTableColumnsInfo(dataDirectory);
        generatedoc(dataDirectory, tableColumnsMap, tableCommentMap);
    }

    private void generatedoc(DataDirectory dataDirectory, Map<String, List<String>> tableMap, Map<String, String> tableCommentMap) {
        String databaseName = dataDirectory.getDatabaseName();
        String filePath = dataDirectory.getBaseFielPath() + databaseName + "数据字典.docx";

        OutputStream out = null;
        try {
            XWPFDocument doc = new XWPFDocument();

            //设置标题
            POIWordUtil.setTitle(doc, databaseName + "数据字典", 20, ParagraphAlignment.CENTER);

            POIWordUtil.nextLine(doc, 3); //换行三次

            Iterator iterator = tableMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String table_name = (String) entry.getKey();
                List<String> columnList = (List<String>) entry.getValue();

                String tableNameStr = table_name;
                if (StringUtils.isNotEmpty(tableCommentMap.get(table_name))) {
                    tableNameStr += "【" + tableCommentMap.get(table_name) + "】";
                }
                POIWordUtil.newParagraphText(doc, tableNameStr); //插入表名

                //创建指定行列的表格
                XWPFTable table = POIWordUtil.createTable(doc, columnList.size() + 1, dataDirectory.getTableHeader().size());

                //设置表头，列宽
                XWPFTableRow rowHeader = table.getRow(0);
                Iterator headeriterator = dataDirectory.getTableHeader().entrySet().iterator();
                for(int i=0; headeriterator.hasNext(); i++){
                    Map.Entry headerEntry = (Map.Entry)headeriterator.next();
                    ColumnInfo columnInfo = (ColumnInfo)headerEntry.getValue();
                    String width = (int)(8000*((double)columnInfo.getWidthPercent()/100))+"";
                    setTableCell(rowHeader, i, columnInfo.getDesc(), width);
                }

                //插入单元格文本
                for (int i = 0; i < columnList.size(); i++) {
                    String[] columninfos = columnList.get(i).split("@");
                    XWPFTableRow row = table.getRow(i + 1);
                    for (int j = 0; j < columninfos.length; j++) {
                        setTableCell(row, j, columninfos[j], "0");
                    }
                }
                POIWordUtil.nextLine(doc, 1);
            }
            out = new FileOutputStream(new File(filePath));
            doc.write(out);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void setTableCell(XWPFTableRow row, int CellPos, String text, String width) {
        XWPFTableCell cell = row.getCell(CellPos);
        POIWordUtil.setTableCellTextCenter(cell);
        if (!"0".equals(width)) {
            POIWordUtil.setTableCellWidth(cell, width);
        }
        cell.setText(text);
    }
}
