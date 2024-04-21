import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MaxLEDLightingApp1 extends Application {
	ToggleButton switchButton;
	ToggleButton ledButton;
	Circle circle;
	ToggleButton[] switches;
	ToggleButton[] leds;
	Circle[] circles;
	Line[] connections;
	int no;

//	GridPane gridPane = new GridPane();
	Pane root1 = new Pane();

//	TilePane root1 = new TilePane();
	public static void main(String[] args) {
		// Launch the JavaFX application
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// main screen
		// buttons
		// add buttons
		Button enter = new Button("Enter Input");
		Button choose = new Button("Choose File");
		// layout the buttons
		String buttonStyle = "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-height: 40px; -fx-min-width: 120px;";
		enter.setStyle(buttonStyle);
		choose.setStyle(buttonStyle);
		// Set up mouseover effect for buttons
		enter.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> enter.setStyle(
				"-fx-background-color: #800080; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-height: 50px; -fx-min-width: 150px;"));
		enter.addEventHandler(MouseEvent.MOUSE_EXITED, e -> enter.setStyle(buttonStyle));

		choose.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> choose.setStyle(
				"-fx-background-color: #800080; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-height: 50px; -fx-min-width: 150px;"));
		choose.addEventHandler(MouseEvent.MOUSE_EXITED, e -> choose.setStyle(buttonStyle));

		// set actions
		enter.setOnAction(e -> {
			// Call the LED lighting function
			LEDLightingCalculator LEDLightingCalculator = new LEDLightingCalculator();
			LEDLightingResult result = LEDLightingCalculator.getInputAndCalculateLEDs();
			VBox root = createUITab(result);
			Scene scene1 = new Scene(root, 700, 500);
			primaryStage.setScene(scene1);
		});
		choose.setOnAction(e -> {
			// Create a FileChooser
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose File");
			// Show the FileChooser dialog
			File selectedFile = fileChooser.showOpenDialog(primaryStage);

			// Check if a file was selected
			if (selectedFile != null) {
				// Do something with the selected file, such as passing its path to a method
				String filePath = selectedFile.getAbsolutePath();
				LEDLightingCalculator LEDLightingCalculator = new LEDLightingCalculator();
				LEDLightingResult result = LEDLightingCalculator.readInputFromFile(filePath);
				VBox root = createUITab(result);
				Scene scene1 = new Scene(root, 700, 500);
				primaryStage.setScene(scene1);
			}
		});
