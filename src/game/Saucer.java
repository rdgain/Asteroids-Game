package game;

import utilities.*;
import static game.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class Saucer extends GameObject {

    public static final Color COLOR_BODY = Color.GREEN;
    public static final Color COLOR_BELT = Color.WHITE;
    public static final int HEIGHT = 12;
    public static final int WIDTH = 24;
    Vector2D d;
    double bulletDelay = 50;
    static final double READY_TO_FIRE = 50;

    Controller ctrl;
    AsteroidsGame game;
    Action action;
    Ship ship;

    Image image = Constants.SAUCER;

    public Saucer(AsteroidsGame game, KeyController ctrl, Ship ship) {
        super();
        this.game = game;
        this.ctrl = ctrl;
        this.ship = ship;
        action = ctrl.action(game);
        d = new Vector2D();
        s.set(Math.random()*FRAME_WIDTH, Math.random()*FRAME_HEIGHT);
        v.set(Math.random()*100,Math.random()*100);
        d.set(1,0);
    }

    @Override
    public void update() {
        Vector2D p = this.to(ship);
        d.rotate(- p.theta() * action.thrust * DT);
        d.normalise();
        v.add(d, 100 * DT);
        v.mult(ship.LOSS);
        s.add(v,DT);
        s.wrap(FRAME_WIDTH,FRAME_HEIGHT);

        if(bulletDelay == READY_TO_FIRE)
            this.shootBullet();

        if (bulletDelay == 0)
            resetBulletDelay();
        else
            bulletDelay--;
    }


    @Override
    public void draw(Graphics2D g) {

        double imW = image.getWidth(null);
        double imH = image.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.scale(8*radius()/imW,8*radius()/imH);
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(s.x, s.y);
        g.drawImage(image, t, null);
        g.setTransform(t0);
    }

    public void shootBullet() {

        Vector2D bV = new Vector2D(v);
        Bullet b = new Bullet(this);
        bV.add(d, b.MUZZLE_SPEED);
        game.add(b);
        SoundManager.saucerFire();
    }

    @Override
    double radius() {
        return HEIGHT/2;
    }

    private void resetBulletDelay() {
        bulletDelay = 50;
    }
}