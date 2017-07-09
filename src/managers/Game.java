package managers;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Game implements ApplicationListener {
    public static int WIDTH;
    public static int HEIGHT;

    private GameStateManager gsm;

    public void create() {
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

        OrthographicCamera cam = new OrthographicCamera(WIDTH, HEIGHT);
        cam.translate(WIDTH / 2, HEIGHT / 2);
        cam.update();

        Gdx.input.setInputProcessor(new InputAdapter());

        gsm = new GameStateManager();
    }

    public void render() {
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gsm.update(Gdx.graphics.getDeltaTime() * Settings.TIME_MULTIPLIER);
        gsm.draw();
    }

    public void resize(int width, int height) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
    }

}
