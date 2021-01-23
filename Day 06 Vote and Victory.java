package mafia;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Hostserver extends Thread{
	static CountDownLatch latchEnter;
	static CountDownLatch latchday;
	
	static boolean isday = true;
	
	static ArrayList<Socket> players = new ArrayList<Socket>();
	
	//이름으로 번호 찾는 Table , 번호로 이름 찾는 Table(Target Client 호출을 위해..)
	static Map<String, Integer> callbyName = new Hashtable<String, Integer>();
	static Map<Integer, String> callbyNumb = new Hashtable<Integer, String>();

	static Socket clientSocket = null;



	//승리 확인 변수 , 함수
	static int n_citizen = 0;
	static int n_mafia = 0;
	
	static int victoryChecker() { //시민승리 1, 마피아 승리 2, 미정 0
		if(n_mafia == 0) {
			return 1;
		}
		else if (n_citizen == n_mafia) {
			return 2;
		}
		else{return 0;}
	}
	
	public int numb = 0;
	public Hostserver(Socket clientSocket, int _numb) {
		this.clientSocket = clientSocket;
		System.out.println(_numb);
		players.add(clientSocket);
		numb = _numb;
	}
	// Array List 를 받아서 투표의 결과를 Return할 함수
	static public int vote_checker(ArrayList<Integer> arr) {
		int Death_is_closed = 0;
		int max = 0;
		boolean collision = false;
		for(int i = 0; i < arr.size(); i++)
		{
			System.out.println(arr.get(i));
			if((max == arr.get(i)) && max != 0) {
				collision = true;
				continue;
			}
			if(arr.get(i) > max) {
				Death_is_closed = i;
				max = arr.get(i);
			}
		}
		if (collision == true) {
			Death_is_closed = -1;
		}
		return Death_is_closed;
	}

	
	//	
	@Override
	public void run() {
		System.out.println("새로운 플레이어가 연결됐습니다.");
		
		
		try{
			InputStream input = clientSocket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			OutputStream out = clientSocket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			
			writer.println("게임 서버에 연결되었습니다. 자신의 이름을 설정해주세요.");
			String inputLine = null;
			String name = null;
			
			boolean idinput = false;
			while ((inputLine = br.readLine()) != null ) { // 클라이언트가 메세지 입력시마다 수행
				if(!idinput) { // 연결 후 한번만 노출
					name = inputLine; // 이름 할당

					idinput = true;
					for(int i = 0; i < players.size(); i++) { // list 안에 클라이언트 정보가 담겨있음
						out = players.get(i).getOutputStream();
						writer = new PrintWriter(out, true);
						writer.println(name + "님이 입장하셨습니다."); // 클라이언트에게 메세지 발송
					}
					latchEnter.countDown();
					continue;
				}

			//		
			
				
				for(int i = 0; i < players.size(); i++) { 
					if(numb == i) {continue ;} //자기 자신에게 중복 채팅 방지
					out = players.get(i).getOutputStream();
					writer = new PrintWriter(out, true);
					writer.println(name + " : " + inputLine); 
				}
			}
			System.out.println("클라이언트가 접속을 종료했습니다.");
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
///////////////////////MAIN////////////////////////
	public static void main(String[] args) throws Exception{

		System.out.print("서버 수용 인원 수를 지정해주세요(4 ~ 10) : ");
		Scanner sc = new Scanner(System.in);
		int init_member = sc.nextInt();
		int member = init_member;
		ExecutorService eService = Executors.newFixedThreadPool(member);
		int numb = 0;
		
	
		try(ServerSocket sSocket = new ServerSocket(10000)){
			latchEnter = new CountDownLatch(member);
			
			while(numb != member) {
				System.out.println("플레이어 입장을 대기 중입니다...");
				Socket socket = sSocket.accept();
				Thread tes = new Hostserver(socket, numb++);
				eService.submit(tes);	
			}
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} 

		// 인원 입장 대기
		
		latchEnter.await(); //이름 입력때까지 대기
		

		
		
		System.out.println("서버가 종료 되었습니다.");
		sc.close();
		eService.shutdownNow();
	}
}









