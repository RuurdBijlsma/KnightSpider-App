document.addEventListener('DOMContentLoaded', init);

function init() {
    MAIN = new Main();
    MAIN.startGame();
}

function java(ping) {
    return ping + " van javascript";
}
