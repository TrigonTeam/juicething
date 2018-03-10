package eu.trigon.juice;

public abstract class GameLayer {
    protected JuiceGame game;


    public GameLayer(JuiceGame game) {
        this.game = game;
    }

    public abstract boolean tickLayersBelow();
    public abstract boolean renderLayersBelow();
    public abstract void renderTick(int tick, float ptt, boolean isActive);
    public abstract void tick(boolean isActive);
    public abstract void dispose();
}
