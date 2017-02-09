package game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import static game.Constants.*;

public class View extends JComponent {
	AsteroidsGame game;
    Image im = Constants.MILKYWAY1;
    Image top = Constants.TOP;
    Image startup = Constants.STARTUP;
    Image gameover = Constants.GAMEOVER;
    AffineTransform bgTransf;
    double imW, imH, scalingFactor;

    public View(AsteroidsGame game) {
		this.game = game;
        double imWidth = im.getWidth(null);
        double imHeight = im.getHeight(null);
        double stretchx = (imWidth > Constants.FRAME_WIDTH? 1 :
                Constants.FRAME_WIDTH/imWidth);
        double stretchy = (imHeight > Constants.FRAME_HEIGHT? 1 :
                Constants.FRAME_HEIGHT/imHeight);
        bgTransf = new AffineTransform();
        bgTransf.scale(stretchx, stretchy);
        imW = Constants.TOP.getWidth(null);
        imH = Constants.TOP.getHeight(null);
        scalingFactor = FRAME_WIDTH/imW;
        top = top.getScaledInstance(FRAME_WIDTH, (int)(imH/scalingFactor),Image.SCALE_SMOOTH);
        startup = startup.getScaledInstance((int)(startup.getWidth(null)/scalingFactor),
                (int)(startup.getHeight(null)/scalingFactor),Image.SCALE_SMOOTH);

	}

	@Override
	public void paintComponent(Graphics g0) {
        try{
		Graphics2D g = (Graphics2D) g0;
		// paint the background
		g.setColor(Constants.BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(im, bgTransf,null);
        // paint the interface background
        g.drawImage(top, 0, 0, null);

        // paint the particles
        synchronized (game) {
            for (GameObject p : game.getParticles())
                p.draw(g);
        }

        //paint the game objects
		for (GameObject i : game.gameObjects) {
            // draw ship shield if shield is active
            if (i instanceof Ship && ((Ship)i).shield>0) {
                Ellipse2D ellipse = new Ellipse2D.Double (i.s.x-32/(int)scalingFactor,
                        i.s.y-28/(int)scalingFactor,
                        4*i.radius()+30/(int)scalingFactor,
                        4*i.radius()+30/(int)scalingFactor);
                g.setColor(Color.YELLOW);
                g.fill(ellipse);
            }
            // draw game objects
			i.draw(g);
		}

        // paint the player's score and number of lives
        g.setColor(Color.WHITE);
        g.drawString("Score: " + game.getScore(), 100/(int)scalingFactor, 27/(int)scalingFactor);
        g.drawString("Lives: ", 100/(int)scalingFactor, 43/(int)scalingFactor);
        int i = 0;
        while (i<game.getLives()) {
            g.drawImage(Constants.LIFE.getScaledInstance(20/(int)scalingFactor,20/(int)scalingFactor,Image.SCALE_SMOOTH),
                    140/(int)scalingFactor + i*25, 28/(int)scalingFactor, null);
            i++;
        }

        // paint the player's ship's energy
        g.drawString("Energy: ", 760/(int)scalingFactor, 30/(int)scalingFactor);
        g.fillRect (650/(int)scalingFactor, 40/(int)scalingFactor, 3*(int)game.ship.energy, 5);

        // paint an indicator of whether the shield is active
        if (game.ship.shield > 0)
            g.drawString("Shield activated", (int)(1225/scalingFactor), 35/(int)scalingFactor);

        // if the game ended, paint a 'GAME OVER' message
        if (!game.gameRunning) {
            g.drawImage(gameover, (int)(FRAME_WIDTH/2 - gameover.getWidth(null)/scalingFactor/2),
                    (int)(FRAME_HEIGHT/2 - gameover.getHeight(null)/scalingFactor/2), null);
        }

        // if the initial delay is still going, paint the startup screen
        if (game.wait > 0) {
            g.drawImage(startup, 380/(int)scalingFactor, 180/(int)scalingFactor, null);
        }
        }
        catch (Exception e) {}
	}

	@Override
	public Dimension getPreferredSize() {
		return Constants.FRAME_SIZE;
	}
}