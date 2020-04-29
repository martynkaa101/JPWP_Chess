package game.pieces;

import game.Game;
import game.Movement;

public abstract class Piece {                                                   //Implementuje ją każda figura
    private final Color color;
    private final String filename;
    private boolean moved = false;

    public Piece(Color color, String filename) {
        this.color = color;
        this.filename = filename;
    }

    public Piece() {
        color = Color.NONE;
        filename = "none";
    }

    public Color getColor() {
        return color;
    }

    public String getFilename() {
        return filename;
    }

    public abstract boolean isValidMovement(Game game, Movement movement);      //Musi być zaimplementowana w dziedziczących

    public void setMoved() {
        moved = true;
    }

}