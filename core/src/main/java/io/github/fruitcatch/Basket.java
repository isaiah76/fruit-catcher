package io.github.fruitcatch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Basket {
    private Sprite sprite;
    private Rectangle bounds;
    private boolean facingRight = true;
    private boolean isMoving = false;
    private float moveSpeed = 4.25f;
    private Vector2 touchPos = new Vector2();

    public Basket(float worldWidth) {
        sprite = new Sprite(Assets.getInstance().basketIdle);
        sprite.setSize(1f, 1f);
        sprite.setOriginCenter();
        sprite.setPosition(worldWidth / 2f - 0.5f, 0.5f);
        bounds = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update(float delta, Viewport viewport, boolean inputBlocked) {
        float minX = 0;
        float maxX = viewport.getWorldWidth() - sprite.getWidth();
        isMoving = false;

        if (inputBlocked) {
            sprite.setRegion(Assets.getInstance().basketIdle);
            return;
        }

        handleInput(delta, viewport);

        sprite.setRegion(
            isMoving ? Assets.getInstance().basketMove : Assets.getInstance().basketIdle
        );

        boolean flipX = !facingRight;
        sprite.setFlip(flipX, false);
        sprite.setX(MathUtils.clamp(sprite.getX(), minX, maxX));
        bounds.setPosition(sprite.getX(), sprite.getY());
    }

    private void handleInput(float delta, Viewport viewport) {
        // Keyboard
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            sprite.translateX(moveSpeed * delta);
            facingRight = true;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            sprite.translateX(-moveSpeed * delta);
            facingRight = false;
            isMoving = true;
        }

        // Touch
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);

            float center = sprite.getX() + sprite.getWidth() / 2f;
            float diff = touchPos.x - center;

            sprite.setCenterX(touchPos.x);

            if (diff > 0) facingRight = true;
            if (diff < 0) facingRight = false;
            isMoving = true;
        }
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getY() {
        return sprite.getY();
    }

    public float getTopY() {
        return sprite.getY() + sprite.getHeight();
    }

    public float getCenterX() {
        return sprite.getX() + sprite.getWidth() / 2f;
    }
}
