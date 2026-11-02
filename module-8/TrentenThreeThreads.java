/*
Trenten Coffman
November 1, 2026
Assignment 8.2

Uses three threads to output random letters, digits, and special characters
to a TextArea. Each character is displayed as soon as it is generated, so the three
types are interleaved.
*/
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.Random;

public class TrentenThreeThreads extends Application {
	
	public static final int COUNT = 10000;  // characters per thread
	public static final int DELAY_MS = 1;  // small pause so threads interleave visibly
	public static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
	public static final String DIGITS = "0123456789";
	public static final String SPECIALS = "!@#$%^&*";
	private static final Random RAND = new Random();
	
	@Override
	public void start(Stage stage) {
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setWrapText(true);
		
		BorderPane root = new BorderPane(textArea);
		stage.setTitle("Trenten Three Threads");
		stage.setScene(new Scene(root, 700, 500));
		stage.show();
		
		Thread[] threads = createThreads(COUNT, DELAY_MS, textArea);
		for (Thread thread : threads) {
			thread.setDaemon(true);  // don't keep the program alive after window closes
			thread.start();
		}
		
		/*
			A fourth thread waits for the three worker threads to finish, then asks the
			waiter thread to run the tests. Because every character was queued with
			Platform.runLater before this point, the tests run after all characters
			have been displayed.
		*/
		Thread waiter = new Thread(() -> {
			try {
				for (Thread thread : threads) thread.join();
			}
			catch (InterruptedException e) {
				return;
			}
			Platform.runLater(() -> runTests(textArea));
		});
		waiter.setDaemon(true);
		waiter.start();
	}
	
	// Character generators
	
	public static char randomFrom(String pool) {
		return pool.charAt(RAND.nextInt(pool.length()));
	}
	
	public static char randomLetter() {
		return randomFrom(LETTERS);
	}
	
	public static char randomDigit() {
		return randomFrom(DIGITS);
	}
	
	public static char randomSpecial() {
		return randomFrom(SPECIALS);
	}
	
	// Thread creation
	
	public static Thread createThread(String name, String pool, int count, int delayMs, TextArea area) {
		return new Thread(() -> {
			for (int i = 0; i < count; i++) {
				String c = String.valueOf(randomFrom(pool));
				
				// Only the JavaFX thread may change the UI, so hand it the character
				Platform.runLater(() -> {
					area.appendText(c);
					area.setScrollTop(Double.MAX_VALUE);  // keep view at the newest text
				});
				
				if (delayMs > 0) {
					try {
						Thread.sleep(delayMs);
					}
					catch (InterruptedException e) {
						return;
					}
				}
			}
		}, name);
	}
	
	// Create the three threads: letters, digits, specials
	
	public static Thread[] createThreads(int count, int delayMs, TextArea area) {
		return new Thread[] {
			createThread("LetterThread", LETTERS, count, delayMs, area),
			createThread("DigitThread", DIGITS, count, delayMs, area),
			createThread("SpecialThread", SPECIALS, count, delayMs, area)
		};
	}
	
	// Main
	
	public static void main(String[] args) {
		launch(args);
	}
	
	// Test code
	
	private static int passed = 0;
	private static int failed = 0;
	private static StringBuilder results = new StringBuilder();
	
	private static void check(String testName, boolean condition) {
		if (condition) {
			passed++;
			results.append("PASS: ").append(testName).append("\n");
		}
		else {
			failed++;
			results.append("FAIL: ").append(testName).append("\n");
		}
	}
	
	private static int typeOf(char c) {
		if (LETTERS.indexOf(c) >= 0) return 0;
		if (DIGITS.indexOf(c) >= 0) return 1;
		return 2;
	}
	
	public static void runTests(TextArea area) {
		passed = 0;
		failed = 0;
		results = new StringBuilder();
		
		String output = area.getText();
		
		// generator tests
		boolean ok = true;
		for (int i = 0; i < 5000; i++) {
			if (LETTERS.indexOf(randomLetter()) < 0) ok = false;
		}
		check("randomLetter only returns letters", ok);
		
		ok = true;
		for (int i = 0; i < 5000; i++) {
			char c = randomDigit();
			if (c < '0' || c > '9') ok = false;
		}
		check("randomDigit only returns 0-9", ok);
		
		ok = true;
		for (int i = 0; i < 5000; i++) {
			if (SPECIALS.indexOf(randomSpecial()) < 0) ok = false;
		}
		check("randomSpecial only returns special characters", ok);
		
		// thread tests
		check("createThreads return three threads",
			createThreads(1, 0, new TextArea()).length == 3);
		
		int letters = 0, digits = 0, specials = 0, changes = 0;
		for (int i = 0; i < output.length(); i++) {
			int type = typeOf(output.charAt(i));
			if (type == 0) letters++;
			else if (type == 1) digits++;
			else specials++;
			
			if (i > 0 && type != typeOf(output.charAt(i - 1))) changes++;
		}
		
		check("letter thread displayed " + COUNT + " characters (found " + letters + ")",
			letters == COUNT);
		check("digit thread displayed " + COUNT + " characters (found " + digits + ")",
			digits == COUNT);
		check("special thread displayed " + COUNT + " characters (found " + specials + ")",
			specials == COUNT);
		
		check("output is interleaved (type changes: " + changes + ")", changes > 10);
		
		// display the results
		area.appendText("\n\n\n---Test Results---\n");
		area.appendText(results.toString());
		area.appendText("---" + passed + " passed, " + failed + " failed---\n");
		area.setScrollTop(Double.MAX_VALUE);
	}
}
