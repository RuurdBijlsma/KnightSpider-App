class Leg extends THREE.Group {
    constructor() {
        super();
        this.gammaJoint = new Joint();
        this.coxa = new Link('models/coxa.obj', 'img/textures/metal.jpg', 8);
        this.alphaJoint = new Joint();
        this.femur = new Link('models/femur.obj', 'img/textures/metal.jpg', 8);
        this.betaJoint = new Joint();
        this.tibia = new Link('models/tibia.obj', 'img/textures/metal.jpg', 12);

        this.links = [this.coxa, this.femur, this.tibia];

        let toLoad = 3;
        for (let link of this.links) {
            link.onload = () => {
                if (--toLoad === 0) {
                    this.add(this.gammaJoint);

                    this.gammaJoint.add(this.coxa.object);
                    this.coxa.object.add(this.alphaJoint);
                    this.coxa.object.position.x = this.coxa.length / 2;

                    this.alphaJoint.add(this.femur.object);
                    this.alphaJoint.position.x = this.coxa.length / 2;
                    this.femur.object.add(this.betaJoint);
                    this.femur.object.position.x = this.femur.length / 2;

                    this.betaJoint.add(this.tibia.object);
                    this.betaJoint.position.x = this.femur.length / 2;
                    this.tibia.object.position.x = this.tibia.length / 2;
                }
            }
        }
    }

    set gamma(value) {
        this.gammaJoint.rotation.y = value;
    }
    set alpha(value) {
        this.alphaJoint.rotation.z = value;
    }
    set beta(value) {
        this.betaJoint.rotation.z = value;
    }

    getClickedJoint(screenPosition) {
        let raycaster = new THREE.Raycaster();
        raycaster.setFromCamera(screenPosition, MAIN.view.camera);
        let meshes = this.links.map(l => l.mesh);
        console.log('meshes', meshes);
        let intersects = raycaster.intersectObjects(meshes);
        return intersects;
    }
}
