package ru.practicum.category;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper {
    Category toCategory(NewCategoryDto dto);

    CategoryDto toDto(Category category);

    List<CategoryDto> toDto(List<Category> categoryList);
}
