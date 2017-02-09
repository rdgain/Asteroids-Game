package game;

import utilities.Vector2D;

import java.awt.*;

public class Particle extends GameObject {
    public static final int RADIUS = 1;
    public static final Color COLOR = Color.red;
    double ttl;

    public Particle(AsteroidsGame game, Vector2D s, double ttl) {
        super(game, new Vector2D(s),
                new Vector2D(Math.random(),
                        Math.random()));
        this.ttl = ttl;
    }

    @Override
    public void update() {
        s.add(v);
        v.mult(0.99);
        ttl--;
        if (ttl < 0) dead = true;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(COLOR);
        g.fillOval((int) s.x - RADIUS, (int) s.y - RADIUS,
                2 * RADIUS, 2 * RADIUS);
    }

    @Override
    public double radius() {
        return RADIUS;
    }

}