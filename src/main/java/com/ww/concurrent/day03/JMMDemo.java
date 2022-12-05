package com.ww.concurrent.day03;

import lombok.extern.slf4j.Slf4j;

/**
 * 'JMM' 体现在以下几个方面 :
 * 可见性 - 保证指令不会受 cpu 缓存的影响
 * 原子性 - 保证指令不会受到线程上下文切换的影响
 * 有序性 - 保证指令不会受 cpu 指令并行优化的影响
 */
@Slf4j
public class JMMDemo {
    static /*volatile*/ boolean run = true;

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            while(run){ //'while' 循环在字段上自旋
                // 如果死循环中加入 System.out.println() 会发现即使不加 volatile 修饰符，线程 t 也能正确看到对 run 变量的修改了，想一想为什么？
            }
        }, "不死线程...");
        t.start();
        Thread.sleep(1000);
        log.info("运行结束");
        run = false; // 线程t不会如预想的停下来,可以用jconsole试试
    }

}
