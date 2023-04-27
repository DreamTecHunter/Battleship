import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * BattleshipController
 */
public class BattleshipController {
    // todo: initiation
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    BattleshipModel model;

    public BattleshipController(BattleshipModel model) {
        this.model = model;
    }

    public String input(BattleshipView view, String... message) {
        for (int i = 0; i < message.length; i++) {
            if (i < message.length - 1) {
                view.println(message[i]);
            } else {
                view.print(message[i]);
            }
        }
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Controller-part

    public boolean menu(BattleshipView view, String input) {
        int choice = input.toCharArray()[0];
        switch (choice) {
            case 'x':
                view.exit();
                break;
            case '0':
                view.play();
                break;
            default:
                break;
        }
        return true;
    }

    public void play(BattleshipView view) {
        model.start();
        view.playing();
    }

    enum PLAYING_STATE{
        EXIT, PLAY, UNDO, REDO
    }

    public PLAYING_STATE playingQuestion(BattleshipView view, String input) {
        try {
            char choice = input.toCharArray()[0];
            switch (choice) {
                case 'x':
                    return PLAYING_STATE.EXIT;
                case 'u':
                    model.undo();
                    return PLAYING_STATE.UNDO;
                case 'r':
                    model.redo();
                    return PLAYING_STATE.REDO;
                default:
                    break;
            }
        } catch (IndexOutOfBoundsException ex) {

        }
        return PLAYING_STATE.PLAY;
    }

    public void shoot(String xInput, String yInput) throws OutOfBoarderException {
        model.shoot(Integer.parseInt(xInput), Integer.parseInt(yInput));
    }


}
