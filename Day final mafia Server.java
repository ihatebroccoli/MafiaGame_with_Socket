package mafia;

import java.io.;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Hostserver extends Thread{
	static CountDownLatch latchEnter;
	static CountDownLatch latchNight; static int night_latch_counter = 0;
	static CountDownLatch latchday;

	
	static boolean isday = true;
	
	static ArrayListSocket players = new ArrayListSocket();
	static ArrayListInteger roles = new ArrayListInteger();
	
	
	static ArrayListInteger day_vote = new ArrayListInteger();
	static ArrayListInteger duplicate_vote = new ArrayListInteger();
	static ArrayListInteger mafia_vote = new ArrayListInteger();
	static ArrayListInteger duplicate_mafia_vote = new ArrayListInteger();
	
	
	static MapString, Integer callbyName = new HashtableString, Integer();
	static MapInteger, String callbyNumb = new HashtableInteger, String();
	
	
	static Socket clientSocket = null;
	static int mafia_vote_counter = 0;
	static String healed = null;
	static String police_target = null;

	승리 확인 변수 , 함수
	static int n_citizen = 0;
	static int n_mafia = 0;
	
	static int victoryChecker() { 시민승리 1, 마피아 승리 2, 미정 0
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
	

	
	static public int vote_checker(ArrayListInteger arr) {
		int Death_is_closed = 0;
		int max = 0;
		boolean collision = false;
		for(int i = 0; i  arr.size(); i++)
		{
			System.out.println(arr.get(i));
			if((max == arr.get(i)) && max != 0) {
				collision = true;
				continue;
			}
			if(arr.get(i)  max) {
				Death_is_closed = i;
				max = arr.get(i);
			}
		}
		if (collision == true) {
			Death_is_closed = -1;
		}
		return Death_is_closed;
	}
	
	static public void toRole_message(String message, String towho) {

		try{
			OutputStream out = clientSocket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			
			
			for(int i = 0; i  players.size(); i++) {
				
				String instruction = ;
				if(towho == individual) {
						if (roles.get(i) == 0){
							instruction = 당신은 시민입니다! 마피아를 색출하여 도시를 지켜내세요.;}
						else if (roles.get(i) == 1){
						instruction = 당신은 마피아입니다! 시민을 암살하고 도시를 무너뜨리세요. n 특수기능  를 이용하여 마피아들 간 회의를 진행하세요. 다른 이들은 볼 수 없습니다! 밤에는 !암살을 사용하여 시민 암살에 대한 투표를 진행할 수 있습니다. n
								+ ex) 지금 경찰이 누군지 알아낸 것 같은데   !암살 OOO;}
						else if (roles.get(i) == 2){
						instruction = 당신은 경찰입니다! 시민을 보호하고 도시를 지켜야합니다. 의심가는 인물을 조사해보세요.n 특수기능  밤에 !조사를 사용하여 마피아에 대한 잠복조사를 진행할 수 있습니다. n
								+ ex) !조사 OOO;}
						else if (roles.get(i) == 3) {
						instruction = 당신은 의사입니다! 암살당할 것 같은 시민에 대한 수슬을 집도하세요. 당신의 선택이 시민의 죽음을 막을 수 있습니다.n 특수기능  밤에 +치료를 사용하여 마피아에 대한 잠복조사를 진행할 수 있습니다. n
								+ ex) +치료 OOO;}
						
					}
				 마피아  활동 밤
				else if(towho == mafiakill) {
					if (roles.get(i) == 1){
						instruction = 암살할 시민에게 투표해주십시오. 투표수가 같으면 무효표가 됩니다!;}
				}
				
				 의사  활동 밤
				else if(towho == doctorheal) {
					if (roles.get(i) == 3){
						instruction = 치료할 사람을 고르십시오;}
				}	
				else if(towho == doctorheal_after) {
					if (roles.get(i) == 3){
						instruction = 수술이 완료되었습니다.;}
				}	
				
				 경찰  활동 밤
				else if(towho == policelook) {
					if (roles.get(i) == 2){
						instruction = 조사할 사람을 고르십시오;}
				}
				else if(towho == policelook_result) {
					String result = 맞습니다!;
					
					if(roles.get(callbyName.get(message)) != 1) {
						result = 아닙니다.;
					}
					
					if (roles.get(i) == 2){
						instruction = 그 사람은 마피아가  + result;}
				}
				
				
				else if(towho == night) {
					instruction = 밤이 되었습니다...;
				}
				else if(towho == day) {
					instruction = 낮이 되었습니다...;
				}
				else if(towho == day_Vote) {
					instruction = 투표 결과...   + message + 님이 최다 표를 받았습니다. 10초 간 마지막 발언이 있습니다.;
				}
				
				else if(towho == day_Kill) {
					instruction = 사형이 집행되었습니다.. 사망자는  + message + (으)로 밝혀졌습니다.;
					
				}
				
				else if(towho == mafia_vote) {

					instruction = 마피아의 암살로 인해  + message + 님이 시신으로 발견되었습니다.;
				}
				

				else {
					instruction = message;
				}
				if(instruction ==   instruction == null) {
					continue;
				}
				writer.println();
				out = players.get(i).getOutputStream();
				writer = new PrintWriter(out, true);
				writer.println(instruction);
			}
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	@Override
	public void run() {
		System.out.println(새로운 플레이어가 연결됐습니다.);
		
		
		try{
			InputStream input = clientSocket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			OutputStream out = clientSocket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			
			writer.println(게임 서버에 연결되었습니다. 자신의 이름을 설정해주세요.);
			String inputLine = null;
			String name = null;
			
			boolean idinput = false;
			while ((inputLine = br.readLine()) != null ) {  클라이언트가 메세지 입력시마다 수행
				if(!idinput) {  연결 후 한번만 노출
					name = inputLine;  이름 할당
					callbyName.put(name, numb);
					callbyNumb.put(numb,name);
					idinput = true;
					for(int i = 0; i  players.size(); i++) {  list 안에 클라이언트 정보가 담겨있음
						out = players.get(i).getOutputStream();
						writer = new PrintWriter(out, true);
						writer.println(name + 님이 입장하셨습니다.);  클라이언트에게 메세지 발송
					}
					latchEnter.countDown();
					continue;
				}

				
				if(inputLine.charAt(0) == '' && roles.get(numb) != 1) { 마피아가 아닐때 마피아 대화이용
					out = players.get(numb).getOutputStream();
					writer = new PrintWriter(out, true);
					writer.println(당신은 마피아가 아닙니다.);
					continue;
				}
				
				if(isday == true) { 경찰 ,의사가 밤이 아닐때 직업스킬 발동 시
					if((roles.get(numb) == 3 && inputLine.contains(!치료 ))  (roles.get(numb) == 2 && inputLine.contains(!조사)))
					{
						out = players.get(numb).getOutputStream();
						writer = new PrintWriter(out, true);
						writer.println(아직 밤이 아닙니다.);
					continue;}
				}

				
				
				 낮 투표
				if(isday == true && inputLine.contains(!투표 ) && duplicate_vote.get(numb) == 0 && roles.get(numb) != 4) {
					String target = inputLine.replace(!투표 , );
					Object _value =callbyName.get(target);
					if (_value == null) {
						out = players.get(numb).getOutputStream();
						writer = new PrintWriter(out, true);
						writer.println(이름을 확인해주세요.);
						continue;
					}
					int value = (Integer)_value;
					
					if(roles.get(value) == 4){
						out = players.get(numb).getOutputStream();
						writer = new PrintWriter(out, true);
						writer.println(이미 사망한 사람입니다.);
						continue;
					}
					
					day_vote.set(value , day_vote.get(value) + 1);
					duplicate_vote.set(numb, 1);
					
					out = players.get(numb).getOutputStream();
					writer = new PrintWriter(out, true);
					writer.println(투표 완료);
					latchday.countDown();
				} else if (isday == true && inputLine.contains(!투표 ) && duplicate_vote.get(numb) != 0) {
					out = players.get(numb).getOutputStream();
					writer = new PrintWriter(out, true);
					writer.println(이미 투표하셨습니다.);
					continue;
				}
				
				if(isday == false) {
					야간 투표
					if(inputLine.contains(!암살 ) && duplicate_mafia_vote.get(numb) == 0 && roles.get(numb) == 1) {
						String target = inputLine.replace(!암살 , );
						Object _value =callbyName.get(target);
						if (_value == null) {
							out = players.get(numb).getOutputStream();
							writer = new PrintWriter(out, true);
							writer.println(이름을 확인해주세요.);
							continue;
						}
						int value = (Integer)_value;
						
						System.out.println(value + value);
						
						if(roles.get(value) == 4){
							out = players.get(numb).getOutputStream();
							writer = new PrintWriter(out, true);
							writer.println(이미 사망한 사람입니다.);
							continue;
						}
						int cur = mafia_vote.get(value);
						mafia_vote.set(value , cur + 1);
						duplicate_mafia_vote.set(numb, 1);
						
						System.out.println(mafia_vote.get(value));
						out = players.get(numb).getOutputStream();
						writer = new PrintWriter(out, true);
						writer.println(투표 완료);
						mafia_vote_counter += 1;
						
						if(mafia_vote_counter == n_mafia) {
							latchNight.countDown();
						}
						continue;
					} else if(isday == false && inputLine.contains(!암살 ) && duplicate_mafia_vote.get(numb) != 0){
						out = players.get(numb).getOutputStream();
						writer = new PrintWriter(out, true);
						writer.println(이미 투표하셨습니다.);
						continue;
					}
					
					경찰의 조사
					else if(police_target == null && roles.get(numb) == 2) {
						if(inputLine.contains(!조사 )) {
							Object _value =callbyName.get(inputLine.replace(!조사 , ));
							if (_value == null) {
								out = players.get(numb).getOutputStream();
								writer = new PrintWriter(out, true);
								writer.println(이름을 확인해주세요.);
								continue;
							}
							
							if(roles.get(callbyName.get(inputLine.replace(!조사 , ))) == 4){
								out = players.get(numb).getOutputStream();
								writer = new PrintWriter(out, true);
								writer.println(이미 사망한 사람입니다.);
								continue;
							}
							
							police_target = inputLine.replace(!조사 , );
							out = players.get(numb).getOutputStream();
							writer = new PrintWriter(out, true);
							writer.println(선택했습니다. 다른 플레이어들을 기다리고 있습니다.);
							
							latchNight.countDown();
							continue;
						}
						else {
							out = players.get(numb).getOutputStream();
							writer = new PrintWriter(out, true);
							writer.println(지금은 밤입니다.);
							continue;
						}
						
					}
					
					의사의 치료
					else if(healed == null && roles.get(numb) == 3) {
						if(inputLine.contains(!치료 )) {
							Object _value =callbyName.get(inputLine.replace(!치료 , ));
							if (_value == null) {
								out = players.get(numb).getOutputStream();
								writer = new PrintWriter(out, true);
								writer.println(이름을 확인해주세요.);
								continue;
							}
							if(roles.get(callbyName.get(inputLine.replace(!치료 , ))) == 4){
								out = players.get(numb).getOutputStream();
								writer = new PrintWriter(out, true);
								writer.println(이미 사망한 사람입니다.);
								continue;
							}
							healed = inputLine.replace(!치료 , );
							out = players.get(numb).getOutputStream();
							writer = new PrintWriter(out, true);
							writer.println(선택했습니다. 다른 플레이어들을 기다리고 있습니다.);
							latchNight.countDown();
							continue;
						}
						else {
							out = players.get(numb).getOutputStream();
							writer = new PrintWriter(out, true);
							writer.println(지금은 밤입니다.);
							continue;
						}
					}
					
					else if(inputLine.charAt(0) == '') {
						for(int i = 0; i  players.size(); i++) {
							if(roles.get(i) != 1) {continue;}
							out = players.get(i).getOutputStream();
							writer = new PrintWriter(out, true);
							writer.println(마피아 대화  + name +    + inputLine);
						}
						continue;
					}
					사망자 간 대화 
					else if(roles.get(numb) == 4) {
						for(int i = 0; i  players.size(); i++) {
							if(roles.get(i) != 4) {continue;}
							out = players.get(i).getOutputStream();
							writer = new PrintWriter(out, true);
							writer.println(사망자 대화  + name +    + inputLine);
						}
						continue;
					}
					
					
					else {
						out = players.get(numb).getOutputStream();
						writer = new PrintWriter(out, true);
						writer.println(지금은 밤입니다);
						continue;
					}
				}
				
				
				if(isday == true) {
					마피아 간 대화
					if(inputLine.charAt(0) == '') {
						for(int i = 0; i  players.size(); i++) {
							if(roles.get(i) != 1) {continue;}
							out = players.get(i).getOutputStream();
							writer = new PrintWriter(out, true);
							writer.println(마피아 대화  + name +    + inputLine);
						}
						continue;
					}
					사망자 간 대화 
	 				if(roles.get(numb) == 4) {
						for(int i = 0; i  players.size(); i++) {
							if(roles.get(i) != 4) {continue;}
							out = players.get(i).getOutputStream();
							writer = new PrintWriter(out, true);
							writer.println(사망자 대화  + name +    + inputLine);
						}
						continue;
					}
				}
				
				
				for(int i = 0; i  players.size(); i++) { 
					if(numb == i) {continue ;}  자기 자신에게 중복 채팅 방지
					out = players.get(i).getOutputStream();
					writer = new PrintWriter(out, true);
					writer.println(name +    + inputLine); 
				}
			}
			System.out.println(클라이언트가 접속을 종료했습니다.);
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
MAIN
	public static void main(String[] args) throws Exception{

		System.out.print(서버 수용 인원 수를 지정해주세요(4 ~ 10)  );
		Scanner sc = new Scanner(System.in);
		int init_member = sc.nextInt();
		int member = init_member;
		ExecutorService eService = Executors.newFixedThreadPool(member);
		int numb = 0;
		
		
		n_mafia = Math.floorDiv(member, 3);
		n_citizen = member - n_mafia;
		int numbforlatch = 2;
		if(init_member = 6) {
			numbforlatch = 3;
		}
		
		
		try(ServerSocket sSocket = new ServerSocket(10000)){
			latchEnter = new CountDownLatch(member);
			night_latch_counter = numbforlatch;
			
			while(numb != member) {
				System.out.println(플레이어 입장을 대기 중입니다...);
				Socket socket = sSocket.accept();
				Thread tes = new Hostserver(socket, numb++);
				eService.submit(tes);	
			}
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}  인원 입장 대기
		
		latchEnter.await(); 이름 입력때까지 대기
		
		toRole_message(인원이 모두 입장하였습니다. 게임을 시작하겠습니다., );
		toRole_message(직업을 결정합니다., );
		role R = new role(players.size());
		roles = R.rolespread();

		toRole_message(, individual);
		
		for (int i = 0; i  players.size(); i++) {
			duplicate_vote.add(0);
			day_vote.add(0);
			mafia_vote.add(0);
			duplicate_mafia_vote.add(0);
		}
		
		while(victoryChecker() == 0) {
			toRole_message(, day);

			
			
			latchday = new CountDownLatch(member);
			
			latchday.await();
			
			int vote_result = vote_checker(day_vote);
			if(vote_result == -1) {
				toRole_message(투표가 무효로 결정되었습니다., );
			}else {
				사망자가 시민인지 마피아인지 직업을 알려준다.
				String what_is_role = 시민;
				if( roles.get(vote_result) == 1) {
					what_is_role = 마피아;
					n_mafia--;
				}
				else {
					if( roles.get(vote_result) == 2  roles.get(vote_result) == 3) {
						night_latch_counter--;
					}
					n_citizen--;
				}
				
				
				
				toRole_message(callbyNumb.get(vote_result), day_Vote);
				Thread.sleep(10000);
				roles.set(vote_result,4);
				toRole_message(what_is_role, day_Kill);
			}
			
			member = n_citizen + n_mafia;
			게임 종료 체크
			int gameEnd = victoryChecker();
			
			if(gameEnd != 0) {
				if(gameEnd == 1) {
					toRole_message(시민의 승리입니다!,);
				}
				else {
					toRole_message(마피아의 승리입니다!,);
				}
				break;
			}
			
			toRole_message(, night);
			isday = false;
			밤 시작
			

			toRole_message(, mafiakill);
			toRole_message(, policelook);
			toRole_message(, doctorheal);
			
			latchNight = new CountDownLatch(night_latch_counter);
			latchNight.await();
			int mafia_vote_result = vote_checker(mafia_vote);
			if(police_target != null) {
				toRole_message(police_target, policelook_result);
			}
			
			
			if(init_member = 6 && healed != null) {
				if(mafia_vote_result == callbyName.get(healed)) {
					mafia_vote_result = -1;
				}
			}


			
			마피아 행동의 결과
			if (mafia_vote_result == -1) {

				toRole_message(아무도 죽지 않았습니다!, );
			}
			else {
				if(roles.get(mafia_vote_result) == 1) {
					n_mafia--;
				}
				else{
					if( roles.get(mafia_vote_result) == 2  roles.get(mafia_vote_result) == 3) {
						night_latch_counter--;
					}
					n_citizen--;
				}
				roles.set(mafia_vote_result,4);
				toRole_message(callbyNumb.get(mafia_vote_result), mafia_vote);
			}
			
			
			게임 종료 체크
			gameEnd = victoryChecker();
			if(gameEnd != 0) {
				if(gameEnd == 1) {
					toRole_message(시민의 승리입니다!,);
				}
				else {
					toRole_message(마피아의 승리입니다!,);
				}
				break;
			}
			member = n_citizen + n_mafia;
			isday = true;
			healed = null;
			police_target = null;
			mafia_vote_counter = 0;
			
			latchNight = new CountDownLatch(night_latch_counter);
			for (int i = 0; i  players.size(); i++) {
				duplicate_vote.set(i, 0);
				day_vote.set(i, 0);
				mafia_vote.set(i, 0);
				duplicate_mafia_vote.set(i, 0);
			}
			
		}
		isday = true;
		for(int i = 0; i  players.size(); i++) {
			roles.set(i, 0);
		}
		
		System.out.println(서버가 종료 되었습니다.);
		eService.shutdownNow();
	}
}


















