package com.ww.concurrent;

import com.ww.concurrent.day01.AsyncTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Future;

@SpringBootTest
@Slf4j
public class AsyncTaskDemo {

    @Autowired
    private AsyncTask asyncTask;

    @Test
    public void testDealNoReturnTask(){
        asyncTask.dealNoReturnTask();
        try {
            log.info("begin to deal other Task!");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDealHaveReturnTask() throws Exception {

        Future<String> future = asyncTask.dealHaveReturnTask();
        log.info("begin to deal other Task!");
        while (true) {
            if(future.isCancelled()){
                log.info("deal async task is Cancelled");
                break;
            }
            if (future.isDone() ) {
                log.info("deal async task is Done");
                log.info("return result is " + future.get());
                break;
            }
            log.info("wait async task to end ...");
            Thread.sleep(1000);
        }
    }
}
