package ru.practicum.hit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/hit")
@RequiredArgsConstructor
public class HitController {

    private final HitService service;
    private final HitMapper mapper;

    @PostMapping
    public ResponseEntity<HitDto> hit(@Valid @RequestBody CreateHitRequest request) {
        Hit hit = service.save(request);
        return new ResponseEntity<>(mapper.toDto(hit), HttpStatus.CREATED);
    }
}
