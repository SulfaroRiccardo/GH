import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.Date;

public class AppServer {

    // we could refactor these two variables into a class named ServerInfo.
    private static final int serverPort = 8080;

    private static final String serverAddress = "localhost";


    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Server in ascolto su porta: " + serverPort);
            serverSocket.setSoTimeout(12000);
            manageConnections(serverSocket);
        } catch (IOException ioe) {
            System.out.println("[CalculatorServer]: IOException");
        }

    }

    public void manageConnections(ServerSocket server) {
        while (true) {
            try {
                Socket client = server.accept();
                System.out.println("nuovo client connesso: " + client.getInetAddress() + " " + Date.from(Instant.now()));
                new Thread(new ClientHandler(client)).start();
            } catch (IOException e) {
                System.out.println("nessuna connessione al client ancora avvenuta");
            }
        }
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    private class ClientHandler implements Runnable {
        private final Socket clientToManage;
        private PrintWriter outToClient;
        private BufferedReader inFromClient;

        public ClientHandler(Socket clientToManage) {
            this.clientToManage = clientToManage;
            getClientStreams();
        }

        public void getClientStreams() {
            try {
                inFromClient = new BufferedReader(new InputStreamReader(clientToManage.getInputStream()));
                outToClient = new PrintWriter(clientToManage.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("errore durante l'ottenimento degli streem. chisura del socket");
                try {
                    clientToManage.close();
                } catch (IOException ex) {
                    System.out.println("errore durante la chiusura della socket");
                }
            }
        }

        @Override
        public void run() {
            try (clientToManage) {
while (true){
                String s = inFromClient.readLine();
                String[] tokens = s.split(" ");

                double area = 0.0;
                if (tokens[0].equals("quadrato")) {
                    double lato = Double.parseDouble(tokens[1]);
                    area = lato * lato;
                } else if (tokens[0].equals("cerchio")) {
                    double raggio = Double.parseDouble(tokens[1]);
                    area = Math.PI * raggio * raggio;
                } else if (tokens[0].equals("rettangolo")) {
                    double lato1 = Double.parseDouble(tokens[1]);
                    double lato2 = Double.parseDouble(tokens[2]);
                    area = lato1 * lato2;
                }
                outToClient.println(area);
            }
            } catch (IOException e) {
                System.out.println("errore durante la comunicazione con il client");
            }
        }
    }

}