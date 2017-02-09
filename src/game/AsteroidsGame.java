package game;

import java.util.ArrayList;
import java.util.ListIterator;
import utilities.*;
import static java.lang.System.exit;

public class AsteroidsGame implements Game {

    ArrayList<GameObject> gameObjects, alive, pending, particles;
    KeyController ctrl;
    int score, lives, nAsteroids, newAsteroid, newSaucer, newPowerUp, newLife, wait;
    boolean gameRunning;
    Ship ship;

    public AsteroidsGame() {
        score = 0;
        lives = 10;
        newAsteroid = 800;
        newLife = 5000;
        newSaucer = (int) (Math.random() * 3500);
        newPowerUp = (int) (Math.random() * 2000);
        nAsteroids = Constants.N_INITIAL_ASTEROIDS;
        gameObjects = new ArrayList<GameObject>();
        alive = new ArrayList<GameObject>();
        pending = new ArrayList<GameObject>();
        particles = new ArrayList<GameObject>();
        ctrl = new KeyController();
        gameRunning = true;

        ship = new Ship(this, ctrl);
        gameObjects.add(ship);
        for (int i = 0; i < nAsteroids; i++) {
            gameObjects.add(Asteroid.makeRandomAsteroid(this));
        }

        wait = 2000;
    }

    public static void main(String[] args) throws Exception {
        AsteroidsGame game = new AsteroidsGame();
        View view = new View(game);
        new JEasyFrameFull(view).addKeyListener(game.ctrl);

        // game loop
        while (true) {
            //determine end of game
            if ((game.noAsteroids() && game.noSaucers()) || game.lives == 0) {
                game.gameRunning = false;
            }
            else
            if (!game.ctrl.waiting) game.wait = 0;
            if (game.wait == 0 && game.gameRunning) {
                game.update();
            }
            else
                if (game.wait>0) game.wait--;
            if (!game.ctrl.fullScreen) {
                exit(1);
            }
            view.repaint();
            Thread.sleep(Constants.DELAY);
        }
    }

    public void update() {
        newAsteroid--;
        newPowerUp--;
        newSaucer--;
        newLife--;

        for (GameObject i : gameObjects) {
            synchronized(this) {
                i.update();
            }
            if  (i instanceof Ship || i instanceof Asteroid || i instanceof Saucer) {
                checkCollision(i);
            }
            if (!i.dead) {
                alive.add(i);
            }
        }

        // check if it's time to add new asteroids, saucers, power-ups or lives and reset their timers
        if (newAsteroid == 0) {
            add(Asteroid.makeRandomAsteroid(this));
            newAsteroid = 800;
        }
        if (newSaucer == 0) {
            add(new Saucer(this, ctrl, ship));
            newSaucer = (int) (Math.random() * 3500);
        }
        if (newPowerUp == 0) {
            add (new PowerUp((int) Math.random() * 10 % 2));
            newPowerUp = (int) (Math.random() * 2000);
        }
        if (newLife == 0) {
            incLives();
            newLife = 5000;
        }

        synchronized (this) {
            gameObjects.clear();
            gameObjects.addAll(alive);
            gameObjects.addAll(pending);
        }
        pending.clear();
        alive.clear();

        updateParticles();
    }

    @Override
    public Iterable<GameObject> getGameObjects() {
        return gameObjects;
    }

    @Override
    public void add(GameObject obj) {
        pending.add(obj);
    }

    /**
     * increment the score by s
     * @param s - int
     */
    @Override
    public void incScore(int s) {
        score += s;
    }

    /**
     * @return - int, the player's score
     */
    @Override
    public int getScore() {
        return score;
    }

    /**
     * @return - int, the player's lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * increase the number of lives left
     */
    public void decLives() {
        lives--;
    }

    /**
     * decrease the number of lives left
     */
    public void incLives() {
        lives++;
    }

    /**
     * generate an explosion
     * @param s - Vector2D, position of the explosion
     * @param n - int, number of particles
     * @param ttl - int, particles' time to live
     */
    public void explosion(Vector2D s, int n, int ttl) {
        for (int i = 0; i < n; i++)
            particles.add(new Particle(this, s, (1 + Math.random() * ttl )));
    }

    public void updateParticles() {
        // iterate over the set of particles, removing any dead ones
        ListIterator<GameObject> it = particles.listIterator();
        synchronized (this) {
            while (it.hasNext()) {
                Particle p = (Particle) it.next();
                p.update();
                if (p.dead) {
                    it.remove();
                }
            }
        }
    }

    public ArrayList<GameObject> getParticles() {
        return particles;
    }

    public void checkCollision(GameObject object) {
        // check with other game objects
        if ( ! object.dead) {
            for (GameObject otherObject : gameObjects) {
                //collisions between the same type of objects,
                // between the ship and the ship’s bullets,
                // between asteroids and saucers and
                // between asteroids and saucer bullets are ignored
                // between saucers and saucer bullets
                // the Ship - PowerUp collision is treated separately
                if (object.getClass()!= otherObject.getClass()
                        && overlap(object, otherObject)
                        && !(object instanceof Ship && otherObject instanceof Bullet && ((Bullet)otherObject).kind == 0)
                        && !(object instanceof Asteroid && otherObject instanceof Saucer)
                        && !(object instanceof Asteroid && otherObject instanceof Bullet && ((Bullet)otherObject).kind == 1)
                        && !(object instanceof Saucer && otherObject instanceof Asteroid)
                        && !(object instanceof Saucer && otherObject instanceof Bullet && ((Bullet)otherObject).kind == 1)
                        && !(otherObject instanceof PowerUp)) {
                    // the object's hit, and the other is also
                    if (object instanceof Ship || otherObject instanceof Ship) {
                        explosion(object.s,100,200);
                    }
                    object.hit();
                    otherObject.hit();
                    return;
                }
                if (object instanceof Ship && otherObject instanceof PowerUp && overlap(object,otherObject)) {
                    if (((PowerUp)otherObject).kind == 0) { // energy PowerUp
                        ship.energy += 40;
                        if (ship.energy > 100)
                            ship.energy = 100;
                    }
                    else { // life PowerUp
                        incLives();
                    }
                    otherObject.hit();
                    return;
                }
            }
        }
    }

    public boolean overlap(GameObject x, GameObject y) {
        // returns true if the distance between their centres is less than
        // the sum of the object radii
        if (x instanceof Saucer)
            return x.s.dist(y.s) <= 8*x.radius() + y.radius();
        if (y instanceof Saucer)
            return x.s.dist(y.s) <= x.radius() + 8*y.radius();
        return x.s.dist(y.s) <= x.radius() + y.radius();
    }

    public boolean noAsteroids() {
        for (GameObject i : gameObjects) {
            if (i instanceof Asteroid) return false;
        }
        return true;
    }

    public boolean noSaucers() {
        for (GameObject i : gameObjects) {
            if (i instanceof Saucer) return false;
        }
        return true;
    }
}