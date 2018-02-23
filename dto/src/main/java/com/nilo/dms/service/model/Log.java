package com.nilo.dms.service.model;

import lombok.Data;

/**
 * Created by ronny on 2017/9/18.
 */
@Data
public class Log {

    public final static String LOG_CONTENT_ATTRIBUTE_NAME="log_content_attr";

    private String id;

    private String merchantId;

    private String operation;

    private String operator;

    private String content;

    private String parameter;

    private String exception;

    private String ip;

    private Long createdTime;
}
