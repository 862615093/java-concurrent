package com.ww.concurrent.day04;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 不可变类设计
 */
@Slf4j
public class ImmutableDemo {
    public static void main(String[] args) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                try {
//                    //此方法追源码就能看出来
//                    log.info("{}", sdf.parse("1951-04-21"));
//                } catch (Exception e) {
//                    log.error("{}", e);
//                }
//            }).start();
//        }
//        synchronizedTest();
        dateTimeFormatterTest();
    }

    //synchronized 对象锁
    public static void synchronizedTest() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    //此方法追源码就能看出来
                    synchronized (sdf) {
                        log.info("{}", sdf.parse("1951-04-21"));
                    }
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }).start();
        }
    }

    //不可变方式 ： 如果一个对象在不能够修改其内部状态（属性），那么它就是线程安全的，因为不存在并发修改啊
    public static void dateTimeFormatterTest() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                LocalDate date = dtf.parse("2018-10-01", LocalDate::from);
                log.info("{}", date);
            }).start();
        }
    }


}
