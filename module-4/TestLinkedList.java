/*
Trenten Coffman
October 4, 2026
Assignment 4.2

Tests the time it takes to traverse a LinkedList using iterator versus get(index)

Results:
	50,000 elements: iterator = 2 ms, get(index) = 707 ms
	500,000 elements: iterator = 4 ms, get(index) = 71,602 ms

Traversing with an iterator is fast because it keeps track of its current node and moves
to the next one in a single step, so the total work is O(n).
Going from 50,000 to 500,000 elements, the iterator time stayed very short.

Traversing with get(index) is much slower because a LinkedList has no random access.
Every get(i) call starts at the head or tail and follows links until it reaches index i,
so looping over the whole list is O(n^2). Making the list 10x bigger made the get(index)
about 100x slower.

At 500,000 elements, get(index) took about 71 seconds versus 4 ms for the iterator.
For a LinkedList, an iterator should be used for traversal.
*/
import java.util.LinkedList;
import java.util.Iterator;

public class TestLinkedList {
	
	public static LinkedList<Integer> buildList(int size) {
		LinkedList<Integer> list = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		return list;
	}
	
	public static long testIterator(LinkedList<Integer> list) {
		Iterator<Integer> iterator = list.iterator();
		long startTime = System.nanoTime();
		while (iterator.hasNext()) {
			iterator.next();
		}
		// converts nanoTime to milliseconds
		return (System.nanoTime() - startTime) / 1000000;
	}
	
	// logic mirrors testIterator() to ensure both methods actually visit every element
	public static int sumIterator(LinkedList<Integer> list) {
		Iterator<Integer> iterator = list.iterator();
		int sum = 0;
		while (iterator.hasNext()) {
			sum += iterator.next();
		}
		return sum;
	}
	
	public static long testGetIndex(LinkedList<Integer> list) {
		long startTime = System.nanoTime();
		for (int i = 0; i < list.size(); i++) {
			list.get(i);
		}
		// converts nanoTime to milliseconds
		return (System.nanoTime() - startTime) / 1000000;
	}
	
	// logic mirrors testGetIndex() to ensure both methods actually visit every element
	public static int sumGetIndex(LinkedList<Integer> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i);
		}
		return sum;
	}
	
	public static void printTimeMessage(String method, int size, long time) {
		System.out.println("\nTime taken to traverse LinkedList with " + size + " elements using " + method + ":");
		System.out.println("\t" + time + "ms");
	}
	
	public static void main(String[] args) {
		// test that methods work as intended
		LinkedList<Integer> testList = buildList(1000);
		int expectedSum = 0;
		
		for (int i = 0; i < 1000; i++) {
			expectedSum += i;
		}
		
		int iteratorSum = sumIterator(testList);
		int getIndexSum = sumGetIndex(testList);
		
		if (expectedSum == iteratorSum && expectedSum == getIndexSum) {
			System.out.println("SUCCESS: Methods visit every element in LinkedList");
		}
		else {
			System.out.println("FAILED: Methods do not visit every element in LinkedList");
			return;
		}
		
		// getting timings for LinkedList with 50,000 elements
		LinkedList<Integer> list1 = buildList(50000);
		
		long iteratorTime1 = testIterator(list1);
		printTimeMessage("iterator", 50000, iteratorTime1);
		
		long getIndexTime1 = testGetIndex(list1);
		printTimeMessage("get(index)", 50000, getIndexTime1);
		
		// getting timings for LinkedList with 500,000 elements
		LinkedList<Integer> list2 = buildList(500000);
		
		long iteratorTime2 = testIterator(list2);
		printTimeMessage("iterator", 500000, iteratorTime2);
		
		long getIndexTime2 = testGetIndex(list2);
		printTimeMessage("get(index)", 500000, getIndexTime2);
	}
}