package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMock;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private static String LIST_MEAL_SERVLET = "/meals";
    private static final Logger log = getLogger(MealServlet.class);
    private MealDao dao;
    private final int caloriesPerDay = 2000;

    public void init() {
        dao = new MealDaoMock();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String forward = "";
        String action = request.getParameter("action");
        if (action == null) action = "listMeal";
        switch (action) {
            case "delete":
                long mealIdDel = Integer.parseInt(request.getParameter("mealId"));
                dao.delete(mealIdDel);
                response.sendRedirect(request.getContextPath() + LIST_MEAL_SERVLET);
                return;
            case "edit":
                long mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = dao.get(mealId);
                forward = INSERT_OR_EDIT;
                request.setAttribute("mealEnt", meal);
                break;
            case "insert":
                response.sendRedirect(request.getContextPath() + INSERT_OR_EDIT);
                return;
            default:
                forward = LIST_MEAL;
                request.setAttribute("meals", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay));
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String description = request.getParameter("description");
        LocalDateTime ldt = LocalDateTime.parse(request.getParameter("dateTime"));
        int calories = Integer.parseInt(request.getParameter("calories"));

        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            Meal meal = new Meal(ldt, description, calories);
            dao.create(meal);
        } else {
            Meal meal = new Meal(Integer.parseInt(mealId), ldt, description, calories);
            dao.update(meal);
        }
        response.sendRedirect(request.getContextPath() + LIST_MEAL_SERVLET);
    }
}
