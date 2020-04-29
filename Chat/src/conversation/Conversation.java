package conversation;

import gui.Chat;
import network.Network;

public class Conversation {
   private Chat chat;

    private String username;

    private final Network network = new Network(this);

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Network getNetwork() {
        return network;
    }

    public Chat getChat() { return chat; }

    public void setChat(Chat chat) { this.chat = chat; }

}
