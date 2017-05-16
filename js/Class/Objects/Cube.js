class Cube extends THREE.Mesh {
    constructor(scene, x = 0, y = 0, z = 0, xSize = 1, ySize = 1, zSize = 1, color = 0xff0000, shadow = true) {
        let geometry = new THREE.BoxGeometry(xSize, ySize, zSize),
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
