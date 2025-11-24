package io.github.fruitcatch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {
    private final Main game;
    private FitViewport viewport;
    private Basket basket;
    private Hud hud;
    private Array<Fruit> fruits;
    private float spawnTimer = 0f;
    private Array<Particle> particles;
    private Array<FadeNumber> fadeNumbers;
    private int combo = 0;
    private float comboShakeTimer = 0f;
    private final float comboShakeDuration = 0.28f;
    private final float comboShakeMagnitude = 0.16f;
    private final float shakeFrequency = 8f;
    private boolean isGameOver = false;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(8f, 5f);
        hud = new Hud(viewport);
        basket = new Basket(8f);

        fruits = new Array<>();
        particles = new Array<>();
        fadeNumbers = new Array<>();

        if (Assets.getInstance().music != null) {
            Assets.getInstance().music.setLooping(true);
            Assets.getInstance().music.play();
        }
    }

    @Override
    public void render(float delta) {
        boolean uiCapturedInput = hud.updateInput(viewport);

        if (!hud.isPaused() && !isGameOver) {
            basket.update(delta, viewport, uiCapturedInput);
            updateFruits(delta);
            checkCollisions();
            updateEffects(delta);

            if (hud.isDead()) {
                gameOver();
            }
        }

        ScreenUtils.clear(Color.BLACK);

        float shakeX = 0f;
        if (comboShakeTimer > 0f) {
            float remaining = comboShakeTimer;
            float progress = remaining / comboShakeDuration;
            float elapsed = (comboShakeDuration - remaining);
            float phase = elapsed * shakeFrequency;
            float envelope = progress * progress;
            shakeX = (float) Math.sin(phase * MathUtils.PI) * comboShakeMagnitude * envelope;

            if (!hud.isPaused()) comboShakeTimer = Math.max(0f, comboShakeTimer - delta);
        }

        viewport.apply();
        Matrix4 origProj = viewport.getCamera().combined.cpy();
        Matrix4 shakenProj = origProj.cpy().translate(shakeX, 0f, 0f);
        game.batch.setProjectionMatrix(shakenProj);

        game.batch.begin();
        game.batch.draw(Assets.getInstance().background, 0, 0, 8f, 5f);
        basket.draw(game.batch);

        for (Fruit f : fruits) {
            f.draw(game.batch);
        }

        for (Particle p : particles) {
            p.draw(game.batch, basket);
        }

        for (FadeNumber fn : fadeNumbers) {
            fn.draw(game.batch, basket);
        }

        game.batch.end();
        game.batch.setProjectionMatrix(origProj);
        game.batch.begin();
        hud.draw(game.batch, viewport);
        game.batch.end();
    }

    private void updateFruits(float delta) {
        spawnTimer += delta;
        if (spawnTimer > 1.5f) {
            spawnFruit();
            spawnTimer = 0f;
        }

        for (Fruit f : fruits) {
            f.update(delta);
        }
    }

    private void updateEffects(float delta) {
        for (int i = particles.size - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update(delta);
            if (p.isDead()) particles.removeIndex(i);
        }

        for (int i = fadeNumbers.size - 1; i >= 0; i--) {
            FadeNumber fn = fadeNumbers.get(i);
            fn.update(delta);
            if (fn.isFinished()) fadeNumbers.removeIndex(i);
        }
    }

    private void spawnFruit() {
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();
        FruitType[] types = FruitType.values();
        FruitType selectedType = types[MathUtils.random(0, types.length - 1)];
        float x = MathUtils.random(0f, Math.max(0f, worldW - selectedType.width));
        fruits.add(new Fruit(selectedType, x, worldH));
    }

    private void checkCollisions() {
        for (int i = fruits.size - 1; i >= 0; i--) {
            Fruit f = fruits.get(i);

            if (f.getBounds().overlaps(basket.getBounds())) {
                if (f.getY() > basket.getTopY() - 0.2f) {
                    collectFruit(f);
                    fruits.removeIndex(i);
                    continue;
                }
            }

            if (f.getY() < -1f) {
                handleMiss();
                fruits.removeIndex(i);
            }
        }
    }

    private void collectFruit(Fruit f) {
        hud.addScore(f.scoreValue);
        playSafe(Assets.getInstance().catchSound);
        combo++;
        spawnParticles(0f, 0f, 12, true);
        float fadeOffsetY = basket.getTopY() - basket.getY() + 0.12f;
        float digitHeight = 0.6f;
        fadeNumbers.add(new FadeNumber(combo, 0f, fadeOffsetY, digitHeight));
    }

    private void handleMiss() {
        hud.damage(12f);
        playSafe(Assets.getInstance().damageSound);
        combo = 0;
        comboShakeTimer = comboShakeDuration;
    }

    private void spawnParticles(float offsetX, float offsetY, int count, boolean followBasket) {
        for (int i = 0; i < count; i++) {
            float vx = MathUtils.random(-1.2f, 1.2f);
            float vy = MathUtils.random(1.2f, 2.5f);
            float life = MathUtils.random(0.45f, 0.9f);
            float size = MathUtils.random(0.04f, 0.12f);
            Color c = new Color(1f, MathUtils.random(0.5f, 1f), MathUtils.random(0.3f, 0.9f), 1f);
            particles.add(new Particle(offsetX, offsetY, vx, vy, life, size, c, followBasket));
        }
    }

    private void gameOver() {
        isGameOver = true;
        game.setHighScore(hud.getScore());
        if (Assets.getInstance().music != null) Assets.getInstance().music.stop();
        game.setScreen(new MenuScreen(game));
    }

    private void playSafe(Sound s) {
        if(s != null) s.play();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); hud.reposition(viewport); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
