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
		System.out.println(ReversiGame.getTurn() + ", Enter a coordinate: ");
		String coordinate = reader.nextLine();
		List<String> coordinates = Arrays.asList(coordinate.split(","));
		int x = Integer.parseInt(coordinates.get(0));
		int y = Integer.parseInt(coordinates.get(1));
		List <Integer> l = Arrays.asList(x,y);
		return l;
	}
	
	public List<Integer> greedyMove(int b, ArrayList<Tuple> vm){
		int biggest = b;
		int x = vm.get(biggest).x;
		int y = vm.get(biggest).y;
		List <Integer> l = Arrays.asList(x, y);
		return l;
	}
	
	public List<Integer> getMove(int b, ArrayList<Tuple> vm){
		//return greedyMove(b, vm);
		return inputMove();
	}
}



