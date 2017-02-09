package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyController extends KeyAdapter implements Controller {

    Action action;
    boolean fullScreen;
    boolean shieldActive;
    boolean waiting;

    public KeyController() {
        action = new Action();
        fullScreen = true;
        waiting = true;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                action.thrust = 1;
                break;
            case KeyEvent.VK_LEFT:
                action.turn = -1;
                break;
            case KeyEvent.VK_RIGHT:
                action.turn = +1;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = true;
                break;
            case KeyEvent.VK_ESCAPE:
                fullScreen = false;
                break;
            case KeyEvent.VK_S:
                shieldActive = true;
                break;
            case KeyEvent.VK_ENTER:
                waiting = false;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                action.thrust = 0;
                break;
            case KeyEvent.VK_LEFT:
                action.turn = 0;
                break;
            case KeyEvent.VK_RIGHT:
                action.turn = 0;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = false;
                break;
            case KeyEvent.VK_ESCAPE:
                fullScreen = true;
                break;
            case KeyEvent.VK_S:
                shieldActive = false;
                break;
            case KeyEvent.VK_ENTER:
                waiting = true;
                break;
        }
    }

    @Override
    public Action action(Game game) {
        return action;
    }
}
