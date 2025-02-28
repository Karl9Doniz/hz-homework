package com.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.collection.IQueue;

import java.util.concurrent.TimeUnit;

public class QueueConsumer {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("dev");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        System.out.println("Consumer connected to Hazelcast cluster");

        IQueue<Integer> queue = client.getQueue("bounded-queue");

        int itemsProcessed = 0;
        try {
            while (true) {
                Integer item = queue.poll(10, TimeUnit.SECONDS);

                if (item != null) {
                    itemsProcessed++;
                    System.out.println("Consumer processed item: " + item + " (Total: " + itemsProcessed + ")");

                    Thread.sleep(200);
                } else {
                    System.out.println("No items in queue for 10 seconds, checking again...");

                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.shutdown();
            System.out.println("Consumer disconnected after processing " + itemsProcessed + " items");
        }
    }
}