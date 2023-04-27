import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

/**
 * BattleshipModel
 * observer-pattern
 * command-pattern
 */
public class BattleshipModel extends Observable {
    // todo: Everything needed vor the observer-pattern (observable) and the initiation

    List<List<Position>> board;
    List<AShip> fleet;
    List<AShip> destroyed;

    public BattleshipModel() {
        init();
    }

    private void init() {
        fleet = ShipFactory.generate(BOARD_WIDTH, BOARD_HEIGHT, 3, 2, 1);
        destroyed = new ArrayList<AShip>();
        board = new ArrayList<List<Position>>();
        history = new Stack<BattleshipStackElement>();
        undoHistory = new Stack<BattleshipStackElement>();
        undoCommand = new UndoBattleshipModelCommand(this);
        redoCommand = new RedoBattleshipModelCommand(this);
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
        notifyObservers(new BattleshipMessage(action, getTurnedAxis(), fleet, destroyed));
    }

    private Stack<BattleshipStackElement> history;
    private Stack<BattleshipStackElement> undoHistory;

    private UndoBattleshipModelCommand undoCommand;
    private RedoBattleshipModelCommand redoCommand;

    public void addHistory(BattleshipStackElement element) {
        history.add(element);
    }

    public void addUndoHistory(BattleshipStackElement element) {
        undoHistory.add(element);
    }

    public Stack<BattleshipStackElement> getHistory() {
        return history;
    }

    public Stack<BattleshipStackElement> getUndoHistory() {
        return undoHistory;
    }

    // todo: Model-part

    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = BOARD_WIDTH;
    public boolean running;

    private boolean checkIfInBoarder(int x, int y) {
        return 0 <= x && x < BOARD_WIDTH && 0 <= y && y < BOARD_HEIGHT;
    }

    private boolean checkIfUntouched(int x, int y) {
        return board.get(x).get(y).getColor().equals(Color.BLUE);
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
        history.add(new BattleshipStackElement(deepCopyPositions(board), deepCopyShips(fleet), deepCopyShips(destroyed)));
        undoHistory.clear();
        if (!running) {
            return;
        }
        if (checkIfInBoarder(x, y)) {
            if (checkIfUntouched(x, y)) {
                Position p = new Position(x, y, null);
                AShip currentShip;
                List<Position> currentShipPositions = null;
                boolean remove, collidedDestroyedShip = false, hit = false;
                int _x, _y;
                for (int i = 0; i < destroyed.size(); i++) {
                    currentShip = destroyed.get(i);
                    try {
                        if (currentShip.isColliding(p)) {
                            collidedDestroyedShip = true;
                            break;

                        }
                    } catch (DirectionException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (!collidedDestroyedShip) {
                    for (int i = 0; i < fleet.size(); i++) {
                        currentShip = fleet.get(i);
                        try {
                            if (currentShip.isColliding(p)) {
                                hit = true;
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
                                    destroyed.add(fleet.remove(i));
                                }
                            }
                            if (!hit) {
                                board.get(x).get(y).setColor(Color.GREEN);
                            }
                        } catch (DirectionException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                try {
                    checkDestroyed();
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

    public void undo() {
        undoCommand.execute();
        notify(BattleshipMessage.ACTION.REDRAW);
    }

    public void redo() {
        redoCommand.execute();
        notify(BattleshipMessage.ACTION.REDRAW);
    }

    public void checkDestroyed() throws DirectionException {
        List<Position> currentShipPositions;
        Position currentShipPosition;
        Position currentBoardPosition;
        for (int i = 0; i < destroyed.size(); i++) {
            currentShipPositions = destroyed.get(i).getShipPositions();
            for (int j = 0; j < currentShipPositions.size(); j++) {
                currentShipPosition = currentShipPositions.get(j);
                currentBoardPosition = board.get(currentShipPosition.getX()).get(currentShipPosition.getY());
                if (!currentBoardPosition.getColor().equals(Color.RED)) {
                    currentBoardPosition.setColor(Color.RED);
                }
            }
        }
    }

    public static List<List<Position>> deepCopyPositions(List<List<Position>> positions) {
        List<List<Position>> newPositions = new ArrayList<List<Position>>();
        List<Position> newColumn;
        for (int x = 0; x < positions.size(); x++) {
            newColumn = new ArrayList<Position>();
            for (int y = 0; y < positions.get(x).size(); y++) {
                newColumn.add(new Position(x, y, positions.get(x).get(y).getColor()));
            }
            newPositions.add(newColumn);
        }
        return newPositions;
    }

    public static List<AShip> deepCopyShips(List<AShip> fleet) {
        List<AShip> newFleet = new ArrayList<AShip>();
        for (int i = 0; i < fleet.size(); i++) {
            newFleet.add(fleet.get(i));
        }
        return newFleet;

    }

}
