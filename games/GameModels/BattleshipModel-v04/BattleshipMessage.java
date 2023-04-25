import java.util.List;

public class BattleshipMessage {
    enum ACTION {
        REDRAW, GAMEOVER
    }

    private ACTION action;
    private List<List<Position>> positions;
    private List<AShip> fleet;

    public BattleshipMessage(ACTION action, List<List<Position>> positions, List<AShip> fleet) {
        this.action = action;
        this.positions = positions;
        this.fleet = fleet;
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
}
