package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryUserMealRepositoryImpl implements UserMealRepository {
    private Map<Integer, UserMeal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(5);

    {
        UserMealsUtil.MEAL_LIST.forEach(this::save);
    }

    @Override
    public UserMeal save(UserMeal userMeal) {
        if (userMeal.isNew()) {
            userMeal.setId(counter.incrementAndGet());
        }
        return repository.put(userMeal.getId(), userMeal);
    }

    @Override
    public boolean delete(int id, int userId) {
        try {
            repository.remove(repository.values()
                    .stream()
                    .filter(userMeal -> userMeal.getId() == id && userMeal.getUserId() == userId)
                    .findFirst()
                    .get()
                    .getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserMeal get(int id, int userId) {
        try {
            return repository.values()
                    .stream()
                    .filter(userMeal -> userMeal.getId() == id && userMeal.getUserId() == userId)
                    .findFirst()
                    .get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<UserMeal> getAll() {

        List<UserMeal> meals = new ArrayList<>(repository.values());
        Collections.sort(meals, (UserMeal m1, UserMeal m2) ->
                m1.getDateTime().compareTo(m2.getDateTime()));
        return meals;
    }
}

