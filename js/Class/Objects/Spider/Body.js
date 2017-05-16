class Body extends THREE.Group {
    constructor() {
        super();

        this.mesh = new ObjMesh('models/body.obj', 'img/textures/knight.png');
        this.mesh.onload = object => this.add(object);
    }
}
