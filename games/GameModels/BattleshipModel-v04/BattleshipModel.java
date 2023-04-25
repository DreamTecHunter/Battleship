import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * BattleshipModel
 * observer-pattern
 */
public class BattleshipModel extends Observable {
    // todo: Everything needed vor the observer-pattern (observable) and the initiation

    List<List<Position>> board;
    List<AShip> fleet;

    public BattleshipModel() {
        init();
    }

    private void init() {
        fleet = ShipFactory.generate(BOARD_WIDTH, BOARD_HEIGHT, 3, 2, 1);
        board = new ArrayList<List<Position>>();
        List<Position> yArea;
        for (int x = 0; x < BOARD_WIDTH; x++) {
            yArea = new ArrayList<Position>();
            for (int y = 0; y < BOARD_WIDTH; y++) {
                yArea.add(new Position(x, y, Color.BLUE));
            }
            board.add(yArea);
        }
        running = true;
    }

    public void notify(BattleshipMessage.ACTION action) {
        setChanged();
        notifyObservers(new BattleshipMessage(action, getTurnedAxis(), fleet));
    }

    // todo: Model-part

    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = BOARD_WIDTH;
    public boolean running;

    private boolean checkIfInBoarder(int x, int y) {
        return 0 <= x && x < BOARD_WIDTH && 0 <= y && y < BOARD_HEIGHT;
    }

    private List<List<Position>> getTurnedAxis() {
        List<List<Position>> positions = new ArrayList<List<Position>>();
        for (int y = 0; y < board.get(0).size(); y++) {
            positions.add(new ArrayList<Position>());
        }
        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.get(x).size(); y++) {
                positions.get(y).add(board.get(x).get(y));
            }
        }
        return positions;
    }

    // todo: functions with notify-method-call

    public void start() {
        notify(BattleshipMessage.ACTION.REDRAW);
    }


    public void shoot(int x, int y) throws OutOfBoarderException {
        if (!running) {
            return;
        }
        if (checkIfInBoarder(x, y)) {
            Position p = new Position(x, y, null);
            AShip currentShip;
            List<Position> currentShipPositions = null;
            boolean remove;
            int _x, _y;
            for (int i = 0; i < fleet.size(); i++) {
                currentShip = fleet.get(i);
                try {
                    if (currentShip.isColliding(p)) {
                        board.get(x).get(y).setColor(Color.YELLOW);
                        currentShipPositions = currentShip.getShipPositions();
                        remove = true;
                        for (int j = 0; j < currentShipPositions.size(); j++) {
                            _x = currentShipPositions.get(j).getX();
                            _y = currentShipPositions.get(j).getY();
                            if (board.get(_x).get(_y).getColor() != Color.yellow) {
                                remove = false;
                            }
                        }
                        if (remove) {
                            fleet.remove(i);
                            return;
                        }
                    } else {
                        board.get(x).get(y).setColor(Color.GREEN);
                    }
                } catch (DirectionException e) {
                    System.out.println(e.getMessage());
                }
            }
            notify(BattleshipMessage.ACTION.REDRAW);
        } else {
            throw new OutOfBoarderException("Coordinates are out of boarder!");
        }
        if (fleet.size() == 0) {
            running = false;
            notify(BattleshipMessage.ACTION.GAMEOVER);
        }
    }

}
