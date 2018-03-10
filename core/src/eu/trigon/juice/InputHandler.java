package eu.trigon.juice;

import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {
    private MainLayer main;
    private UILayer ui;
    private JuiceGame game;

    public InputHandler(JuiceGame game, MainLayer main, UILayer ui) {
        this.game = game;
        this.main = main;
        this.ui = ui;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (this.ui.isActive()) {

        } else {
            this.main.makeTestSplash(screenX);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == 0) {

        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    //------ Unused ------
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
}
