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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class JuiceGame extends ApplicationAdapter {
    SpriteBatch batch;
    ShapeRenderer shape;
    BitmapFont font;
    GlyphLayout l;
    final String msg = "Ahoj, šotoušku :)";

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.shape = new ShapeRenderer();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 120;
        p.color = Color.WHITE;
        p.characters = msg;
        p.borderColor = Color.BLACK;
        p.borderWidth = 2;
        this.font = generator.generateFont(p);
        generator.dispose();

        this.l = new GlyphLayout();
        l.setText(this.font, this.msg);
    }

    float h = 0;

    @Override
    public void render() {
        Color c = new Color();
        c.fromHsv(h, 0.8f, 1);

        h += 50 * Gdx.graphics.getDeltaTime();
        if (h >= 360) {
            h = 0;
        }

        Gdx.gl.glClearColor(c.r, c.g, c.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        this.font.draw(batch, this.msg, Gdx.graphics.getWidth() / 2 - this.l.width / 2,
                Gdx.graphics.getHeight() / 2 + this.l.height / 2);
        batch.end();

        /*shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.BLACK);
        shape.rect(Gdx.graphics.getWidth() / 2 - this.l.width / 2,
                Gdx.graphics.getHeight() / 2 - this.l.height / 2,
                this.l.width, this.l.height);
        shape.end();*/
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.font.dispose();
    }
}
