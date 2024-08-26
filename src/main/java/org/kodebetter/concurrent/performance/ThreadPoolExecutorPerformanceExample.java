package org.kodebetter.concurrent.performance;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorPerformanceExample {

    public static void main(String[] args) {
        PerformanceMonitor monitor = new PerformanceMonitor();
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)  // Switch to HTTP/1.1
                .build();
        int numTasks = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(12);

        Runnable task = () -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://jsonplaceholder.typicode.com/posts/" + ThreadLocalRandom.current().nextInt(2, 100)))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                // Process the response (for demonstration purposes, we'll just print the status code)
                System.out.println("Response code: " + response.statusCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        ExecutorService exec = Executors.newFixedThreadPool(10);
        for (int i = 0; i < numTasks; i++) {
            exec.submit(task);
        }
        exec.shutdown();
        try {
            exec.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

