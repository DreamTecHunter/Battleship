import java.awt.*;

public class Position {
    private int x;
    private int y;
    private Color color;

    public Position() {
        this(-1, -1, null);
    }

    public Position(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("(%d %d)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        Position p = (Position) obj;
        return x == p.x && y == p.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
