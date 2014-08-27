package com.gbax

import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by abayanov
 * Date: 27.08.14
 */
class App {
    public static void main(String[] args) {

        println "run"
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 200; i++) {
            service.submit(new Runnable() {
                public void run() {
                    final units = new Units();
                    countDownLatch.await()
                    units.doSomthing()
                }
            });
        }
        countDownLatch.countDown()
    }
}
