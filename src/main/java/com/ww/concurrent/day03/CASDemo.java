package com.ww.concurrent.day03;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * cas - AtomicReference
 */
@Slf4j
public class CASDemo {
    public static void main(String[] args) {
        //线程不安全测试
        Account.demo(new AccountUnsafeImpl(new BigDecimal(10000)));

        //cas锁 线程安全测试
//        Account.demo(new AccountSafeImpl(new AtomicReference<BigDecimal>(new BigDecimal(10000))));

    }
}

/**
 * 模拟存取款接口
 */
interface Account {

    //取款
    BigDecimal getBalance();

    //存款
    void withDraw(BigDecimal bigDecimal);

    //模拟1000个线程取款，每个线程取10元
    static void demo(Account account) {
        ArrayList<Thread> threads = new ArrayList<>();
        long s = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            threads.add(new Thread(() -> {
                account.withDraw(BigDecimal.TEN);
            }));
        }
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.getStackTrace();
            }
        });
        long e = System.currentTimeMillis();
        System.out.println("余额=" + account.getBalance() + "--->" + "消耗时间=" + (e - s));
    }
}

/**
 * 模拟不安全取款
 */
class AccountUnsafeImpl implements Account {

    /**
     * 多线程共享资源
     */
    private BigDecimal balance;

    public AccountUnsafeImpl(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public void withDraw(BigDecimal bigDecimal) {
        //这里导致并发不完全
        balance = balance.subtract(bigDecimal);
    }
}


/**
 * 模拟安全取款
 */
class AccountSafeImpl implements Account {

    /**
     * 多线程共享资源
     */
    private AtomicReference<BigDecimal> balance;

    public AccountSafeImpl(AtomicReference<BigDecimal> balance) {
        this.balance = balance;
    }

    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withDraw(BigDecimal bigDecimal) {
        while (true) {
            BigDecimal prev = getBalance();
            BigDecimal next = prev.subtract(bigDecimal);
             /*
             compareAndSet 正是做这个检查，在 set 前，先比较 prev 与当前值
             - 不一致了，next 作废，返回 false 表示失败
             比如，别的线程已经做了减法，当前值已经被减成了 990
             那么本线程的这次 990 就作废了，进入 while 下次循环重试
             - 一致，以 next 设置为新值，返回 true 表示成功
            */
            if (balance.compareAndSet(prev, next)) {
                break;
            }
        }
    }
}
