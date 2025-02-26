package ru.practicum.client;

import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.event.Event;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class EventStatClient extends EntityStatClient<Event>{

    public EventStatClient(StatClient statClient) {
        super(statClient);
    }

    @Override
    public void setHits(List<Event> events) {
        LocalDateTime start = getStart(events);
        LocalDateTime end = LocalDateTime.now();

        Map<String, Long> hits = getHits(start, end, events);

        events.forEach(event -> {
            String uri = getUri(event);
            Long hit = hits.getOrDefault(uri, 0L);
            event.setViews(hit);
        });
    }

    public void setHits(Event event) {
        setHits(List.of(event));
    }

    @Override
    protected LocalDateTime getStart(List<Event> events) {
        return Collections.min(events.stream().map(Event::getCreatedOn).toList());
    }

    @Override
    protected String getUri(Event event) {
        return "/events/" + event.getId();
    }
}
