/*
Trenten Coffman
November 16, 2026
Assignment 10.2

Tests functionality of FanApp
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.FutureTask;
import javafx.application.Platform;

public class FanTest {
	static final int ID = 999001;
	static int failed = 0;
	
	static void check(String name, boolean ok) {
		if (!ok) {
			failed++;
		}
		System.out.println((ok ? "PASS: " : "FAIL: ") + name);
	}
	
	static void exec(String sql, Object... params) throws SQLException {
		try (Connection conn = DriverManager.getConnection(FanApp.URL, FanApp.USER, FanApp.PASSWORD);
			PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
			ps.executeUpdate();
		}
	}
	
	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Platform.startup(() -> { });
		
		exec("DELETE FROM fans WHERE ID = ?", ID);
		exec("INSERT INTO fans VALUES (?, 'Test', 'Fan', 'Packers')", ID);
		try {
			FutureTask<Void> task = new FutureTask<>(() -> {
				runTests();
				return null;
			});
			Platform.runLater(task);
			task.get();
		}
		catch (Exception e) {
			failed++;
			e.printStackTrace();
		}
		finally {
			exec("DELETE FROM fans WHERE ID = ?", ID);
		}
		System.out.println(failed == 0 ? "All tests passed." : failed + " test(s) FAILED.");
		System.exit(failed);
	}
	
	static void runTests() throws SQLException {
		FanApp app = new FanApp();
		app.buildRoot();
		
		// Database methods
		String[] fan = app.getFan(ID);
		check("getFan finds an existing fan",
			fan != null && fan[0].equals("Test") && fan[1].equals("Fan") && fan[2].equals("Packers"));
		check("getFan returns null for a missing ID", app.getFan(-1) == null);
		check("updateFan returns false for a missing ID", !app.updateFan(-1, "A", "B", "C"));
		
		// Display button
		app.idField.setText(String.valueOf(ID));
		app.displayButton.fire();
		check("Display button fills in the record",
			app.firstNameField.getText().equals("Test")
			&& app.lastNameField.getText().equals("Fan")
			&& app.favoriteTeamField.getText().equals("Packers"));
		
		// Update button (uses the values now showing, with two changed)
		app.firstNameField.setText("Alex");
		app.favoriteTeamField.setText("Bears");
		app.updateButton.fire();
		fan = app.getFan(ID);
		check("Update button saves the changes", fan[0].equals("Alex") && fan[2].equals("Bears"));
		
		// Bad input
		app.idField.setText("-1");
		app.displayButton.fire();
		check("Missing ID shows a message", app.statusLabel.getText().contains("No fan found"));
		
		app.idField.setText("abc");
		app.displayButton.fire();
		check("Non-numeric ID is rejected", app.statusLabel.getText().contains("valid"));
		
		app.idField.setText(String.valueOf(ID));
		app.firstNameField.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZ"); // 26 characters
		app.updateButton.fire();
		check("Text over 25 characters is rejected", app.getFan(ID)[0].equals("Alex"));
	}
}