package io.github.fruitcatch;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Fruit {
    private Sprite sprite;
    private float speed;
    private float rotSpeed;
    public int scoreValue;

    public Fruit(FruitType type, float x, float y) {
        Texture t = Assets.getInstance().fruitTextures.get(type.textureName);
        sprite = new Sprite(t);
        float aspect = (float) t.getWidth() / t.getHeight();
        float h = type.width / aspect;
        sprite.setSize(type.width, h);
        sprite.setOriginCenter();
        sprite.setPosition(x, y);
        this.speed = type.baseSpeed * MathUtils.random(0.9f, 1.18f);
        float baseRot = MathUtils.random(-type.maxRotation, type.maxRotation);
        this.rotSpeed = baseRot * (this.speed / 3.5f);
        this.scoreValue = type.score;
    }

    public void update(float delta) {
        sprite.translateY(-speed * delta);
        sprite.rotate(rotSpeed * delta);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public float getY() {
        return sprite.getY();
    }
}
