package game;

import utilities.*;

import java.awt.*;

public class Bullet extends GameObject {

    Ship ship;
    Saucer saucer;
    int kind; // 0 - ship; 1 - saucer
    public static final double MUZZLE_SPEED = 5;
    public double ttl;

    public Bullet(Ship ship) {
        this.ship = ship;
        s.set(ship.s);
        v.set(ship.d);
        ttl = 100;
        kind = 0;
    }

    public Bullet(Saucer saucer) {
        this.saucer = saucer;
        s.set(saucer.s);
        v.set(saucer.d);
        ttl = 150;
        kind = 1;
    }

    public Bullet(AsteroidsGame game, Vector2D s, Vector2D v) {
        super(game,s,v);
        ttl = 100;
    }

    @Override
    double radius() {
        return 0;
    }

    @Override
    void update() {
        if (ttl==1)
            dead = true;
        else {
            ttl--;
            s.add(v,MUZZLE_SPEED);
            s.wrap(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        }
    }

    @Override
    void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawLine((int)s.x, (int)s.y, (int)s.x, (int)s.y+2);
    }
}
