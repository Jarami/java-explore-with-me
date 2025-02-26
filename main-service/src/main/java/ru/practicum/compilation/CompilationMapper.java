package ru.practicum.compilation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.event.EventMapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {EventMapper.class})
public interface CompilationMapper {
    CompilationDto toDto(Compilation compilation);
}
