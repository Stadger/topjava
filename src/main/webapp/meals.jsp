<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <title>Список еды</title>
</head>
<body>
<a href="index.html" target="_blank">HOME</a>

<hr align="left" color="Red" size="5" width="400"/>
<h1>Meals</h1>
<table border="1">
    <tr>
        <td>ID</td>
        <td>Время</td>
        <td>Описание</td>
        <td>Калории</td>
    </tr>
    <c:forEach items="${meals}" var="mealTo">

        <tr style="color:${mealTo.isExcess() ? 'red' : 'green'}">
            <td>${mealTo.getId()}</td>
            <td>${mealTo.getDateTime().format( DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm"))}</td>
            <td>${mealTo.getDescription()}</td>
            <td>${mealTo.getCalories()}</td>
            <td><a href="meals?action=edit&mealId=<c:out value="${mealTo.getId()}"/>">Update</a></td>
            <td><a href="meals?action=delete&mealId=<c:out value="${mealTo.getId()}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="meals?action=insert">добавить приём пищи</a></p>
</body>
</html>