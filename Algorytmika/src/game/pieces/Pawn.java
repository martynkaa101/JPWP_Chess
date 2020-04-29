package game.pieces;
import game.Game;
import game.Movement;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color, "pawn");
    }

    @Override
    public boolean isValidMovement(Game game, Movement movement) {

        return false;
    }
}

