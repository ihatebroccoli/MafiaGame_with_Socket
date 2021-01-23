import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ListeningThread extends Thread {
	Socket socket = null;

	public ListeningThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true) {
				System.out.println(reader.readLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
