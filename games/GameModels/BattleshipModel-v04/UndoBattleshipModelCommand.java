import java.util.List;
import java.util.Stack;

public class UndoBattleshipModelCommand implements IBattleshipModelCommand {
    private BattleshipModel model;

    public UndoBattleshipModelCommand(BattleshipModel model) {
        this.model = model;
    }

    @Override
    public void execute() {
        Stack<BattleshipStackElement> history = model.getHistory();
        if (history.empty()) {
            return;
        }
        model.addUndoHistory(
                new BattleshipStackElement(
                        BattleshipModel.deepCopyPositions(model.board),
                        BattleshipModel.deepCopyShips(model.fleet),
                        BattleshipModel.deepCopyShips(model.destroyed)
                )
        );
        BattleshipStackElement element = history.pop();

        model.board = element.getBoard();
        model.fleet = element.getFleet();
        model.destroyed = element.getDestroyed();

    }
}
