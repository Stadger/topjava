package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMock;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet  extends HttpServlet {

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

        String forward="";
        String action = request.getParameter("action");
       // if (action ==null || action.isEmpty()) action = "listMeal";

        if (action.equalsIgnoreCase("delete")){
            long userId = Integer.parseInt(request.getParameter("mealId"));
            dao.delete(userId);
            forward = LIST_MEAL;
            request.setAttribute("meals", dao.getAll());
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            long mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = dao.get(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeal")){
            forward = LIST_MEAL;
            request.setAttribute("meals", dao.getAll());
            dao.getAll().stream().forEach(System.out::println);
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);

//        request.getRequestDispatcher("/users.jsp").forward(request, response);
//        response.sendRedirect("meals.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("description");
        LocalDateTime ldt = LocalDateTime.parse("dateTime", TimeUtil.dtf);
        int calories = Integer.parseInt("calories");

        String mealId = request.getParameter("mealId");
        if(mealId == null || mealId.isEmpty())
        {
            dao.save(ldt,description,calories);
        }
        else
        {
            dao.update(Integer.parseInt(mealId),ldt,description,calories);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        request.setAttribute("Meals", dao.getAll());
        view.forward(request, response);
    }


}
