package ru.practicum.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    String GET_ALL_WITH_LIMIT_AND_OFFSET = """
            SELECT c
            FROM Category c
            ORDER BY id
            LIMIT :size
            OFFSET :from""";

    @Query(GET_ALL_WITH_LIMIT_AND_OFFSET)
    List<Category> getCategories(Long from, Long size);
}
