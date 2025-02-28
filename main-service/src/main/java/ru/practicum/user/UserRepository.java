package ru.practicum.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String FIND_USERS_WITH_LIMIT_AND_OFFSET = """
            SELECT u
            FROM User u
            ORDER BY id
            LIMIT :size
            OFFSET :from""";

    String FIND_USERS_BY_IDS_WITH_LIMIT_AND_OFFSET = """
            SELECT u
            FROM User u
            WHERE u.id IN (:ids)
            ORDER BY id
            LIMIT :size
            OFFSET :from""";

    @Query(FIND_USERS_WITH_LIMIT_AND_OFFSET)
    List<User> findUsersWithLimitAndOffset(long from, long size);

    @Query(FIND_USERS_BY_IDS_WITH_LIMIT_AND_OFFSET)
    List<User> findUsersByIdsWithLimitAndOffset(List<Long> ids, long from, long size);
}
