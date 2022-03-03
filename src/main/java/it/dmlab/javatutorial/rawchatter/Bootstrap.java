package it.dmlab.javatutorial.rawchatter;

import java.io.IOException;

public class Bootstrap {

	public static void main(String[] args) {
		
		try {
			RawchatterServer server = new RawchatterServer(60000);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
