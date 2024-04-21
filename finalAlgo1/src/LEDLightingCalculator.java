
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class LEDLightingCalculator {

	private static int n; // Number of LEDs
	private static int[] ledsOrder; // Order of LEDs

	public static LEDLightingResult getInputAndCalculateLEDs() {
		// Get input for the number of LEDs
		TextInputDialog numberDialog = new TextInputDialog();
		numberDialog.setTitle("Number of LEDs");
		numberDialog.setHeaderText(null);
		numberDialog.setContentText("Enter the number of LEDs:");
		// Apply styles to the dialog pane
		numberDialog.getDialogPane().setStyle("-fx-background-color: #FF6F61;" // Set the background color
		);
		// declare var string to store the input
		String result = numberDialog.showAndWait().orElse("");

		// Check if the user clicked Cancel or OK without entering anything
		if (result.trim().isEmpty() || !result.matches("\\d+")) {
			showWarning("Please enter a valid number for the number of LEDs.");
			return getInputAndCalculateLEDs(); // Retry the input
		}
		// n is the number of LEDs
		n = Integer.parseInt(result);

		// Get input for the order of LEDs
		TextInputDialog orderDialog = new TextInputDialog();
		orderDialog.setTitle("Order of LEDs");
		orderDialog.setHeaderText(null);
		orderDialog.setContentText("Enter the order of LEDs (space-separated):");
		// Add a button for generating a random order
		ButtonType randomOrderButton = new ButtonType("Random Order :)");
		orderDialog.getDialogPane().getButtonTypes().add(randomOrderButton);

		// Create an instance of LEDLightingCalculator
		LEDLightingCalculator calculator = new LEDLightingCalculator();

		// Set action for the random order button
		orderDialog.setResultConverter(dialogButton -> {
			if (dialogButton == randomOrderButton) {
				int[] randomOrder = calculator.generateRandomOrder(n);
				StringBuilder randomOrderString = new StringBuilder();
				for (int i = 0; i < randomOrder.length; i++) {
					randomOrderString.append(randomOrder[i]);
					if (i < randomOrder.length - 1) {
						randomOrderString.append(" ");
					}
				}
				return randomOrderString.toString(); // Return the generated random order
			}
			return orderDialog.getEditor().getText(); // Return the entered order
		});
		// Apply styles to the dialog pane
		orderDialog.getDialogPane().setStyle("-fx-background-color: #89CFF0;" // Set the background color
		);
		result = orderDialog.showAndWait().orElse("");

		// Check if the user clicked Cancel or OK without entering anything
		if (result.trim().isEmpty() || !result.matches("\\d+( \\d+)*")) {
			showWarning("Please enter a valid space-separated list of numbers for the order of LEDs.");
			return getInputAndCalculateLEDs(); // Retry the input
		}

		// Split the input string into tokens using spaces as delimiters
		String[] tokens = result.split(" ");

		// Validate that the number of LEDs and the length of the order array are the
		// same
		if (tokens.length != n) {
			showWarning("The number of LEDs and the length of the order must be the same.");
			return getInputAndCalculateLEDs(); // Retry the input
		}

		// Create an array to store the parsed integers
		ledsOrder = new int[tokens.length];

		// Iterate over the tokens and parse each one into an integer
		for (int i = 0; i < tokens.length; i++) {
			ledsOrder[i] = Integer.parseInt(tokens[i]);

			// Check for duplicates before the current index
			for (int j = 0; j < i; j++) {
				if (ledsOrder[j] == ledsOrder[i]) {
					// Duplicate number detected
					showWarning("Duplicate numbers are not allowed in the order of LEDs.");
					return getInputAndCalculateLEDs(); // Retry the input
				}
			}
		}
//		}
		return LEDLightingCalculator.maxLEDLighting(n, ledsOrder);

	}

	private static void showWarning(String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(message);

		// Get the result when the user closes the alert
		alert.showAndWait().ifPresent(result -> {
			if (result == ButtonType.OK) {
				// User clicked OK, close the alert
				alert.close();
				System.out.println("OK clicked");
			}
		});
	}

	// LED lighting function using dynamic programming
	public static LEDLightingResult maxLEDLighting(int n, int[] ledsOrder) {
		// Create a 2D array to store intermediate results
		int[][] dp = new int[n + 1][n + 1];

		// Step 1: Dynamic Programming to fill the DP array
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				if (ledsOrder[j - 1] == i) {
					// If the current LED matches the order, increment the count
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					if (dp[i][j - 1] > dp[i - 1][j]) {
						dp[i][j] = dp[i][j - 1];
					} else {
						dp[i][j] = dp[i - 1][j];
					}

				}
			}
		}

		// Step 2: Extract the maximum number of LEDs that can be lit
		int maxL = dp[n][n];
		System.out.println("max leds" + maxL);

		// Step 3: Reconstruct the path to find the LEDs that give the expected result
		LED[] ledArray = new LED[maxL];
		int i = n, j = n;
		int index = maxL - 1; // Index for the LED array
		while (i > 0 && j > 0) {
			System.out.println(i + " " + j);
			System.out.println(ledsOrder[j - 1] + "=?" + i + " " + dp[i][j] + "=?" + dp[i - 1][j - 1] + 1);
			if (ledsOrder[j - 1] == i && dp[i][j] == dp[i - 1][j - 1] + 1) {
				// If the current LED contributes to the maximum, add it to the result
				ledArray[index] = new LED(ledsOrder[j - 1], i);
				index--;
				i--;
				j--;
				System.out.println("take " + i + " " + j);
			} else if (dp[i][j] == dp[i - 1][j]) {
				System.out.println(dp[i][j] + "=" + dp[i - 1][j]);
				// If the current cell comes from the top, move upward
				i--;
				System.out.println("move top " + i + " " + j);
			} else {
				// If the current cell comes from the left, move leftward
				j--;
				System.out.println("move left " + i + " " + j);
			}
		}

		// Step 4: Sort the LED connections based on their order
		Arrays.sort(ledArray, Comparator.comparingInt(LED::getLed));

		// Step 5: Return the result as an LEDLightingResult object
		return new LEDLightingResult(maxL, ledArray, dp);
	}

