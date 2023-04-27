import java.util.List;

public class BattleshipMessage {
    enum ACTION {
        REDRAW, GAMEOVER
    }

    private ACTION action;
    private List<List<Position>> positions;
    private List<AShip> fleet;
    private List<AShip> destroyed;

    public BattleshipMessage(ACTION action, List<List<Position>> positions, List<AShip> fleet, List<AShip> destroyed) {
        this.action = action;
        this.positions = positions;
        this.fleet = fleet;
        this.destroyed = destroyed;
    }

    public ACTION getAction() {
        return action;
    }

    public List<List<Position>> getPositions() {
        return positions;
    }

    public List<AShip> getFleet() {
        return fleet;
    }

    public List<AShip> getDestroyed() {
        return destroyed;
    }
}
