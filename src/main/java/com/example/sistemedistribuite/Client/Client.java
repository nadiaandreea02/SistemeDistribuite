package com.example.sistemedistribuite.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
   public static int count = 0;
    public static   long totalTime = 0;
    private static final String SERVER_IP = "192.168.1.3";
    private static final int SERVER_PORT = 9090;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Create a new instance of the Heartbeat class
            Heartbeat heartbeat = new Heartbeat(socket, 5000);

            // Create a new Thread object and start the heartbeat thread
            Thread heartbeatThread = new Thread(heartbeat);
            heartbeatThread.start();

            // Citim datele de la tastatură și le trimitem către server
            String name;
            do {
                System.out.print("Introduceti numele (sau bye pentru a iesi): ");
                name = scanner.nextLine();
                out.println(name);
            } while (!name.equals("bye"));

            // Așteptăm finalizarea firului de execuție și afișăm timpul mediu de răspuns
            heartbeat.stopHeartbeat();
            heartbeatThread.join();
            System.out.println("Timpul mediu de raspuns al serverului: " + (heartbeat.getAverageResponseTime()) + " ms");
        } catch (IOException | InterruptedException e) {
            System.err.println("Eroare la conectarea la server!");
            e.printStackTrace();
        }
}}


