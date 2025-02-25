package ru.practicum.event;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.category.CategoryMapper;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.user.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UserMapper.class, CategoryMapper.class})
public interface EventMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    Event toEvent(NewEventDto dto);

    Location toLocation(LocationDto dto);

    LocationDto toLocationDto(Location location);

    EventFullDto toFullDto(Event event);

    List<EventFullDto> toFullDto(List<Event> events);

    EventShortDto toShortDto(Event event);

    List<EventShortDto> toShortDto(List<Event> events);
}
