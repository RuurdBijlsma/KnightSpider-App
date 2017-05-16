class AmbientLight extends THREE.AmbientLight {
    constructor(scene, intensity = 1, color = 0x404040) {
        super(color, intensity);
        scene.add(this);
    }
}
