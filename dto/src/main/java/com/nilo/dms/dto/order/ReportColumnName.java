package com.nilo.dms.dto.order;

import java.lang.annotation.*;

/**
 * Created by wenzhuo-company on 2018/3/28.
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface ReportColumnName {
    String alias();
}
