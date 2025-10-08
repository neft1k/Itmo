window.onload = function() {
    const canvas = document.getElementById("canvas");
    const ctx = canvas.getContext("2d");

    function drawCircle(r) {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.beginPath();
        ctx.arc(150, 150, r * 40, 0, 2 * Math.PI);
        ctx.stroke();
    }

    function drawPoint(x, y) {
        ctx.beginPath();
        ctx.arc(x * 40 + 150, 150 - y * 40, 5, 0, 2 * Math.PI);
        ctx.fill();
    }

    drawCircle(3);  // Радиус по умолчанию

    // Пример рисования точки
    drawPoint(1, 1);  // Пример точки
};
