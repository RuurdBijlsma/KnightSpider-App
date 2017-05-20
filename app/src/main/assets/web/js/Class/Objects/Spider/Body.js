class Body extends THREE.Group {
    constructor() {
        super();

        this.mesh = new ObjMesh(`models/${bodyType}/body.obj`, 'img/textures/metal.jpg');
        this.mesh.onload = object => this.add(object);
    }
}
