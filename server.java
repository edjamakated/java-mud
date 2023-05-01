import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private ArrayList<ClientHandler> clients;
    private ExecutorService executorService;

    public Server(int port) {
        this.port = port;
        clients = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(10);
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
            ClientHandler clientHandler = new ClientHandler(clientSocket, this);
            clients.add(clientHandler);
            executorService.execute(clientHandler);
        }
    }

    public synchronized void broadcast(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}