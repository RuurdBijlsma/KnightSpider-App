document.addEventListener('DOMContentLoaded', init);

function init() {
    MAIN = new Main();
    MAIN.startGame();

    console.log('started');

    if (location.hash == '#shutdown')
        MAIN.view.spider.engage();
    else if (location.hash === '#engage')
        MAIN.view.spider.engage();
}

function send(message) {
    MAIN.view.spider.setStance(JSON.parse(message));
    return "Settings stance";
}

function makechain() {
    var solver = new Fullik.Structure(MAIN.view);
    var target = new THREE.Vector3(280, 0, 0);
    solver.clear();
    var startLoc = new Fullik.V3();
    var X_AXIS = new Fullik.V3(1, 0, 0);
    var Y_AXIS = new Fullik.V3(0, 1, 0);
    var Z_AXIS = new Fullik.V3(0, 0, 1);
    var chain, basebone;

    // 0 spine

    chain = new Fullik.Chain(0xFFFF00);
    basebone = new Fullik.Bone(startLoc, new Fullik.V3(0, 0, 0));
    chain.addBone(basebone);
    chain.addConsecutiveRotorConstrainedBone(Y_AXIS, 77, 300);
    chain.addConsecutiveRotorConstrainedBone(Z_AXIS, 80, 300);
    chain.addConsecutiveRotorConstrainedBone(Z_AXIS, 115, 300);

    solver.add(chain, target, true);
    solver.update();
    return solver;
}
