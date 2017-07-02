import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import managers.Game;
import managers.Settings;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Evolution";
        cfg.width = Settings.WINDOW_WIDTH;
        cfg.height = Settings.WINDOW_HEIGHT;
        cfg.useGL20 = true;
        cfg.resizable = false;

        new LwjglApplication(new Game(), cfg);
    }
}
