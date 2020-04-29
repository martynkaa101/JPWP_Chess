package game.pieces;

import game.Board;
import game.Game;
import game.Movement;
import game.Position;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color, "bishop");
    }

    @Override
    public boolean isValidMovement(Game game, Movement movement) {
        if (Math.abs(movement.getRowOffset()) != Math.abs(movement.getColOffset())) {
            return false;
        }
        Board board = game.getBoard();
        //ROZWIAZANIE ZADANIA 3

        for(int i = 1; i < Math.abs(movement.getRowOffset()); i++) {
            int rows = i * (movement.getRowOffset() < 0 ? -1 : 1);
            int cols = i * (movement.getColOffset() < 0 ? -1 : 1);
            Position tmp = new Position(movement.getOriginRow() + rows, movement.getOriginCol() + cols);
            if((board.at(tmp) instanceof None) == false) {
                return false;
            }
        }
        return true;
    }

}