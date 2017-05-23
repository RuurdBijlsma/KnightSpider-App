class View extends Scene {
    constructor(renderElement, main) {
        super(renderElement, main);
        this.camera.position.set(-80.91068146283502, 166.43753086645592, -310.7409529606531);
        this.camera.rotation.set(-2.5107914157222195, -0.16706760816999697, -3.020734777520994)

        this.controls = new THREE.OrbitControls(this.camera, this.renderElement);

        // this.skyBox = new SkyBox(this, 'img/skybox/space/');

        this.spider = new Spider();
        this.add(this.spider);

        // this.floor = new Cube(this, 0, -1, 0, 20, 1, 20, 0x55dd00);

        this.lights = {
            ambient: new AmbientLight(this),
            directional: new DirectionalLight(this, 10, 6, 4, new THREE.Vector3)
        };
    }

    rayCast(screenPosition, objects) {
        let normalizedPosition = new THREE.Vector2();
        normalizedPosition.x = (screenPosition.x / window.innerWidth) * 2 - 1;
        normalizedPosition.y = -(screenPosition.y / window.innerHeight) * 2 + 1;
        let rayCaster = new THREE.Raycaster();
        rayCaster.setFromCamera(normalizedPosition, this.camera);
        return rayCaster.intersectObjects(objects);
    }
}
