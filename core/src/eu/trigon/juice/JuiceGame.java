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
    SpriteBatch batch;
    ShapeRenderer shape;
    ImmediateModeRenderer surfRenderer;
    BitmapFont font;
    GlyphLayout l;

    FluidSurface fluidSurface;
    private int waterHeight = 640;

    private Random rand;
    private List<FluidDrop> drops;

    private int tps = 60, nanosPerSec = 1000000000;
    private double tickTime = 1d / tps;
    private double tickTimeSec = this.tickTime * this.nanosPerSec;
    private long time, lastTime;
    private int ticks;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.shape = new ShapeRenderer();
        this.surfRenderer = new ImmediateModeRenderer20(false, true, 0);

        this.fluidSurface = new FluidSurface(Gdx.graphics.getWidth() / 4, 0.04f, 0.015f, 0.4f, 6);
        this.drops = new ArrayList<FluidDrop>();

        this.rand = new Random();

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

        this.time = TimeUtils.nanoTime();
        ;
        while (time - lastTime >= this.tickTimeSec) {
            this.ticks++;
            tick();

            lastTime += this.tickTimeSec;
        }
    }

    boolean wasTouched = false;
    private void tick() {
        if (Gdx.input.isTouched()) {
            if(!wasTouched) {
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();

                double scale = this.fluidSurface.getSegCount() / (double) Gdx.graphics.getWidth();

                this.fluidSurface.splash((int) (scale * x), 300);

                wasTouched = true;
            }
        } else {
            if(wasTouched) {
                wasTouched = false;
            }
        }

        this.fluidSurface.tick();

        if (this.rand.nextInt(10) == 0) {
            this.drops.add(new FluidDrop(rand.nextInt(Gdx.graphics.getWidth()), Gdx.graphics.getHeight() + 100, 0, 0, 10+rand.nextInt(15)));
        }

        List<FluidDrop> splashDrops = new ArrayList<FluidDrop>();

        Iterator<FluidDrop> it = this.drops.iterator();
        while (it.hasNext()) {
            FluidDrop d = it.next();
            d.tick();

            if (d.getVelY() > 0) {
                continue;
            }

            if(d.getX() > Gdx.graphics.getWidth() || d.getX() < 0) {
                it.remove();
                continue;
            }

            int x = (int)d.getX();

            double scale = this.fluidSurface.getSegCount() / (double) Gdx.graphics.getWidth();
            x = (int)(scale*x);

            if (d.getY() < this.fluidSurface.getSegHeight(x) + this.waterHeight - d.getSize()*2) {
                it.remove();

                if (d.getSize() >= 5f) {

                    int pCount = 2 + this.rand.nextInt(4);

                    for (int i = 0; i < pCount; i++) {
                        splashDrops.add(new FluidDrop(d.getX(), d.getY() - d.getSize(), (rand.nextFloat()-0.5f)*d.getSize()/2, d.getSize()+rand.nextFloat()*d.getSize()/2, d.getSize() / (1.75f + this.rand.nextFloat()*1.5f)));
                    }

                    this.fluidSurface.splash(x, -d.getSize()*3);
                }
            }
        }

        this.drops.addAll(splashDrops);
    }

    private void renderTick(int tick, float ptt) {
        Color c = new Color();
        c.fromHsv(h, 0.1f, 1);

        h += 50 * Gdx.graphics.getDeltaTime();
        if (h >= 360) {
            h = 0;
        }

        Gdx.gl.glClearColor(c.r, c.g, c.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float height = this.waterHeight;

        int segCount = this.fluidSurface.getSegCount();
        float step = Gdx.graphics.getWidth() / (float) (segCount - 1);

        this.shape.begin(ShapeRenderer.ShapeType.Filled);

        this.shape.setColor(0.42f, 0.65f, 1f, 1f);

        for (FluidDrop d : this.drops) {
            this.shape.circle(d.getRenderX(ptt), d.getRenderY(ptt), d.getSize(), 16);
        }

        this.shape.end();

        this.surfRenderer.begin(this.batch.getProjectionMatrix(), GL20.GL_TRIANGLE_STRIP);
        for (int i = 0; i < (segCount); i++) {
            float x0 = i*step;
            float y0 = this.fluidSurface.getRenderSegHeight(i, ptt) + height;

            this.surfRenderer.color(0.0f, 0.38f, 1f, 1f);
            this.surfRenderer.vertex(x0, 0, 0);
            this.surfRenderer.color(0.42f, 0.65f, 1f, 1f);
            this.surfRenderer.vertex(x0, y0, 0);
        }
        this.surfRenderer.end();

        String msg = Integer.toString(this.drops.size());
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
