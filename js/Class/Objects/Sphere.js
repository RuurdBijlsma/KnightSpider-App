class Sphere extends THREE.Mesh {
    constructor(scene, x = 0, y = 0, z = 0, radius = 0.5, color = 0xff0000, shadow = true) {
        let geometry = new THREE.SphereGeometry(radius, 36, 36),
            material = new THREE.MeshStandardMaterial({
                color: color
            });
        super(geometry, material);
        this.receiveShadow = shadow;
        this.castShadow = shadow;
        this.position.set(x, y, z);
        scene.add(this);
    }
}