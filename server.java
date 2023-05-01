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

    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                server.broadcast(message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
                clientSocket.close();
                server.removeClient(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}