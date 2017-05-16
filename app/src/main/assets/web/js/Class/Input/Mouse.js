class Mouse {
    constructor() {
        document.addEventListener('touchstart', e => this.handleTouchStart(e));
        document.addEventListener('touchmove', e => this.handleTouchMove(e));
        document.addEventListener('touchend', e => this.handleTouchEnd(e));

        document.addEventListener('mousedown', e => this.handleMouseDown(e));
        document.addEventListener('mousemove', e => this.handleMouseMove(e));
        document.addEventListener('mouseup', e => this.handleMouseUp(e));

        this.x = 0;
        this.y = 0;

        this.down = false;
    }

    setPosFromEvent(e) {
        this.x = e.pageX;
        this.y = e.pageY;
    }

    handleMouseDown(e) {
        this.setPosFromEvent(e);
        this.down = true;
    }

    handleMouseMove(e) {
        this.setPosFromEvent(e);
    }

    handleMouseUp(e) {
        this.setPosFromEvent(e);
        this.down = false;
    }

    handleTouchStart(e) {
        this.setPosFromEvent(e.touches[0]);
        this.down = true;
    }

    handleTouchMove(e) {
        this.setPosFromEvent(e.touches[0]);
    }

    handleTouchEnd(e) {
        this.setPosFromEvent(e.touches[0]);
        this.down = false;
    }
}
