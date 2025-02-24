package ru.practicum.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdatedCategoryDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminController {

    private final CategoryService service;
    private final CategoryMapper mapper;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody NewCategoryDto newCategory) {
        Category category = service.createCategory(newCategory);
        return new ResponseEntity<>(mapper.toDto(category), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long catId) {
        service.deleteCategoryById(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long catId,
                                                      @Valid @RequestBody UpdatedCategoryDto dto) {

        Category category = service.updateCategory(catId, dto);
        return new ResponseEntity<>(mapper.toDto(category), HttpStatus.OK);
    }
}
