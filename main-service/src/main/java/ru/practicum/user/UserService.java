package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;
    private final UserMapper mapper;

    @Transactional
    public User createUser(NewUserRequest request) {
        User user = mapper.toUser(request);
        return repo.save(user);
    }

    public List<User> getUsers(List<Long> ids, long from, long size) {

        if (ids == null || ids.isEmpty()) {
            return repo.findUsersWithLimitAndOffset(from, size);
        }
        return repo.findUsersByIdsWithLimitAndOffset(ids, from, size);
    }

    @Transactional
    public void deleteUserById(long userId) {
        User user = getById(userId);
        repo.delete(user);
    }

    public User getById(long userId) {
        return repo.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь с id = " + userId + " не найден"));
    }

    public List<User> getById(List<Long> ids) {
        return repo.findAllById(ids);
    }
}
