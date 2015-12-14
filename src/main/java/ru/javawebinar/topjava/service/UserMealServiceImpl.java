package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Service
public class UserMealServiceImpl implements UserMealService {

    @Autowired
    private UserMealRepository repository;

    @Override
    public boolean delete(int id, int userId) {

        return repository.delete(id, userId);
    }

    @Override
    public UserMeal get(int id, int userId) {
        return repository.get(id, userId);
    }

    @Override
    public List<UserMeal> getAll() {
        return new ArrayList<>(repository.getAll());
    }

    @Override
    public UserMeal save(UserMeal userMeal) {
        return repository.save(userMeal);
    }
}
