class Scene extends THREE.Scene {
    constructor(renderElement, main) {
        super();
        this.main = main;

        this.renderElement = renderElement;
        this.camera = new THREE.PerspectiveCamera(70, this.renderElement.offsetWidth / this.renderElement.offsetHeight, 0.1, 10000);
        this.camera.position.set(10, 10, 10);

        this.renderer = new THREE.WebGLRenderer({
            alpha: true,
            antialias: true
        });

        this.renderer.shadowMap.enabled = true;
        this.renderer.shadowMap.type = THREE.PCFSoftShadowMap;
        this.renderer.gammaInput = true;
        this.renderer.gammaOutput = true;

        this.renderer.setSize(this.renderElement.offsetWidth, this.renderElement.offsetHeight);
        this.renderElement.appendChild(this.renderer.domElement);
        window.addEventListener('resize', () => this.onWindowResize());

        this.stats = new Stats();
        this.stats.showPanel(0);
        document.body.appendChild(this.stats.dom);

        this.render();
    }

    render() {
        this.stats.begin();
        this.renderer.render(this, this.camera);
        this.stats.end();
        requestAnimationFrame(() => this.render());
    }

    onWindowResize() {
        this.camera.aspect = this.renderElement.offsetWidth / this.renderElement.offsetHeight;
        this.renderer.setSize(this.renderElement.offsetWidth, this.renderElement.offsetHeight);
        this.camera.updateProjectionMatrix();
    }
}
