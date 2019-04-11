package Model;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

// greedy algoritme voor Reversi
public class Greedy {
	public Greedy() {
		
	}
	
	public List<Integer> inputMove() {
		Scanner reader = new Scanner(System.in);
		//System.out.println(ReversiGame.getTurn() + ", Enter a coordinate: ");
		String coordinate = reader.nextLine();
		List<String> coordinates = Arrays.asList(coordinate.split(","));
		int x = Integer.parseInt(coordinates.get(0));
		int y = Integer.parseInt(coordinates.get(1));
		List <Integer> l = Arrays.asList(x,y);
		return l;
	}
	


	/**
	 * get the move with most flips at this moment
	 * @return index of the move with the most flips
	 */
	
	public int maxValueOfList(ArrayList<Integer> m) {
		int biggestInt = 0;
		for(int i = 0; i < m.size(); i++) {
			if(biggestInt < m.get(i)) {
				biggestInt = m.get(i);
			}
		}
		
		return 0;
	}
	
	public List<Tuple> greedyMove(ArrayList<Integer> w, ArrayList<Tuple> v) {
		System.out.println(w);
		ArrayList<Tuple> a = new ArrayList<>();
		if(w.size() < 1) {
			return null;
		} else {
			int big = Collections.max(w);
			for(int i = 0; i < w.size(); i++) {
				if(w.get(i) == big) {
					a.add(v.get(i));
				}
			}
		}
		return a;
	}
}



