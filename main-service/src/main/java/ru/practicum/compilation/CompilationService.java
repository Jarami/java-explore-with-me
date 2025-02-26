package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationService {

    private final EventService eventService;
    private final CompilationRepo repo;

    public List<Compilation> getCompilations(boolean pinned, long from, long size) {

        log.info("get compilations for pinned {}, from {}, size {}", pinned, from, size);

        if (from < 0) {
            throw new BadRequestException("from должен быть неотрицательным");
        }

        if (size < 0) {
            throw new BadRequestException("size должен быть неотрицательным");
        }

        return repo.findAllByPinnedWithLimitAndOffset(pinned, from, size);
    }

    public Compilation createCompilation(NewCompilationDto dto) {

        log.info("create compilation {}", dto);

        List<Event> events = dto.getEvents().isEmpty() ? List.of() : eventService.getAllFullById(dto.getEvents());
        checkEvents(events, dto.getEvents());

        Compilation compilation = Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .events(events)
                .build();

        return repo.save(compilation);
    }

    public void deleteCompilationById(Long id) {
        Compilation compilation = getById(id);
        repo.delete(compilation);
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

    public Compilation getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка с id = " + id + " не найдена"));
    }
}
