package eu.trigon.juice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainLayer extends GameLayer {
    // TODO: Move all render/viewport stuff to JuiceGame

    private SpriteBatch batch;
    private ShapeRenderer shape;
    private ImmediateModeRenderer surfRenderer;

    public ExtendViewport viewport;
    public OrthographicCamera camera;

    public float gameWidth() {
        return this.viewport.getWorldWidth();
    }

    public float gameHeight() {
        return this.viewport.getWorldHeight();
    }

    private FluidSurface fluidSurface;

    private Random rand;
    private List<FluidDrop> drops;

    public MainLayer(JuiceGame g) {
        super(g);

        camera = new OrthographicCamera();
        //camera.setToOrtho(false, 720, 1280);
        viewport = new ExtendViewport(720, 1280, camera);
        viewport.apply();

        this.fluidSurface = new FluidSurface(400, 0.04f,
                0.015f, 0.4f, 6);
        this.drops = new ArrayList<FluidDrop>();
        this.rand = new Random();

        //camera.position.set(1080/2, 1920/2, 0);
        //camera.update();

        this.batch = new SpriteBatch();
        this.shape = new ShapeRenderer();

        this.surfRenderer = new ImmediateModeRenderer20(false,
                true, 0);
    }

    @Override
    public boolean tickLayersBelow() {
        return false;
    }

    @Override
    public boolean renderLayersBelow() {
        return false;
    }

    @Override
    public void renderTick(int tick, float ptt, boolean isTop) {
        this.camera.update();
        this.batch.setProjectionMatrix(this.camera.combined);
        this.shape.setProjectionMatrix(this.camera.combined);

        float height = gameHeight()/3;

        int segCount = this.fluidSurface.getSegCount();
        float step = gameWidth() / (float) (segCount - 1);

        this.shape.begin(ShapeRenderer.ShapeType.Filled);

        this.shape.setColor(0.42f, 0.65f, 1f, 1f);

        for (FluidDrop d : this.drops) {
            this.shape.circle(d.getRenderX(ptt), d.getRenderY(ptt), d.getSize(), 16);
        }

        this.shape.end();

        this.surfRenderer.begin(this.camera.combined, GL20.GL_TRIANGLE_STRIP);
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
            this.drops.add(new FluidDrop(rand.nextInt((int)gameWidth()), gameHeight() + 100, 0, 0, 10 + rand.nextInt(15)));
        }

        List<FluidDrop> splashDrops = new ArrayList<FluidDrop>();

        Iterator<FluidDrop> it = this.drops.iterator();
        while (it.hasNext()) {
            FluidDrop d = it.next();
            d.tick();

            if (d.getVelY() > 0) {
                continue;
            }

            if (d.getX() > gameWidth() || d.getX() < 0) {
                it.remove();
                continue;
            }

            int x = (int) d.getX();

            float scale = this.fluidSurface.getSegCount() / gameWidth();
            x = (int) (scale * x);

            if (d.getY() < this.fluidSurface.getSegHeight(x) + gameHeight()/3 - d.getSize() * 2) {
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
        double scale = this.fluidSurface.getSegCount() / (double) gameWidth();
        this.fluidSurface.splash((int) (scale * x), 300);
    }
}
