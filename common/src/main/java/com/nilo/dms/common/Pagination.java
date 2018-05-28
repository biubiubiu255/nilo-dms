/*
 * Kilimall Inc.
 * Copyright (c) 2016. All Rights Reserved.
 */

package com.nilo.dms.common;

import org.apache.ibatis.session.RowBounds;

/**
 * Mybatis分页参数及查询结果封装. 注意所有序号从1开始.
 *
 * @author StarZou, Spance
 * @since 2014年5月18日 下午1:34:32
 **/
public class Pagination extends RowBounds {

    private final static int DEFAULT_SIZE = 10;

    /**
     * 偏移量 : 第一条数据在表中的位置
     */
    protected int offset;

    /**
     * 限定数 : 每页的数量
     */
    protected int limit;

    /**
     * 总条数
     */
    protected long totalCount;


    public Pagination() {
        limit = DEFAULT_SIZE;
    }

    public Pagination(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从0开始.
     */
    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    /**
     * 取得总记录数, 默认值为-1.
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总记录数.
     */
    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
    }
}
