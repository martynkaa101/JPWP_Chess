package game.pieces;

import game.Game;
import game.Movement;

public class None extends Piece {

    @Override
    public boolean isValidMovement(Game game, Movement movement) {
        return false;
    }
}
