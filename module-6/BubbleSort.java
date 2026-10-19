/*
Trenten Coffman
October 18, 2026
Assignment 6.2

Generic bubble sort using the Comparable and Comparator interfaces
with a few tests to check that both methods work
*/
import java.util.Arrays;
import java.util.Comparator;

public class BubbleSort {
	
	// Sorts in ascending order using the elements' natural ordering
	public static <E extends Comparable<E>> void bubbleSort(E[] list) {
		boolean swapped = true;
		for (int pass = 1; pass < list.length && swapped; pass++) {
			swapped = false;
			for (int i = 0; i < list.length - pass; i++) {
				if (list[i].compareTo(list[i + 1]) > 0) {
					swap(list, i, i + 1);
					swapped = true;
				}
			}
		}
	}
	
	// Sorts using the order defined by the given Comparator
	public static <E> void bubbleSort(E[] list, Comparator<? super E> comparator) {
		boolean swapped = true;
		for (int pass = 1; pass < list.length && swapped; pass++) {
			swapped = false;
			for (int i = 0; i < list.length - pass; i++) {
				if (comparator.compare(list[i], list[i + 1]) > 0) {
					swap(list, i, i + 1);
					swapped = true;
				}
			}
		}
	}
	
	// helper method that swaps elements in an array
	private static <E> void swap(E[] list, int a, int b) {
		E temp = list[a];
		list[a] = list[b];
		list[b] = temp;
	}
	
	// Class used to test Comparator version of bubbleSort
	static class Person {
		String name;
		int age;
		
		Person(String name, int age) {
			this.name = name;
			this.age = age;
		}
		
		public String toString() {
			return name + " (" + age + ")";
		}
	}
	
	// Tests: sort with bubbleSort, then compare against Java's built-in sort
	public static void main(String[] args) {
		// Comparable tests
		Integer[] numbers = {5, 2, 9, 1, 7, 3};
		Integer[] expectedNumbers = {1, 2, 3, 5, 7, 9};
		bubbleSort(numbers);
		report("Comparable - Integers", Arrays.equals(numbers, expectedNumbers), numbers);
		
		String[] words = {"soccer", "basketball", "baseball", "tennis", "boxing"};
		String[] expectedWords = {"baseball", "basketball", "boxing", "soccer", "tennis"};
		bubbleSort(words);
		report("Comparable - Strings", Arrays.equals(words, expectedWords), words);
		
		Integer[] empty = {};
		bubbleSort(empty);
		report("Comparable - Empty Array", empty.length == 0, empty);
		
		// Comparator tests
		Integer[] descending = {5, 2, 9, 1, 7, 3};
		Integer[] expectedDescending = {9, 7, 5, 3, 2, 1};
		bubbleSort(descending, Comparator.reverseOrder());
		report("Comparator - Descending", Arrays.equals(descending, expectedDescending), descending);
		
		Person[] people = {
			new Person("Rick", 56),
			new Person("Samantha", 29),
			new Person("Angie", 34)
		};
		bubbleSort(people, Comparator.comparingInt(p -> p.age));
		boolean byAge = people[0].name.equals("Samantha")
			&& people[1].name.equals("Angie")
			&& people[2].name.equals("Rick");
		report("Comparator - Persons by age", byAge, people);
	}
	
	// Prints the result of a test
	private static void report(String testName, boolean passed, Object[] result) {
		System.out.println((passed ? "PASS: " : "FAIL: ") + testName + " " + Arrays.toString(result));
	}
}