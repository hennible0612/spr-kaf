package com.learnkafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
// @KafkaListener
public class LibraryEventsConsumer {

	@KafkaListener(
		topics = {"library-events"}
		, groupId = "library-events-listener-group")
	public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {

		log.info("ConsumerRecord : {} ", consumerRecord);

	}
}
