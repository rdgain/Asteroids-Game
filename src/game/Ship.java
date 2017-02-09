package game;

import utilities.*;
import static game.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Ship extends GameObject {
    static final int RADIUS = 8;
    static final double STEER_RATE = 2* Math.PI;  // in radians per second
    double bulletDelay = 10;
    static final double READY_TO_FIRE = 10;

    // magnitude of acceleration when thrust is applied
    static final double MAG_ACC = 200;

    // constant speed loss factor
    static final double LOSS = 0.99;

    // direction in which ship is turning
    // this is a "unit" vector (so magnitude is 1)
    Vector2D d;

    KeyController ctrl;
    Image image = Constants.SHIP1;
    Action action;

    int shield;
    double energy;

    public Ship(AsteroidsGame game, KeyController ctrl) {
        super();
        d = new Vector2D();
        this.game = game;
        this.ctrl = ctrl;
        action = ctrl.action(game);
        shield = 100;
        energy = 100; //shield cost: 60; 0.1 regeneration / frame
        reset();
    }

    public void reset() {
        s.set(FRAME_WIDTH / 2, FRAME_HEIGHT / 2);
        v.set(0,0);
        d.set(0,-1);
    }

    public double radius() {
        return RADIUS;
    }

    public void update() {
        d.rotate(action.turn*STEER_RATE*DT);
        d.normalise();
        v.add(d, MAG_ACC * DT * action.thrust);
        v.mult(LOSS);
        s.add(v,DT);
        s.wrap(FRAME_WIDTH,FRAME_HEIGHT);

        if(action.shoot && bulletDelay == READY_TO_FIRE)
            this.shootBullet();

        if (bulletDelay == 0)
            resetBulletDelay();
        else
            bulletDelay--;

        if (action.thrust == 1) {
            SoundManager.startThrust();
        }
        else {
            SoundManager.stopThrust();
        }

        if (ctrl.shieldActive && energy >= 60) {
            shield = 100;
            energy -= 60;
            ctrl.shieldActive = false;
        }
        if (shield > 0) shield--;
        if (energy < 99.9) {
            energy += 0.1;
            if (energy > 100)
                energy = 100;
        }
    }

    public void draw(Graphics2D g) {

        double imW = image.getWidth(null);
        double imH = image.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.rotate(d.x, d.y, 0, 0);
        t.scale(8*radius()/imW,8*radius()/imH);
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(s.x, s.y);
        g.drawImage(image, t, null);
        g.setTransform(t0);
    }

    public void hit() {
        if (game.getLives() > 0 && shield == 0) {
            game.decLives();
            this.reset();
        }
        else if (shield == 0) {
            dead = true;
            SoundManager.play(SoundManager.bangMedium);
        }
    }

    private void shootBullet() {
        Vector2D bV = new Vector2D(v);
        Bullet b = new Bullet(this);
        bV.add(d, b.MUZZLE_SPEED);
        game.add(b);
        SoundManager.fire();
    }

    private void resetBulletDelay() {
        bulletDelay = 10;
    }
}