package ru.practicum.compilation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationPublicController {

    private final CompilationService service;
    private final CompilationMapper mapper;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(defaultValue = "false") boolean pinned,
                                                                @PositiveOrZero @RequestParam(defaultValue = "0") long from,
                                                                @Positive @RequestParam(defaultValue = "10") long size) {

        List<Compilation> compilations = service.getCompilations(pinned, from, size);
        return new ResponseEntity<>(mapper.toDto(compilations), HttpStatus.OK);

    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable Long compId) {

        Compilation compilation = service.getById(compId);
        return new ResponseEntity<>(mapper.toDto(compilation), HttpStatus.OK);
    }
}
