/**
 * Kilimall Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.nilo.dms.common.utils;

import com.nilo.dms.dto.util.CellData;
import com.nilo.dms.dto.util.ExcelData;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * POI读取excel
 *
 * @author Ronny.zeng
 * @version $Id: ReadExcel.java, v 0.1 2016年9月2日 下午3:59:33 Exp $
 */
public class ReadExcel {

    public static ExcelData readTable(InputStream ips, String fileName) throws Exception {

        ExcelData excelData = new ExcelData();
        Workbook wb = null;
        //判断版本
        if (fileName.indexOf("xlsx") > 0) {
            wb = new XSSFWorkbook(ips);
        } else {
            wb = new HSSFWorkbook(ips);
        }
        Sheet sheet = wb.getSheetAt(0);

        Map<String, List<CellData>> data = new LinkedHashMap<String, List<CellData>>();

        //获取表头信息
        Iterator<Row> iteRow = sheet.rowIterator();
        Row headRow = iteRow.next();
        Iterator<Cell> headCell = headRow.cellIterator();
        List<String> header = new ArrayList<String>();
        while (headCell.hasNext()) {
            Cell cell = headCell.next();
            header.add(cell.getStringCellValue());
        }
        iteRow.remove();

        int row = 1;
        while (iteRow.hasNext()) {
            row++;
            Row r = iteRow.next();
            Iterator<Cell> iteCell = r.cellIterator();
            List<CellData> rowList = new ArrayList<CellData>();
            while (iteCell.hasNext()) {
                Cell cell = iteCell.next();
                CellData cellData = new CellData();
                int col = cell.getColumnIndex();
                cellData.setCol(col + 1);
                cellData.setRow(row);
                cellData.setColName(header.get(col));
                cellData.setValue(getCellStringValue(cell));
                rowList.add(cellData);
            }
            data.put("" + row, rowList);
        }
        excelData.setData(data);
        excelData.setRow(row);
        excelData.setHeader(header);
        return excelData;

    }

    public static String getCellStringValue(Cell cell) {
        String cellValue = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING://字符串类型
                cellValue = cell.getStringCellValue();
                if (cellValue.trim().equals("") || cellValue.trim().length() <= 0)
                    cellValue = " ";
                break;
            case Cell.CELL_TYPE_NUMERIC: //数值类型
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_BLANK:
                cellValue = " ";
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                break;
            case Cell.CELL_TYPE_ERROR:
                break;
            default:
                break;
        }
        return cellValue;
    }
}