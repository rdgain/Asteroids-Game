package game;

import utilities.*;

import java.awt.*;

abstract public class GameObject {

    AsteroidsGame game;
    // position, velocity
    Vector2D s, v;
    boolean dead = false;

    public GameObject () {
        this.s = new Vector2D();
        this.v = new Vector2D();
    }

    public GameObject (AsteroidsGame game, Vector2D s, Vector2D v) {
        this.game = game;
        this.s = s;
        this.v = v;
    }

    public void hit () {
        dead = true;
    }

    //returns distance between the current object and o
    public double dist (GameObject o) {
        return Math.sqrt((this.s.x - o.s.x)*(this.s.x - o.s.x) + (this.s.y - o.s.y)*(this.s.y - o.s.y));
    }

    public Vector2D to(GameObject o) {
        return new Vector2D(o.s.x-s.x, o.s.y-s.y);
    }

    abstract double radius();

    abstract void update();

    abstract void draw(Graphics2D g);



}
