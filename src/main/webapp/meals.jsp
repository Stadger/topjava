<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<html>
<head>
    <title>Список еды</title>
</head>
<body>
<a href="index.html" target="_blank">HOME</a>

<hr align="left" color="Red" size="5" width="400"/>
<h1>Meals</h1>
<table border="1" bgcolor="white">
    <tr>
        <td>Время</td>
        <td>Описание</td>
        <td>Калории</td>
    </tr>
    <c:forEach items="${meals}" var="mealTo">
        <jsp:useBean id="mealTo"
                     type="ru.javawebinar.topjava.model.MealTo"/>
        <tr style="color:${mealTo.isExcess() ? 'red' : 'green'}">
            <td>${mealTo.dateTime.format(TimeUtil.dtf)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?action=edit&mealId=${mealTo.id}">Update</a></td>
            <td><a href="meals?action=delete&mealId=${mealTo.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="meals?action=insert">добавить приём пищи</a></p>
</body>
</html>