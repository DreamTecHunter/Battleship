import javax.print.event.PrintJobAttributeListener;
import java.util.Stack;

public class RedoBattleshipModelCommand implements IBattleshipModelCommand {
    private BattleshipModel model;

    public RedoBattleshipModelCommand(BattleshipModel model) {
        this.model = model;
    }

    @Override
    public void execute() {
        Stack<BattleshipStackElement> undoHistory = model.getUndoHistory();
        if (undoHistory.empty()) {
            return;
        }
        model.addHistory(
                new BattleshipStackElement(
                        BattleshipModel.deepCopyPositions(model.board),
                        BattleshipModel.deepCopyShips(model.fleet),
                        BattleshipModel.deepCopyShips(model.destroyed)
                )
        );
        BattleshipStackElement element = undoHistory.pop();
        model.board = element.getBoard();
        model.fleet = element.getFleet();
        model.destroyed = element.getDestroyed();
    }
}
