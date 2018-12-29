/*
 * Decompiled with CFR 0_132.
 */
package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import server.Server;

public class InputThread
extends Thread {
    @Override
    public void run() {
        super.run();
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            do {
                int i2;
                String msg = buf.readLine();
                System.out.println("msg:" + msg);
                String[] split = msg.split("195727");
                if (split.length == 2 && split[0].equals("curr")) {
                    Server.nearStatus = Server.currStatus;
                    Server.currStatus = split[1];
                    System.out.println("curr");
                    System.out.println(Server.currStatus);
                    System.out.println(Server.newStatus);
                    System.out.println(Server.nearStatus);
                    System.out.println(Server.prevStatus);
                    continue;
                }
                if (split.length == 2 && split[0].equals("new")) {
                    Server.prevStatus = Server.newStatus;
                    Server.nearStatus = Server.currStatus;
                    Server.newStatus = split[1];
                    Server.currStatus = split[1];
                    System.out.println("new");
                    System.out.println(Server.currStatus);
                    System.out.println(Server.newStatus);
                    System.out.println(Server.nearStatus);
                    System.out.println(Server.prevStatus);
                    continue;
                }
                if (msg.equals("prev")) {
                    i2 = 0;
                    while (i2 < 4) {
                        if (Server.clientFilled.get(i2) != null) {
                            Server.clientFilled.get(i2).println("Server>prev>" + Server.prevStatus);
                            Server.clientFilled.get(i2).flush();
                        }
                        ++i2;
                    }
                    Server.newStatus = Server.prevStatus;
                    Server.currStatus = Server.prevStatus;
                    System.out.println("far");
                    System.out.println(Server.currStatus);
                    System.out.println(Server.newStatus);
                    System.out.println(Server.nearStatus);
                    System.out.println(Server.prevStatus);
                    continue;
                }
                if (msg.equals("middle")) {
                    i2 = 0;
                    while (i2 < 4) {
                        if (Server.clientFilled.get(i2) != null) {
                            Server.clientFilled.get(i2).println("Server>middle>" + Server.newStatus);
                            Server.clientFilled.get(i2).flush();
                        }
                        ++i2;
                    }
                    Server.currStatus = Server.newStatus;
                    System.out.println("middle");
                    System.out.println(Server.currStatus);
                    System.out.println(Server.newStatus);
                    System.out.println(Server.nearStatus);
                    System.out.println(Server.prevStatus);
                    continue;
                }
                if (msg.equals("near")) {
                    i2 = 0;
                    while (i2 < 4) {
                        if (Server.clientFilled.get(i2) != null) {
                            Server.clientFilled.get(i2).println("Server>near>" + Server.nearStatus);
                            Server.clientFilled.get(i2).flush();
                        }
                        ++i2;
                    }
                    Server.currStatus = Server.nearStatus;
                    if (Server.newStatus.split("::")[12].equals("new")) {
                        Server.newStatus = Server.prevStatus;
                    }
                    System.out.println("near");
                    System.out.println(Server.currStatus);
                    System.out.println(Server.newStatus);
                    System.out.println(Server.nearStatus);
                    System.out.println(Server.prevStatus);
                    continue;
                }
                if (split.length != 2 || !split[0].equals("end")) continue;
                Server.prevStatus = Server.newStatus;
                Server.nearStatus = Server.currStatus;
                Server.newStatus = split[1];
                Server.currStatus = split[1];
                System.out.println("end");
                System.out.println(Server.currStatus);
                System.out.println(Server.newStatus);
                System.out.println(Server.nearStatus);
                System.out.println(Server.prevStatus);
            } while (true);
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return;
        }
    }
}

