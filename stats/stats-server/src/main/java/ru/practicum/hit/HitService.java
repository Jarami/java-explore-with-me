package ru.practicum.hit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HitService {
    private final HitRepo repo;
    private final HitMapper mapper;

    public Hit save(CreateHitRequest request) {
        return repo.save(mapper.toEntity(request));
    }
}
