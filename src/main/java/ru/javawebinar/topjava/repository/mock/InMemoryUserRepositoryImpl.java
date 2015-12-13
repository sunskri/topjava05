package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by master on 12.12.2015.
 */
@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    List<User> repository = new ArrayList<>();

    @Override
    public User save(User user) {
        repository.add(user);
        return user;
    }

    @Override
    public boolean delete(int id) {
        try {
            repository.remove(repository
                    .stream()
                    .filter(user -> user.getId() == id)
                    .findFirst()
                    .get());
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

    @Override
    public User get(int id) {
        try {
            return repository
                    .stream()
                    .filter(user -> user.getId() == id)
                    .findFirst()
                    .get();
        } catch (NullPointerException e) {
            return null;
        }

    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>(repository);
        Collections.sort(users, (User n1, User n2) -> n1.getName().compareTo(n2.getName()));
        return users;
    }
}
