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

    public String conString() {
    	return Server.clientIDs.get(0) + "#%" + Server.clientIDs.get(1) + "#%" + Server.clientIDs.get(2) + "#%" + Server.clientIDs.get(3);  
    }
    
	@Override
	public void run() {
		super.run();
		try {
			BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String text;
			String prefix = "Server>";
			
			while(true) {
				System.out.println("waiting new input in thread");
				text = buff.readLine();

				System.out.println(m_ID + "> " + text);
				if(text == null) {
					System.out.println(m_ID + " exited from server");
					break;
				}
			
				String[] split = text.split("195727");
				if(split.length >= 2) {
					System.out.println(split[0] + " " + split[1]);
				}
				if(split[0].equals("pos")) {
					position = Integer.parseInt(split[1]);
					
					if(Server.clientIDs.get(position) != null) {
						writer.println("Server>Taken");
						writer.close();
						throw new Exception("taken");
					} else {
						Server.clientFilled.set(position, writer);
					}
				}
				else if(split[0].equals("ID")) {
					m_ID = split[1];
					Server.clientIDs.set(position, m_ID);
					
					Server.connection++;
					System.out.println(m_ID + " entered server");
					System.out.println(conString());
					System.out.println("current connection:" + Server.connection);
					
					for(int i = 0; i < 4; i++) {
						if (Server.clientFilled.get(i) != null) {
						Server.clientFilled.get(i).println(prefix + conString());
						Server.clientFilled.get(i).flush();
						Server.clientFilled.get(i).println(Server.connection);
						Server.clientFilled.get(i).flush();
						}
					}
				}
				else if(split[0].equals("curr") || split[0].equals("new") || split[0].equals("prev") || split[0].equals("near") || split[0].equals("middle") || split[0].equals("end")){
					setStatus(text);
				}
				
				
				for(int i = 0; i < 4; i++) {
					if (Server.clientFilled.get(i) != null) {
					Server.clientFilled.get(i).println(m_ID + ">" + text);
					Server.clientFilled.get(i).flush();
					
					}
				}
			
			}
		} catch (Exception e) {
			Server.clientFilled.remove(writer);
			System.out.println("current connection:" + Server.clientFilled.size());
			e.printStackTrace();
		} finally {
			if(Server.clientIDs.contains(m_ID)) {
				Server.clientFilled.set(position, null);
				Server.clientIDs.set(position, null);
				Server.connection--;
			}
			
			System.out.println("current connection:" + Server.connection);
			for(int i = 0; i < Server.clientFilled.size(); i++) {
				if (Server.clientFilled.get(i) != null) {
					Server.clientFilled.get(i).println(Server.connection);
					Server.clientFilled.get(i).flush();
				}
			}
			try {
			socket.close();
			} catch (Exception e) {
			}
			
		}
	}
	
	public void setStatus(String msg) {
		int i2 = 0;
        System.out.println("msg:" + msg);
        String[] split = msg.split("195727");
        
        if (msg.equals("prev")) {
        	i2 = 0;
        	for(int i = 0; i < 4; i++) {
                if (Server.clientFilled.get(i) != null) {
                    Server.clientFilled.get(i).println("Server>prev>" + Server.prevStatus);
                    Server.clientFilled.get(i).flush();
                }
            }
            Server.newStatus = Server.prevStatus;
            Server.currStatus = Server.prevStatus;
            System.out.println("far");
            System.out.println(Server.currStatus);
            System.out.println(Server.newStatus);
            System.out.println(Server.nearStatus);
            System.out.println(Server.prevStatus);
        } else if (msg.equals("middle")) {
            for(int i = 0; i < 4; i++) {
                if (Server.clientFilled.get(i) != null) {
                    Server.clientFilled.get(i).println("Server>middle>" + Server.newStatus);
                    Server.clientFilled.get(i).flush();
                }
            }
            Server.currStatus = Server.newStatus;
            System.out.println("middle");
            System.out.println(Server.currStatus);
            System.out.println(Server.newStatus);
            System.out.println(Server.nearStatus);
            System.out.println(Server.prevStatus);
        } else if (msg.equals("near")) {
        	for(int i = 0; i < 4; i++) {
                if (Server.clientFilled.get(i) != null) {
                    Server.clientFilled.get(i).println("Server>near>" + Server.nearStatus);
                    Server.clientFilled.get(i).flush();
                }
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
        } else {
        if (split.length == 2 && split[0].equals("curr")) {
            Server.nearStatus = Server.currStatus;
            Server.currStatus = split[1];
            System.out.println("curr");
            System.out.println(Server.currStatus);
            System.out.println(Server.newStatus);
            System.out.println(Server.nearStatus);
            System.out.println(Server.prevStatus);
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
        }
        if (split.length != 2 || split[0].equals("end")) {
        	Server.prevStatus = Server.newStatus;
        	Server.nearStatus = Server.currStatus;
        	Server.newStatus = split[1];
        	Server.currStatus = split[1];
        	System.out.println("end");
        	System.out.println(Server.currStatus);
        	System.out.println(Server.newStatus);
        	System.out.println(Server.nearStatus);
        	System.out.println(Server.prevStatus);
        	}
        }
	}
}

