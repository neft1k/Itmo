<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Проверка Точки</title>
    <style>
        /* Общие стили */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f8ff;
        }

        /* Шапка с информацией о студенте */
        header {
            width: 100%;
            background-color: #2c3e50;
            color: white;
            padding: 10px 0;
            text-align: center;
        }

        header h1 {
            margin: 5px 0;
            font-size: 24px;
        }

        header p {
            margin: 0;
            font-size: 16px;
        }

        /* Основной контейнер */
        .container {
            display: flex;
            justify-content: space-between;
            max-width: 1000px;
            margin: 20px auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 8px;
        }

        /* Левая колонка */
        .left-column {
            display: flex;
            flex-direction: column;
            align-items: center;
            width: 50%;
            padding-right: 15px;
        }

        /* Разделитель */
        .separator {
            width: 1px;
            background-color: #ddd;
            margin: 0 15px;
        }

        /* Правая колонка */
        .right-column {
            width: 45%;
        }

        h3 {
            text-align: center;
        }

        /* Таблица */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 8px;
            border: 1px solid #ddd;
            text-align: center;
        }

        th {
            background-color: #f4f4f4;
        }

        /* Стиль для холста */
        #coordinate-plane {
            border: 1px solid black;
            width: 300px;
            height: 300px;
            margin: 20px 0;
        }

        /* Кнопка отправки */
        input[type="submit"] {
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            border-radius: 5px;
            font-size: 16px;
            margin-top: 10px;
        }

        input[type="submit"]:hover {
            background-color: #2980b9;
        }

        /* Стиль для формы */
        #point-form {
            display: flex;
            flex-direction: column;
            align-items: center;
            width: 100%;
        }

        .form-group {
            margin-bottom: 15px;
            width: 100%;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            text-align: center;
            font-weight: bold;
        }

        .form-group input[type="text"] {
            display: block;
            margin: 0 auto;
            padding: 5px;
            width: 50%;
            box-sizing: border-box;
        }

        .options-row {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            justify-content: center;
        }

        .options-row label {
            display: flex;
            align-items: center;
            margin: 0;
        }

        .options-row input {
            margin-right: 5px;
        }
    </style>
    <script>
        let points = [];

        function drawArea(r) {
            const canvas = document.getElementById("coordinate-plane");
            const ctx = canvas.getContext("2d");
            const centerX = canvas.width / 2;
            const centerY = canvas.height / 2;
            const scale = 150 / r;

            ctx.clearRect(0, 0, canvas.width, canvas.height);

            ctx.fillStyle = "rgba(0, 0, 255, 0.5)";
            ctx.fillRect(centerX - r * scale, centerY - r * scale, r * scale, r * scale);

            ctx.beginPath();
            ctx.moveTo(centerX, centerY);
            ctx.arc(centerX, centerY, r * scale, 3 * Math.PI / 2, 0, false);
            ctx.closePath();
            ctx.fill();

            ctx.beginPath();
            ctx.moveTo(centerX, centerY);
            ctx.lineTo(centerX - r * scale, centerY);
            ctx.lineTo(centerX, centerY + r * scale / 2);
            ctx.closePath();
            ctx.fill();

            ctx.strokeStyle = "black";
            ctx.beginPath();
            ctx.moveTo(centerX, 0);
            ctx.lineTo(centerX, canvas.height);
            ctx.moveTo(0, centerY);
            ctx.lineTo(canvas.width, centerY);
            ctx.stroke();

            ctx.font = "12px Arial";
            ctx.fillStyle = "black";
            ctx.fillText("-R", centerX - r * scale, centerY + 15);
            ctx.fillText("-R/2", centerX - r * scale / 2, centerY + 15);
            ctx.fillText("R/2", centerX + r * scale / 2, centerY + 15);
            ctx.fillText("R", centerX + r * scale, centerY + 15);
            ctx.fillText("-R", centerX + 5, centerY + r * scale);
            ctx.fillText("-R/2", centerX + 5, centerY + r * scale / 2);
            ctx.fillText("R/2", centerX + 5, centerY - r * scale / 2);
            ctx.fillText("R", centerX + 5, centerY - r * scale);

            points.forEach(point => drawPoint(parseFloat(point[0]), parseFloat(point[1]), point[3] === "inside", r));
        }

        function drawPoint(x, y, isInside, r) {
            const canvas = document.getElementById("coordinate-plane");
            const ctx = canvas.getContext("2d");
            const centerX = canvas.width / 2;
            const centerY = canvas.height / 2;
            const scale = 150 / r;

            ctx.fillStyle = isInside ? "green" : "red";
            ctx.beginPath();
            ctx.arc(centerX + x * scale, centerY - y * scale, 3, 0, 2 * Math.PI);
            ctx.fill();
        }

        function validateAndSubmit(event) {
            event.preventDefault();

            var yValue = document.getElementById('y').value.trim();
            var y = parseFloat(yValue.replace(',', '.'));
            if (isNaN(y) || y < -3 || y > 3) {
                alert('Ошибка: Значение Y должно быть числом от -3 до 3.');
                return;
            }

            var xValue = null;
            var xCheckboxes = document.getElementsByName('x');
            for (var i = 0; i < xCheckboxes.length; i++) {
                if (xCheckboxes[i].checked) {
                    xValue = xCheckboxes[i].value;
                    break;
                }
            }
            if (xValue === null) {
                alert('Ошибка: Выберите значение для X.');
                return;
            }

            var rValue = null;
            var rRadios = document.getElementsByName('r');
            for (var j = 0; j < rRadios.length; j++) {
                if (rRadios[j].checked) {
                    rValue = rRadios[j].value;
                    break;
                }
            }
            if (rValue === null) {
                alert('Ошибка: Выберите значение для R.');
                return;
            }

            sendCoordinates(xValue, y, rValue);
        }

        function sendCoordinates(x, y, r) {
            var xhr = new XMLHttpRequest();
            var url = "controller?x=" + encodeURIComponent(x) + "&y=" + encodeURIComponent(y) + "&r=" + encodeURIComponent(r);
            xhr.open("GET", url, true);
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    window.location.href = "result.jsp";
                }
            };
            xhr.send();
        }

        function onCanvasClick(event) {
            const rRadios = document.getElementsByName('r');
            let rValue = null;
            for (const radio of rRadios) {
                if (radio.checked) {
                    rValue = parseFloat(radio.value);
                    break;
                }
            }

            if (rValue === null) {
                alert("Пожалуйста, выберите значение R перед кликом на плоскости.");
                return;
            }

            const canvas = document.getElementById("coordinate-plane");
            const rect = canvas.getBoundingClientRect();
            const x = ((event.clientX - rect.left - 150) / (150 / rValue)).toFixed(2);
            const y = (-(event.clientY - rect.top - 150) / (150 / rValue)).toFixed(2);

            sendCoordinates(x, y, rValue);
        }

        function toggleCheckbox(checkbox) {
            var checkboxes = document.getElementsByName('x');
            checkboxes.forEach(cb => {
                if (cb !== checkbox) cb.checked = false;
            });
        }

        document.addEventListener("DOMContentLoaded", function () {
            const rValue = document.querySelector('input[name="r"]:checked') ? parseFloat(document.querySelector('input[name="r"]:checked').value) : 1;
            drawArea(rValue);

            <%-- Извлекаем список точек из сессии и передаем его в JavaScript --%>
            <%
                List<String[]> results = (List<String[]>) session.getAttribute("results");
                if (results != null) {
                    for (String[] result : results) {
            %>
            points.push([<%= result[0] %>, <%= result[1] %>, <%= result[2] %>, "<%= result[3] %>"]);
            <%
                    }
                }
            %>
            points.forEach(point => drawPoint(parseFloat(point[0]), parseFloat(point[1]), point[3] === "inside", rValue));
        });
    </script>
