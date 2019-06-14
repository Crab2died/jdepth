package com.github.jms.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaReceiver {

    public static void main(String... args) throws UnknownHostException {
        Properties config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("group.id", "foo");
        config.put("bootstrap.servers", "localhost:9092");
        Consumer<String, String> consumer = new KafkaConsumer<>(config, new StringDeserializer(), new StringDeserializer());
        for (; ; ) {
            try {
                consumer.subscribe(Arrays.asList("test"));
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
                records.records("test").forEach(x -> {
                    System.out.println(x.value());
                });
            } catch (WakeupException e) {
                // Ignore exception if closing
            }
        }
    }

}
