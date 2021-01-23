import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WritingThread extends Thread { 
	Socket socket = null;
	Scanner scanner = new Scanner(System.in);
	
	public WritingThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			while(true) {
				writer.println(scanner.nextLine());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}


}
