/*
Trenten Coffman
October 25, 2026
Assignment 7.2

Displays four circles styled with an external style sheet (mystyle.css).
Two use the "plainCircle" class (white fill, black stroke), and the other
two use the IDs "redCircle" and "greenCircle". Test code checks the styles
and prints PASS or FAIL for each check.
*/
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CircleExercise extends Application {

	@Override
	public void start(Stage primaryStage) {
		// First circle sits inside a stack pane
		Circle circle1 = new Circle(30);
		circle1.getStyleClass().add("plainCircle");
		StackPane borderPane = new StackPane(circle1);
		borderPane.setPrefSize(65, 250);
		borderPane.getStyleClass().add("border");
		
		Circle circle2 = new Circle(30);
		circle2.getStyleClass().add("plainCircle");
		
		Circle circle3 = new Circle(30);
		circle3.setId("redCircle");
		
		Circle circle4 = new Circle(30);
		circle4.setId("greenCircle");
		
		HBox hBox = new HBox(5, borderPane, circle2, circle3, circle4);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(5));
		
		Scene scene = new Scene(hBox, 300, 260);
		scene.getStylesheets().add(
			CircleExercise.class.getResource("mystyle.css").toExternalForm());
		
		primaryStage.setTitle("Circle Exercise");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		runTests(scene);
	}
	
	// Test code
	
	private static int failures = 0;
	
	private static void check(String name, boolean condition) {
		System.out.println((condition ? "PASS: " : "FAIL: ") + name);
		if (!condition) failures++;
    }
	
	private static void runTests(Scene scene) {
		scene.getRoot().applyCss();   // make sure the CSS has been applied
		
		check("Style sheet loaded",
			scene.getStylesheets().size() == 1
			&& scene.getStylesheets().get(0).endsWith("mystyle.css"));
		
		long circleCount = scene.getRoot().lookupAll("*").stream()
			.filter(n -> n instanceof Circle).count();
		check("Four circles displayed", circleCount == 4);
		
		var plain = scene.getRoot().lookupAll(".plainCircle");
		check("Two circles use the plainCircle class", plain.size() == 2);
		for (var node : plain) {
			Circle c = (Circle) node;
			check("Plain circle has white fill", Color.WHITE.equals(c.getFill()));
			check("Plain circle has black stroke", Color.BLACK.equals(c.getStroke()));
		}
		
		Circle red = (Circle) scene.getRoot().lookup("#redCircle");
		check("Red circle found by ID", red != null);
		if (red != null) {
			check("Red circle has red fill", Color.RED.equals(red.getFill()));
		}
		
		Circle green = (Circle) scene.getRoot().lookup("#greenCircle");
		check("Green circle found by ID", green != null);
		if (green != null) {
			check("Green circle has green fill", Color.GREEN.equals(green.getFill()));
		}
		
		System.out.println(failures == 0
			? "All tests passed." : failures + " test(s) failed.");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}