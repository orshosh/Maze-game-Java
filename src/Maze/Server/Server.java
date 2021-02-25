package Server;

import Server.IServerStrategy;


import java.io.Console;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    private int port;
    private int listeningInterval;
    private IServerStrategy serverStrategy;
    private volatile boolean stop;
    ThreadPoolExecutor threadPoolExecutor = null;



    public Server(int port, int listeningInterval, IServerStrategy serverStrategy) {
        this.port = port;
        this.listeningInterval = listeningInterval;
        this.serverStrategy = serverStrategy;
        this.threadPoolExecutor =  (ThreadPoolExecutor) Executors.newCachedThreadPool();
        threadPoolExecutor.setCorePoolSize(Configurations.setPool());
        threadPoolExecutor.setMaximumPoolSize(Configurations.setPool());
    }

    public void start() {
        threadPoolExecutor.execute(()->{
            runServer();
        });
    }

    private void runServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningInterval);
            System.out.println(String.format("Server starter at %s!", serverSocket));
            System.out.println(String.format("Server's Strategy: %s", serverStrategy.getClass().getSimpleName()));
            System.out.println("Server is waiting for clients...");
            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept(); // blocking call
                    System.out.println(String.format("Client excepted: %s", clientSocket));
                    new Thread(() -> {
                        synchronized (this) {
                            handleClient(clientSocket);
                            System.out.println(String.format("Finished handle client: %s", clientSocket));
                        }
                    }).start();

                } catch (SocketTimeoutException e) {
                    System.out.println("Socket Timeout - No clients pending!");
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            System.out.println(String.format("Handling client with socket: %s", clientSocket.toString()));
            serverStrategy.ServerStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
    public void close(){
        threadPoolExecutor.shutdown(); //shutdown executor
        try {

            threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES); //wait maximum one hour for all tasks to complete. After one hour, exit.

        } catch (InterruptedException e) {
            System.out.println("Error await termination for ThreadPool");
        }

    }

    public void stop() {
        System.out.println("Stopping server..");
        stop = true;
        threadPoolExecutor.shutdown(); //shutdown executor
        try {

            threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES); //wait maximum one hour for all tasks to complete. After one hour, exit.

        } catch (InterruptedException e) {
            System.out.println("Error await termination for ThreadPool");
        }
        //close();
    }

}
