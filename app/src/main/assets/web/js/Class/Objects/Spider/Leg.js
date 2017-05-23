class Leg extends THREE.Group {
    constructor(motorIds) {
        super();
        this.gammaJoint = new Joint();
        this.coxa = new Link(`models/${bodyType}/coxa.obj`, 'img/textures/metal.jpg', 62.3);
        this.alphaJoint = new Joint();
        this.femur = new Link(`models/${bodyType}/femur.obj`, 'img/textures/metal.jpg', 70);
        this.betaJoint = new Joint();
        this.tibia = new Link(`models/${bodyType}/tibia.obj`, 'img/textures/metal.jpg', 116.4);

        this.links = [this.coxa, this.femur, this.tibia];

        this.motorIds = {
            coxa: motorIds[0],
            femur: motorIds[1],
            tibia: motorIds[2]
        };

        let toLoad = 3;
        for (let link of this.links) {
            link.onload = () => {
                if (--toLoad === 0) {
                    this.add(this.gammaJoint);

                    this.coxa.mesh.name = 'coxa';
                    this.femur.mesh.name = 'femur';
                    this.tibia.mesh.name = 'tibia';

                    this.gammaJoint.add(this.coxa.object);
                    this.coxa.object.add(this.alphaJoint);
                    // this.coxa.object.position.x = this.coxa.length / 2;

                    this.alphaJoint.add(this.femur.object);
                    this.alphaJoint.position.x = this.coxa.length;
                    this.femur.object.add(this.betaJoint);
                    // this.femur.object.position.x = this.femur.length / 2;

                    this.betaJoint.add(this.tibia.object);
                    this.betaJoint.position.x = this.femur.length;
                    // this.tibia.object.position.x = this.tibia.length / 2;
                }
            }
        }
    }

    animate(link, targetAngle, duration) {
        return new Promise(resolve => {
            let startAngle = this[link];
            let update = (a, b, c, d, e) => {
                this[link] = startAngle + (targetAngle - startAngle) * a;
                if (a === 1)
                    resolve();
            }
            let tween = new TWEEN.Tween(this[link])
                .to(targetAngle, duration)
                .easing(TWEEN.Easing.Quintic.InOut)
                .onUpdate(update);
            tween.start();
        });
    }

    set gamma(value) {
        this.gammaJoint.rotation.y = value;
    }

    get gamma() {
        return this.gammaJoint.rotation.y;
    }

    set alpha(value) {
        this.alphaJoint.rotation.z = value;
    }

    get alpha() {
        return this.alphaJoint.rotation.z;
    }

    set beta(value) {
        this.betaJoint.rotation.z = value;
    }

    get beta() {
        return this.betaJoint.rotation.z;
    }

    getClickedJoint(screenPosition) {
        let meshes = this.links.map(l => l.mesh);
        return MAIN.view.rayCast(screenPosition, meshes);
    }

    getLinkById(id) {
        for (let link in this.motorIds) {
            if (id === this1.motorIds[link]) {
                return this[link];
            }
        }
    }
}
