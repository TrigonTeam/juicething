package eu.trigon.juice;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class JuiceGame extends ApplicationAdapter {
    private int tps = 60, nanosPerSec = 1000000000;
    private double tickTime = 1d / tps;
    private double tickTimeSec = this.tickTime * this.nanosPerSec;
    private long time, lastTime;
    private int ticks;

    private MainScreen s;

    @Override
    public void create() {
        this.time = this.lastTime = TimeUtils.nanoTime();
        this.s = new MainScreen();
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

    private void tick() {
        this.s.tick();
    }

    private void renderTick(int tick, float ptt) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.s.renderTick(tick, ptt);
    }

    @Override
    public void dispose() {
        this.s.dispose();
    }
}
