package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientManagerThread
extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private String m_ID;
    int position;

    ClientManagerThread(Socket s2, PrintWriter p2) {
        this.socket = s2;
        this.writer = p2;
    }

	@Override
	public void run() {
		super.run();
		try {
			BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String text;
			
			while(true) {
				System.out.println("waiting new input in thread");
				text = buff.readLine();

				System.out.println(m_ID + "> " + text);
				if(text == null) {
					System.out.println(m_ID + " exited from server");
					for(int i = 0; i < 4; i++) {
						Server.clientFilled.get(i).println(m_ID + " exited from server");
						Server.clientFilled.get(i).flush();
					}
					break;
				}
			
			String[] split = text.split("195727");
			System.out.println(split[0] + " " + split[1]);
			
						
			
			}
						
			Server.clientFilled.remove(writer);
			System.out.println("current connection:" + Server.clientFilled.size());
			for(int i = 0; i < Server.clientFilled.size(); i++) {
				if (Server.clientFilled.get(i) != null) {
					Server.clientFilled.get(i).println(Server.clientFilled.size());
					Server.clientFilled.get(i).flush();
				}
			}
			socket.close();
		} catch (Exception e) {
			Server.clientFilled.remove(writer);
			System.out.println("current connection:" + Server.clientFilled.size());
			e.printStackTrace();
		}
	}
}

