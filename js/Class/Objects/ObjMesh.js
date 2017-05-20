class ObjMesh {
    constructor(objUrl, textureUrl, textureScale = 10, castShadow = false, receiveShadow = true, bump = true, bumpScale = 0.02) {
        let map = Loader.getTextureMap(textureUrl, textureScale);
        let materialSettings = bump ? {
            map: map,
            bumpMap: map,
            bumpScale: bumpScale
        } : {
            map: map
        };
        let material = new THREE.MeshPhongMaterial(materialSettings);

        Loader.getObjMesh(objUrl, material).then(object => {
            this.object = object;
            this.mesh = object.children[0];
            this.mesh.receiveShadow = receiveShadow;
            this.mesh.castShadow = castShadow;
            object.children[0].material = material;
            this.onload(object);
        });

        this.onload = () => {
        }
    }
}
