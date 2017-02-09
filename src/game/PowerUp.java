package game;

import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import static game.Constants.*;

public class PowerUp extends GameObject {
    Image imageEnergy = Constants.ENERGY;
    Image imageLife = Constants.LIFE;

    int kind; // 0 - energy; 1 - life;
    int ttl;

    public PowerUp (int kind) {
        this.kind = kind;
        ttl = 300;
        v.set(0,0);
        s.set(new Vector2D((Math.random()*FRAME_WIDTH + 100) % FRAME_WIDTH,
                Math.random() * FRAME_HEIGHT));
    }

    @Override
    double radius() {
        return 25;
    }

    @Override
    void update() {
        if (ttl > 0) {
            ttl--;
        }
        else dead = true;
    }

    @Override
    void draw(Graphics2D g) {
        double imW, imH;
        AffineTransform t, t0;
        if (kind == 0) {
            imW = imageEnergy.getWidth(null);
            imH = imageEnergy.getHeight(null);
            t = new AffineTransform();
            t.scale(2*radius()/imW,2*radius()/imH);
            t.translate(-imW/2.0, -imH/2.0);
            t0 = g.getTransform();
            g.translate(s.x, s.y);
            g.drawImage(imageEnergy, t, null);
            g.setTransform(t0);
        }
        else {
            imW = imageLife.getWidth(null);
            imH = imageLife.getHeight(null);
            t = new AffineTransform();
            t.scale(2*radius()/imW,2*radius()/imH);
            t.translate(-imW/2.0, -imH/2.0);
            t0 = g.getTransform();
            g.translate(s.x, s.y);
            g.drawImage(imageLife, t, null);
            g.setTransform(t0);
        }
    }
}
