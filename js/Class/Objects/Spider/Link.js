class Link extends ObjMesh {
    constructor(objUrl, textureUrl, length) {
        super(objUrl, textureUrl, 10, false, true, true, 0.02);
        this.length = length;
    }
}
