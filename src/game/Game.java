package game;

public interface Game {
    public Iterable<GameObject> getGameObjects();
    public void add(GameObject obj);
    public void incScore(int s);
    public int getScore();
}