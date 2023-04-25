public class BattleshipPlayground {
    public static void main(String[] args) {
        BattleshipModel model = new BattleshipModel();
        BattleshipController controller = new BattleshipController(model);
        BattleshipView view = BattleshipView.getInstance();
        view.init("Battleship", controller, model);
        view.start();
    }
}
