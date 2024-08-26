package org.kodebetter.concurrent.performance;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class VirtualThreadsPerformanceExample {
    public static void main(String[] args) {
        PerformanceMonitor monitor = new PerformanceMonitor();
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)  // Switch to HTTP/1.1
                .build();
        int numTasks = 10000;
        CountDownLatch latch = new CountDownLatch(numTasks);
        Runnable task = () -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://jsonplaceholder.typicode.com/posts/"+ ThreadLocalRandom.current().nextInt(2, 100)))
                        .build();
                System.out.println(request.uri().toURL());
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                // Process the response (for demonstration purposes, we'll just print the status code)
                System.out.println("Response code: " + response.statusCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                latch.countDown();
            }

        };

        for (int i = 0; i < numTasks; i++) {
            Thread.startVirtualThread(task);
        }
        try {
            latch.await();  // Wait for all tasks to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
