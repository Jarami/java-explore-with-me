package ru.practicum.event.dto;

import java.time.LocalDateTime;

public interface UpdateEventRequest {
    String getTitle();

    String getAnnotation();

    Long getCategory();

    String getDescription();

    LocalDateTime getEventDate();

    LocationDto getLocation();

    Boolean getPaid();

    Integer getParticipantLimit();

    Boolean getRequestModeration();

    String getStateAction();
}
