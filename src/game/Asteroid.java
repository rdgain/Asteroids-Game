package game;

import utilities.*;
import static game.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Asteroid extends GameObject {
    public static final int BIG_RADIUS = 30;
    public static final int MEDIUM_RADIUS = 20;
    public static final int SMALL_RADIUS = 10;
    public static final double MAX_SPEED = 100;
    public static final int BIG = 0;
    public static final int MEDIUM = 1;
    public static final int SMALL = 2;

    public int size;
    public int radius;
    Image image = Constants.ASTEROID1;

    public Asteroid() {
        super();
    }

    public Asteroid(Vector2D s, Vector2D v, AsteroidsGame game) {
        this.s = new Vector2D(s);
        this.v = new Vector2D(v);
        this.game = game;
    }

    public Asteroid(Vector2D s, Vector2D v, AsteroidsGame game, int size) {
        this.s = new Vector2D(s);
        this.v = new Vector2D(v);
        this.game = game;
        this.size = size;
    }

    public static Asteroid makeRandomAsteroid(AsteroidsGame game) {
        return new Asteroid(
                new Vector2D(
                        BIG_RADIUS + Math.random() * (FRAME_WIDTH - 2 * BIG_RADIUS),
                        BIG_RADIUS + Math.random() * (FRAME_HEIGHT - 2 * BIG_RADIUS)
                ),
                new Vector2D(
                        Math.random() * MAX_SPEED,
                        Math.random() * MAX_SPEED
                ),
                game,
                BIG
        );
    }

    public double radius() {
        switch (size) {
            case BIG: radius = BIG_RADIUS; break;
            case MEDIUM: radius = MEDIUM_RADIUS; break;
            case SMALL: radius = SMALL_RADIUS; break;
        }
        return radius;
    }

    public void update() {
        s.add(v, Constants.DT);
        s.add(FRAME_WIDTH, FRAME_HEIGHT);
        s.wrap(FRAME_WIDTH, FRAME_HEIGHT);
    }

    public void draw(Graphics2D g) {
        double imW = image.getWidth(null);
        double imH = image.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.rotate(180, 0, 0);
        t.scale(2*radius()/imW,2*radius()/imH);
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(s.x, s.y);
        g.drawImage(image, t, null);
        g.setTransform(t0);
    }

    public void hit() {
        Vector2D v2 = new Vector2D(v);
        v2.mult(-1);
        switch (size) {
            case BIG:
                size = MEDIUM;
                game.add(new Asteroid(s,v2,game,size));
                game.add(new Asteroid(s,v,game,size));
                SoundManager.play(SoundManager.bangSmall);
                game.incScore(100);
                break;
            case MEDIUM:
                size = SMALL;
                game.add(new Asteroid(s,v2,game,size));
                game.add(new Asteroid(s,v,game,size));
                SoundManager.play(SoundManager.bangSmall);
                game.incScore(200);
                break;
            case SMALL:
                dead = true;
                SoundManager.play(SoundManager.bangMedium);
                game.incScore(500);
                break;
        }

    }

}