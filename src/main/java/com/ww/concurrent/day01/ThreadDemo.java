package com.ww.concurrent.day01;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class ThreadDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 创建任务对象
        FutureTask<Integer> task3 = new FutureTask<>(() -> {
            System.out.println(new Date() + "hello");
            Thread.sleep(1000);
            return 100;
        });

        // 参数1 是任务对象; 参数2 是线程名字，推荐
        new Thread(task3, "t3").start();

        // 主线程阻塞，同步等待 task 执行完毕的结果
        Integer result = task3.get();
        System.out.println(new Date() + "t3线程执行，结果是=" +  result);


        System.out.println(new Date() + "main线程执行完了....");

    }
}
