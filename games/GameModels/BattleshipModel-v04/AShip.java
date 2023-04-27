import java.util.ArrayList;
import java.util.List;

/**
 * AShip
 * factory-pattern
 */
public abstract class AShip {
    private Position head;
    private Direction direction;
    private int length;

    public AShip() {
        this(null, null, -1);
    }

    public AShip(Position head, Direction direction, int length) {
        this.head = head;
        this.direction = direction;
        this.length = length;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()
                + "\thead:\t" + this.head
                + "\tdirection:\t" + this.direction
                + "\tlength:\t" + this.length;
    }

    public List<Position> getShipPositions() throws DirectionException {
        int dx = 0;
        int dy = 0;
        switch (direction) {
            case UP:
                dy -= 1;
                break;
            case RIGHT:
                dx += 1;
                break;
            case DOWN:
                dy += 1;
                break;
            case LEFT:
                dx -= 1;
                break;
            default:
                throw new DirectionException("Direction not defined!");
        }
        List<Position> positions = new ArrayList<Position>();
        for (int l = 0; l < length; l++) {
            positions.add(new Position(head.getX() + dx * l, head.getY() + dy * l, null));
        }
        return positions;
    }

    public Boolean isColliding(Position position) throws DirectionException {
        List<Position> thisPositions = getShipPositions();
        for (int j = 0; j < thisPositions.size(); j++) {
            if (position.equals(thisPositions.get(j))) {
                return true;
            }
        }
        return false;
    }

    public Boolean isColliding(AShip ship) throws DirectionException {
        List<Position> thisPositions = getShipPositions();
        List<Position> shipPosition = ship.getShipPositions();
        for (int i = 0; i < thisPositions.size(); i++) {
            for (int j = 0; j < shipPosition.size(); j++) {
                if (thisPositions.get(i).equals(shipPosition.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Position getHead() {
        return head;
    }

    public void setHead(Position head) {
        this.head = head;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
