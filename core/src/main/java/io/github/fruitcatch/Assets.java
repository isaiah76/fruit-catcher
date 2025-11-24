package io.github.fruitcatch;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

public class Assets {
    private static Assets instance;
    private AssetManager manager;
    public Texture background;
    public Texture basketIdle;
    public Texture basketMove;
    public Texture pauseIcon;
    public Texture whitePixel;
    public Texture[] scoreDigits;
    public Texture[] digitTextures;
    public ObjectMap<String, Texture> fruitTextures;
    public Sound catchSound;
    public Sound damageSound;
    public Music music;

    private Assets() {
        manager = new AssetManager();
    }

    public synchronized static Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }
        return instance;
    }

    public void load() {
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        manager.load("background.jpg", Texture.class, param);
        manager.load("idle.png", Texture.class, param);
        manager.load("move.png", Texture.class, param);
        manager.load("pause.png", Texture.class, param);

        for (int i = 0; i < 10; i++) {
            manager.load("score" + i + ".png", Texture.class, param);
            manager.load(i + ".png", Texture.class, param);
        }

        String[] fruitNames = {"red-apple.png", "banana.png", "orange.png", "black-berry-light.png", "strawberry.png", "lemon.png", "red-cherry.png", "watermelon.png"};
        for (String f : fruitNames) {
            manager.load(f, Texture.class, param);
        }

        manager.load("drop.mp3", Sound.class);
        manager.load("damage.mp3", Sound.class);
        manager.load("music.mp3", Music.class);

        manager.finishLoading();

        background = manager.get("background.jpg");
        basketIdle = manager.get("idle.png");
        basketMove = manager.get("move.png");
        pauseIcon = manager.get("pause.png");
        catchSound = manager.get("drop.mp3");
        damageSound = manager.get("damage.mp3");
        music = manager.get("music.mp3");

        scoreDigits = new Texture[10];
        digitTextures = new Texture[10];
        for (int i = 0; i < 10; i++) {
            scoreDigits[i] = manager.get("score" + i + ".png");
            digitTextures[i] = manager.get(i + ".png");
        }

        fruitTextures = new ObjectMap<>();
        for (String f : fruitNames) {
            fruitTextures.put(f, manager.get(f, Texture.class));
        }

        createWhitePixel();
    }

    private void createWhitePixel() {
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(Color.WHITE);
        px.fill();
        whitePixel = new Texture(px);
        whitePixel.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        px.dispose();
    }

    public void dispose() {
        manager.dispose();
        if (whitePixel != null) whitePixel.dispose();
    }
}
