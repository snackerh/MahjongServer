/*
 * Decompiled with CFR 0_132.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import server.ClientManagerThread;
import server.InputThread;

public class Server {
    public static int connection;
    public static ArrayList<PrintWriter> clientFilled;
    public static ArrayList<String> clientIDs;
    public static BufferedReader buff;
    public static PrintWriter writer;
    public static String prevStatus;
    public static String newStatus;
    public static String nearStatus;
    public static String currStatus;
    public static ServerSocket server;

    public static void main(String[] args) {
        try {
            try {
                System.out.println("Starting Server for 9004 port...");
                connection = 0;
                clientFilled = new ArrayList<PrintWriter>();
                clientIDs = new ArrayList<String>();
                int i2 = 0;
                while (i2 < 4) {
                    clientFilled.add(null);
                    clientIDs.add(null);
                    ++i2;
                }
                InputThread inp = new InputThread();
                inp.start();
                server = new ServerSocket(9004);
                do {
                    Socket socket = server.accept();
                    System.out.println("New Connection Established");
                    writer = new PrintWriter(socket.getOutputStream());
                    buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    if (connection == 4) {
                        System.out.println("New connection refused; Server is full");
                        writer.println("Server>Refused");
                        writer.close();
                        buff.close();
                        continue;
                    }
                    ClientManagerThread cThread = new ClientManagerThread(socket, writer);
                    cThread.start();
                } while (true);
            }
            catch (Exception e2) {
                e2.printStackTrace();
                try {
                    server.close();
                }
                catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        catch (Throwable throwable) {
            try {
                server.close();
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
            throw throwable;
        }
    }
}

