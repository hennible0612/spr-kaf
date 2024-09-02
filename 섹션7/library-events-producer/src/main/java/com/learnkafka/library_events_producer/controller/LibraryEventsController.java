package com.learnkafka.library_events_producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.library_events_producer.domain.LibraryEvent;
import com.learnkafka.library_events_producer.producer.LibraryEventsProducer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LibraryEventsController {

    private final LibraryEventsProducer libraryEventsProducer;

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent)
        throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        log.info("Posting library event: {}", libraryEvent);

//        libraryEventsProducer.sendLibraryEvent(libraryEvent);
        libraryEventsProducer.sendLibraryEvent_Approach2(libraryEvent);

//        libraryEventsProducer.sendLibraryEvent_approach3(libraryEvent);
        log.info("libraryEvent를 보낸 후");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