</head>
<body>

<header>
    <h1>Зиятдинов Карим Ильдарович</h1>
    <p>Группа: P3213 | Вариант: 776</p>
</header>

<div class="container">
    <div class="left-column">
        <h3>Введите координаты точки</h3>
        <form id="point-form" onsubmit="validateAndSubmit(event)">
            <div class="form-group">
                <label>Значение X:</label>
                <div class="options-row">
                    <label><input type="checkbox" name="x" value="-2" onclick="toggleCheckbox(this)"> -2</label>
                    <label><input type="checkbox" name="x" value="-1.5" onclick="toggleCheckbox(this)"> -1.5</label>
                    <label><input type="checkbox" name="x" value="-1" onclick="toggleCheckbox(this)"> -1</label>
                    <label><input type="checkbox" name="x" value="-0.5" onclick="toggleCheckbox(this)"> -0.5</label>
                    <label><input type="checkbox" name="x" value="0" onclick="toggleCheckbox(this)"> 0</label>
                    <label><input type="checkbox" name="x" value="0.5" onclick="toggleCheckbox(this)"> 0.5</label>
                    <label><input type="checkbox" name="x" value="1" onclick="toggleCheckbox(this)"> 1</label>
                    <label><input type="checkbox" name="x" value="1.5" onclick="toggleCheckbox(this)"> 1.5</label>
                    <label><input type="checkbox" name="x" value="2" onclick="toggleCheckbox(this)"> 2</label>
                </div>
            </div>

            <div class="form-group">
                <label for="y">Введите значение Y (-3 ... 3):</label>
                <input type="text" name="y" id="y" placeholder="-3 ... 3">
            </div>

            <div class="form-group">
                <label>Значение R:</label>
                <div class="options-row">
                    <label><input type="radio" name="r" value="1" onclick="drawArea(1)"> 1</label>
                    <label><input type="radio" name="r" value="1.5" onclick="drawArea(1.5)"> 1.5</label>
                    <label><input type="radio" name="r" value="2" onclick="drawArea(2)"> 2</label>
                    <label><input type="radio" name="r" value="2.5" onclick="drawArea(2.5)"> 2.5</label>
                    <label><input type="radio" name="r" value="3" onclick="drawArea(3)"> 3</label>
                </div>
            </div>

            <input type="submit" value="Проверить">
        </form>

        <canvas id="coordinate-plane" width="300" height="300" onclick="onCanvasClick(event)"></canvas>
    </div>

    <div class="separator"></div>

    <div class="right-column">
        <h3>Результаты</h3>
        <table>
            <tr>
                <th>X</th>
                <th>Y</th>
                <th>R</th>
                <th>Результат</th>
            </tr>
            <%
                if (results != null) {
                    for (String[] result : results) {
            %>
            <tr>
                <td><%= result[0] %></td>
                <td><%= result[1] %></td>
                <td><%= result[2] %></td>
                <td><%= result[3] %></td>
            </tr>
            <%
                }
            } else {
            %>
            <tr><td colspan='4'>Нет доступных результатов</td></tr>
            <%
                }
            %>
        </table>
    </div>
</div>

</body>
</html>













