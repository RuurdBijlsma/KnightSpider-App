document.addEventListener('DOMContentLoaded', init);

function init() {
    MAIN = new Main();
    MAIN.startGame();
}

function send(message) {
    return "ping from javascript " + message;
}