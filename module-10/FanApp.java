/*
Trenten Coffman
November 16, 2026
Assignment 10.2

JavaFX application views and updates records in the existing fans table of databasedb
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FanApp extends Application {
	// port is set to 3307 instead of the default 3306
	public static final String URL = "jdbc:mysql://localhost:3307/databasedb";
	public static final String USER = "student1";
	public static final String PASSWORD = "pass";
	static final int MAX_LENGTH = 25;
	
	final TextField idField = new TextField();
	final TextField firstNameField = new TextField();
	final TextField lastNameField = new TextField();
	final TextField favoriteTeamField = new TextField();
	final Button displayButton = new Button("Display");
	final Button updateButton = new Button("Update");
	final Label statusLabel = new Label("Enter an ID and click Display.");
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("Fan Information");
		stage.setScene(new Scene(buildRoot()));
		stage.show();
	}
	
	// builds the interface
	VBox buildRoot() {
		GridPane form = new GridPane();
		form.setHgap(10);
		form.setVgap(8);
		form.addRow(0, new Label("ID:"), idField);
		form.addRow(1, new Label("First name:"), firstNameField);
		form.addRow(2, new Label("Last name:"), lastNameField);
		form.addRow(3, new Label("Favorite team:"), favoriteTeamField);
		
		displayButton.setOnAction(e -> displayFan());
		updateButton.setOnAction(e -> updateFan());
		
		VBox root = new VBox(12, form, new HBox(10, displayButton, updateButton), statusLabel);
		root.setPadding(new Insets(15));
		return root;
	}
	
	// ----- Database Methods -----
	
	private Connection connect() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			throw new SQLException("MySQL driver not found. Add the Connector/J JAR to the classpath.");
		}
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	String[] getFan(int id) throws SQLException {
		String sql = "SELECT firstname, lastname, favoriteteam FROM fans WHERE ID = ?";
		try (Connection conn = connect();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new String[] {
						rs.getString("firstname"),
						rs.getString("lastname"),
						rs.getString("favoriteteam")
					};
				}
				return null;
			}
		}
	}
	
	// updates with the given ID, returns true if a row matched
	boolean updateFan(int id, String first, String last, String team) throws SQLException {
		String sql = "UPDATE fans SET firstname = ?, lastname = ?, favoriteteam = ? WHERE ID = ?";
		try (Connection conn = connect();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, first);
			ps.setString(2, last);
			ps.setString(3, team);
			ps.setInt(4, id);
			return ps.executeUpdate() > 0;
		}
	}
	
	// ----- Button Actions -----
	
	void displayFan() {
		Integer id = readId();
		if (id == null) {
			return;
		}
		try {
			String[] fan = getFan(id);
			if (fan == null) {
				firstNameField.clear();
				lastNameField.clear();
				favoriteTeamField.clear();
				statusLabel.setText("No fan found with ID " + id + ".");
			}
			else {
				firstNameField.setText(fan[0]);
				lastNameField.setText(fan[1]);
				favoriteTeamField.setText(fan[2]);
				statusLabel.setText("Record displayed.");
			}
		}
		catch (SQLException ex) {
			statusLabel.setText("Database error: " + ex.getMessage());
		}
	}
	
	void updateFan() {
		Integer id = readId();
		if (id == null) {
			return;
		}
		String first = firstNameField.getText().trim();
		String last = lastNameField.getText().trim();
		String team = favoriteTeamField.getText().trim();
		
		if (first.length() > MAX_LENGTH || last.length() > MAX_LENGTH || team.length() > MAX_LENGTH) {
			statusLabel.setText("Each name/team must be " + MAX_LENGTH + " characters or fewer.");
			return;
		}
		try {
			if (updateFan(id, first, last, team)) {
				statusLabel.setText("Record updated.");
			}
			else {
				statusLabel.setText("No fan found with ID " + id + ".");
			}
		}
		catch (SQLException ex) {
			statusLabel.setText("Database error: " + ex.getMessage());
		}
	}
	
	private Integer readId() {
		try {
			return Integer.valueOf(idField.getText().trim());
		}
		catch (NumberFormatException ex) {
			statusLabel.setText("Please enter a valid whole-number ID.");
			return null;
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}