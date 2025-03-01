package ru.practicum.participation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.participation.ParticipationStatus;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {
    private Long id;

    private Long event;

    private Long requester;

    private ParticipationStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}