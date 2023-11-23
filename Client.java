


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static final double NUM_CLIENTS = 10;

    public static void main(String[] args) {

        String serverAddress = AppServer.getServerAddress();
        int serverPort = AppServer.getServerPort();



        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("Inserisci la figura (quadrato, cerchio, rettangolo) seguita dalle misure:");
                String s = scanner.nextLine().toLowerCase();

                String[] tokens = s.split(" ");

                if (tokens.length == 2 && (tokens[0].equals("quadrato") || tokens[0].equals("cerchio"))) {
                    out.println(s);

                } else if (tokens.length == 3 && tokens[0].equals("rettangolo")) {
                    out.println(s);

                } else {
                    System.out.println("Input non valido. Assicurati di inserire la figura corretta e le misure necessarie.");
                }

                double result = Double.parseDouble(in.readLine());
                System.out.println("Area: " + result);
            }

        } catch (UnknownHostException unknownHostException) {
            System.out.println(serverAddress + " sconosciuto");
        } catch (ConnectException connectException){
            System.out.println("Server not active.");
            System.exit(0);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("eccezione durante la creazione della socket");
        }
    }
}