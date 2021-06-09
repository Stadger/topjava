<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Добавить или изменить приём пищи</title>
    <style>
    div.field {
    padding-bottom: 5px;
    }
    div.field label {
    display: block;
    float: left;
    width: 70px;
    height: 15px;
    }
    </style>
</head>
<a href="meals" target="_blank">meals</a>

<hr align="left" color="Red" size="5" width="400"/>
<h1>прием пищи</h1>
<body>
<form method="POST" action='meals' name="frmAddUser">
    <div class="field">
        <label>description</label>
        <input
                type="text" name="description"
                value=${mealEnt.getDescription()}>
    </div>
    <div class="field">
        <label>calories</label>
        <input
                type="number" name="calories"
                value=${mealEnt.getCalories()}>
    </div>
    <div class="field">
        <label>Date</label>
        <input
                type="datetime-local" name="dateTime"
                value=${mealEnt.getDateTime()}>
    </div>
    <div class="field">
        <label></label>
        <input type="submit" value="Submit">
    </div>
</form>
</body>
</html>


