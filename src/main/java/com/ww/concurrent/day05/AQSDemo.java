package com.ww.concurrent.day05;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Slf4j
public class AQSDemo {
    public static void main(String[] args) {
        MyLock myLock = new MyLock();

        //第一个线程
        new Thread(() -> {
            //加锁
            myLock.lock();
            try {
                log.info("t1获取锁了...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                //释放锁
                myLock.unlock();
                log.info("t1释放锁了...");
            }
        }, "t1").start();

        //第二个线程
        new Thread(() -> {
            //加锁
            myLock.lock();
            try {
                log.info("t2获取锁了...");
            } finally {
                //释放锁
                myLock.unlock();
                log.info("t2获取锁了...");
            }
        }, "t2").start();

    }
}


/**
 * 自定义锁 （不可重入锁）
 */
class MyLock implements Lock {

    /**
     * 自定义同步器类
     */
    private MySync sync = new MySync();

    class MySync extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                //加上了锁 并设置 owner 为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        @Override//是否持有锁
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }


    @Override//加锁 不成功会进入等待队列 看源码
    public void lock() {
        sync.acquire(1);
    }

    @Override//加锁 可打断
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);

    }

    @Override//尝试加锁 (一次)
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override//尝试加锁 (带超时)
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override//解锁
    public void unlock() {
        sync.release(1);
    }

    @Override//创建条件变量
    public Condition newCondition() {
        return null;
    }
}