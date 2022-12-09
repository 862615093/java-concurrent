package com.ww.concurrent.day05;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import static cn.hutool.core.thread.ThreadUtil.sleep;

/**
 * 读写锁
 */
public class ReentrantReadWriteLockDemo {
    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();
        //测试读读并发
//        new Thread(() -> {
//            Object read = dataContainer.read();
//        }, "t1").start();
//
//        new Thread(() -> {
//            Object read = dataContainer.read();
//        }, "t2").start();

        //测试读写并发


        new Thread(() -> {
            dataContainer.write();
        }, "t2").start();

        sleep(100);
        new Thread(() -> {
            Object read = dataContainer.read();
        }, "t1").start();

//        new Thread(() -> {
//            dataContainer.write();
//        }, "t3").start();
    }
}

@Slf4j
class DataContainer {
    private Object data;

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.ReadLock r = readWriteLock.readLock();

    private ReentrantReadWriteLock.WriteLock w = readWriteLock.writeLock();

    public Object read() {
        log.info("尝试获取读锁...");
        r.lock();
        try {
            log.info("读取");
            sleep(2000);
            return data;
        } finally {
            log.info("释放读锁...");
            r.unlock();
        }
    }

    public void write() {
        log.info("尝试获取写锁...");
        w.lock();
        try {
            log.info("写入");
            sleep(2000);
        } finally {
            log.info("释放写锁...");
            w.unlock();
        }
    }
}
