package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.UpdatedCategoryDto;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repo;
    private final CategoryMapper mapper;

    public Category createCategory(NewCategoryDto dto) {
        Category category = mapper.toCategory(dto);
        return repo.save(category);
    }

    public void deleteCategoryById(Long catId) {
        repo.deleteById(catId);
    }

    public Category updateCategory(Long catId, UpdatedCategoryDto dto) {

        Category category = getById(catId);
        category.setName(dto.getName());

        return repo.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> getCategories(Long from, Long size) {
        return repo.getCategories(from, size);
    }

    @Transactional(readOnly = true)
    public Category getById(Long catId) {
        return repo.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + catId + " не найдена"));
    }

    @Transactional(readOnly = true)
    public List<Category> getById(List<Long> ids) {
        return repo.findAllById(ids);
    }
}
