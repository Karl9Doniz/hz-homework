package com.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class OptimisticLockClient {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("dev");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        System.out.println("Connected to Hazelcast cluster");

        IMap<String, Integer> map = client.getMap("concurrent-map-optimistic");

        map.putIfAbsent("key", 0);

        long startTime = System.currentTimeMillis();

        for (int k = 0; k < 10_000; k++) {
            boolean updated = false;
            while (!updated) {
                Integer oldValue = map.get("key");
                Integer newValue = oldValue + 1;

                updated = map.replace("key", oldValue, newValue);

                // If replace fails, it means someone else modified the value,
                // so we'll retry
            }

            if (k % 1000 == 0) {
                System.out.println("Completed " + k + " iterations");
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("All iterations completed");
        System.out.println("Final value: " + map.get("key"));
        System.out.println("Time taken: " + duration + " ms");

        client.shutdown();
    }
}