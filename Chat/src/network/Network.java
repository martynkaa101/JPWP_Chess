package network;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import conversation.Conversation;

import java.io.IOException;
import java.net.*;

public class Network {
    private final static int PORT = 9999;
    private DatagramSocket socket;
    private final byte[] buffer = new byte[4096];
    private InetAddress friendsAddress;
    private int friendsPort = -1;
    private Conversation conversation;

    public Network(Conversation conversation) {
        this.conversation = conversation;
    }


    public void run() {
        while (true) {
            if (receive()) {
                System.out.println("Closing network");
                socket.close();
                break;
            }
        }
    }

    //TODO uzupełnij poniższą metodę i dobierz odpowiedni wyjątek
    public boolean startServer() {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            return false;
        }
        return true;
    }

    public boolean startClient(String ip) {
        try {
            socket = new DatagramSocket();
            friendsAddress = InetAddress.getByName(ip);
            friendsPort = PORT;
        } catch (SocketException | UnknownHostException e) {
            System.out.println("Unable to start client: " + e);
            return false;
        }

        Packet packet = new Packet();
        return send(packet);
    }

    private boolean send(Packet packet) {
        String json = new Gson().toJson(packet);
        System.out.println("Sending to " + friendsAddress + ":" + friendsPort + " : " + json);

        byte[] bytes = json.getBytes();
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, friendsAddress, friendsPort);

        try {
            socket.send(dp);
        } catch (IOException e) {
            System.out.println("Exception while sending packet: " + e);
            return false;
        }

        return true;
    }

    //TODO STWÓRZ METODĘ message() w podanym poniżej formacie
    /**public boolean message(String username, String message){(...)return send(packet);}//
     Powinna ona przygotowywać do przesłania pakiet z odpowiednio przypisanymi argumentami
     (odwołanie do tej metody jest w klasie Chat w linii 69).
     **/
    public boolean message(String username, String message){
        Packet packet = new Packet();
        packet.type = PacketType.MESSAGE;
        packet.username = username;
        packet.message = message;
        return send(packet);
    }

    public boolean receive() {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        String data;

        System.out.println("Awaiting packet");

        try {
            socket.receive(dp);
            data = new String(dp.getData(), 0, dp.getLength());
            System.out.println("Received from " + dp.getAddress() + ": " + data);
        } catch (IOException e) {
            System.out.println("Exception while receiving packet: " + e);
            return false;
        }

        Packet packet;
        try {
            packet = new Gson().fromJson(data, Packet.class);
        } catch (JsonParseException e) {
            System.out.println("Exception while parsing JSON: " + e);
            return false;
        }

        System.out.println("Received " + packet);

        //TODO Zaimplementuj sposób postępowania w zależności od odebranego pakietu.
        //podpowiedź - spójrz na metody w klasie Chat
        if(packet.type == PacketType.REGISTER) {
            friendsAddress = dp.getAddress();
            friendsPort = dp.getPort();
        } else if(packet.type == PacketType.MESSAGE) {
            conversation.getChat().addMessage(packet.username, packet.message);
        } else {
            return false;
        }

        return true;
    }
}
