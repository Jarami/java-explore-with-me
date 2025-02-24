package ru.practicum.hit;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stats.Stat;
import ru.practicum.stats.StatDto;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HitMapper {

    @Mapping(target = "timestamp", source = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    HitDto toDto(Hit hit);

    @Mapping(target = "timestamp", source = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Hit toEntity(CreateHitRequest request);

    StatDto toStatDto(Stat stat);

    List<StatDto> toStatDto(List<Stat> stats);
}