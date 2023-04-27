import java.util.List;

public class BattleshipStackElement {
    private List<List<Position>> board;
    private List<AShip> fleet;
    private List<AShip> destroyed;

    public BattleshipStackElement(List<List<Position>> board, List<AShip> fleet, List<AShip> destroyed) {
        this.board = board;
        this.fleet = fleet;
        this.destroyed = destroyed;
    }

    public List<List<Position>> getBoard() {
        return board;
    }

    public List<AShip> getFleet() {
        return fleet;
    }

    public List<AShip> getDestroyed() {
        return destroyed;
    }
}
