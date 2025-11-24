package io.github.fruitcatch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public SpriteBatch batch;

    private static final String PREFS_NAME = "FruitCatcherPrefs";
    private static final String KEY_HIGH_SCORE = "high_score";

    @Override
    public void create() {
        batch = new SpriteBatch();
        Assets.getInstance().load();
        setScreen(new MenuScreen(this));
    }

    public int getHighScore() {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        return prefs.getInteger(KEY_HIGH_SCORE, 0);
    }

    public void setHighScore(int score) {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        if (score > getHighScore()) {
            prefs.putInteger(KEY_HIGH_SCORE, score);
            prefs.flush();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        Assets.getInstance().dispose();
    }
}
