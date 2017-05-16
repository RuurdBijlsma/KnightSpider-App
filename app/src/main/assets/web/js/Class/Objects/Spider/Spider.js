class Spider extends THREE.Group {
    constructor() {
        super();
        this.body = new Body();
        //20, 160, 180, 200, 340, 0
        let legPositions = [{
            position: new THREE.Vector3(12, 0, 13.5), //front right
            rotation: Utils.deg2rad(340),
            color: new THREE.Color(0, 255, 255),
        }, {
            position: new THREE.Vector3(-12, 0, 13.5), //front left
            rotation: Utils.deg2rad(200),
            color: new THREE.Color(255, 0, 255),
        }, {
            position: new THREE.Vector3(-15, 0, 0), //mid left
            rotation: Utils.deg2rad(180),
            color: new THREE.Color(255, 255, 0),
        }, {
            position: new THREE.Vector3(-12, 0, -13.5), //back left
            rotation: Utils.deg2rad(160),
            color: new THREE.Color(0, 0, 255),
        }, {
            position: new THREE.Vector3(12, 0, -13.5), //back right
            rotation: Utils.deg2rad(20),
            color: new THREE.Color(255, 0, 0),
        }, {
            position: new THREE.Vector3(15, 0, 0), //mid right
            rotation: Utils.deg2rad(0),
            color: new THREE.Color(0, 255, 0),
        }];

        this.legs = [];
        for (let position of legPositions) {
            let leg = new Leg();

            this.legs.push(leg);
            // let cube = new Cube(this.body, position.position.x, position.position.y, position.position.z, 5, 1, 1, position.color);
            // cube.rotation.y = position.rotation;
            // this.cubes.push(cube);
            this.body.add(leg);

            leg.position.copy(position.position);
            leg.rotation.y = position.rotation;
        }

        this.add(this.body);
    }

    getClickedJoint(screenPosition) {
        for (let leg of this.legs) {
            let clickedJoint = leg.getClickedJoint(screenPosition);
            console.log(clickedJoint);
            if (clickedJoint) {
                return clickedJoint;
            }
        }
    }
}
