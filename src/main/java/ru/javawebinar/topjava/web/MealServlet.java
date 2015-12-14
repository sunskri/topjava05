package ru.javawebinar.topjava.web;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.LoggerWrapper;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.UserMealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final LoggerWrapper LOG = LoggerWrapper.get(MealServlet.class);
    private UserMealRestController mealController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealController = appCtx.getBean(UserMealRestController.class);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        UserMeal userMeal = new UserMeal(id.isEmpty() ? null : Integer.valueOf(id),
                mealController.getUserId(),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        LOG.info(userMeal.isNew() ? "Create {}" : "Update {}", userMeal);
        mealController.save(userMeal);
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String userIdStr = request.getParameter("user");
        if (userIdStr != null) {
            int userId = Integer.parseInt(userIdStr);
            mealController.setUserId(userId);
            request.getRequestDispatcher("index.html").forward(request, response);
        }
        if (request.getParameter("filter") != null) {
            LocalDateTime fromDateTime = getDateTimeFromRequest(request.getParameter("fromDate"), request.getParameter("fromTime"));
            LocalDateTime toDateTime = getDateTimeFromRequest(request.getParameter("toDate"), request.getParameter("toTime"));
            request.setAttribute("mealList",
                    UserMealsUtil.getWithExceeded(mealController.getAll(), UserMealsUtil.DEFAULT_CALORIES_PER_DAY)
                            .stream()
                            .filter(um -> TimeUtil.isBetween(um.getDateTime(), fromDateTime, toDateTime))
                            .collect(Collectors.toList()));
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        }
        if (action == null) {
            LOG.info("getAll");
            request.setAttribute("mealList",
                    UserMealsUtil.getWithExceeded(mealController.getAll(),
                            UserMealsUtil.DEFAULT_CALORIES_PER_DAY));
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int id = getId(request);
            LOG.info("Delete {}", id);
            if(!mealController.delete(id)){
                throw new NotFoundException("user meal not found");
            }
            response.sendRedirect("meals");
        } else {
            final UserMeal meal = action.equals("create") ?
                    new UserMeal(mealController.getUserId(), LocalDateTime.now(), "", 1000) :
                    mealController.get(getId(request));
            if(meal == null){
                throw new NotFoundException("user meal not found");
            }
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("mealEdit.jsp").forward(request, response);
        }
    }

    private LocalDateTime getDateTimeFromRequest(String date, String time){
        StringBuilder dateTime = new StringBuilder();
        if(!date.equals("null")){
            dateTime.append(date);
        }
        if(!time.equals("null")){
            dateTime.append(" " + time);
        }
        try {
            return LocalDateTime.parse(dateTime.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }catch (DateTimeParseException e){
            return LocalDateTime.parse(dateTime.toString() + "00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }

    }
    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
