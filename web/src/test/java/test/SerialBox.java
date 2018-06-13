package test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class SerialBox {

    private static AtomicLong historyNumber = new AtomicLong(0);

    private static long historySerial = 0;

    private static SerialBox serialBox = new SerialBox();

    private SerialBox(){}

    public static SerialBox getInstance(){
        return serialBox;
    }

    //底数为14位，第二个参数为附加多少位
    private static String next(int digits){

        int random = (int)((Math.random()*9+1)*Math.pow(10, digits-1));


        long now = System.currentTimeMillis() + random;

        //System.out.println("测试结果 = " + serialStr.toString().length());

        String serialStr = now + "";

        if (serialStr.toString().length()!=17){
            //System.out.println("测试结果len:"+serialStr.toString().length()+" 随机数:" + random);
        }

        if(isVaild(now)){
            return serialStr;
        }else {
            return next(digits);
        }

    }

    private synchronized static boolean isVaild(long num){
        if (num==historySerial){
            return false;
        }else {
            historySerial = num;
            return true;
        }
    }

    public static void main(String[] args) {
        long ah = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            next(1);
        }
        System.out.println(System.currentTimeMillis() - ah);
    }
}
