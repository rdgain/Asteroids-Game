package game;

import utilities.*;

import java.awt.*;
import java.io.IOException;

public class Constants {

    public final static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    public final static GraphicsDevice device = env.getScreenDevices()[0];
    public static final Rectangle RECTANGLE = device.getDefaultConfiguration().getBounds();
    public static final int WIDTH = RECTANGLE.width;
    public static final int HEIGHT = RECTANGLE.height;

    // frame dimensions
	//public static final int FRAME_HEIGHT = 480;
	//public static final int FRAME_WIDTH = 640;
    public static final int FRAME_HEIGHT = HEIGHT;
    public static final int FRAME_WIDTH = WIDTH;
	public static final Dimension FRAME_SIZE = new Dimension(
			Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
	
	// background color
	public static final Color BG_COLOR = Color.BLACK;

	// constants relating to frame rate
	public static final int DELAY = 20;
	public static final double DT = DELAY / 1000.0;
	
	// number of asteroids on 1st level
	public static final int N_INITIAL_ASTEROIDS = 3;

    public static Image ASTEROID1, MILKYWAY1, SHIP1, ENERGY, LIFE, SAUCER, TOP, STARTUP, GAMEOVER;
    static {
        try {
            ASTEROID1 = ImageManager.loadImage("asteroid3");
            MILKYWAY1 = ImageManager.loadImage("milkyway1");
            SHIP1 = ImageManager.loadImage("sheep1");
            ENERGY = ImageManager.loadImage("energy");
            LIFE = ImageManager.loadImage("life");
            SAUCER = ImageManager.loadImage("saucer");
            TOP = ImageManager.loadImage("top");
            STARTUP = ImageManager.loadImage("startup");
            GAMEOVER = ImageManager.loadImage("gameover");
        } catch (IOException e) { System.exit(1); }
    }
}