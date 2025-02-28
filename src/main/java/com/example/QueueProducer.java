package com.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.collection.IQueue;

import java.util.concurrent.TimeUnit;

public class QueueProducer {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("dev");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        System.out.println("Producer connected to Hazelcast cluster");

        IQueue<Integer> queue = client.getQueue("bounded-queue");

        int i = 1;
        while (i <= 100) {
            try {
                System.out.println("Attempting to add: " + i);

                boolean offered = queue.offer(i, 5, TimeUnit.SECONDS);
                if (offered) {
                    System.out.println("Added item: " + i);
                    i++;
                } else {
                    System.out.println("Queue full! Retrying item: " + i);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer interrupted: " + e.getMessage());
            }
        }

        System.out.println("All items added. Waiting for consumers...");
        while (!queue.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        client.shutdown();
        System.out.println("Producer disconnected");
    }
}
