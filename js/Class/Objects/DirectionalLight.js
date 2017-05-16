class DirectionalLight extends THREE.DirectionalLight {
    constructor(scene, x = 10, y = 10, z = 10, target, shadow = true, color = 0xffffff, intensity = 0.3) {
        super(color);
        this.intensity = intensity;

        if (shadow) {
            this.castShadow = true;
            this.shadow = new THREE.LightShadow(new THREE.PerspectiveCamera(80, 1, 1, 2500));
            this.shadow.bias = 0.00005;
            this.shadow.mapSize.height = 2048;
            this.shadow.mapSize.width = 2048;
        }

        this.position.set(x, y, z);
        if (target)
            this.lookAt = target.position;

        scene.add(this);
    }
}
