package eu.trigon.juice;

public class UILayer extends GameLayer {
    public UILayer(JuiceGame game) {
        super(game);
    }

    @Override
    public boolean tickLayersBelow() {
        return true;
    }

    @Override
    public boolean renderLayersBelow() {
        return true;
    }

    @Override
    public void renderTick(int tick, float ptt, boolean isTop) {

    }

    @Override
    public void tick(boolean isActive) {

    }

    @Override
    public void dispose() {

    }

    public boolean isActive() {
        return false;
    }
}
