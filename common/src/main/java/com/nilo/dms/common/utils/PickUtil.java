package com.nilo.dms.common.utils;

import com.nilo.dms.common.Principal;
import org.apache.commons.collections.ListUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Alvin
 * Date: 2018/06/07 9:33
 * Just go for it and give it a try
 */
public class PickUtil {
    public PickUtil() {
    }

    public static  <T> T[] getArray(Class<T> clazz){

        T[] res =(T[]) Array.newInstance(clazz, 1);
        //通过反射在运行时构出实际类型为type[]的对象数组，避免了类型擦除，从而转换成功，无ClassCastException
        //T[] res = (T[]) new Object[1];
        res[0] = clazz.cast(5);
        return res;
    }

    public static void main(String[] args) {
        //Integer[] nums =  getArray(Integer.class);
        //System.out.println("本次测试 = " + Arrays.toString(nums));
        List<Principal> list = new ArrayList<Principal>();
        Principal principal = new Principal();
        Principal principal2 = new Principal();
        Principal principal3 = new Principal();
        principal.setUserName("aa");
        principal2.setUserName("bb");
        principal3.setUserName("cc");
        list.add(principal);
        list.add(principal2);
        list.add(principal3);

        String[] userNames = buildArray(list, "userName", String.class);
        System.out.println("本次测试 = " + Arrays.toString(userNames));
        //System.out.println("本次测试 = " + "sd");
        //System.exit(1);
        //System.out.println("本次测试 = " + Arrays.toString(userNames));

    }

    public static <T> T[] buildArray(List list, String fname, Class<T> clazz){

        //通过反射在运行时构出实际类型为type[]的对象数组，避免了类型擦除，从而转换成功，无ClassCastException
        T[] arr = (T[]) Array.newInstance(clazz, list.size());

        for (int i=0;i< list.size(); i++) {
            Field[] fs = list.get(i).getClass().getDeclaredFields();
            for (int j = 0; j < fs.length; j++) {
                Field f = fs[j];
                f.setAccessible(true); // 设置些属性是可以访问的
                try {
                    if (f.getName().endsWith(fname)) {
                        arr[i] = clazz.cast(f.get(list.get(i)));
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return arr;
    }


}
