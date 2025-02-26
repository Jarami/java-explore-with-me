package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import ru.practicum.StatClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class EntityStatClient<T> {

    protected final StatClient statClient;

    protected Map<String, Long> getHits(LocalDateTime start, LocalDateTime end, List<T> entities) {
        Map<String, Long> hits = new HashMap<>();

        List<String> uris = entities.stream().map(this::getUri).toList();

        statClient.getStats(start, end, uris, true)
                .forEach(stat -> hits.put(stat.getUri(), stat.getHits()));

        return hits;
    }

    public abstract void setHits(List<T> entities);

    protected abstract LocalDateTime getStart(List<T> entities);

    protected abstract String getUri(T entity);
}
