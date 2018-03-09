package eu.trigon.juice;

public class FluidSurface {

    private int segCount;

    private float[] prevSegHeights;
    private float[] segHeights;
    private float[] segSpeeds;

    private float[] dl;
    private float[] dr;

    private float stiffness, dampening, spread;
    private int iterations;

    public FluidSurface(int segCount, float stiffness, float dampening, float spread, int iterations) {
        this.segCount = segCount;

        this.prevSegHeights = new float[segCount];
        this.segHeights = new float[segCount];
        this.segSpeeds = new float[segCount];

        this.dl = new float[segCount];
        this.dr = new float[segCount];

        this.stiffness = stiffness;
        this.dampening = dampening;
        this.spread = spread;
        this.iterations = iterations;
    }

    public void tick() {
        for (int i = 0; i < segCount; i++) {
            this.prevSegHeights[i] = this.segHeights[i];
            tickSeg(i);
        }

        for (int j = 0; j < this.iterations; j++) {
            for (int i = 0; i < segCount; i++) {
                if (i > 0) {
                    this.dl[i] = this.spread * (this.segHeights[i] - this.segHeights[i-1]);
                    this.segSpeeds[i-1] += this.dl[i];
                }

                if (i < segCount - 1) {
                    this.dr[i] = this.spread * (this.segHeights[i] - this.segHeights[i+1]);
                    this.segSpeeds[i+1] += this.dr[i];
                }
            }

            for (int i = 0; i < segCount; i++) {
                if (i > 0) {
                    this.segHeights[i-1] += this.dl[i];
                }

                if (i < segCount - 1) {
                    this.segHeights[i+1] += this.dr[i];
                }
            }
        }
    }

    public void splash(int x, float speed) {
        if (x >= this.segCount || x < 0) {
            return;
        }

        this.segSpeeds[x] = speed;
    }

    public int getSegCount() {
        return this.segCount;
    }

    public float getSegHeight(int x) {
        if (x >= this.segCount || x < 0) {
            return 9999999;
        }

        return this.segHeights[x];
    }

    public float getRenderSegHeight(int x, float ptt) {
        return this.prevSegHeights[x] + (this.segHeights[x] - this.prevSegHeights[x]) * ptt;
    }

    public float getSegSpeed(int x) {
        return this.segSpeeds[x];
    }

    private void tickSeg(int i) {
        this.segSpeeds[i] += -this.segHeights[i] * this.stiffness - this.segSpeeds[i] * this.dampening;
        this.segHeights[i] += this.segSpeeds[i];
    }

}
