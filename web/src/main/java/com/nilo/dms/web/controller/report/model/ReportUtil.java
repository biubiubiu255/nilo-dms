package com.nilo.dms.web.controller.report.model;

import com.nilo.dms.dto.order.ReportColumnName;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wenzhuo-company on 2018/3/28.
 */
public class ReportUtil {

    public static String queryColumnToStr(LinkedHashMap<String, String> map){

        String point = "";
        StringBuilder str = new StringBuilder();

        for (Map.Entry<String, String> entry : map.entrySet()){
            point = ParsefieldStr(entry.getValue());
            if(point!=null && !point.equals("")){
                str.append(entry.getKey()+": "+point+" | ");
            }

        }
        return str.toString().trim();
    }

    public static String ParsefieldStr(String str){
        if (str.length()==10 && isNumeric(str)){
            str = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(Long.valueOf(str)*1000));
        }
        return str.trim();
    }


    public static boolean isNumeric(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
                return false;
        }
        return true;
    }

    public static boolean isNumerics(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static String queryDOtoStr(Object object){
        //System.out.println("本次测试 = 进入测试了");
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        Class clazz    = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields){
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), clazz);
                Method getMethod = propertyDescriptor.getReadMethod();
                if (getMethod.invoke(object)!=null){
                    boolean fieldHasAnno = field.isAnnotationPresent(ReportColumnName.class);
                    String alias = "";
                    if(fieldHasAnno){
                        ReportColumnName fieldAnno = field.getAnnotation(ReportColumnName.class);
                        alias = fieldAnno.alias();
                    }else {
                        alias = field.getName();
                    }
                    if (map.get(alias)!=null) {
                        map.put(alias, map.get(alias) + "~" + ParsefieldStr(getMethod.invoke(object).toString()));
                    }else {
                        map.put(alias, ParsefieldStr(getMethod.invoke(object).toString()));
                    }

                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return queryColumnToStr(map);
    }
}
