package com.github.jms.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.UUID;

//https://docs.confluent.io/current/clients/index.html
public class KafkaSender {

    public static void main(String... args) throws UnknownHostException {
        Properties config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("bootstrap.servers", "localhost:9092");
        config.put("transactional.id", UUID.randomUUID().toString());
        config.put("acks", "all");
        Producer<String, String> producer = new KafkaProducer<>(config, new StringSerializer(), new StringSerializer());
        producer.initTransactions();
        try {
            producer.beginTransaction();
            for (int i = 0; i < 100; i++)
                producer.send(new ProducerRecord<>("test", Integer.toString(i), Integer.toString(i)));
            producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            // We can't recover from these exceptions, so our only option is to close the producer and exit.
            producer.close();
        } catch (KafkaException e) {
            // For all other exceptions, just abort the transaction and try again.
            producer.abortTransaction();
        }
        producer.close();
    }

}
