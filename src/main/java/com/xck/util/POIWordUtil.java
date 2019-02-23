package com.xck.util;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.math.BigInteger;

public class POIWordUtil {

    /**
     * 换行设置
     *
     * @param doc
     * @param times 换几行
     */
    public static void nextLine(XWPFDocument doc, int times) {
        for (int i = 0; i < times; i++) {
            XWPFParagraph nextLine = doc.createParagraph();
            nextLine.createRun().setText("\r");
        }
    }

    /**
     * 设置标题
     * @param doc
     * @param text
     * @param fontSize
     * @param align
     */
    public static void setTitle(XWPFDocument doc, String text, int fontSize, ParagraphAlignment align){
        XWPFParagraph titleParagraph = doc.createParagraph();
        titleParagraph.setAlignment(align);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText(text);
        titleRun.setFontSize(fontSize);
    }

    /**
     * 新开一个段落，插入文本
     * @param doc
     * @param text
     */
    public static void newParagraphText(XWPFDocument doc, String text){
        XWPFParagraph xwpfParagraph = doc.createParagraph();
        xwpfParagraph.createRun().setText(text);
    }

    public static XWPFTable createTable(XWPFDocument doc, int rows, int cols){
        XWPFTable table = doc.createTable(rows, cols);
        CTTblPr tablePr = table.getCTTbl().getTblPr();
        tablePr.getTblW().setType(STTblWidth.DXA);
        tablePr.getTblW().setW(new BigInteger("8000"));
        return table;
    }

    /**
     * 设置表格中单元格的宽度
     *
     * @param cell
     * @param width
     */
    public static void setTableCellWidth(XWPFTableCell cell, String width) {
        CTTc cttc = cell.getCTTc();
        CTTblWidth ctTblWidth = cttc.addNewTcPr().addNewTcW();
        ctTblWidth.setType(STTblWidth.DXA); //将
        ctTblWidth.setW(new BigInteger(width));
    }

    /**
     * 设置单元格文本居中
     *
     * @param cell
     */
    public static void setTableCellTextCenter(XWPFTableCell cell) {
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
    }
}
