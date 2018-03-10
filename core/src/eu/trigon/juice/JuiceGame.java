package eu.trigon.juice;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class JuiceGame extends ApplicationAdapter {
    private int tps = 60, nanosPerSec = 1000000000;
    private double tickTime = 1d / tps;
    private double tickTimeSec = this.tickTime * this.nanosPerSec;
    private long time, lastTime;
    private int ticks;

    private ArrayList<GameLayer> currentLayers = new ArrayList<GameLayer>();
    private int currentLayerIndex = -1;
    private GameLayer currentLayer = null;

    private MainLayer main;
    private UILayer ui;

    private InputHandler handler;

    @Override
    public void create() {
        this.time = this.lastTime = TimeUtils.nanoTime();

        this.main = new MainLayer(this);
        this.ui = new UILayer(this);

        this.currentLayers.add(this.main);
        this.currentLayers.add(this.ui);

        this.handler = new InputHandler(this, this.main, this.ui);
        Gdx.input.setInputProcessor(this.handler);
    }

    @Override
    public void render() {
        float ptt = (this.time - this.lastTime) / ((float) this.tickTimeSec);

        renderTick(this.ticks, ptt);

        this.time = TimeUtils.nanoTime();
        while (time - lastTime >= this.tickTimeSec) {
            this.ticks++;
            tick();

            lastTime += this.tickTimeSec;
        }
    }

    @Override
    public void dispose() {
        for (int i = 0; i < this.currentLayers.size(); i++) {
            this.currentLayers.get(i).dispose();
        }
    }

    private void tick() {
        int ti = 0;

        for (int i = 0; i < this.currentLayers.size(); i++) {
            GameLayer g = this.currentLayers.get(i);
            if (!g.tickLayersBelow) {
                ti = i;
            }
        }

        for (int i = ti; i < this.currentLayers.size(); i++) {
            this.currentLayers.get(i).tick(i == this.currentLayerIndex);
        }
    }

    private void renderTick(int tick, float ptt) {
        int ti = 0;

        for (int i = 0; i < this.currentLayers.size(); i++) {
            GameLayer g = this.currentLayers.get(i);

            if (!g.tickLayersBelow) {
                ti = i;
            }
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (int i = ti; i < this.currentLayers.size(); i++) {
            this.currentLayers.get(i).tick(i == this.currentLayerIndex);
        }
    }

    public GameLayer getCurrentLayer() {
        return this.currentLayer;
    }
}
