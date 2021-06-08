package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMock;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.TimeUtil;
import sun.nio.cs.UTF_8;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private MealDao dao;
    private static final Logger log = getLogger(MealServlet.class);

    public MealServlet() {
        dao = new MealDaoMock();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String forward = "";
        String action = request.getParameter("action");
        // if (action ==null || action.isEmpty()) action = "listMeal";

        if (action.equalsIgnoreCase("delete")) {
            long userId = Integer.parseInt(request.getParameter("mealId"));
            dao.delete(userId);
            forward = LIST_MEAL;
            request.setAttribute("meals", dao.getAll());
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            long mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = dao.get(mealId);
            request.setAttribute("mealEnt", meal);
        } else if (action.equalsIgnoreCase("insert")) {
            forward = INSERT_OR_EDIT;
        } else if (action.equalsIgnoreCase("listMeal")) {
            forward = LIST_MEAL;
            request.setAttribute("meals", dao.getAll());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("description");
        LocalDateTime ldt = LocalDateTime.parse(request.getParameter("dateTime"));
        //LocalDateTime ldt = LocalDateTime.parse("dateTime", TimeUtil.dtf);
        int calories = Integer.parseInt(request.getParameter("calories"));

        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            dao.save(ldt, description, calories);
        } else {
            dao.update(Integer.parseInt(mealId), ldt, description, calories);
        }
        request.setAttribute("meals", dao.getAll());
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        view.forward(request, response);
    }
}
