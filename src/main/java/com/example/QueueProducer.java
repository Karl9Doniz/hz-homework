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

        try {
            for (int i = 1; i <= 100; i++) {
                System.out.println("Attempting to add: " + i);

                //Option 1: Using the offer method with TimeUnit
                boolean offered = queue.offer(i, 5, TimeUnit.SECONDS);

                // Option 2: Regular offer without timeout (for testing full queue behavior)
                // boolean offered = queue.offer(i);

                // Option 3: Blocking put (will wait indefinitely if queue is full)
                //queue.put(i);
                //boolean offered = true;

                if (offered) {
                    System.out.println("Added item: " + i);
                } else {
                    System.out.println("Failed to add item: " + i + " (Queue full)");
                }

            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.shutdown();
            System.out.println("Producer disconnected");
        }
    }
}