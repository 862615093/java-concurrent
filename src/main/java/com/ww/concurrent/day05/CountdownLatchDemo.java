package com.ww.concurrent.day05;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static cn.hutool.core.thread.ThreadUtil.sleep;

/**
 * 用来进行线程同步协作，等待所有线程完成倒计时。
 * 其中构造参数用来初始化等待计数值，await() 用来等待计数归零，countDown() 用来让计数减一
 */
@Slf4j
public class CountdownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        new Thread(() -> {
            log.info("t1 begin...");
            sleep(1000);
            latch.countDown();
            log.info("t1 -1...");
        }, "t1").start();

        new Thread(() -> {
            log.info("t1 begin...");
            sleep(2000);
            int i = 1 / 0;
            latch.countDown();
            log.info("t2 -1...");
        }, "t2").start();

        new Thread(() -> {
            log.info("t3 begin...");
            sleep(3000);
            latch.countDown();
            log.info("t3 -1...");
        }, "t3").start();

        log.info("main waiting...");
        latch.await();
//        latch.await(8, TimeUnit.SECONDS);//等待计数归0,并设置超时
        log.info("end...");
    }
}

@Slf4j
class CountdownLatchDemo1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        log.info("begin...");
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(4);
        Future<Map<String, Object>> f1 = service.submit(() -> {
            Map<String, Object> response = restTemplate.getForObject("http://localhost:8080/order/{1}", Map.class, 1);
            return response;
        });
        Future<Map<String, Object>> f2 = service.submit(() -> {
            Map<String, Object> response1 = restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 1);
            return response1;
        });
        Future<Map<String, Object>> f3 = service.submit(() -> {
            Map<String, Object> response1 = restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 2);
            return response1;
        });
        Future<Map<String, Object>> f4 = service.submit(() -> {
            Map<String, Object> response3 = restTemplate.getForObject("http://localhost:8080/logistics/{1}", Map.class, 1);
            return response3;
        });

        log.info("{}", f1.get());
        log.info("{}", f2.get());
        log.info("{}", f3.get());
        log.info("{}", f4.get());
        log.debug("执行完毕");
    }

}


@RestController
class TestCountDownlatchController {

    @GetMapping("/order/{id}")
    public Map<String, Object> order(@PathVariable int id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("total", "2300.00");
        sleep(2000);
        return map;
    }

    @GetMapping("/product/{id}")
    public Map<String, Object> product(@PathVariable int id) {
        HashMap<String, Object> map = new HashMap<>();
        if (id == 1) {
            map.put("name", "小爱音箱");
            map.put("price", 300);
        } else if (id == 2) {
            map.put("name", "小米手机");
            map.put("price", 2000);
        }
        map.put("id", id);
        sleep(1000);
        return map;
    }

    @GetMapping("/logistics/{id}")
    public Map<String, Object> logistics(@PathVariable int id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", "中通快递");
        sleep(2500);
        return map;
    }
}
