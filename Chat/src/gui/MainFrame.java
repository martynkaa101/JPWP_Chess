package gui;

import conversation.Conversation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainFrame extends JFrame {

    private final ConnectDialog connectDialog;
    private Conversation conversation = new Conversation();
    private final Chat chat = new Chat(conversation);
    public MainFrame() {
        conversation.setChat(chat);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);

        constraints.gridx = 0;                  //Umiejscowienie planszy
        constraints.gridy = 0;
        constraints.gridwidth = 4;
        constraints.gridheight = 4;
        layout.setConstraints(chat, constraints);
        add(chat);

        setTitle("Chat");
        setResizable(false);
        pack();
        setLocationRelativeTo(null);            // Wyśrodkowanie okna
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        connectDialog = new ConnectDialog(this, conversation);
        connectDialog.setVisible(true);

        if (!connectDialog.isSucceeded()) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else {
            connectDialog.setVisible(false);
            conversation.getNetwork().run();
        }
    }
}
