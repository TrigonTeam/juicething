package eu.trigon.juice;

public class FluidDrop {

    private float x, y, lastX, lastY, velX, velY, size;

    public FluidDrop(float x, float y, float velX, float velY, float size) {
        this.x = x;
        this.lastX = x;

        this.y = y;
        this.lastY = y;

        this.velX = velX;
        this.velY = velY;

        this.size = size;
    }

    public void tick() {
        this.lastX = x;
        this.lastY = y;

        this.velY -= 1;

        this.x += velX;
        this.y += velY;

        this.velX *= 0.99f;
        this.velY *= 0.99f;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getVelX() {
        return this.velX;
    }

    public float getVelY() {
        return this.velY;
    }

    public float getSize() {
        return this.size;
    }

    public float getRenderX(float ptt) {
        return this.lastX + (this.x - this.lastX) * ptt;
    }

    public float getRenderY(float ptt) {
        return this.lastY + (this.y - this.lastY) * ptt;
    }

}
