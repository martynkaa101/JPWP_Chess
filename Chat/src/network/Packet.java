package network;

public class Packet {
    //public PacketType type;

    public String username;
    public String message;

    @Override
    public String toString() {
        return "Packet{" +
                //"packetType=" + type +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
