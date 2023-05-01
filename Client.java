import java.io.*;
import java.net.*;

public class Client {
    private String host;
    private int port;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public Client(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        socket = new Socket(host, port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    public String receiveMessage() throws IOException {
        return input.readLine();
    }

    public void closeConnection() throws IOException {
        socket.close();
    }
}
