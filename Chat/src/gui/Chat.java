package gui;

import conversation.Conversation;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Chat extends JPanel {

    private final JEditorPane chat;                                                                 //element, ktory wyswietla htmla
    private final HTMLDocument document;
    private final Element body;
    private final JTextField chatInput;
    Conversation conversation;

    public Chat(Conversation conversation) {
        this.conversation = conversation;
        chat = new JEditorPane("text/html", "<html><body id='body'></body></html>");
        chat.setBackground(Color.LIGHT_GRAY);
        chat.setEditable(false);
        document = (HTMLDocument) chat.getDocument();
        body = document.getElement("body");

        JScrollPane scrollPane = new JScrollPane(chat);                                             //opakowuje czat w skrolowalny widok
        scrollPane.setViewportView(chat);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        add(scrollPane);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        chatInput = new JTextField(22);
        constraints.anchor = GridBagConstraints.WEST;
        inputPanel.add(chatInput, constraints);

        JButton sendButton = new JButton("Send");
        constraints.anchor = GridBagConstraints.EAST;
        inputPanel.add(sendButton, constraints);

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {                                                    //poczatek akcji wyslania wiadomosci
                sendMessage(chatInput.getText());
                chatInput.setText("");
            }
        };

        sendButton.addActionListener(action);
        chatInput.addActionListener(action);

        add(inputPanel);
        setBorder(BorderFactory.createTitledBorder("Chat"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void sendMessage(String message) {
        chatInput.grabFocus();

        if (message.isEmpty()) {
            return;
        }
        /*
        if (!conversation.getNetwork().message(conversation.getUsername(), message)) {
            return;
        }

         */

        addMessage(conversation.getUsername(), message);
    }

    public void addMessage(String username, String message) {
        try {
            document.insertBeforeEnd(body, "<p><b>" + username + ": </b>" + message + "</p>");  //doklejamy htmla do tego obiektu
            chat.setCaretPosition(document.getLength());
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }
    }
}
