package ru.practicum.hit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HitService {
    private final HitRepo repo;
    private final HitMapper mapper;

    public Hit save(CreateHitRequest request) {
        return repo.save(mapper.toEntity(request));
    }

    public List<Stat> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return repo.getStatsWithUniqueIp(start, end);
            }
            return repo.getStats(start, end);

        } else {
            if (unique) {
                return repo.getStatsByUrisWithUniqueIp(start, end, uris);
            }
            return repo.getStatsByUris(start, end, uris);
        }
    }
}
