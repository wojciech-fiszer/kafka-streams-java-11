package com.github.wojciechfiszer.kafkastreamsjava11.filterusersbycolor;

import com.github.wojciechfiszer.kafkastreamsjava11.filterusersbycolor.avro.User;
import com.github.wojciechfiszer.kafkastreamsjava11.filterusersbycolor.avro.UserKey;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        final var properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "filter-users-by-color");
        properties.put(StreamsConfig.CLIENT_ID_CONFIG, "filter-users-by-color-client");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        properties.put(StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG), "earliest");
        properties.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        final var streamsBuilder = new StreamsBuilder();
        KStream<UserKey, User> users = streamsBuilder.stream("users");
        KStream<UserKey, User> filteredUsers = users.filter((k, v) -> "red".equalsIgnoreCase(v.getFavoriteColor()));
        filteredUsers.to("users-who-like-red");

        final var kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);
        kafkaStreams.cleanUp(); // do not do this on production
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
