package io.github.fruitcatch;

public enum FruitType {
    APPLE("red-apple.png", 0.40f, 2, 3.2f, 160f),
    ORANGE("orange.png", 0.45f, 2, 3.3f, 150f),
    BLACKBERRY("black-berry-light.png", 0.40f, 3, 3.9f, 220f),
    STRAWBERRY("strawberry.png", 0.30f, 3, 3.6f, 200f),
    LEMON("lemon.png", 0.45f, 2, 3.4f, 170f),
    CHERRY("red-cherry.png", 0.25f, 1, 3.8f, 320f),
    WATERMELON("watermelon.png", 0.85f, 5, 2.6f, 80f),
    BANANA("banana.png", 0.45f, 2, 3.1f, 140f);

    public final String textureName;
    public final float width;
    public final int score;
    public final float baseSpeed;
    public final float maxRotation;

    FruitType(String textureName, float width, int score, float baseSpeed, float maxRotation) {
        this.textureName = textureName;
        this.width = width;
        this.score = score;
        this.baseSpeed = baseSpeed;
        this.maxRotation = maxRotation;
    }
}
