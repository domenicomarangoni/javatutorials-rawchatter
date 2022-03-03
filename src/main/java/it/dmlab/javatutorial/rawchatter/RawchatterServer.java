package it.dmlab.javatutorial.rawchatter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import it.dmlab.javatutorial.rawchatter.session.RawchatterClientSession;

public class RawchatterServer extends Thread{

	private boolean goAhead=true;
	private ServerSocket srv;
	private Set<RawchatterClientSession> sessions=new HashSet<RawchatterClientSession>();
	
	
	public RawchatterServer(int listeningPort) throws IOException{
		System.out.println("Starting Rawchatter server v. 0.0.9");
		srv=new ServerSocket(listeningPort);
	} 
	
	public void run() {
		while(goAhead) {
			try {
				Socket clientSocket=srv.accept();
				System.out.println("Received connection from "+clientSocket.getRemoteSocketAddress());
				RawchatterClientSession session = new RawchatterClientSession(this, clientSocket);
				sessions.add(session);
				forwardMessage("A new client connected");
				session.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void forwardMessage(String message) {
		sessions.forEach(s -> s.sendMessage(message));
	}
	
	public void removeClientSession(RawchatterClientSession session) {
		if(sessions.contains(session)) {
			System.out.println(session.getUser() +" quitted");
			sessions.remove(session);
		}
	}
	
}
