package com.github.wojciechfiszer.kafkastreamsjava11.filterusersbycolor;

import com.github.wojciechfiszer.kafkastreamsjava11.filterusersbycolor.avro.User;
import com.github.wojciechfiszer.kafkastreamsjava11.filterusersbycolor.avro.UserKey;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;
import java.util.Properties;

public class InsertUsers {
    public static void main(String[] args) {
        final var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, SpecificAvroSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SpecificAvroSerializer.class);
        properties.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        Producer<UserKey, User> producer = new KafkaProducer<>(properties);
        final var names = List.of("John", "Ann", "Mark", "Stephen", "Julia");
        final var colors = List.of("red", "blue", "green", "yellow");

        for (int i = 0; i < 10000; i++) {
            final var key = UserKey.newBuilder()
                    .setId(i)
                    .build();
            final var value = User.newBuilder()
                    .setName(names.get(i % names.size()))
                    .setFavoriteNumber(i)
                    .setFavoriteColor(colors.get(i % colors.size()))
                    .build();
            producer.send(new ProducerRecord<>("users", key, value));
        }
        producer.close();
    }
}
