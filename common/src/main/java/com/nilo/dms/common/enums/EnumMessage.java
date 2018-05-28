package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/12/21.
 */
public interface EnumMessage {

    Map<String, Map> enumMaps = new HashMap<>();

    Object getCode();

    String getDesc();
}
