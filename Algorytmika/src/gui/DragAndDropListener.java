package gui;

import game.Movement;
import game.Position;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DragAndDropListener implements MouseListener, MouseMotionListener {

    private BoardPanel boardPanel;

    private boolean dragging;
    private int startRow;
    private int startCol;
    private int dragOffsetX;
    private int dragOffsetY;

    public DragAndDropListener(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {        //pobieramy na ktorym kwadracie kliknelismy
        startRow = getRow(e);
        startCol = getCol(e);
        dragOffsetX = e.getPoint().x - BoardPanel.SQUARE_SIZE * getCol(e);  //zeby dokladnie odwzorowac miejsce klikniecia
        dragOffsetY = e.getPoint().y - BoardPanel.SQUARE_SIZE * getRow(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (dragging) {
            boardPanel.postDrag(new Movement(startRow, startCol, getRow(e), getCol(e)));
        }
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {     //czy jestesmy w trakcie przesuwqania
            boardPanel.dragPiece(e.getPoint().x - dragOffsetX, e.getPoint().y - dragOffsetY);   //przenosi na dany xy
        } else {
            boardPanel.preDrag(new Position(startRow, startCol), e.getPoint().x - dragOffsetX, e.getPoint().y - dragOffsetY);
            dragging = true;
        }
    }

    private int getRow(MouseEvent e) {
        return e.getPoint().y / BoardPanel.SQUARE_SIZE;
    }   //na podstawie pikseli zwraca ktory kwadrat

    private int getCol(MouseEvent e) {
        return e.getPoint().x / BoardPanel.SQUARE_SIZE;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}