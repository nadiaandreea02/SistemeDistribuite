package com.example.sistemedistribuite.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class Heartbeat implements Runnable {

    private Socket socket;
    private int interval;
    private boolean running;
    private long totalResponseTime;
    private int numResponses;

    public Heartbeat(Socket socket, int interval) {
        this.socket = socket;
        this.interval = interval;
        this.running = true;
        this.totalResponseTime = 0;
        this.numResponses = 0;
    }

    public void stopHeartbeat() {
        this.running = false;
    }

    public synchronized long getAverageResponseTime() {
        return (numResponses > 0) ? totalResponseTime / numResponses : 0;
    }

    @Override
    public void run() {
        try {
            while (running) {
                long startTime = System.currentTimeMillis();
                socket.getOutputStream().write(("heartbeat\n").getBytes());
                String response = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                long endTime = System.currentTimeMillis();
                long responseTime = endTime - startTime;

              //  System.out.println("Heartbeat response time: " + responseTime + "ms");

                synchronized (this) {
                    totalResponseTime += responseTime;
                    numResponses++;
                }

                Thread.sleep(interval);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}