//---------------
		// Create a label for the title
		Label titleLabel = new Label("~<* Max LED Lighting App *>~");
		titleLabel.setFont(Font.font("Roman", FontWeight.BOLD, 40));
		titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);

		// Set up the scale transition for continuous font size animation
		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3), titleLabel);
		scaleTransition.setFromX(1.2);
		scaleTransition.setToX(0.8);
		scaleTransition.setFromY(1.2);
		scaleTransition.setToY(0.8);
		scaleTransition.setAutoReverse(true);
		scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
		scaleTransition.play();
		// Layout the title and buttons
		VBox vbox = new VBox(20); // Increased spacing between title and buttons
		vbox.getChildren().addAll(titleLabel, enter, choose);
		vbox.setAlignment(Pos.CENTER);

		// add image
		Image backgroundImage = new Image("we.png");
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

		StackPane stackPane = new StackPane();
		stackPane.setBackground(new Background(background));

		// stackPane.getChildren().add(imageView);
		stackPane.getChildren().add(vbox);
		// scene
		Scene scene = new Scene(stackPane, 900, 500);
		primaryStage.setTitle("<< LED Lighting >>");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private VBox createUITab(LEDLightingResult result) {
		// Create a VBox for the user interface
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
//		
		// Create a TabPane to hold different tabs
		TabPane tabPane = new TabPane();

		// Display the correct maximum number of LEDs in the first tab
		Tab resultTab = createResultTab(result);
		tabPane.getTabs().add(resultTab);

		// Display the DP table in the second tab
		Tab dpTableTab = createDPTableTab(result);
		tabPane.getTabs().add(dpTableTab);

		// Display the LED Connection in the third tab
		if (LEDLightingCalculator.getN() < 12) {
			System.out.println("under 10");
			Tab drawTab = createDrawTab(result);
			tabPane.getTabs().add(drawTab);
		} else {
			System.out.println("more 10");
			Tab drawTab2 = createDrawTab2(result);
			tabPane.getTabs().add(drawTab2);
		}
		vbox.getChildren().add(tabPane);

		return vbox; // Return the VBox
	}

	private Tab createResultTab(LEDLightingResult result) {
		// Display the correct maximum number of LEDs in the first tab
		Tab resultTab = new Tab("Result");
		VBox resultVBox = new VBox();
		resultVBox.setAlignment(Pos.CENTER);
		resultVBox.setSpacing(10);

		ScrollPane scrollPane = new ScrollPane(resultVBox);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		// Create a Background with the image
		try {
			Image backgroundImage = new Image("b.png");
			BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
					new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

			resultVBox.setBackground(new Background(background));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Image backgroundImage = new Image("b.png");

			// Adjust BackgroundSize to cover both width and height
			BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);

			// Adjust BackgroundRepeat to REPEAT or NO_REPEAT based on your preference
			BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,
					BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize);

			// Apply background to GridPane
			Background gridBackground = new Background(background);
			resultVBox.setBackground(gridBackground);

			// Set background color for ScrollPane
			BackgroundFill backgroundFill = new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY);
			Background backgroundColor = new Background(backgroundFill);

			// Set background color for the content of ScrollPane
			Background contentBackground = new Background(background);
			scrollPane.setBackground(backgroundColor);
			scrollPane.setContent(resultVBox);
			((Region) scrollPane.getContent()).setBackground(contentBackground);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Label maxLEDsLabel = new Label("Maximum LEDs: " + result.getMaxLEDs());
		maxLEDsLabel.setStyle(
				"-fx-border-color: #800080; -fx-padding: 5px; -fx-font-weight: bold; -fx-background-color: #89CFF0;");
		resultVBox.getChildren().add(maxLEDsLabel);

		// Display LEDs and sources in the first tab
		LED[] ledConnections = result.getLEDs();
		for (int i = 0; i < ledConnections.length; i++) {
			LED connection = ledConnections[i];
			Label connectionLabel = new Label(
					"LED " + connection.getLed() + " connected to Source " + connection.getSource());
			connectionLabel.setStyle(
					"-fx-border-color: #800080; -fx-padding: 5px; -fx-font-weight: bold; -fx-background-color: #89CFF0;");
			resultVBox.getChildren().add(connectionLabel);
		}
		resultTab.setStyle("-fx-background-color: #FF6F61; -fx-text-fill: white; -fx-font-weight: bold;");
		resultTab.setContent(scrollPane);
		return resultTab;
	}

	private Tab createDPTableTab(LEDLightingResult result) {
		// Display the DP table in the second tab
		Tab dpTableTab = new Tab("DP Table");
		VBox dpTableVBox = new VBox();
		dpTableVBox.setAlignment(Pos.CENTER);
		dpTableVBox.setSpacing(10);

		int[][] dpTable = result.getDPTable();
//		TableView<String[]> tableView = createTableView(dpTable);
		LEDLightingCalculator calculator1 = new LEDLightingCalculator();

		int[] ledOrder = calculator1.getLedsOrder();
		TableView<String[]> tableView = createTableView(dpTable, ledOrder);

		// Set the background color for the VBox
		dpTableVBox.setStyle("-fx-background-color: #294421;");
		dpTableTab.setStyle("-fx-background-color: #FF6F61; -fx-text-fill: white; -fx-font-weight: bold;");

		dpTableVBox.getChildren().add(tableView);

		dpTableTab.setContent(dpTableVBox);
		return dpTableTab;
	}

	private Tab createDrawTab(LEDLightingResult result) {
		LEDLightingCalculator calculator = new LEDLightingCalculator();
		int numberOfLEDs = calculator.getN();
		int[] ledOrder = calculator.getLedsOrder();
		LED[] ledConnections = result.getLEDs();

		System.out.println("number of leds" + calculator.getN());
		System.out.println("order of leds" + Arrays.toString(calculator.getLedsOrder()));
		System.out.println("max" + result.getNumberOfLEDs());
		System.out.println("the max led" + result.getLEDs());

		// Display the LED Connection in the third tab
		Tab drawTab = new Tab("LED Connection");

		ScrollPane scrollPane = new ScrollPane(root1);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setPrefSize(900, 200);
		scrollPane.setMinSize(400, 100);
		root1.setMinSize(900, 1000);

		switches = new ToggleButton[numberOfLEDs];
		leds = new ToggleButton[numberOfLEDs];
		circles = new Circle[numberOfLEDs];

		// lines = new Line[numberOfLEDs];
		System.out.println("number of leds" + numberOfLEDs);
		for (int i = 0; i < numberOfLEDs; i++) {
			switchButton = new ToggleButton("Switch " + (i + 1));
			ledButton = new ToggleButton();

			// switch button styling
			String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-height: 40px; -fx-min-width: 120px;";
			switchButton.setStyle(buttonStyle);
			ledButton.setStyle(buttonStyle);
			Label ledLabel = new Label("LED " + ledOrder[i]);
			ledButton.setGraphic(ledLabel);
			switches[i] = switchButton;
			leds[i] = ledButton;
			// create LEDS
			circle = createCircle(300, 10 + 30 * i);
			circle.setAccessibleText("LED " + ledOrder[i]);

			circles[i] = circle;

			// Add ledButton and circle to the GridPane
//			initializeConnections();
			root1.getChildren().add(switchButton);
			root1.getChildren().add(ledButton);
			root1.getChildren().add(circle);
//			arrangeNodes(root1);
		}
		initializeConnections();
		arrangeNodes(root1);
		for (LED connection : ledConnections) {
			int ledOrder1 = connection.getLed();
			int switchNumber = connection.getSource() - 1; // Assuming switches are 0-indexed

			// Find the LED index in the ledOrder array
			int ledIndex = -1;
			for (int i = 0; i < ledOrder.length; i++) {
				if (ledOrder[i] == connection.getLed()) {
					ledIndex = i;
					break;
				}
			}

			if (switchNumber >= 0 && switchNumber < switches.length && ledIndex >= 0 && ledIndex < leds.length) {
				switchButton = switches[switchNumber];
				ledButton = leds[ledIndex];
				String buttonStyle = "-fx-background-color: #FFA500; -fx-border-color: #FF0000; -fx-border-width: 2px; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-height: 40px; -fx-min-width: 120px;";
				switchButton.setStyle(buttonStyle);
				ledButton.setStyle(buttonStyle);
				connectSwitchToLed(switchNumber, ledIndex);

				System.out.println("Connected switch " + (switchNumber + 1) + " to LED " + connection.getLed());
			}
			// Find the circle with the corresponding LED order
			Circle foundCircle = null;
			for (Circle circle : circles) {
				if (circle.getAccessibleText().equals("LED " + ledOrder1)) {
					foundCircle = circle;
					break;
				}

			}

			// If the circle is found, perform the flashing animation
			if (foundCircle != null) {
				FillTransition ft = new FillTransition(Duration.millis(500), foundCircle, Color.RED, Color.YELLOW);
				ft.setCycleCount(Animation.INDEFINITE);
				ft.setAutoReverse(true);
				ft.play();
			}

		}

		// Create a Background with the image
		try {
			Image backgroundImage = new Image("s.png");

			// Adjust BackgroundSize to cover both width and height
			BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);

			// Adjust BackgroundRepeat to REPEAT or NO_REPEAT based on your preference
			BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,
					BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize);

			// Apply background to GridPane
			Background gridBackground = new Background(background);
			root1.setBackground(gridBackground);

			// Set background color for ScrollPane
			BackgroundFill backgroundFill = new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY);
			Background backgroundColor = new Background(backgroundFill);

			// Set background color for the content of ScrollPane
			Background contentBackground = new Background(background);
			scrollPane.setBackground(backgroundColor);
			scrollPane.setContent(root1);
			((Region) scrollPane.getContent()).setBackground(contentBackground);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		root1.setMinSize(900, 500);
		drawTab.setStyle("-fx-background-color: #FF6F61; -fx-text-fill: white; -fx-font-weight: bold;");
		// drawTab.setContent(scrollPane);
		drawTab.setContent(new StackPane(scrollPane));

		// drawTab.setContent(gridPane);
		return drawTab;
	}

	private Tab createDrawTab2(LEDLightingResult result) {
		LEDLightingCalculator calculator = new LEDLightingCalculator();
		int numberOfLEDs = calculator.getN();
		int[] ledOrder = calculator.getLedsOrder();
		LED[] ledConnections = result.getLEDs();

		System.out.println("number of leds" + calculator.getN());
		System.out.println("order of leds" + Arrays.toString(calculator.getLedsOrder()));
		System.out.println("max" + result.getNumberOfLEDs());
		System.out.println("the max led" + Arrays.toString(result.getLEDs()));

		// Display the LED Connection in the third tab
		Tab drawTab2 = new Tab("LED Connection");
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(20);
		ScrollPane scrollPane = new ScrollPane(gridPane);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		// scrollPane.setMinSize(400, 300);
		switches = new ToggleButton[numberOfLEDs];
		leds = new ToggleButton[numberOfLEDs];
		circles = new Circle[numberOfLEDs];

		// lines = new Line[numberOfLEDs];
		System.out.println("number of leds" + numberOfLEDs);
		for (int i = 0; i < numberOfLEDs; i++) {
			switchButton = new ToggleButton("Switch " + (i + 1));
			ledButton = new ToggleButton();

			// switch button styling
			String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-height: 40px; -fx-min-width: 120px;";
			switchButton.setStyle(buttonStyle);
			ledButton.setStyle(buttonStyle);
			Label ledLabel = new Label("LED " + ledOrder[i]);
			ledButton.setGraphic(ledLabel);
			switches[i] = switchButton;
			leds[i] = ledButton;
			// create LEDS
			Circle circle = createCircle(300, 10 + 30 * i);
			circle.setAccessibleText("LED " + ledOrder[i]);

			circles[i] = circle;

			// Add ledButton and circle to the GridPane
			gridPane.add(switchButton, 0, i);
			gridPane.add(ledButton, 18, i);
			gridPane.add(circle, 19, i);
			gridPane.setHgap(20);
			gridPane.setVgap(10);

		}

		// Use LED connections to connect circles to switches and set the corresponding
		// switch as selected
		// Call connectSwitchToLed method to establish connections
		for (LED connection : ledConnections) {
			int ledOrder1 = connection.getLed();
			int switchNumber = connection.getSource() - 1; // Assuming switches are 0-indexed

			// Find the LED index in the ledOrder array
			int ledIndex = -1;
			for (int i = 0; i < ledOrder.length; i++) {
				if (ledOrder[i] == connection.getLed()) {
					ledIndex = i;
					break;
				}
			}

			if (switchNumber >= 0 && switchNumber < switches.length && ledIndex >= 0 && ledIndex < leds.length) {
				switchButton = switches[switchNumber];
				ledButton = leds[ledIndex];
				String buttonStyle = "-fx-background-color: #FFA500; -fx-border-color: #FF0000; -fx-border-width: 2px; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-height: 40px; -fx-min-width: 120px;";
				switchButton.setStyle(buttonStyle);
				ledButton.setStyle(buttonStyle);
//		        connectSwitchToLed(switchNumber, ledIndex);

				System.out.println("Connected switch " + (switchNumber + 1) + " to LED " + connection.getLed());
			}
			// Find the circle with the corresponding LED order
			Circle foundCircle = null;
			for (Circle circle : circles) {
				if (circle.getAccessibleText().equals("LED " + ledOrder1)) {
					foundCircle = circle;
					break;
				}
			}

			// If the circle is found, perform the flashing animation
			if (foundCircle != null) {

				FillTransition ft = new FillTransition(Duration.millis(500), foundCircle, Color.RED, Color.YELLOW);
				ft.setCycleCount(Animation.INDEFINITE);
				ft.setAutoReverse(true);
				ft.play();
			}

//			connectSwitchToLED(switch1,ledOrder1);

		}

		// Create a Background with the image
		try {
			Image backgroundImage = new Image("s.png");

			// Adjust BackgroundSize to cover both width and height
			BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);

			// Adjust BackgroundRepeat to REPEAT or NO_REPEAT based on your preference
			BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,
					BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize);

			// Apply background to GridPane
			Background gridBackground = new Background(background);
			gridPane.setBackground(gridBackground);

			// Set background color for ScrollPane
			BackgroundFill backgroundFill = new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY);
			Background backgroundColor = new Background(backgroundFill);

			// Set background color for the content of ScrollPane
			Background contentBackground = new Background(background);
			scrollPane.setBackground(backgroundColor);
			scrollPane.setContent(gridPane);
			((Region) scrollPane.getContent()).setBackground(contentBackground);
		} catch (Exception e) {
			e.printStackTrace();
		}

		drawTab2.setStyle("-fx-background-color: #FF6F61; -fx-text-fill: white; -fx-font-weight: bold;");
		// drawTab.setContent(scrollPane);
		drawTab2.setContent(new StackPane(scrollPane));

		// drawTab.setContent(gridPane);
		return drawTab2;
	}

	private TableView<String[]> createTableView(int[][] dpTable, int[] ledOrder) {
		TableView<String[]> tableView = new TableView<>();
		ObservableList<String[]> data = FXCollections.observableArrayList();

		for (int i = 0; i < dpTable.length; i++) {
			String[] rowData = new String[dpTable[i].length + 1]; // Increase the length by 1
			rowData[0] = "Switch " + i; // Add switch number to the first column
			for (int j = 0; j < dpTable[i].length; j++) {
				rowData[j + 1] = String.valueOf(dpTable[i][j]);
			}
			data.add(rowData);
		}

		// Add default "0" column
		TableColumn<String[], String> defaultColumn = new TableColumn<>("Switch Number");
		final int defaultColumnIndex = 0;
		defaultColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[defaultColumnIndex]));
		tableView.getColumns().add(defaultColumn);

		// Create a new column for switches
		TableColumn<String[], String> switchColumn = new TableColumn<>("LED 0");
		final int switchColumnIndex = 1; // Incremented by 1
		switchColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[switchColumnIndex]));
		tableView.getColumns().add(switchColumn);

		// Create table columns using the order of LEDs
		for (int i = 0; i < ledOrder.length; i++) {
			TableColumn<String[], String> column = new TableColumn<>("LED " + ledOrder[i]);
			final int columnIndex = i + 2; // Start from 2 to skip the switch and default "0" columns
			column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[columnIndex]));
			tableView.getColumns().add(column);
		}

		// Method to print the DP table
		for (int i = 0; i < dpTable.length; i++) {
			for (int j = 0; j < dpTable[0].length; j++) {
				System.out.print(dpTable[i][j] + " ");
			}
			System.out.println();
		}

		// Set the background color for the TableView
		tableView.setItems(data);
		tableView.setStyle("-fx-control-inner-background: green;");

		return tableView;
	}

	private Circle createCircle(double centerX, double centerY) {
		System.out.println("Creating circle at: (" + centerX + ", " + centerY + ")");
		Circle circle = new Circle(centerX, centerY, 10, Color.BLACK);
		return circle;
	}

	private void initializeConnections() {
		connections = new Line[switches.length];
		for (int i = 0; i < switches.length; i++) {
			connections[i] = new Line();
		}
	}

	private void arrangeNodes(Pane root) {
		double switchSpacing = 50;
		double ledSpacing = 50;
		double cSpacing = 23;

		// Arrange switches
		for (int i = 0; i < switches.length; i++) {
			switchButton = switches[i];
			switchButton.setLayoutX(50);
			switchButton.setLayoutY(50 + i * switchSpacing);
		}

		// Arrange LEDs
		for (int i = 0; i < leds.length; i++) {
			ledButton = leds[i];
			ledButton.setLayoutX(500);
			ledButton.setLayoutY(50 + i * ledSpacing);
		}

		// Arrange circles
		for (int i = 0; i < circles.length; i++) {
			circle = circles[i];
			circle.setLayoutX(302);
			circle.setLayoutY(50 + i * cSpacing);
		}

		// Initialize connections array after switches size is known
		connections = new Line[switches.length];

		// Arrange connections
		for (int i = 0; i < switches.length; i++) {
			connections[i] = new Line();
			connections[i].setStrokeWidth(2);
		}
	}

	private void connectSwitchToLed(int switchNumber, int ledIndex) {
		if (ledIndex >= 0 && ledIndex < connections.length && ledIndex < switches.length && ledIndex < leds.length) {
			Line connection = connections[ledIndex];
			switchButton = switches[switchNumber];
			ledButton = leds[ledIndex];

			// Calculate the center coordinates of the first button (switchButton)
			double switchCenterX = switchButton.getLayoutX()*3.4 + switchButton.getWidth() ;
			double switchCenterY = switchButton.getLayoutY() + switchButton.getHeight();

			// Use the center coordinates for the start of the line
			connection.setStartX(switchCenterX);
			connection.setStartY(switchCenterY);

			double ledCenterX = ledButton.getLayoutX() + ledButton.getWidth();
			double ledCenterY = ledButton.getLayoutY() + ledButton.getHeight();
			connection.setEndX(ledCenterX);
			connection.setEndY(ledCenterY);
			connection.setStroke(Color.BLACK);

			// Add the line to the root1 pane
			root1.getChildren().add(connection);

			// Print statements for debugging
			System.out.println("Connected switch " + switchNumber + " to LED " + (ledIndex + 1));
			System.out.println("Switch position: (" + switchCenterX + ", " + switchCenterY + ")");
			System.out.println("LED position: (" + ledCenterX + ", " + ledCenterY + ")");
		}
	}

}