package it.dmlab.javatutorial.rawchatter.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import it.dmlab.javatutorial.rawchatter.RawchatterServer;

public class RawchatterClientSession extends Thread{

	private String user;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private RawchatterServer server;
		
	public RawchatterClientSession (RawchatterServer server, Socket clientSocket) throws IOException {
		this.socket=clientSocket;
		this.server=server;
		this.in=socket.getInputStream();
		this.out=socket.getOutputStream();
	}
	
	public void setUser(String user) {
		this.user=user;
	}

	public String getUser() {
		return this.user;
	}
	
	public void sendMessage(String msg) {
		try {
			this.out.write(msg.getBytes());
			this.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public String receiveMessage() {
		try {
			StringBuilder sb=new StringBuilder();
			int b=0;
			while(b !=10) {
				b=in.read();
				if(b!=13 && b!=10)
					sb.append((char)b);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public void run() {
		sendMessage("Username? ");
		String userName=receiveMessage();
		this.setUser(userName);
		sendMessage("Welcome "+this.getUser()+"! Now you can send yout messages");
		boolean quit=false;
		while(!quit) {
			String msg = receiveMessage();
			server.forwardMessage(this.getUser()+": "+msg);
			if(msg.equalsIgnoreCase("quit")) {
				quit=true;
				sendMessage("See you next time. Bye");
				server.removeClientSession(this);
			}
		}
	}
	
}
