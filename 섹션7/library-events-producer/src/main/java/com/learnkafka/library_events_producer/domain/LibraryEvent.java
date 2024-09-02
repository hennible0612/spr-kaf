package com.learnkafka.library_events_producer.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.awt.print.Book;

public record LibraryEvent(
    Integer libraryEventId,
    LibraryEventType libraryEventType,
    @NotNull
    @Valid
    Book book
) {
}