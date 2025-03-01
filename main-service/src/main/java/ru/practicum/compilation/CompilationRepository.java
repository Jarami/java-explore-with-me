package ru.practicum.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    String FIND_ALL_WITH_LIMIT_AND_OFFSET = """
            SELECT c
            FROM Compilation c
            WHERE c.pinned = :pinned
            ORDER BY c.id
            LIMIT :size
            OFFSET :from
            """;

    @Query(FIND_ALL_WITH_LIMIT_AND_OFFSET)
    List<Compilation> findAllByPinnedWithLimitAndOffset(boolean pinned, long from, long size);
}

