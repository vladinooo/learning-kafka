package com.hartmann.learningkafka.tutorial1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoWithCallback {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);

        final String bootstrapServers = "127.0.0.1:9092";

        // Create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i < 10; i++) {
            // Create Producer Record
            ProducerRecord<String, String> record = new ProducerRecord<>("first_topic", "Hello World " + i);

            // Send data - asynchronous
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    // executes every time a record is successfully sent or an exception is thrown
                    if (e == null) {
                        // record was successfully received
                        logger.info("Received new metadata. \n" +
                                "Topic: " + recordMetadata.topic() + "\n" +
                                "Partition: " + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "Rimestamp: " + recordMetadata.timestamp());
                    } else {
                        logger.error("Error while producing: ", e);
                    }
                }
            });
        }

        // Flush data
        producer.flush();

        // or Flush and Close the producer
        producer.close();
    }
}
