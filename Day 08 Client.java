import java.io.IOException;
import java.net.Socket;

public class Client{
	
	public static void main(String[] args) {
		try {
			Socket socket = null;
			socket = new Socket("127.0.0.1", 10000);
			System.out.println("게임에 접속하였습니다.");
			
			ListeningThread Lt = new ListeningThread(socket);
			WritingThread Wt = new WritingThread(socket);

			Lt.start();
			Wt.start();
            
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

