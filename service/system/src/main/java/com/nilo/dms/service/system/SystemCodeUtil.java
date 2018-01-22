package com.nilo.dms.service.system;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.common.utils.WebUtil;
import com.nilo.dms.service.system.model.Dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2017/10/27.
 */
public class SystemCodeUtil {

    public static List getSystemCodeList(String merchantId,String type){
        Set<String> typeArray = RedisUtil.smembers("System_code"+merchantId+type);

        List<Object> list = new ArrayList<>();
        for(String s : typeArray){
            Dictionary d = JSON.parseObject(s,Dictionary.class);
            list.add(d);
        }
        return list;
    }
    public static String getCodeVal(String merchantId,String type,String code){
        Set<String> typeArray = RedisUtil.smembers("System_code"+merchantId+type);
        for(String s : typeArray){
            Dictionary d = JSON.parseObject(s,Dictionary.class);
            if(StringUtil.equals(code,d.getCode())){
                return d.getValue();
            }
        }
        return "";
    }

}
