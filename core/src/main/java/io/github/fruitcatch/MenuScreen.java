package io.github.fruitcatch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MenuScreen implements Screen {
    private final Main game;
    private static final float BASE_WIDTH = 800f;
    private static final float BASE_HEIGHT = 500f;
    private final FitViewport viewport;
    private final Stage stage;
    private Texture background;
    private Texture buttonTexture;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private Music music;

    public MenuScreen(Main game) {
        this.game = game;

        viewport = new FitViewport(BASE_WIDTH, BASE_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);

        try {
            background = new Texture(Gdx.files.internal("menu.png"));
        } catch (Exception e) {
            Pixmap p = new Pixmap(1, 1, Format.RGB888);
            p.setColor(Color.DARK_GRAY);
            p.fill();
            background = new Texture(p);
            p.dispose();
        }

        createFonts();
        createUI();
        createMusic();
        Gdx.input.setInputProcessor(stage);
    }

    private void createFonts() {
        float scaleFactor = 1f;
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("Gluten-Regular.ttf"));
        FreeTypeFontParameter p = new FreeTypeFontParameter();
        p.size = Math.round(72 * scaleFactor);
        p.minFilter = Texture.TextureFilter.Linear;
        p.magFilter = Texture.TextureFilter.Linear;
        p.hinting = FreeTypeFontGenerator.Hinting.Full;
        titleFont = gen.generateFont(p);
        p.size = Math.round(36 * scaleFactor);
        buttonFont = gen.generateFont(p);
        gen.dispose();
    }

    private void createUI() {
        Pixmap pix = new Pixmap(300, 72, Format.RGBA8888);
        pix.setColor(0.12f, 0.6f, 0.2f, 1f);
        pix.fill();
        buttonTexture = new Texture(pix);
        pix.dispose();
        TextureRegionDrawable btnDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label.LabelStyle scoreStyle = new Label.LabelStyle(buttonFont, new Color(1f, 0.85f, 0.1f, 1f));

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.up = btnDrawable;
        Pixmap pixDown = new Pixmap(300, 72, Format.RGBA8888);
        pixDown.setColor(0.08f, 0.45f, 0.15f, 1f);
        pixDown.fill();
        Texture downTex = new Texture(pixDown);
        pixDown.dispose();
        tbs.down = new TextureRegionDrawable(new TextureRegion(downTex));
        tbs.font = buttonFont;
        tbs.fontColor = Color.WHITE;

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label title = new Label("Fruit Catcher", titleStyle);

        int highScore = game.getHighScore();
        Label scoreLabel = new Label("High Score: " + highScore, scoreStyle);

        TextButton startBtn = new TextButton("Start", tbs);
        TextButton exitBtn = new TextButton("Exit", tbs);

        startBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopMusicIfPlaying();
                Gdx.input.setInputProcessor(null);
                startBtn.setDisabled(true);
                game.setScreen(new GameScreen(game));
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopMusicIfPlaying();
                Gdx.input.setInputProcessor(null);
                exitBtn.setDisabled(true);
                Gdx.app.exit();
            }
        });

        table.add(title).padTop(30f).row();
        table.add(scoreLabel).padTop(10f).row();
        table.add().height(20f).row();
        table.add(startBtn).width(300f).height(72f).padTop(10f).row();
        table.add(exitBtn).width(300f).height(72f).padTop(12f).row();

        stage.addActor(table);
    }

    private void createMusic() {
        try {
            music = Gdx.audio.newMusic(Gdx.files.internal("menu_music.wav"));
            music.setLooping(true);
            music.setVolume(0.6f);
            music.play();
        } catch (Exception e) {
            Gdx.app.log("MenuScreen", "Couldn't load/play music: " + e.getMessage());
            music = null;
        }
    }

    private void stopMusicIfPlaying() {
        if (music != null && music.isPlaying()) {
            music.stop();
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        stage.getBatch().begin();
        if (background != null) {
            stage.getBatch().draw(background, 0, 0, BASE_WIDTH, BASE_HEIGHT);
        }
        stage.getBatch().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            stopMusicIfPlaying();
            Gdx.input.setInputProcessor(null);
            game.setScreen(new GameScreen(game));
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        stopMusicIfPlaying();
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (titleFont != null) titleFont.dispose();
        if (buttonFont != null) buttonFont.dispose();
        if (background != null) background.dispose();
        if (buttonTexture != null) buttonTexture.dispose();
        if (music != null) music.dispose();
    }
}
