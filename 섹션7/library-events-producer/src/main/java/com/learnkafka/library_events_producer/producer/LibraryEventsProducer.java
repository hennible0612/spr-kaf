package com.learnkafka.library_events_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.library_events_producer.domain.LibraryEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LibraryEventsProducer { // Producer

    @Value("${spring.kafka.topic}")
    private String topic;

    private final KafkaTemplate<Integer, String> kafkaTemplate; // final 키워드 추가
    private final ObjectMapper objectMapper;

    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key = libraryEvent.libraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        // 비동기 처리
        // 1. blocking call - get metadata bout the kafka cluster, 최초한번만 실행
            // 실패하면 에러 던짐
        // 2. Send message happens - Returns a ComletableFuture
        var completableFuture = kafkaTemplate.send(topic, key, value);

        return completableFuture
            .whenComplete((sendResult, throwable) -> {
                if (throwable != null) {
                    handleFailure(key, value, throwable);
                } else {
                    handleSuccess(key, value, sendResult);

                }
            });
    }

    //동기
    public SendResult<Integer, String> sendLibraryEvent_Approach2(LibraryEvent libraryEvent)
        throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {

        Integer key = libraryEvent.libraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

//        var sendResult = kafkaTemplate.send(topic, key, value).get();
        var sendResult = kafkaTemplate.send(topic, key, value)
            .get(3, TimeUnit.SECONDS);


        handleSuccess(key, value, sendResult);

        return sendResult;
    }

    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent_approach3(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);

        var producerRecord = buildProducerRecord(key,value);
        // 1. blocking call - get metadata about the kafka cluster
        //2.Send message happens - Returns a CompletableFuture
        var completableFuture = kafkaTemplate.send(producerRecord);

        return completableFuture
            .whenComplete((sendResult, throwable) -> {
                if(throwable!=null){
                    handleFailure(key, value, throwable);
                }else{
                    handleSuccess(key, value, sendResult);
                }
            });

    }
    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value) {

        List<Header> recordHeaders = List.of(new RecordHeader("event-source","scanner".getBytes()));

        return new ProducerRecord<>(topic,null, key, value, recordHeaders);
    }


    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {


        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error Sending the Message and the exception is {}", ex.getMessage());
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }
}
