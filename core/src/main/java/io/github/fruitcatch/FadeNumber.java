package io.github.fruitcatch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class FadeNumber {
    private final int value;
    private final float offsetX;
    private final float offsetY;
    private float timer;
    private final float FADE_DURATION = 0.9f;
    private final float startDigitHeight;

    public FadeNumber(int value, float offsetX, float offsetY, float digitHeight) {
        this.value = value;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.timer = 0f;
        this.startDigitHeight = digitHeight;
    }

    public void update(float dt) {
        timer += dt;
    }

    public boolean isFinished() {
        return timer >= FADE_DURATION;
    }

    public void draw(SpriteBatch batch, Basket basket) {
        float t = MathUtils.clamp(timer / FADE_DURATION, 0f, 1f);
        float scaleUp = 1f + 0.3f * (float) Math.sin(MathUtils.PI * t);
        float alpha = MathUtils.clamp(1f - t, 0f, 1f);
        float digitHeight = startDigitHeight * scaleUp;
        String s = Integer.toString(value);
        float totalWidth = 0f;
        Texture[] digits = Assets.getInstance().digitTextures;
        float[] widths = new float[s.length()];

        for (int i = 0; i < s.length(); i++) {
            int d = s.charAt(i) - '0';
            if (d >= 0 && d < digits.length) {
                Texture dt = digits[d];
                float aspect = (float) dt.getWidth() / dt.getHeight();
                widths[i] = digitHeight * aspect;
                totalWidth += widths[i];
            }
        }

        float anchorX = basket.getCenterX();
        float anchorY = basket.getY() + offsetY;
        float left = anchorX + offsetX - totalWidth / 2f;

        batch.setColor(1f, 1f, 1f, alpha);
        for (int i = 0; i < s.length(); i++) {
            int d = s.charAt(i) - '0';
            if (d >= 0 && d < digits.length) {
                batch.draw(digits[d], left, anchorY, widths[i], digitHeight);
                left += widths[i];
            }
        }
        batch.setColor(Color.WHITE);
    }
}
