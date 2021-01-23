package mafia;

import java.util.ArrayList;
import java.util.Random;



public class role {
	int size= 0;
	ArrayList<Integer> roles;
	role(int _size){
		size = _size;
		roles = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			roles.add(0);
		}		
	}
	public ArrayList<Integer> rolespread(){
		long seed = System.currentTimeMillis();
		Random rand = new Random(seed);
		int n_mafia = Math.floorDiv(size, 3); // 1
		int n_police = 1;  // 2
		int n_doctor = 1; //3  ,,  //0 -> 시민
		int cnt = 0;
		while(cnt != n_mafia) {
			int target = rand.nextInt(size);
			if(roles.get(target) == 0) {
				cnt++;
				roles.set(target, 1);
			}	
		} cnt = 0;
		while(cnt != n_police) {
			int target = rand.nextInt(size);
			if(roles.get(target) == 0) {
				cnt++;
				roles.set(target, 2);
			}	
		} cnt =0;
		if(size >= 6) {
			while(cnt != n_doctor) {
			int target = rand.nextInt(size);
			if(roles.get(target) == 0) {
				cnt++;
				roles.set(target, 3);
				}	
			}
		}


		return roles;
		
	}
	
	
}
