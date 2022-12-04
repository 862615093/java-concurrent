package com.ww.concurrent.day02;

import java.util.concurrent.SynchronousQueue;

/**
 * 线程并发产生的问题demo
 */
public class ThreadLocalDemo {

    /**
     * 设置资源共享变量
     */
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public static void main(String[] args) {
        ThreadLocalDemo threadLocalDemo = new ThreadLocalDemo();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                threadLocalDemo.setContent(Thread.currentThread().getName() + "的数据");
                System.out.println(Thread.currentThread().getName() + "----------->" + threadLocalDemo.getContent());
            }).start();
        }
    }
}

/**
 * 使用 ThreadLocal 高并发下，线程隔离
 */
class ThreadLocalDemo1 {

    ThreadLocal<String> t1 = new ThreadLocal<>();

    private String Content;

    public String getContent() {
        //获取当前线程绑定的变量
        return t1.get();
    }

    public void setContent(String content) {
        //变量content绑定到当前线程
        t1.set(content);
    }

    public static void main(String[] args) {
        ThreadLocalDemo1 threadLocalDemo = new ThreadLocalDemo1();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                threadLocalDemo.setContent(Thread.currentThread().getName() + "的数据");
                System.out.println(Thread.currentThread().getName() + "----------->" + threadLocalDemo.getContent());
            }).start();
        }
    }
}


/**
 * 使用 synchronized 高并发下，线程隔离
 */
class ThreadLocalDemo2 {


    /**
     * 设置资源共享变量
     */
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public static void main(String[] args) {
        ThreadLocalDemo2 threadLocalDemo = new ThreadLocalDemo2();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (ThreadLocalDemo2.class) {
                    threadLocalDemo.setContent(Thread.currentThread().getName() + "的数据");
                    System.out.println(Thread.currentThread().getName() + "----------->" + threadLocalDemo.getContent());
                }
            }).start();
        }
    }
}