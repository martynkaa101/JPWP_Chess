package game;

import game.pieces.Bishop;
import game.pieces.None;
import game.pieces.Pawn;
import game.pieces.Piece;

import static game.pieces.Color.BLACK;
import static game.pieces.Color.WHITE;

public class Board {
    private Piece[][] board = new Piece[8][8];

    public Board() {
        reset();
    }

    public Piece at(Position position) {        //zwraca pionek na danej pozycji
        int row = position.getRow();
        int col = position.getCol();
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return new None();
        }

        return board[row][col];
    }

    public void move(Movement movement) {    //faktycznie przestawia na poziomie gry, a nie interfejsu
        board[movement.getTargetRow()][movement.getTargetCol()] = at(movement.getOrigin());
        board[movement.getOriginRow()][movement.getOriginCol()] = new None();
    }   //zwraca czy sie udalo przesunac pionek

    public void reset() {   //poczatkowe ustawienie na planszy
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = new None();
            }
        }

        board[3][3] = new Bishop(BLACK);
        board[1][1] = new Pawn(WHITE);
        board[6][6] = new Pawn(WHITE);
        board[5][1] = new Pawn(BLACK);

    }
}