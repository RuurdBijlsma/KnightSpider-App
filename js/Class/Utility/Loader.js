class Loader {
    static getTextureMap(url, textureScale = 10) {
        if (Loader.textureLoads === undefined)
            Loader.textureLoads = [];

        let map;

        if (Loader.textureLoads.map(t => t.url).includes(url)) {
            map = Loader.textureLoads.find(t => t.url === url).map;
        } else {
            let textureLoader = new THREE.TextureLoader();
            map = textureLoader.load(url);

            map.repeat.set(textureScale, textureScale);
            map.wrapS = map.wrapT = THREE.RepeatWrapping;

            Loader.textureLoads.push({
                url: url,
                map: map
            });
        }

        return map;
    }

    static getObjMesh(url, material) {
        return new Promise(resolve => {
            if (Loader.objLoads === undefined)
                Loader.objLoads = [];

            let obj;

            if (Loader.objLoads.map(t => t.url).includes(url)) {
                obj = Loader.objLoads.find(t => t.url === url).obj;
                if (obj.loaded)
                    resolve(obj.clone(true));
                else {
                    obj.onLoads.push(object => {
                        resolve(object.clone(true));
                    });
                }
            } else {
                let objLoader = new THREE.OBJLoader();
                let obj = new THREE.Group();
                obj.loaded = false;
                obj.onLoads = [];

                objLoader.load(url, object => {
                    obj.add(object.children[0]);
                    obj.children[0].material = material;
                    for (let onLoad of obj.onLoads) {
                        onLoad(obj);
                    }
                    obj.loaded = true;
                    resolve(obj);
                });

                Loader.objLoads.push({
                    url: url,
                    obj: obj
                });
            }
        });
    }
}