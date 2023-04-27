import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * BattleshipView
 * singleton-pattern
 * observer-pattern
 */
public class BattleshipView implements Observer {
    // todo: Everything what is needed for the  initiation of the object, singleton- and the observer-pattern (observer-side)


    private String name;
    private BattleshipController controller;
    private static BattleshipView instance = new BattleshipView();

    private BattleshipView() {

    }

    public void init(String name, BattleshipController controller, BattleshipModel model) {
        this.name = name;
        model.addObserver(this);
        this.controller = controller;

    }

    @Override
    public void update(Observable o, Object arg) {
        BattleshipMessage msg = (BattleshipMessage) arg;
        switch (msg.getAction()) {
            case GAMEOVER:
                break;

            case REDRAW:
                try {
                    drawBoard(msg.getPositions(), msg.getFleet(), msg.getDestroyed());
                } catch (ColorException e) {
                    throw new RuntimeException(e);
                }
                break;

            default:
                break;

        }

    }

    public static BattleshipView getInstance() {
        return instance;

    }

    // todo: view-part

    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public void start() {
        println("Welcome to " + name);
        menu();
    }

    private static int menuCounter = 0;

    public void exit() {
        println("Bye. C u soon!");
        System.exit(1);
    }

    public void menu() {
        boolean repeat;
        do {
            repeat = controller.menu(this, controller.input(this, "x:\texit", "0:\tplay", "input:\t"));
        } while (repeat);

    }

    public void play() {
        println("Rules:" +
                "\n1.\tAreas marked blue/\"-\" are not hit." +
                "\n2.\tAreas marked green/\"o\" are hit." +
                "\n3.\tAreas yellow/\"+\" are hit parts of ships" +
                "\n4.\tAreas red/\"x\" are whole destroyed ships\n");
        controller.play(this);
    }

    public void playing() {
        BattleshipController.PLAYING_STATE state = BattleshipController.PLAYING_STATE.PLAY;
        do {
            println("\naction:\t" + state.name().toLowerCase() + "\n");
            try {
                if (state == BattleshipController.PLAYING_STATE.PLAY) {
                    controller.shoot(controller.input(this, "x:\t"), controller.input(this, "y:\t"));
                }
                state = controller.playingQuestion(this, controller.input(this, "x:\texit", "u:\tundo", "r:\tredo", "other:\tplay further", "input:"));
            } catch (OutOfBoarderException e) {
                println(e.getMessage());
                state = BattleshipController.PLAYING_STATE.PLAY;
            } catch (Exception ex) {
                state = BattleshipController.PLAYING_STATE.PLAY;
            } finally {
            }
        } while (state != BattleshipController.PLAYING_STATE.EXIT);
    }

    public void drawBoard(List<List<Position>> positions, List<AShip> fleet, List<AShip> destroyed) throws ColorException {
        final String colorExceptionText = "Board is missing right hit-definitions.";
        StringBuffer sb = new StringBuffer();
        sb.append("\nlasting ships:\t" + fleet.size() + "\tdestroyed ships:\t" + destroyed.size() + "\n\n");
        sb.append("\t");
        for (int x = 0; x < positions.get(0).size(); x++) {
            sb.append("\t" + x);

        }
        sb.append("\n");
        for (int y = 0; y < positions.size(); y++) {
            sb.append("\t" + y);
            for (int x = 0; x < positions.get(y).size(); x++) {
                if (positions.get(y).get(x).getColor() == null) {
                    throw new ColorException(colorExceptionText);

                } else if (positions.get(y).get(x).getColor().equals(Color.BLUE)) {
                    sb.append("\t-");

                } else if (positions.get(y).get(x).getColor().equals(Color.GREEN)) {
                    sb.append("\to");

                } else if (positions.get(y).get(x).getColor().equals(Color.YELLOW)) {
                    sb.append("\t+");

                } else if (positions.get(y).get(x).getColor().equals(Color.RED)) {
                    sb.append("\tx");

                } else {
                    throw new ColorException(colorExceptionText);

                }

            }
            sb.append("\n");

        }
        System.out.println(sb.toString());

    }


}
