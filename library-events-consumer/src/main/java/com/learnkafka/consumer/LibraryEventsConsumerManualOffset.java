// package com.learnkafka.consumer;
//
// import org.apache.kafka.clients.consumer.ConsumerRecord;
// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.kafka.listener.AcknowledgingMessageListener;
// import org.springframework.kafka.support.Acknowledgment;
// import org.springframework.stereotype.Component;
//
// import com.fasterxml.jackson.core.JsonProcessingException;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Component
// @Slf4j
// // @KafkaListener
// public class LibraryEventsConsumerManualOffset implements AcknowledgingMessageListener<Integer, String> {
//
//
// 	@KafkaListener(
// 		topics = {"library-events"}
// 		, groupId = "library-events-listener-group")
// 	public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
// 		log.info("ConsumerRecord in Manual Offset Consumer: {} ", consumerRecord );
// 		acknowledgment.acknowledge(); //해당 레코드를 봤다 표시
// 	}
// }
