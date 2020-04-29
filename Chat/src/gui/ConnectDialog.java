package gui;

import conversation.Conversation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ConnectDialog extends JDialog {
    private final JTextField usernameField;
    private final JTextField ipField;
    private final JLabel usernameLabel;
    private final JLabel ipLabel;
    private final JButton connectButton;
    private final JButton hostButton;
    private boolean succeeded;

    public ConnectDialog(Frame parent, Conversation conversation) {
        super(parent, "Connect", true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        usernameLabel = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(usernameLabel, cs);

        usernameField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(usernameField, cs);

        ipLabel = new JLabel("Server IP: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(ipLabel, cs);

        ipField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(ipField, cs);
        panel.setBorder(new LineBorder(Color.GRAY));

        connectButton = new JButton("Connect");

        connectButton.addActionListener(e -> {
            if (getUsername().equals("")) {
                JOptionPane.showMessageDialog(ConnectDialog.this,
                        "Username cannot be empty",
                        "Connect",
                        JOptionPane.ERROR_MESSAGE);
            } else if (conversation.getNetwork().startClient(getIp())) {
                JOptionPane.showMessageDialog(ConnectDialog.this,
                        "Hi " + getUsername() + "! You have successfully connected to game.",
                        "Connect",
                        JOptionPane.INFORMATION_MESSAGE);
                succeeded = true;
                conversation.setUsername(getUsername());
                dispose();
            } else {
                JOptionPane.showMessageDialog(ConnectDialog.this,
                        "Unable to connect to server",
                        "Connect",
                        JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                ipField.setText("");
                succeeded = false;
            }
        });

        hostButton = new JButton("Host");
        hostButton.addActionListener(e -> {
            if (getUsername().equals("")) {
                JOptionPane.showMessageDialog(ConnectDialog.this,
                        "Username cannot be empty",
                        "Host",
                        JOptionPane.ERROR_MESSAGE);
            } else if (conversation.getNetwork().startServer()) {
                JOptionPane.showMessageDialog(ConnectDialog.this,
                        "Hi " + getUsername() + "! You have successfully hosted a chat.",
                        "Host",
                        JOptionPane.INFORMATION_MESSAGE);
                succeeded = true;
                conversation.setUsername(getUsername());
                dispose();
            } else {
                JOptionPane.showMessageDialog(ConnectDialog.this,
                        "Unable to host a game",
                        "Host",
                        JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                ipField.setText("");
                succeeded = false;
            }
        });

        JPanel bp = new JPanel();
        bp.add(connectButton);
        bp.add(hostButton);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getIp() {
        return ipField.getText().trim();
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
