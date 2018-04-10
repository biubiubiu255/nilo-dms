/**
 * Kilimall Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.nilo.dms.dto.util;

import java.util.List;
import java.util.Map;

/**
 * Excel数据
 * @author Ronny.zeng
 * @version $Id: ExcelData.java, v 0.1 2016年9月2日 下午4:36:55 Exp $
 */
public class ExcelData {

    /** 行数 */
    private long                        row;

    /** 表头 */
    private List<String>                header;

    /** excel数据, map-key 为行号, value为当前行对应单元格 */
    private Map<String, List<CellData>> data;

    /**
     * Getter method for property <tt>row</tt>.
     * 
     * @return property value of row
     */
    public long getRow() {
        return row;
    }

    /**
     * Setter method for property <tt>row</tt>.
     * 
     * @param row value to be assigned to property row
     */
    public void setRow(long row) {
        this.row = row;
    }

    /**
     * Getter method for property <tt>header</tt>.
     * 
     * @return property value of header
     */
    public List<String> getHeader() {
        return header;
    }

    /**
     * Setter method for property <tt>header</tt>.
     * 
     * @param header value to be assigned to property header
     */
    public void setHeader(List<String> header) {
        this.header = header;
    }

    /**
     * Getter method for property <tt>data</tt>.
     * 
     * @return property value of data
     */
    public Map<String, List<CellData>> getData() {
        return data;
    }

    /**
     * Setter method for property <tt>data</tt>.
     * 
     * @param data value to be assigned to property data
     */
    public void setData(Map<String, List<CellData>> data) {
        this.data = data;
    }

}
