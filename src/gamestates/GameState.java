package gamestates;

import managers.GameStateManager;

public abstract class GameState {

    private GameStateManager gsm;

    GameState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public abstract void init();

    public abstract void update(float dt);

    public abstract void draw();

    public abstract void handleInput();

    public abstract void dispose();

}
