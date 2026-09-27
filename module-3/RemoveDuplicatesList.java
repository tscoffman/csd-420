/*
Trenten Coffman
September 27, 2026
Assignment 3.2

Tests removeDuplicates method by passing it a list with random ints then 
printing the returned list
*/
import java.util.ArrayList;

public class RemoveDuplicatesList {
	
	public static <E> ArrayList<E> removeDuplicates(ArrayList<E> list) {
		ArrayList<E> newList = new ArrayList<>();
		
		// loop through list checking if newList contains each element
		// if not add to newList
		for (E element : list) {
			if (!newList.contains(element)) {
				newList.add(element);
			}
		}
		
		return newList;
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> originalList = new ArrayList<>();
		
		// generate 50 random numbers 1-20 for list
		for (int i = 0; i < 50; i++) {
			originalList.add((int)(Math.random() * 20) + 1);
		}
		
		// get new list from removeDuplicates()
		ArrayList<Integer> newList = removeDuplicates(originalList);
		
		// print returned list
		for (int number : newList) {
			System.out.println(number);
		}
		
		// Additional Tests:
		// empty list
		ArrayList<Integer> testList1 = new ArrayList<>();
		ArrayList<Integer> testList1Returned = removeDuplicates(testList1);
		
		// strings
		ArrayList<String> testList2 = new ArrayList<>();
		testList2.add("a");
		testList2.add("b");
		testList2.add("a");
		testList2.add("c");
		ArrayList<String> testList2Returned = removeDuplicates(testList2);
		
		for (String letter : testList2Returned) {
			System.out.println(letter);
		}
	}
}