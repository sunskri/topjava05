package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealServiceImpl;

import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class UserMealRestController {
    @Autowired
    private UserMealServiceImpl service;

    public List<UserMeal> getAll(){
        return service.getAll();
    }

    public UserMeal save(UserMeal userMeal){
        return service.save(userMeal);
    }

    public boolean delete(int id){
        return service.delete(id);
    }

    public UserMeal get(int id){
        return service.get(id);
    }

}