package com.ww.concurrent.day03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *  cas 原子数组
 */
public class CASDemo1 {
    public static void main(String[] args) {
//        demo(
//                ()->new int[10], //多线程共享的资源
//                (array)->array.length,
//                (array, index) -> array[index]++,
//                array-> System.out.println(Arrays.toString(array))
//        );

        demo(
                ()-> new AtomicIntegerArray(10),
                (array) -> array.length(),
                (array, index) -> array.getAndIncrement(index),
                array -> System.out.println(array)
        );
    }


    /**
     参数1，提供数组、可以是线程不安全数组或线程安全数组
     参数2，获取数组长度的方法
     参数3，自增方法，回传 array, index
     参数4，打印数组的方法
     */
    // supplier 提供者 无中生有  ()->结果
    // function 函数   一个参数一个结果   (参数)->结果  ,  BiFunction (参数1,参数2)->结果
    // consumer 消费者 一个参数没结果  (参数)->void,      BiConsumer (参数1,参数2)->
    private static <T> void demo(Supplier<T> supplier, Function<T, Integer> function, BiConsumer<T, Integer> biConsumer, Consumer<T> consumer) {

        //获取数组
        T array = supplier.get();
        //获取数组长度
        Integer length = function.apply(array);

        ArrayList<Thread> ts = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            // 每个线程对数组作 10000 次操作
            ts.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    biConsumer.accept(array, j % length);
                }
            }));
        }

        ts.forEach(Thread::start); // 启动所有线程
        ts.forEach(t -> {
            try {
                t.join(); //等所有线程结束
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        consumer.accept(array);
    }



}
