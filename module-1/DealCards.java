/*
Trenten Coffman
September 13, 2026
Assignment 1.3

Javafx application that displays 4 random card images with a refresh button to load new ones
*/
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.util.Random;

public class DealCards extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		Random rand = new Random();
		
		// fetch and store card images in an array
		Image[] cardImages = new Image[52];
		for (int i = 0; i < cardImages.length; i++) {
			cardImages[i] = new Image("cards/" + (i + 1) + ".png");
		}
		
		// create ImageViews for the 4 cards and initialize them with random images from cardImages[]
		ImageView card1 = new ImageView(cardImages[rand.nextInt(52)]);
		ImageView card2 = new ImageView(cardImages[rand.nextInt(52)]);
		ImageView card3 = new ImageView(cardImages[rand.nextInt(52)]);
		ImageView card4 = new ImageView(cardImages[rand.nextInt(52)]);
		
		// create a refresh button to replace images in the ImageViews with new random ones
		Button refreshBtn = new Button("Refresh");
		refreshBtn.setOnAction(e -> {
			card1.setImage(cardImages[rand.nextInt(52)]);
			card2.setImage(cardImages[rand.nextInt(52)]);
			card3.setImage(cardImages[rand.nextInt(52)]);
			card4.setImage(cardImages[rand.nextInt(52)]);
		});
		
		// ImageViews and Button are placed into a GridPane
		GridPane root = new GridPane();
		root.setHgap(10);
		root.setVgap(10);
		root.add(card1, 0, 0);
		root.add(card2, 1, 0);
		root.add(card3, 0, 1);
		root.add(card4, 1, 1);
		root.add(refreshBtn, 1, 2);
		
		// set scene and show window
		primaryStage.setScene(new Scene(root, 154, 240));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}