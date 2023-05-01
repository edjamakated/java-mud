import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private BufferedReader input;
    private PrintWriter output;

    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = input.readLine()) != null) {
                System.out.println("Received message: " + message);
                server.broadcast(message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.removeClient(this);
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }
}