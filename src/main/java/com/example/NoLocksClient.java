package com.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class NoLocksClient {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("dev");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        System.out.println("Connected to Hazelcast cluster");

        IMap<String, Integer> map = client.getMap("concurrent-map");

        map.putIfAbsent("key", 0);

        long startTime = System.currentTimeMillis();

        for (int k = 0; k < 10_000; k++) {
            var value = map.get("key");
            value++;
            map.put("key", value);

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