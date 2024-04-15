<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Point Clicker</title>
    <style>
        body{
            display: flex;
            justify-content: center;
            flex-direction: column;
            height: 100vh;
            align-items: center;
            width: 100vw;
            margin: 0;
        }
        #canvas {
            width: 200px;
            height: 200px;
            border: 1px solid black;
            border-radius: 50%;
        }
        #point {
            width: 5px;
            height: 5px;
            background-color: red;
            border-radius: 50%;
            position: absolute;
            display: none;
        }
    </style>
</head>
<body onclick="handleClick(event)">

<h1>Point Clicker</h1>

<div id="canvas">
    <div id="point"></div>
</div>

<p>Total Points: <span id="totalPoints">0</span></p>
<p>Outside Circle Points: <span id="outsideCirclePoints">0</span></p>
<p>Average Interval: <span id="averageInterval">0</span> milliseconds</p>

<script>
    function handleClick(event) {
        var x = event.clientX;
        var y = event.clientY;
        document.getElementById('point').style.left = x + 'px';
        document.getElementById('point').style.top = y + 'px';
        document.getElementById('point').style.display = 'block';

        var rect = document.getElementById('canvas').getBoundingClientRect();
        var centerX = rect.left + rect.width / 2;
        var centerY = rect.top + rect.height / 2;
        var distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        var radius = rect.width / 2; 

        // Отправляем координаты клика на сервер
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "pointclick?isInCircle=" + (distance <= radius), true);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                document.getElementById('totalPoints').innerText = response.totalPoints;
                document.getElementById('outsideCirclePoints').innerText = response.outsideCirclePoints;
                document.getElementById('averageInterval').innerText = response.averageInterval;
            }
        };
        xhr.send();
    }
</script>

</body>
</html>
