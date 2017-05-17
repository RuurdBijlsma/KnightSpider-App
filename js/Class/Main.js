const bodyType = 1;
const selectedColor = new THREE.Color(1, 1, 0);
const deselectedColor = new THREE.Color(1, 1, 1);

class Main {
    constructor() {
        this.loop = new GameLoop(120);
        this.view = new View(document.getElementById('renderElement'), this);
        this.input = new Input(this.loop);

        document.addEventListener('click', e => {
            let joint = this.view.spider.getClickedJoint(new THREE.Vector2(e.pageX, e.pageY));
            if (joint)
                this.view.spider.select(joint.leg, joint.linkName);
            else
                this.view.spider.deselectAll();
        });
    }

    startGame() {
        this.game = new Game();
    }
}
