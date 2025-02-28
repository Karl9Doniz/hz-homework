package com.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class DistributedMapDemo {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("dev");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        System.out.println("Connected to Hazelcast cluster");

        IMap<Integer, String> map = client.getMap("distributed-map");

        for (int i = 0; i < 1000; i++) {
            map.put(i, "Value-" + i);

            if (i % 100 == 0) {
                System.out.println("Added " + i + " entries");
            }
        }

        System.out.println("Map size: " + map.size());

    }
}