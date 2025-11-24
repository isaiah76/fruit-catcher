package io.github.fruitcatch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {
    private Sprite pauseBtn;
    private boolean isPaused = false;
    private Rectangle pauseBounds;

    private float maxHealth = 100f;
    private float currentHealth = 100f;
    private final float HEALTH_WIDTH = 2.6f;
    private final float HEALTH_HEIGHT = 0.20f;
    private int score = 0;
    private int combo = 0;
    private boolean wasTouched = false;

    public Hud(Viewport viewport) {
        Texture t = Assets.getInstance().pauseIcon;
        if (t != null) {
            pauseBtn = new Sprite(t);
            pauseBtn.setSize(0.56f, 0.56f);
            reposition(viewport);
        }
    }

    public void reposition(Viewport viewport) {
        if (pauseBtn == null)
            return;
        float margin = 0.18f;
        float x = margin;
        float y = viewport.getWorldHeight() - margin - HEALTH_HEIGHT - 0.08f - pauseBtn.getHeight();
        pauseBtn.setPosition(x, y);
        pauseBounds = new Rectangle(x, y, pauseBtn.getWidth(), pauseBtn.getHeight());
    }

    public boolean updateInput(Viewport viewport) {
        boolean touched = Gdx.input.isTouched();
        boolean justClicked = touched && !wasTouched;
        wasTouched = touched;

        if (justClicked && pauseBounds != null) {
            Vector2 touch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touch);

            if (pauseBounds.contains(touch.x, touch.y)) {
                isPaused = !isPaused;
                if (isPaused)
                    pauseBtn.setColor(Color.GRAY);
                else
                    pauseBtn.setColor(Color.WHITE);
                return true;
            }
        }
        return false;
    }

    public void draw(SpriteBatch batch, Viewport viewport) {
        drawHealth(batch, viewport);
        drawScore(batch, viewport);
        if (pauseBtn != null)
            pauseBtn.draw(batch);
    }

    private void drawHealth(SpriteBatch batch, Viewport viewport) {
        float margin = 0.18f;
        float left = margin;
        float top = viewport.getWorldHeight() - margin;
        float bottom = top - HEALTH_HEIGHT;
        float border = 0.03f;

        batch.setColor(0.99f, 0.97f, 0.87f, 1f);
        batch.draw(Assets.getInstance().whitePixel, left, bottom, HEALTH_WIDTH, HEALTH_HEIGHT);

        batch.setColor(0.12f, 0.12f, 0.12f, 1f);
        batch.draw(Assets.getInstance().whitePixel, left + border, bottom + border,
                HEALTH_WIDTH - border * 2, HEALTH_HEIGHT - border * 2);

        batch.setColor(0.9f, 0.5f, 0.3f, 1f);
        float healthPct = currentHealth / maxHealth;
        batch.draw(Assets.getInstance().whitePixel, left + border, bottom + border,
                (HEALTH_WIDTH - border * 2) * healthPct, HEALTH_HEIGHT - border * 2);

        batch.setColor(Color.WHITE);
    }

    private void drawScore(SpriteBatch batch, Viewport viewport) {
        String s = String.format("%06d", score);

        float rightMargin = 0.15f;
        float x = viewport.getWorldWidth() - rightMargin - (s.length() * 0.3f);
        float y = viewport.getWorldHeight() - 0.6f;

        for (char c : s.toCharArray()) {
            int i = c - '0';
            batch.draw(Assets.getInstance().scoreDigits[i], x, y, 0.3f, 0.45f);
            x += 0.3f;
        }
    }

    public void addScore(int value) {
        score += value;
    }

    public void damage(float amt) {
        currentHealth -= amt;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int getScore() {
        return score;
    }
}
