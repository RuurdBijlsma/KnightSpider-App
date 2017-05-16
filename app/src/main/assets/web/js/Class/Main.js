class Main {
    constructor() {
        this.loop = new GameLoop(120);
        this.view = new View(document.getElementById('renderElement'), this);
        this.input = new Input(this.loop);

        document.addEventListener('click', e => {
            this.view.spider.getClickedJoint(new THREE.Vector2(e.pageX, e.pageY));
        });
    }

    startGame() {
        this.game = new Game();
    }
}
