package eu.trigon.juice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainLayer extends GameLayer {
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private ImmediateModeRenderer surfRenderer;

    FluidSurface fluidSurface;
    private int waterHeight = 640;

    private Random rand;
    private List<FluidDrop> drops;

    public MainLayer(JuiceGame g) {
        super(g, false, false);

        this.fluidSurface = new FluidSurface(Gdx.graphics.getWidth() / 4, 0.04f,
                0.015f, 0.4f, 6);
        this.drops = new ArrayList<FluidDrop>();
        this.rand = new Random();

        this.batch = new SpriteBatch();
        this.shape = new ShapeRenderer();
        this.surfRenderer = new ImmediateModeRenderer20(false,
                true, 0);
    }


    @Override
    public void renderTick(int tick, float ptt, boolean isActive) {
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
            float x0 = i * step;
            float y0 = this.fluidSurface.getRenderSegHeight(i, ptt) + height;

            this.surfRenderer.color(0.0f, 0.38f, 1f, 1f);
            this.surfRenderer.vertex(x0, 0, 0);
            this.surfRenderer.color(0.42f, 0.65f, 1f, 1f);
            this.surfRenderer.vertex(x0, y0, 0);
        }
        this.surfRenderer.end();
    }

    @Override
    public void tick(boolean isActive) {
        this.fluidSurface.tick();

        if (this.rand.nextInt(10) == 0) {
            this.drops.add(new FluidDrop(rand.nextInt(Gdx.graphics.getWidth()), Gdx.graphics.getHeight() + 100, 0, 0, 10 + rand.nextInt(15)));
        }

        List<FluidDrop> splashDrops = new ArrayList<FluidDrop>();

        Iterator<FluidDrop> it = this.drops.iterator();
        while (it.hasNext()) {
            FluidDrop d = it.next();
            d.tick();

            if (d.getVelY() > 0) {
                continue;
            }

            if (d.getX() > Gdx.graphics.getWidth() || d.getX() < 0) {
                it.remove();
                continue;
            }

            int x = (int) d.getX();

            double scale = this.fluidSurface.getSegCount() / (double) Gdx.graphics.getWidth();
            x = (int) (scale * x);

            if (d.getY() < this.fluidSurface.getSegHeight(x) + this.waterHeight - d.getSize() * 2) {
                it.remove();

                if (d.getSize() >= 5f) {

                    int pCount = 2 + this.rand.nextInt(4);

                    for (int i = 0; i < pCount; i++) {
                        splashDrops.add(new FluidDrop(d.getX(), d.getY() - d.getSize(), (rand.nextFloat() - 0.5f) * d.getSize() / 2, d.getSize() + rand.nextFloat() * d.getSize() / 2, d.getSize() / (1.75f + this.rand.nextFloat() * 1.5f)));
                    }

                    this.fluidSurface.splash(x, -d.getSize() * 3);
                }
            }
        }

        this.drops.addAll(splashDrops);
    }

    @Override
    public void dispose() {
        this.surfRenderer.dispose();
        this.batch.dispose();
        this.shape.dispose();
    }

    public void makeTestSplash(int x) {
        double scale = this.fluidSurface.getSegCount() / (double) Gdx.graphics.getWidth();
        this.fluidSurface.splash((int) (scale * x), 300);
    }
}
