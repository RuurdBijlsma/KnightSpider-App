class PointLight extends THREE.PointLight {
    constructor(scene, x = 0, y = 0, z = 0, color = 0xFFFFFF, intensity = 1, shadow = true, distance = 0, decay = 1) {
        super(color, intensity, distance, decay);
        this.position.set(x, y, z);

        if (shadow) {
            this.castShadow = true;
            this.shadow = new THREE.LightShadow(new THREE.PerspectiveCamera(80, 1, 1, 2500));
            this.shadow.bias = 0.0005;
            this.shadow.mapSize.height = 1024;
            this.shadow.mapSize.width = 1024;
        }

        scene.add(this);
    }
}
