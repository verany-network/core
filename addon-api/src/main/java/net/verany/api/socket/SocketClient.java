package net.verany.api.socket;

import java.net.Socket;

public class SocketClient {

    private Socket socket;

    public SocketClient(String address, int port) {
        try {
            socket = new Socket(address, port);
        } catch (Exception e) {
            System.out.println("Initializing error. Make sure that server is alive!\n" + e);
        }


    }
}
