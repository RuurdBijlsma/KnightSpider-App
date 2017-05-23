class Spider extends THREE.Group {
    constructor() {
        super();
        this.body = new Body();
        //20, 160, 180, 200, 340, 0
        let legPositions = [{
            position: new THREE.Vector3(69.8, 0, 103.65), //front right
            rotation: Utils.deg2rad(330),
            color: new THREE.Color(0, 255, 255),
            motorIds: [1, 2, 3]
        }, {
            position: new THREE.Vector3(-70.8, 0, 104.12), //front left
            rotation: Utils.deg2rad(210),
            color: new THREE.Color(255, 0, 255),
            motorIds: [4, 5, 6]
        }, {
            position: new THREE.Vector3(-83, 0, 0), //mid left
            rotation: Utils.deg2rad(180),
            color: new THREE.Color(255, 255, 0),
            motorIds: [7, 8, 9]
        }, {
            position: new THREE.Vector3(-71.09, 0, -108.50), //back left
            rotation: Utils.deg2rad(150),
            color: new THREE.Color(0, 0, 255),
            motorIds: [10, 11, 12]
        }, {
            position: new THREE.Vector3(69.58, 0, -108.97), //back right
            rotation: Utils.deg2rad(30),
            color: new THREE.Color(255, 0, 0),
            motorIds: [13, 14, 15]
        }, {
            position: new THREE.Vector3(83, 0, 0), //mid right
            rotation: Utils.deg2rad(0),
            color: new THREE.Color(0, 255, 0),
            motorIds: [16, 17, 18]
        }];

        this.legs = [];
        for (let position of legPositions) {
            let leg = new Leg(position.motorIds);

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
            if (clickedJoint.length > 0) {
                return {
                    leg: leg,
                    linkName: clickedJoint[0].object.name
                }
            }
        }
    }

    deselectAll(){
        for (let leg of this.legs) {
            for (let link of leg.links) {
                link.mesh.material.color = deselectedColor;
            }
        }
    }

    select(leg, linkName) {
        this.deselectAll();
        leg[linkName].mesh.material.color = selectedColor;
    }
}
