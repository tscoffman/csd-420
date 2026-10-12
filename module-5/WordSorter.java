/*
Trenten Coffman
October 11, 2026
Assignment 5.2

Test program that reads words from a text file and displays all
non-duplicate words in ascending order and then in descending order.
The test code at the bottom checks that the methods work correctly.
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WordSorter {
	
	private static final String FILE_NAME = "collection_of_words.txt";
	
	private static int passed = 0;
	private static int failed = 0;
	
	public static void main(String[] args) {
		try {
			TreeSet<String> words = readUniqueWords(FILE_NAME);
			
			System.out.println("Non-duplicate words in ascending order:");
			for (String word : words) {
				System.out.println(word);
			}
			
			System.out.println("\nNon-duplicate words in descending order:");
			for (String word: words.descendingSet()) {
				System.out.println(word);
			}
			
			// Run test code after main output
			System.out.println("\n----- Running tests -----");
			runTests();
		}
		catch (FileNotFoundException e) {
			System.out.println("Program failed: the file " + FILE_NAME + " could not be found "
				+ "or opened. Make sure it is in the same folder you are running the program from.");
		}
		catch (SecurityException e) {
			System.out.println("Program failed: permission was denied when trying to read " + FILE_NAME + ".");
		}
		catch (IOException e) {
			System.out.println("Program failed: an input/output error occurred while working with files." + e.getMessage());
		}
	}
	
	public static TreeSet<String> readUniqueWords(String fileName) throws FileNotFoundException {
		TreeSet<String> words = new TreeSet<>();
		
		try (Scanner input = new Scanner(new File(fileName))) {
			input.useDelimiter("[^a-zA-Z']+");
			
			while (input.hasNext()) {
				String word = input.next().toLowerCase();
				if (!word.isEmpty()) {
					words.add(word);
				}
			}
		}
		return words;
	}
	
	// ----- test code -----
	private static void runTests() throws IOException {
		testRemoveDuplicates();
		testIgnoreCaseAndPunctuation();
		testAscendingOrder();
		testDescendingOrder();
		testEmptyFile();
		testMissingFile();
		testActualWordFile();
		
		System.out.println("\nResults: " + passed + " passed, " + failed + " failed");
	}
	
	private static String makeTempFile(String text) throws IOException {
		File temp = File.createTempFile("words", ".txt");
		temp.deleteOnExit();
		try (FileWriter writer = new FileWriter(temp)) {
			writer.write(text);
		}
		return temp.getPath();
	}
	
	private static void check(String testName, Object expected, Object actual) {
		if (expected.equals(actual)) {
			System.out.println("PASS: " + testName);
			passed++;
		}
		else {
			System.out.println("FAIL: " + testName);
			System.out.println("\tExpected: " + expected);
			System.out.println("\tActual: " + actual);
			failed++;
		}
	}
	
	private static void testRemoveDuplicates() throws IOException {
		TreeSet<String> words = readUniqueWords(makeTempFile("apple orange apple watermelon pear apple"));
		check("Duplicates are removed", Arrays.asList("apple", "orange", "pear", "watermelon"), new ArrayList<>(words));
	}
	
	private static void testIgnoreCaseAndPunctuation() throws IOException {
		TreeSet<String> words = readUniqueWords(makeTempFile("Hello, hello! HELLO. World?"));
		check("Case and punctuation are ignored", Arrays.asList("hello", "world"), new ArrayList<>(words));
	}
	
	private static void testAscendingOrder() throws IOException {
		TreeSet<String> words = readUniqueWords(makeTempFile("zebra mango apple"));
		check ("Ascending order", Arrays.asList("apple", "mango", "zebra"), new ArrayList<>(words));
	}
	
	private static void testDescendingOrder() throws IOException {
		TreeSet<String> words = readUniqueWords(makeTempFile("apple mango zebra"));
		check("Descending order", Arrays.asList("zebra", "mango", "apple"), new ArrayList<>(words.descendingSet()));
	}
	
	private static void testEmptyFile() throws IOException {
		TreeSet<String> words = readUniqueWords(makeTempFile(""));
		check("Empty file gives no words", 0, words.size());
	}
	
	private static void testMissingFile() {
		boolean exceptionThrown = false;
		try {
			readUniqueWords("this_file_does_not_exist.txt");
		}
		catch (FileNotFoundException e) {
			exceptionThrown = true;
		}
		check("Missing file throws FileNotFoundException", true, exceptionThrown);
	}
	
	private static void testActualWordFile() throws IOException {
		List<String> ascending = new ArrayList<>(readUniqueWords(FILE_NAME));
		List<String> sortedCopy = new ArrayList<>(ascending);
		Collections.sort(sortedCopy);
		
		check("collection_of_words.txt is in ascending order", sortedCopy, ascending);
		check("collection_of_words.txt contains 'fish'", true, ascending.contains("fish"));
	}
}