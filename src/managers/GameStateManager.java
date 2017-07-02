package managers;

public class GameStateManager {
    private gamestates.GameState gameState;

    private static final int MENU = 0;
    private static final int PLAY = 893746;

    GameStateManager() {
        setState(PLAY);
    }

    private void setState(int state) {
        if (gameState != null)
            gameState.dispose();

        // if (state == MENU)
            // gameState = new MenuState(this);

        if (state == PLAY)
            gameState = new gamestates.PlayState(this);
    }

    void update(float dt) {
        gameState.update(dt);
    }

    void draw() {
        gameState.draw();
    }

}
