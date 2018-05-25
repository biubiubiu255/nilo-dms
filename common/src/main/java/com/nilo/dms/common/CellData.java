/**
 * Kilimall Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.nilo.dms.common;

/**
 * 单元格数据
 * @author Ronny.zeng
 * @version $Id: CellData.java, v 0.1 2016年9月2日 下午4:42:35 Exp $
 */
public class CellData {

    /** 单元格值 */
    private String value;

    /** 所在列 */
    private long   col;

    /** 所在行 */
    private long   row;

    private String colName;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getCol() {
        return col;
    }

    public void setCol(long col) {
        this.col = col;
    }

    public long getRow() {
        return row;
    }

    public void setRow(long row) {
        this.row = row;
    }

}