//	// Method to print LED connections
//	public static void printLEDs(List<LED> LEDs) {
//		for (LED connection : LEDs) {
//			System.out.println("LED " + connection.getLed() + " connected to Source " + connection.getSource());
//		}
//	}

	// Method to print LED connections
	public static void printLEDs(LED[] LEDs) {
		for (LED connection : LEDs) {
			System.out.println("LED " + connection.getLed() + " connected to Source " + connection.getSource());
		}
	}

	public static LEDLightingResult readInputFromFile(String filePath) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			// Read the number of LEDs from the first line
			n = Integer.parseInt(reader.readLine().trim());

			// Read the order of LEDs from the second line
			String[] orderTokens = reader.readLine().trim().split(" ");

			// Validate the input format
			if (orderTokens.length != n) {
				showWarning("Invalid input format. The number of LEDs does not match the order length.");
				return readInputFromFile(filePath); // Retry the input
			}

			// Parse the order tokens into an array of integers
			ledsOrder = new int[n];
			for (int i = 0; i < n; i++) {
				ledsOrder[i] = Integer.parseInt(orderTokens[i]);

				// Check for duplicates before the current index
				for (int j = 0; j < i; j++) {
					if (ledsOrder[j] == ledsOrder[i]) {
						// Duplicate number detected
						showWarning("Duplicate numbers are not allowed in the order of LEDs.");
						return readInputFromFile(filePath); // Retry the input
					}
				}
			}

			// Call the LED lighting function with the obtained input
			return maxLEDLighting(n, ledsOrder);
		} catch (IOException | NumberFormatException e) {
			// Handle file reading or parsing errors
			showWarning("Error reading input from file: " + ((Throwable) e).getMessage());
			return readInputFromFile(filePath); // Retry the input
		}

	}

	private static int[] generateRandomOrder(int numLEDs) {
		if (numLEDs <= 0) {
			// Handle the case where numLEDs is not a positive number
			return new int[0]; // or throw an exception, depending on your requirements
		}

		int[] orderArray = new int[numLEDs];

		// Fill the array with numbers from 1 to numLEDs
		for (int i = 1; i <= numLEDs; i++) {
			orderArray[i - 1] = i;
		}

		// Shuffle the array to get a random order
		Random random = new Random();
		for (int i = numLEDs - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			// Swap elements
			int temp = orderArray[index];
			orderArray[index] = orderArray[i];
			orderArray[i] = temp;
		}

		return orderArray;
	}

	public static int getN() {
		return n;
	}

	public static void setN(int n) {
		LEDLightingCalculator.n = n;
	}

	public int[] getLedsOrder() {
		return ledsOrder;
	}

	public static void setLedsOrder(int[] ledsOrder) {
		LEDLightingCalculator.ledsOrder = ledsOrder;
	}

}
