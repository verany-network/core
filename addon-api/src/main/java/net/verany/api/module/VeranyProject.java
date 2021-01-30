package net.verany.api.module;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.module.VeranyModule.DatabaseConnection;
import net.verany.api.socket.SocketClient;
import net.verany.api.socket.SocketServer;

import java.io.Serializable;

@Getter
@Setter
public abstract class VeranyProject implements Serializable {

    private VeranyModule module;
    private DatabaseConnection connection;

    public abstract void enable();

    public abstract void disable();

    public SocketServer getNewServer(int port) {
        return new SocketServer(port);
    }

    public SocketClient getNewClient(String address, int port) {
        return new SocketClient(address, port);
    }

}
