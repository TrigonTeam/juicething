package eu.trigon.juice;

public abstract class GameLayer {
    protected JuiceGame game;

    protected boolean renderLayersBelow;
    protected boolean tickLayersBelow;

    public GameLayer(JuiceGame game, boolean renderLayersBelow, boolean tickLayersBelow) {
        this.game = game;

        this.renderLayersBelow = renderLayersBelow;
        this.tickLayersBelow = tickLayersBelow;
    }

    public boolean tickLayersBelow() {
        return this.tickLayersBelow;
    }

    public boolean renderLayersBelow() {
        return this.renderLayersBelow;
    }

    public boolean isActive() {
        return this.game.getCurrentLayer() == this;
    }

    public abstract void renderTick(int tick, float ptt, boolean isActive);
    public abstract void tick(boolean isActive);
    public abstract void dispose();
}
