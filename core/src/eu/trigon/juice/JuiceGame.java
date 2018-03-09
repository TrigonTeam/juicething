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

public class JuiceGame extends ApplicationAdapter {
    SpriteBatch batch;
    ShapeRenderer shape;
    ImmediateModeRenderer surfRenderer;
    BitmapFont font;
    GlyphLayout l;

    FluidSurface fluidSurface;

    private int tps = 20, nanosPerSec = 1000000000;
    private double tickTime = 1d / tps;
    private double tickTimeSec = this.tickTime * this.nanosPerSec;
    private long time, lastTime;
    private int ticks;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.shape = new ShapeRenderer();
        this.surfRenderer = new ImmediateModeRenderer20(false, true, 0);

        this.fluidSurface = new FluidSurface(Gdx.graphics.getWidth()/4, 0.15f, 0.025f, 0.25f, 3);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 120;
        p.color = Color.WHITE;
        //p.characters = "123456789";
        p.borderColor = Color.BLACK;
        p.borderWidth = 2;
        this.font = generator.generateFont(p);
        generator.dispose();

        this.l = new GlyphLayout();

        this.time = this.lastTime = TimeUtils.nanoTime();
    }

    float h = 0;

    @Override
    public void render() {
        float ptt = (this.time - this.lastTime) / ((float) this.tickTimeSec);

        renderTick(this.ticks, ptt);

        this.time = TimeUtils.nanoTime();;
        while (time - lastTime >= this.tickTimeSec) {
            this.ticks++;
            tick();

            lastTime += this.tickTimeSec;
        }
    }

    private void tick() {

    }

    private void renderTick(int tick, float ptt) {
        Color c = new Color();
        c.fromHsv(h, 0.8f, 1);

        h += 50 * Gdx.graphics.getDeltaTime();
        if (h >= 360) {
            h = 0;
        }

        Gdx.gl.glClearColor(c.r, c.g, c.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float height = 300;

        int segCount = this.fluidSurface.getSegCount();
        float step = Gdx.graphics.getWidth() / (float) (segCount-1);

        this.surfRenderer.begin(this.batch.getProjectionMatrix(), GL20.GL_TRIANGLE_STRIP);
        for (int i = 0; i < (segCount); i++) {
            float x0 = i*step;
            float ybase = Gdx.graphics.getHeight();
            float y0 = this.fluidSurface.getRenderSegHeight(i, ptt) - height + ybase;

            this.surfRenderer.color(0.5f, 0.5f, 1f, 1f);
            this.surfRenderer.vertex(x0, ybase, 0);
            this.surfRenderer.color(0.7f, 0.7f, 1f, 1f);
            this.surfRenderer.vertex(x0, y0, 0);
        }
        this.surfRenderer.end();

        String msg = Integer.toString(ticks);
        this.l.setText(this.font, msg);

        batch.begin();
        this.font.draw(batch, msg, Gdx.graphics.getWidth() / 2 - this.l.width / 2,
                Gdx.graphics.getHeight() / 2 + this.l.height / 2);
        batch.end();

    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.font.dispose();
    }
}
