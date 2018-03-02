package com.nilo.dms.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExportExcel {

    private HSSFWorkbook wb;

    private HSSFSheet sheet;

    /**
     */
    public ExportExcel(HSSFWorkbook wb) {
        this.wb = wb;
        sheet = this.wb.createSheet();
    }

    /**
     * @param list
     */
    public int fillData(List<Map<String, String>> list, List<String> header) {

        int rowNum = 0;

        HSSFFont poDateFont = wb.createFont();
        poDateFont.setFontHeightInPoints((short) 10);

        //文字靠左样式
        HSSFCellStyle leftStyle = wb.createCellStyle();
        leftStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        leftStyle.setFont(poDateFont);
        leftStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        leftStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        leftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        leftStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //header
        HSSFRow headerRow = sheet.createRow(rowNum);
        int hCellNum = 0;
        for (String headerCol : header) {
            HSSFCell cell = headerRow.createCell(hCellNum);
            cell.setCellValue(headerCol);
            cell.setCellStyle(leftStyle);
            hCellNum++;
        }

        for (Map<String, String> map : list) {
            rowNum++;
            HSSFRow proRow = sheet.createRow(rowNum);
            int cellNum = 0;
            for (String headerCol : header) {
                HSSFCell cell = proRow.createCell(cellNum);
                cell.setCellValue(map.get(headerCol));
                cell.setCellStyle(leftStyle);
                cellNum++;
            }
        }

        sheet.setRowBreak(rowNum);
        return rowNum;
    }

    public void export(String fileName) {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);//把Workbook对象输出到文件workbook.xls中
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (IOException e) {
            }
        }
    }
}