package net.verany.api.socket;

import lombok.Getter;
import lombok.SneakyThrows;
import net.verany.api.AbstractVerany;
import net.verany.api.event.events.SocketInEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@Getter
public class SocketServer {

    private ServerSocket serverSocket;
    private final Socket clientSocket;

    @SneakyThrows
    public SocketServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Socket server ready!");
        } catch (Exception e) {
            System.out.println("Initializing error. Try changing port number!" + e);
        }
        clientSocket = serverSocket.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String message;
        while (true) {
            message = in.readLine();
            AbstractVerany.eventManager.execute(new SocketInEvent(message));
        }

    }
}
