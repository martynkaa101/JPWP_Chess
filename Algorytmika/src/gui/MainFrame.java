package gui;

import game.Game;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final Game game = new Game();

    private final BoardPanel boardPanel = new BoardPanel(game);

    public MainFrame() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);


        constraints.gridx = 0;      //tu plansza
        constraints.gridy = 0;
        constraints.gridwidth = 4;
        constraints.gridheight = 4;
        layout.setConstraints(boardPanel, constraints);
        add(boardPanel);

        setTitle("Chess");
        setResizable(false);
        pack();
        setLocationRelativeTo(null);  // Center the window
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
