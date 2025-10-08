<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <title>Result Display</title>
    <style>
        body, html {
            height: 100%;
            margin: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
        }
        #display {
            font-size: 80px; /* Увеличенный размер шрифта для координат */
            display: flex;
            flex-direction: row;
            align-items: center;
            justify-content: center;
        }
        #result {
            font-size: 100px; /* Ещё больший размер шрифта для результата */
            margin-top: 50px;
        }
        #back-button {
            margin-top: 20px;
            font-size: 20px;
        }
        .value {
            margin-right: 20px;
            opacity: 0;
            transition: opacity 0.5s ease-in-out;
        }
        .visible {
            opacity: 1;
        }
    </style>
</head>
<body>

<div id="display"></div>
<div id="result"></div>

<button id="back-button" onclick="window.location.href='index.jsp'">Вернуться на прошлую страницу</button>

<%
    List<String[]> results = (List<String[]>) session.getAttribute("results");
    if (results != null) {
        String[] latestResult = results.get(results.size() - 1);
        String x = latestResult[0];
        String y = latestResult[1];
        String r = latestResult[2];
        String result = latestResult[3];
%>

<script>
    var values = ["X: <%= x %>", "Y: <%= y %>", "R: <%= r %>"];
    var index = 0;

    function displayNext() {
        if (index < values.length) {
            var displayDiv = document.getElementById('display');

            var span = document.createElement('span');
            span.className = 'value';
            span.innerHTML = values[index];

            span.style.color = 'black';

            displayDiv.appendChild(span);

            // Плавное появление
            setTimeout((function(spanElement) {
                return function() {
                    spanElement.classList.add('visible');
                };
            })(span), 100);

            index++;
            setTimeout(displayNext, 2000); // Пауза между значениями
        } else if (index == values.length) {
            // Добавляем GIF
            var displayDiv = document.getElementById('display');

            var img = document.createElement('img');
            if ("<%= result %>".toLowerCase() === 'inside') {
                img.src = 'ricardo-ricardo-flick.gif';
                const audio = document.createElement("audio");
                audio.src = "wait-wait-wait-what-the-hell-legend-sound.mp3";
                audio.autoplay = true;
                audio.loop = false;
                displayDiv.appendChild(audio);
            } else {
                img.src = 'office-no.gif';
                const audio = document.createElement("audio");
                audio.src = "no-god-please-no-noooooooooo.mp3";
                audio.autoplay = true;
                audio.loop = false;
                displayDiv.appendChild(audio);
            }

            img.className = 'value';
            img.style.width = '100px'; // Настройте размер по необходимости
            img.style.height = '100px';
            img.style.marginRight = '20px';

            displayDiv.appendChild(img);

            // Плавное появление GIF
            setTimeout((function(imgElement) {
                return function() {
                    imgElement.classList.add('visible');
                };
            })(img), 100);

            index++;
            setTimeout(displayNext, 2000); // Пауза перед выводом результата
        } else if (index == values.length + 1) {
            // Выводим результат ниже
            var resultDiv = document.getElementById('result');
            resultDiv.innerHTML = "<%= result %>";

            if ("<%= result %>".toLowerCase() === 'inside') {
                resultDiv.style.color = 'green';
            } else if ("<%= result %>".toLowerCase() === 'outside') {
                resultDiv.style.color = 'red';
            } else {
                resultDiv.style.color = 'black';
            }

            index++;
        }
    }

    window.onload = displayNext;
</script>

<%
} else {
%>
<div id="display">Нет доступных результатов</div>
<%
    }
%>

</body>
</html>


