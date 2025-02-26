package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationService {

    private final EventService eventService;
    private final CompilationRepo repo;

    public Compilation createCompilation(NewCompilationDto dto) {

        List<Event> events = dto.getEvents().isEmpty() ? List.of() : eventService.getAllFullById(dto.getEvents());
        checkEvents(events, dto.getEvents());

        Compilation compilation = Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .events(events)
                .build();

        return repo.save(compilation);
    }

    private void checkEvents(List<Event> events, List<Long> inputEventIds) {
        if (!inputEventIds.isEmpty()) {
            List<Long> eventIds = events.stream().map(Event::getId).toList();
            List<Long> absentIds = inputEventIds.stream()
                    .filter(id -> !eventIds.contains(id)).toList();

            if (!absentIds.isEmpty()) {
                throw new NotFoundException("Можно делать подборки только из существующих событий (" + absentIds + ")");
            }
        }
    }
}
