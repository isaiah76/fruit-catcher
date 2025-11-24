package io.github.fruitcatch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Particle {
    private float localX, localY;
    private float vx, vy;
    private float life;
    private float age;
    private float size;
    private Color color;
    private boolean followBasket;

    public Particle(float localX, float localY, float vx, float vy, float life, float size, Color color, boolean followBasket) {
        this.localX = localX;
        this.localY = localY;
        this.vx = vx;
        this.vy = vy;
        this.life = life;
        this.age = 0f;
        this.size = size;
        this.color = new Color(color);
        this.followBasket = followBasket;
    }

    public void update(float dt) {
        age += dt;
        localX += vx * dt;
        localY += vy * dt;
        vy -= 5f * dt; // Gravity
    }

    public boolean isDead() {
        return age >= life;
    }

    public void draw(SpriteBatch batch, Basket basket) {
        float alpha = MathUtils.clamp(1f - (age / life), 0f, 1f);
        batch.setColor(color.r, color.g, color.b, alpha);

        float drawX, drawY;

        if (followBasket && basket != null) {
            float anchorX = basket.getCenterX();
            float anchorY = basket.getTopY();
            drawX = anchorX + localX - size / 2f;
            drawY = anchorY + localY - size / 2f;
        } else {
            drawX = localX - size / 2f;
            drawY = localY - size / 2f;
        }

        batch.draw(Assets.getInstance().whitePixel, drawX, drawY, size, size);
        batch.setColor(Color.WHITE);
    }
}
