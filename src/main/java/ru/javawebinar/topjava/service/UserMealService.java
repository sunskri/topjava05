package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.UserMeal;

import java.util.Collection;
import java.util.List;

/**
 * GKislin
 * 15.06.2015.
 */
public interface UserMealService {

    UserMeal save(UserMeal userMeal);

    boolean delete(int id, int userId);

    UserMeal get(int id, int userId);

    Collection<UserMeal> getAll(int userId);
}
