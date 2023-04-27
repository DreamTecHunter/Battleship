import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ShipFactory
 * factory-pattern
 */
public class ShipFactory {
    private static final int ALLOWED_FAIL_AMOUNT = 100;
    private static Random rand = new Random();

    public ShipFactory() {

    }

    /**
     * @param width   boarderWidth
     * @param height  boarderHeight
     * @param amounts amount of small,medium and large ships in this order(or only small ships/ small and medium ships)
     * @return
     */
    public static List<AShip> generate(int width, int height, int... amounts) {
        List<AShip> fleet = new ArrayList<AShip>();
        int failCounter, x, xMin, xMax, y, yMin, yMax;
        AShip newShip;
        Direction direction;
        boolean save;
        for (int i = 0; i < amounts.length; i++) {
            for (int j = 0; j < amounts[i]; j++) {
                switch (i) {
                    case 0:
                        newShip = new SmallShip();
                        break;
                    case 1:
                        newShip = new MediumShip();
                        break;
                    case 2:
                        newShip = new LargeShip();
                        break;
                    default:
                        return fleet;
                }
                direction = Direction.values()[rand.nextInt(Direction.values().length)];
                newShip.setDirection(direction);
                failCounter = 0;
                do {
                    save = true;
                    xMin = 0;
                    xMax = width;
                    yMin = 0;
                    yMax = height;
                    switch (direction) {
                        case UP:
                            yMin += newShip.getLength();
                            break;
                        case RIGHT:
                            xMax -= newShip.getLength();
                            break;
                        case DOWN:
                            yMax -= newShip.getLength();
                            break;
                        case LEFT:
                            xMin += newShip.getLength();
                            break;
                        default:
                            System.err.println("Wrong Direction-object!");
                            return null;
                    }
                    x = rand.nextInt(xMin, xMax);
                    y = rand.nextInt(yMin, yMax);
                    newShip.setHead(new Position(x, y, null));
                    for (int k = 0; k < fleet.size(); k++) {
                        try {
                            if (newShip.isColliding(fleet.get(k))) {
                                save = false;
                                failCounter += 1;
                            }
                        } catch (DirectionException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (failCounter > ALLOWED_FAIL_AMOUNT) {
                        break;
                    }
                }
                while (!save);
                if (save) {
                    fleet.add(newShip);
                }
            }
        }
        return fleet;
    }
}
