class ObjMesh {
    constructor(objUrl, textureUrl, textureScale = 10, castShadow = false, receiveShadow = true, bump = true, bumpScale = 0.02) {
        let textureLoader = new THREE.TextureLoader(),
            map = textureLoader.load(textureUrl),
            materialSettings = bump ? {
                map: map,
                bumpMap: map,
                bumpScale: bumpScale
            } : {
                map: map
            },
            material = new THREE.MeshPhongMaterial(materialSettings),
            objLoader = new THREE.OBJLoader();

        map.repeat.set(textureScale, textureScale);
        map.wrapS = map.wrapT = THREE.RepeatWrapping;

        objLoader.load(objUrl, object => {
            this.object = object;
            this.mesh = object.children[0];
            this.mesh.receiveShadow = receiveShadow;
            this.mesh.castShadow = castShadow;
            object.children[0].material = material;
            this.onload(object);
        });

        this.onload = () => {}
    }
}
