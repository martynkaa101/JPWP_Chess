package gui;

import game.Board;
import game.Game;
import game.Movement;
import game.Position;
import game.pieces.Bishop;
import game.pieces.None;
import game.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static game.pieces.Color.BLACK;
import static game.pieces.Color.WHITE;

public class BoardPanel extends JPanel {
    final static int SQUARE_SIZE = 60;      //kwadracik

    private final Game game;
    private final Board board;

    private HashMap<String, ImageIcon> images = new HashMap<>();
    private JLayeredPane layeredPane = new JLayeredPane();      //zeby sie dalo ladnie dragowac szachy
    private JPanel panel = new JPanel(new GridLayout(8, 8));    //umozliwia ustawianie w gridzie elementow

    private JPanel[][] squares = new JPanel[8][8];      //tablica osobnych jpanelow

    public BoardPanel(Game game) {
        super(new BorderLayout());      //wyzsza klasa to j bez tego nie dziala

        this.game = game;
        this.board = game.getBoard();

        panel.setBounds(0, 0, SQUARE_SIZE * 8, SQUARE_SIZE * 8);    //glowny panel
        layeredPane.setPreferredSize(new Dimension(SQUARE_SIZE * 8, SQUARE_SIZE * 8));
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);     //glowny panel jako default layer
        DragAndDropListener dragAndDropListener = new DragAndDropListener(this);    //gdy zlapiemy pionka to rpzenosi sie na druga layer
        layeredPane.addMouseListener(dragAndDropListener);
        layeredPane.addMouseMotionListener(dragAndDropListener);
        layeredPane.setVisible(true);
        add(layeredPane, BorderLayout.CENTER);      //dziecko board panela zawiera latajace pionki
        createIcons();
    }

    public void preDrag(Position position, int x, int y) {
        Piece originPiece = board.at(position); //zwraca pionka na danej pozycji
        if (originPiece instanceof None) {
            return;
        }

        if (originPiece.getColor() == BLACK && originPiece instanceof Bishop) {
            //chowa pionka oryginalnego, duplikuje i przesuwa

            squares[position.getRow()][position.getCol()].getComponent(0).setVisible(false);    //bierzemy klikniety kwadracik i jego pierwszy komponent
            //znika kliknietego pionka /\
            JLabel draggedPieceImageLabel = new JLabel(getPieceIcon(originPiece));  //tworzy nowego pionka z ta sama ikonka i on jest tym przesuwanym
            draggedPieceImageLabel.setLocation(x, y);   //ustawia x i y  tam gdzei jest myszka
            draggedPieceImageLabel.setSize(SQUARE_SIZE, SQUARE_SIZE);   //dodaje do warswy tej drugiej, z latajacymi pionkami
            layeredPane.add(draggedPieceImageLabel, JLayeredPane.DRAG_LAYER);

            highlightPossibleMoves(position);
        }

    }

    public void dragPiece(int x, int y) {       //w trakcie ruszania myszka

        Component[] dragLayer = layeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER);  //bierze dzieci w drag layer
        if (dragLayer.length == 0) {    //jesli nie ma latajacych pionkow to nic nie robimy
            return;
        }
        //do post drag dac jako sprawdzenie/\

        JLabel draggedPieceImageLabel = (JLabel) dragLayer[0];  //pobieramy latajacego pionka i zmieniamy na aktualna pozycje myszki
        if (draggedPieceImageLabel != null) {
            draggedPieceImageLabel.setLocation(x, y);
        }
    }

    public void postDrag(Movement movement) {       //po puszczeniu myszki po przesuwaniu
        for (int row = 0; row < 8; row++) {         //opisuje konkretny ruch na szachownicy, a nie w pikselach
            for (int col = 0; col < 8; col++) {
                squares[row][col].setBackground(getBackgroundColor(row, col, false));   //ustawiamy z powrotem kolory tla, zeby nie byly zielone
            }
        }

        Piece originPiece = board.at(movement.getOrigin()); //pobieramy ten trzymany pod kursorem pionek, czy na pewno przeciagalismy pionek
        Component[] dragLayer = layeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER);  //bierze dzieci w drag layer
        if (originPiece instanceof None) {
            return;
        }
        if (dragLayer.length == 0) {    //jesli nie ma latajacych pionkow to nic nie robimy
            return;
        }
        JLabel draggedPieceImageLabel = (JLabel) layeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)[0];  //usuwamy latajacy pionek z warstwy drugiej
        layeredPane.remove(draggedPieceImageLabel);
        layeredPane.repaint();      //czyscimy te druga warstwe i przywracamy te pierwsza

        if (squares[movement.getOriginRow()][movement.getOriginCol()].getComponent(0) != null) {
            squares[movement.getOriginRow()][movement.getOriginCol()].getComponent(0).setVisible(true);
        }   //zeby w tej statycznej warstwie byly widoczne kwadraciki

        if (game.move(movement)) {     //w pamieci szachownicy juz zmienia w game
            move(movement);             //sprawdza czy na pewno dobry ruch i sie powiodl to move rusza na
        }
    }

    private void highlightPossibleMoves(Position position) {        //pozycja  pionka, ktory sprawdzamy
        Piece piece = board.at(position);                           //pobieramy pionka na niej
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row == position.getRow() && col == position.getCol()) { //przechodzimy po calej planszy i sprawdzamy gdzie moze stanac
                    continue;
                }
                Movement movement = new Movement(position, new Position(row, col)); //movement z pozycji pierwotnej na nowa pozycje, te potencjalna
                if (game.isValidMovement(movement)) {                  //tu zasady gry
                    squares[row][col].setBackground(getBackgroundColor(row, col, true));
                }
            }
        }
    }

    private void move(Movement movement) {      //bierze kwadracik z origina i daje na target ikonke przenosi
        JPanel originSquare = squares[movement.getOriginRow()][movement.getOriginCol()];
        JPanel targetSquare = squares[movement.getTargetRow()][movement.getTargetCol()];
        targetSquare.removeAll();
        targetSquare.add(originSquare.getComponent(0));
        targetSquare.repaint();
        originSquare.removeAll();
        originSquare.repaint();
    }

    private void createIcons() {            //wypelnia jpanele te 8x8. kazd ywidoczny kwadrat
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel square = new JPanel(new GridLayout(1, 1));
                square.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                square.setSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                square.setBackground(getBackgroundColor(row, col, false));  //czy biale czy szare ma byc
                square.add(new JLabel(getPieceIcon(board.at(new Position(row, col))))); //
                panel.add(square);
                squares[row][col] = square;
            }
        }
    }

    private Color getBackgroundColor(int row, int col, boolean highlight) {     //zwraca ustalony kolor
        if (row % 2 == col % 2) {
            return highlight ? new Color(96, 128, 96) : new Color(128, 128, 128);
        } else {
            return highlight ? new Color(192, 255, 192) : new Color(255, 255, 255);
        }
    }

    private ImageIcon getPieceIcon(Piece piece) {   //przy kazdym apdejcie sie wykonuje
        if (piece instanceof None) {
            return new ImageIcon();     //domyslna "pusta" ikona, gdy nie ma pionka
        }

        String path = "pictures/" + (piece.getColor() == WHITE ? "white" : "black") + "_" + piece.getFilename() + ".png";

        if (images.containsKey(path)) { //jesli wczensije byl wczytany
            return images.get(path);
        } else {
            Image img = new ImageIcon(path).getImage();     //wczytujemy z dysku obrazek i skalujemy przy smooth cos tam i do hasz mapy
            ImageIcon icon = new ImageIcon(img.getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH));
            images.put(path, icon);
            return icon;
        }
    }
}