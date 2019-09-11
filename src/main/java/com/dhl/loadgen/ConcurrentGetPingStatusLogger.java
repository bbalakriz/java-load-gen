package com.dhl.loadgen;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentGetPingStatusLogger {
    private static String THREAD_COUNT_ENV = System.getenv("THREAD_COUNT");
    private static String TIMEOUT_ENV = System.getenv("TIMEOUT");
    private static String CONCURRENT_USERS_ENV = System.getenv("CONCURRENT_USERS");
    private static int THREAD_COUNT = 1000;
    private static int CONCURRENT_USERS = 1000;
    private static int TIMEOUT = 3000;

    public static void main(String args[]) throws Exception {

        if (null != THREAD_COUNT_ENV) {
            THREAD_COUNT = Integer.parseInt(THREAD_COUNT_ENV);
        }

        if (null != CONCURRENT_USERS_ENV) {
            CONCURRENT_USERS = Integer.parseInt(CONCURRENT_USERS_ENV);
        }

        if (null != TIMEOUT_ENV) {
            TIMEOUT = Integer.parseInt(TIMEOUT_ENV);
        }

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        String endPoint = System.getenv("ENDPOINT_URL");
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        for (int i = 0; i < CONCURRENT_USERS; i++) {

            String url = endPoint;
            Runnable worker = new MyRunnable(url);
            executor.execute(worker);
        }
        executor.shutdown();
        // Wait until all threads have finished processing
        while (!executor.isTerminated()) {

        }
        System.out.println("\nFinished all threads");
    }

    public static class MyRunnable implements Runnable {
        private final String url;

        MyRunnable(String url) {
            this.url = url;
        }

        @Override
        public void run() {

            String result = "";
            int code = 200;
            try {
                URL siteURL = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(TIMEOUT);
                connection.connect();

                code = connection.getResponseCode();
                if (code == 200) {
                    result = "-> All OK <-\t" + "Code: " + code;
                    ;
                } else {
                    result = "-> Not OK.. Check the code to see why... <-\t" + "Code: " + code;
                }
            } catch (java.net.SocketTimeoutException e) {
                result = "-> Socket timeout... <-\t" + ": " + e.getMessage();
            } catch (Exception e) {
                result = "-> Something is terribly wrong... <-\t" + "Exception: " + e.getMessage();
            }
            System.out.println(url + "\t\tStatus:" + result);

        }
    }
}