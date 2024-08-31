package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Main extends Application {

	private String titles;
	private File file;
	private CLinkedList<Integer, CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>> years = new CLinkedList<>();
	private Button back = new Button("Go Back");
	private Font btnFont = Font.font("Times New Roman", FontWeight.NORMAL, 12.5);

	@Override
	public void start(Stage primaryStage) {
		try {
			// set a title
			primaryStage.setTitle("Electricity in the Gaza Strip");

			// set a new icon
			primaryStage.getIcons().add(new Image("icon.jpg"));

			// set the primary scene in the stage
			Scene scene = primary(primaryStage);
			primaryStage.setScene(scene);

			//
			back.setOnAction(e -> {
				primaryStage.setScene(scene);
			});

			// show the stage
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		System.out.println(Font.getFamilies());
		launch(args);
	}

	public Scene primary(Stage stage) {
		// create the root pane
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(30));
		root.setStyle("-fx-background-color: white;");

		// create scene
		Scene scene = new Scene(root, 700, 600);
		scene.setFill(Color.WHITE);

		// create imageView to hold the image
		ImageView img = new ImageView(new Image("Gaza.jpg"));
		img.fitHeightProperty().bind(root.heightProperty().divide(3));
		img.fitWidthProperty().bind(root.widthProperty().divide(2));

		// text object holds the text and text2 holds the reference(OCHA)
		String desc = "The ongoing power shortage has severely affected the availability of \r\n"
				+ "essential services, particularly health, water and sanitation services, and undermined Gaza’s fragile \r\n"
				+ "economy, particularly the manufacturing and agriculture sectors.";
		Text text = new Text(desc);
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 15));
		Text text2 = new Text("-OCHA");
		// text2.setFill(Color.RED);
		text2.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 10));

		// create a VBox to hold the imageView with its contents
		VBox box = new VBox(10);
		box.getChildren().addAll(img, text);
		box.setAlignment(Pos.CENTER);

		//
		HBox tbox = new HBox();
		tbox.getChildren().add(text2);
		tbox.setAlignment(Pos.BASELINE_RIGHT);
		tbox.setPadding(new Insets(5, 25, 5, 25));

		//
		VBox bBox = new VBox();
		bBox.getChildren().addAll(box, tbox);

		// add box to the root
		root.setTop(bBox);

		// add the buttons from method loading
		root.setCenter(loading(stage));

		return scene;
	}

	public void load() throws Exception {
		try {
			Scanner sc = new Scanner(file);
			titles = sc.nextLine();
			int count = 0;
			while (sc.hasNext()) {
//				String month = "";
				String[] record = sc.nextLine().split(",");
				String[] date = record[0].split("/");
				int monthInt = Integer.parseInt(date[0]);
				int day = Integer.parseInt(date[1]);
				int year = Integer.parseInt(date[2]);

				System.out.println(day + "/" + monthInt + "/" + year);
				ElectricityRecord rec = new ElectricityRecord(Double.parseDouble(record[1]),
						Double.parseDouble(record[2]), Double.parseDouble(record[3]), Double.parseDouble(record[4]),
						Double.parseDouble(record[5]), Double.parseDouble(record[6]), Double.parseDouble(record[7]));

				insertRecord(year, monthInt, day, rec);
				count++;

			}
			sc.close();
			System.out.println("count = " + count);
		} catch (Exception e) {
			throw new Exception();
		}
	}

	private void insertRecord(int year, int month, int day, ElectricityRecord rec) {

		Node<Integer, CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>> yearNode = years
				.searchSorted(year);
		if (yearNode == null) {
			yearNode = new Node<>(year, new CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>());
			years.insertSorted(year, yearNode.getData());
		}

		CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>> months = yearNode.getData();
		
		Node<Integer, CLinkedList<Integer, ElectricityRecord>> monthNode = months.searchSorted(month);
		if (monthNode == null) {
			monthNode = new Node<>(month, new CLinkedList<Integer, ElectricityRecord>());
			months.insertSorted(month, monthNode.getData());
		}

		CLinkedList<Integer, ElectricityRecord> days = monthNode.getData();
		Node<Integer, ElectricityRecord> dayNode = days.searchSorted(day);
		if (dayNode == null) {
			dayNode = new Node<>(day, rec);
			days.insertSorted(day, rec);
		}
	}

	public VBox loading(Stage stage) {
		// create a GridPane to hold the buttons
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(5);
		pane.setVgap(5);

		// Creating a FileChooser called chooser to select a file:
		FileChooser chooser = new FileChooser();

		// Create a button called select to select a file from the fileChooser and a
		// label called selected
		Button select = new Button("Select a file");
		select.setFont(btnFont);
		Label selected = new Label("No files have been selected");
		selected.setFont(btnFont);
		pane.add(select, 0, 0);
		pane.add(selected, 1, 0);

		// create a button called load to load data
		Button load = new Button("Load data");
		load.setFont(btnFont);
		load.setDisable(true);
		Label loaded = new Label("click the button to load data");
		loaded.setFont(btnFont);
		pane.add(load, 0, 1);
		pane.add(loaded, 1, 1);

		//
		ComboBox<String> cBox = options(stage);

		select.setOnAction(e -> {
			file = chooser.showOpenDialog(stage);
			if (file != null) {
				selected.setText(file.getAbsolutePath());
				load.setDisable(false);
			}
		});

		load.setOnAction(e -> {
			try {
				if (file.getName().equals("Electricity.csv") || file.getName().equals("Electricity.txt")) {
					loaded.setText("Loaded Successfully!");
					loaded.setTextFill(Color.GREEN);
					load();
					cBox.setDisable(false);
					System.out.println(years.searchSorted(2023).getData().searchSorted(5).getData().searchSorted(14));
					System.out.println("good");
				} else {
					loaded.setText("You have Selected a Wrong File!");
					loaded.setTextFill(Color.RED);
				}
			} catch (Exception o) {
				loaded.setText("Error Occured During Loading Process!");
				loaded.setTextFill(Color.RED);
			}
		});

		VBox box = new VBox(30);
		box.getChildren().addAll(pane, cBox);
		box.setPadding(new Insets(40));
		box.setAlignment(Pos.CENTER);
		return box;
	}

	public ComboBox<String> options(Stage stage) {

		// Creating a ComboBox object to select an option
		String[] options = { "Go to Mangement Screen", "Go to Statistics Screen", "Go to Save Screen" };

		ComboBox<String> userOptions = new ComboBox<>(FXCollections.observableArrayList(options));
		userOptions.setDisable(true);

		userOptions.setOnAction(e -> {
			if (userOptions.getValue() == options[0])
				stage.setScene(management());
			else if (userOptions.getValue() == options[1])
				stage.setScene(statistics());
			else if (userOptions.getValue() == options[2])
				stage.setScene(save(stage));

		});

		return userOptions;
	}

	// this method is only to be used in search(int year, int month, int day) and
	// delete(int year, int month, int day) methods
	private Node<Integer, CLinkedList<Integer, ElectricityRecord>> searchMonth(int year, int month) {
		Node<Integer, CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>> n = years.searchSorted(year);
		if (n != null) {
			CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>> months = n.getData();
			if (months != null) {
				Node<Integer, CLinkedList<Integer, ElectricityRecord>> b = months.searchSorted(month);
				if (b != null) {
					return b;
				}
			}
		}
		return null;
	}

	public Node<Integer, ElectricityRecord> search(int year, int month, int day) {
		Node<Integer, CLinkedList<Integer, ElectricityRecord>> b = searchMonth(year, month);
		if (b != null) {
			CLinkedList<Integer, ElectricityRecord> days = b.getData();
			if (days != null) {
				Node<Integer, ElectricityRecord> dayy = days.searchSorted(day);
				if (dayy != null) {
					return dayy;
				}
			}
		}
		return null;
	}

	// this method takes a year as a node(which is a CLinkedList) and returns null
	// if the year is null or the month is not in the data of the year
	public Node<Integer, CLinkedList<Integer, ElectricityRecord>> searchMonthInYear(
			Node<Integer, CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>> year, int month) {

		if (year == null) {
			return null;
		}

		// if year.getData is null, construct a new empty CLinkedList of months as the
		// data of that year
		if (year.getData() == null) {
			year.setData(new CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>());
			return null;
		}

		// if year node and the year.getData linkedList are not null, search for the
		// month and return its reference
		Node<Integer, CLinkedList<Integer, ElectricityRecord>> monthh = year.getData().searchSorted(month);
		return monthh;
	}

	public Node<Integer, ElectricityRecord> searchDayInMonth(
			Node<Integer, CLinkedList<Integer, ElectricityRecord>> month, int day) {

		if (month == null) {
			return null;
		}

		// if month.getData is null, construct a new empty CLinkedList of months as the
		// data of that month
		if (month.getData() == null) {
			month.setData(new CLinkedList<Integer, ElectricityRecord>());
			return null;
		}

		// if month node and the month.getData linkedList are not null, search for the
		// day and return its reference
		Node<Integer, ElectricityRecord> dayy = month.getData().searchSorted(day);
		return dayy;
	}

//	public void insertRecord(Node<Integer, CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>> yearNode,
//			Node<Integer, CLinkedList<Integer, ElectricityRecord>> monthNode, int yearInt, int monthInt, int dayInt,
//			ElectricityRecord record) {
//
//		if (yearNode == null) {
//			yearNode = new Node(yearInt, new CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>());
//			years.insertSorted(yearNode.getLabel(), yearNode.getData());
//		}
//
//		if (monthNode == null) {
//			monthNode = new Node(monthInt, new CLinkedList<Integer, ElectricityRecord>());
//			yearNode.getData().insertSorted(monthNode.getLabel(), monthNode.getData());
//		}
//
//		Node<Integer, ElectricityRecord> day = new Node(dayInt, record);
//		monthNode.getData().insertSorted(day.getLabel(), record);
//	}

	public boolean updateIsraeliLines(int year, int month, int day, double israeli_lines) {
		Node<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			dayy.getData().setOccupation_lines(israeli_lines);
			return true;
		}
		return false;
	}

	public boolean updateEgyptionLines(int year, int month, int day, double Egyption_lines) {
		Node<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			dayy.getData().setEgyption_lines(Egyption_lines);
			return true;
		}
		return false;
	}

	public boolean updateTotal(int year, int month, int day, double total) {
		Node<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			dayy.getData().setDaily_supply(total);
			return true;
		}
		return false;
	}

	public boolean updateDemand(int year, int month, int day, double demand) {
		Node<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			dayy.getData().setDemand(demand);
			return true;
		}
		return false;
	}

	public boolean updatePowerCut(int year, int month, int day, double power_cut) {
		Node<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			dayy.getData().setPower_cuts_hour_day(power_cut);
			return true;
		}
		return false;
	}

	public boolean updatePowerPlant(int year, int month, int day, double power_plant) {
		Node<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			dayy.getData().setPower_plant(power_plant);
			return true;
		}
		return false;
	}

	public boolean updateTemp(int year, int month, int day, double temp) {
		Node<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			dayy.getData().setTemp(temp);
			return true;
		}
		return false;
	}

	public Node<Integer, ElectricityRecord> delete(int year, int month, int day) {
		Node<Integer, CLinkedList<Integer, ElectricityRecord>> b = searchMonth(year, month);
		if (b != null) {
			CLinkedList<Integer, ElectricityRecord> days = b.getData();
			if (days != null) {
				return days.deleteSorted(day);
			}
		}
		return null;
	}

	public Scene management() {
		// create a root node
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(40));
		root.setStyle("-fx-background-color: white;");

		// create a scene
		Scene scene = new Scene(root, 700, 600);

		// create a label
		Label dateLabel = new Label("Select a date");
		dateLabel.setFont(btnFont);

		// create a DatePicker
		DatePicker datePicker = new DatePicker();

		// create an HBox to hold the DatePicker with the label
		HBox dateBox = new HBox(5);
		dateBox.getChildren().addAll(dateLabel, datePicker);
		dateBox.setAlignment(Pos.CENTER);

		// create radioButtons
		Label btnLabel = new Label("What do you want to do?");
		RadioButton insert = new RadioButton("Insert Record   ");
		RadioButton delete = new RadioButton("Delete Record ");
		RadioButton update = new RadioButton("Update Record");
		RadioButton search = new RadioButton("Search Record");

		insert.setFont(btnFont);
		delete.setFont(btnFont);
		update.setFont(btnFont);
		search.setFont(btnFont);

		insert.setDisable(true);
		delete.setDisable(true);
		update.setDisable(true);
		search.setDisable(true);

		btnLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

		ToggleGroup group = new ToggleGroup();
		insert.setToggleGroup(group);
		delete.setToggleGroup(group);
		update.setToggleGroup(group);
		search.setToggleGroup(group);

		// Create a label
		Label screen = new Label("Management Screen");
		screen.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 40));
		screen.setTextAlignment(TextAlignment.CENTER);
		screen.setTextFill(Color.CRIMSON);

		// set the buttons in a VBox
		VBox btnBox = new VBox(10);
		btnBox.getChildren().addAll(btnLabel, insert, delete, update, search);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.setPadding(new Insets(5));

		// set the dateBox and the btnBox in an HBox
		HBox hbox = new HBox(30);
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(dateBox, btnBox);

		// set the basic nodes on the root
		root.setTop(hbox);
		root.setCenter(screen);
		root.setBottom(back);

		// setOnAction
		datePicker.setOnAction(e -> {
			insert.setDisable(false);
			delete.setDisable(false);
			update.setDisable(false);
			search.setDisable(false);

			insert.setSelected(false);
			delete.setSelected(false);
			update.setSelected(false);
			search.setSelected(false);

			LocalDate date = datePicker.getValue();
			insert.setOnAction(i -> {
				if (insert.isSelected()) {
					Parent parent = insertView(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
					root.setCenter(parent);
				}
			});
			update.setOnAction(i -> {
				Parent parent = updateView(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
				root.setCenter(parent);
			});
			delete.setOnAction(i -> {
				Parent parent = deleteView(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
				root.setCenter(parent);
			});
			search.setOnAction(i -> {
				Parent parent = searchView(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
				root.setCenter(parent);
			});

		});

		return scene;
	}

	public Parent insertView(int year, int month, int day) {
		Label exist = new Label("A record for " + month + "/" + day + "/" + year + " already exists!");
		exist.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
		exist.setTextFill(Color.FIREBRICK);

		Node<Integer, CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>> yearr = years.searchSorted(year);
		Node<Integer, CLinkedList<Integer, ElectricityRecord>> monthh = searchMonthInYear(yearr, month);
		Node<Integer, ElectricityRecord> dayy = searchDayInMonth(monthh, day);

		if (yearr != null && monthh != null && dayy != null) {
			return exist;
		}

		Label line1 = new Label("Israeli Lines: ");
		Label plant = new Label("Gaza Plant:");
		Label line2 = new Label("Egyption Lines: ");
		Label total = new Label("Total Daily Supply: ");
		Label demand = new Label("Overall Demand: ");
		Label cuts = new Label("Power Cuts: ");
		Label temp = new Label("Temperature");

		line1.setFont(btnFont);
		plant.setFont(btnFont);
		line2.setFont(btnFont);
		total.setFont(btnFont);
		demand.setFont(btnFont);
		cuts.setFont(btnFont);
		temp.setFont(btnFont);

		TextField tfLine1 = new TextField();
		TextField tfPlant = new TextField();
		TextField tfLine2 = new TextField();
		TextField tfTotal = new TextField();
		TextField tfDemand = new TextField();
		TextField tfCuts = new TextField();
		TextField tfTemp = new TextField();

		tfLine1.setFont(btnFont);
		tfPlant.setFont(btnFont);
		tfLine2.setFont(btnFont);
		tfTotal.setFont(btnFont);
		tfDemand.setFont(btnFont);
		tfCuts.setFont(btnFont);
		tfTemp.setFont(btnFont);

		tfLine1.setPromptText("Enter a new value");
		tfPlant.setPromptText("Enter a new value");
		tfLine2.setPromptText("Enter a new value");
		tfTotal.setPromptText("Enter a new value");
		tfDemand.setPromptText("Enter a new value");
		tfCuts.setPromptText("Enter a new value");
		tfTemp.setPromptText("Enter a new value");

		Button add = new Button("Insert Record");

		// create a GridPane
		GridPane pane = new GridPane();
		pane.setHgap(5);
		pane.setVgap(5);
		pane.setPadding(new Insets(30));
		pane.setAlignment(Pos.CENTER);

		pane.add(line1, 0, 0);
		pane.add(plant, 0, 1);
		pane.add(line2, 0, 2);
		pane.add(total, 0, 3);
		pane.add(demand, 0, 4);
		pane.add(cuts, 0, 5);
		pane.add(temp, 0, 6);

		pane.add(tfLine1, 1, 0);
		pane.add(tfPlant, 1, 1);
		pane.add(tfLine2, 1, 2);
		pane.add(tfTotal, 1, 3);
		pane.add(tfDemand, 1, 4);
		pane.add(tfCuts, 1, 5);
		pane.add(tfTemp, 1, 6);

		pane.add(add, 1, 7);

		Label label = new Label("");
		label.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));

		VBox box = new VBox(5);
		box.getChildren().addAll(pane, label);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(20));

		tfPlant.setDisable(true);
		tfLine2.setDisable(true);
		tfTotal.setDisable(true);
		tfDemand.setDisable(true);
		tfCuts.setDisable(true);
		tfTemp.setDisable(true);

		add.setDisable(true);

		Text tx = new Text("");

		tfLine1.setOnAction(e -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfLine1.getText());
				tx.setText("" + num);
				tfPlant.setDisable(false);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		tfPlant.setOnAction(i -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfPlant.getText());
				tx.setText(tx.getText() + "," + num);
				tfLine2.setDisable(false);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		tfLine2.setOnAction(e -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfLine2.getText());
				tx.setText(tx.getText() + "," + num);
				tfTotal.setDisable(false);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		tfTotal.setOnAction(b -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfTotal.getText());
				tx.setText(tx.getText() + "," + num);
				tfDemand.setDisable(false);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		tfDemand.setOnAction(r -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfDemand.getText());
				tx.setText(tx.getText() + "," + num);
				tfCuts.setDisable(false);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		tfCuts.setOnAction(v -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfCuts.getText());
				tx.setText(tx.getText() + "," + num);
				tfTemp.setDisable(false);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		tfTemp.setOnAction(h -> {
			try {
				label.setText("");
				Double num = Double.parseDouble(tfTemp.getText());
				tx.setText(tx.getText() + "," + num);
				add.setDisable(false);
				label.setText("");
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		add.setOnAction(g -> {
			try {
				String[] numbers = tx.getText().split(",");
				ElectricityRecord rec = new ElectricityRecord(Double.parseDouble(numbers[0]),
						Double.parseDouble(numbers[1]), Double.parseDouble(numbers[2]), Double.parseDouble(numbers[3]),
						Double.parseDouble(numbers[4]), Double.parseDouble(numbers[5]), Double.parseDouble(numbers[6]));
				insertRecord(year, month, day, rec);
				label.setText("The record has been added successfully!");
				label.setTextFill(Color.GREEN);
			} catch (Exception n) {
				label.setText("Error!");
				label.setTextFill(Color.RED);
			}
		});

		return box;

	}

	public Parent updateView(int year, int month, int day) {
		Label exist = new Label("There is no available record for " + month + "/" + day + "/" + year);
		exist.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
		exist.setTextFill(Color.FIREBRICK);
		Node<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy == null) {
			return exist;
		}
		exist.setText("Record for " + month + "/" + day + "/" + year + " has been updated successfully!");
		exist.setTextFill(Color.GREEN);

		Label checkLabel = new Label("Please tick each box next to the attributes you want to update");
		checkLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

		// create checkBox
		CheckBox line1 = new CheckBox("Israeli Lines");
		CheckBox plant = new CheckBox("Power Plants");
		CheckBox line2 = new CheckBox("Egyption Lines");
		CheckBox total = new CheckBox("Total Daily Supply");
		CheckBox demand = new CheckBox("Overall Demand");
		CheckBox cuts = new CheckBox("Power Cuts");
		CheckBox temp = new CheckBox("Temperature");

		line1.setFont(btnFont);
		plant.setFont(btnFont);
		line2.setFont(btnFont);
		total.setFont(btnFont);
		demand.setFont(btnFont);
		cuts.setFont(btnFont);
		temp.setFont(btnFont);

		// create a GridPane
		GridPane pane = new GridPane();
		pane.setHgap(5);
		pane.setVgap(5);
		pane.setPadding(new Insets(30));
		pane.setAlignment(Pos.CENTER);
		pane.add(line1, 0, 0);
		pane.add(plant, 0, 1);
		pane.add(line2, 0, 2);
		pane.add(total, 0, 3);
		pane.add(demand, 0, 4);
		pane.add(cuts, 0, 5);
		pane.add(temp, 0, 6);

		TextField tf1 = new TextField();
		TextField tf2 = new TextField();
		TextField tf3 = new TextField();
		TextField tf4 = new TextField();
		TextField tf5 = new TextField();
		TextField tf6 = new TextField();
		TextField tf7 = new TextField();

		tf1.setPromptText("Enter updated value");
		tf2.setPromptText("Enter updated value");
		tf3.setPromptText("Enter updated value");
		tf4.setPromptText("Enter updated value");
		tf5.setPromptText("Enter updated value");
		tf6.setPromptText("Enter updated value");
		tf7.setPromptText("Enter updated value");

		tf1.setDisable(true);
		tf2.setDisable(true);
		tf3.setDisable(true);
		tf4.setDisable(true);
		tf5.setDisable(true);
		tf6.setDisable(true);
		tf7.setDisable(true);

		Button update1 = new Button("update");
		Button update2 = new Button("update");
		Button update3 = new Button("update");
		Button update4 = new Button("update");
		Button update5 = new Button("update");
		Button update6 = new Button("update");
		Button update7 = new Button("update");

		update1.setDisable(true);
		update2.setDisable(true);
		update3.setDisable(true);
		update4.setDisable(true);
		update5.setDisable(true);
		update6.setDisable(true);
		update7.setDisable(true);

		pane.add(tf1, 1, 0);
		pane.add(tf2, 1, 1);
		pane.add(tf3, 1, 2);
		pane.add(tf4, 1, 3);
		pane.add(tf5, 1, 4);
		pane.add(tf6, 1, 5);
		pane.add(tf7, 1, 6);

		pane.add(update1, 2, 0);
		pane.add(update2, 2, 1);
		pane.add(update3, 2, 2);
		pane.add(update4, 2, 3);
		pane.add(update5, 2, 4);
		pane.add(update6, 2, 5);
		pane.add(update7, 2, 6);

		Label label1 = new Label("");
		Label label2 = new Label("");
		Label label3 = new Label("");
		Label label4 = new Label("");
		Label label5 = new Label("");
		Label label6 = new Label("");
		Label label7 = new Label("");

		label1.setFont(btnFont);
		label2.setFont(btnFont);
		label3.setFont(btnFont);
		label4.setFont(btnFont);
		label5.setFont(btnFont);
		label6.setFont(btnFont);
		label7.setFont(btnFont);

		pane.add(label1, 3, 0);
		pane.add(label2, 3, 1);
		pane.add(label3, 3, 2);
		pane.add(label4, 3, 3);
		pane.add(label5, 3, 4);
		pane.add(label6, 3, 5);
		pane.add(label7, 3, 6);

		// setOnAction
		line1.setOnAction(e -> {
			if (line1.isSelected()) {
				tf1.setDisable(false);
			} else {
				tf1.setDisable(true);
				update1.setDisable(true);
			}
		});

		plant.setOnAction(e -> {
			if (plant.isSelected()) {
				tf2.setDisable(false);
			} else {
				tf2.setDisable(true);
				update2.setDisable(true);
			}
		});

		line2.setOnAction(e -> {
			if (line2.isSelected()) {
				tf3.setDisable(false);
			} else {
				tf3.setDisable(true);
				update3.setDisable(true);
			}
		});

		total.setOnAction(e -> {
			if (total.isSelected()) {
				tf4.setDisable(false);
			} else {
				tf4.setDisable(true);
				update4.setDisable(true);
			}
		});

		demand.setOnAction(e -> {
			if (demand.isSelected()) {
				tf5.setDisable(false);
			} else {
				tf5.setDisable(true);
				update5.setDisable(true);
			}
		});

		cuts.setOnAction(e -> {
			if (cuts.isSelected()) {
				tf6.setDisable(false);
			} else {
				tf6.setDisable(true);
				update6.setDisable(true);
			}
		});

		temp.setOnAction(e -> {
			if (temp.isSelected()) {
				tf7.setDisable(false);
			} else {
				tf7.setDisable(true);
				update7.setDisable(true);
			}
		});

		tf1.setOnAction(e -> {
			update1.setDisable(false);
			label1.setText("");
		});

		tf2.setOnAction(e -> {
			update2.setDisable(false);
			label2.setText("");
		});

		tf3.setOnAction(e -> {
			update3.setDisable(false);
			label3.setText("");
		});

		tf4.setOnAction(e -> {
			update4.setDisable(false);
			label4.setText("");
		});

		tf5.setOnAction(e -> {
			update5.setDisable(false);
			label5.setText("");
		});

		tf6.setOnAction(e -> {
			update6.setDisable(false);
			label6.setText("");
		});

		tf7.setOnAction(e -> {
			update7.setDisable(false);
			label7.setText("");
		});

		update1.setOnAction(e -> {
			try {
				label1.setText("Error!");
				label1.setTextFill(Color.RED);
				double value = Double.parseDouble(tf1.getText());
				if (updateIsraeliLines(year, month, day, value)) {
					label1.setText("Updated \nSuccessfully!");
					label1.setTextFill(Color.GREEN);
				}
			} catch (Exception n) {
			}
			update1.setDisable(true);
		});

		update2.setOnAction(e -> {
			try {
				label2.setText("Error!");
				label2.setTextFill(Color.RED);
				double value = Double.parseDouble(tf2.getText());
				if (updatePowerPlant(year, month, day, value)) {
					label2.setTextFill(Color.GREEN);
					label2.setText("Updated \nSuccessfully!");
				}
			} catch (Exception n) {
			}
			update2.setDisable(true);
		});

		update3.setOnAction(e -> {
			try {
				label3.setText("Error!");
				label3.setTextFill(Color.RED);
				double value = Double.parseDouble(tf3.getText());
				if (updateEgyptionLines(year, month, day, value)) {
					label3.setTextFill(Color.GREEN);
					label3.setText("Updated \nSuccessfully!");
				}
			} catch (Exception n) {
			}
			update3.setDisable(true);
		});

		update4.setOnAction(e -> {
			try {
				label4.setText("Error!");
				label4.setTextFill(Color.RED);
				double value = Double.parseDouble(tf4.getText());
				if (updateTotal(year, month, day, value)) {
					label4.setTextFill(Color.GREEN);
					label4.setText("Updated \nSuccessfully!");
				}
			} catch (Exception n) {
			}
			update4.setDisable(true);
		});

		update5.setOnAction(e -> {
			try {
				label5.setText("Error!");
				label5.setTextFill(Color.RED);
				double value = Double.parseDouble(tf5.getText());
				if (updateDemand(year, month, day, value)) {
					label5.setTextFill(Color.GREEN);
					label5.setText("Updated \nSuccessfully!");
				}
			} catch (Exception n) {
			}
			update5.setDisable(true);
		});

		update6.setOnAction(e -> {
			try {
				label6.setText("Error!");
				label6.setTextFill(Color.RED);
				double value = Double.parseDouble(tf6.getText());
				if (updatePowerCut(year, month, day, value)) {
					label6.setTextFill(Color.GREEN);
					label6.setText("Updated \nSuccessfully!");
				}
			} catch (Exception n) {
			}
			update6.setDisable(true);
		});

		update7.setOnAction(e -> {
			try {
				label7.setText("Error!");
				label7.setTextFill(Color.RED);
				double value = Double.parseDouble(tf7.getText());
				if (updateTemp(year, month, day, value)) {
					label7.setTextFill(Color.GREEN);
					label7.setText("Updated \nSuccessfully!");
				}
			} catch (Exception n) {
			}
			update7.setDisable(true);
		});

		return pane;
	}

	public Parent deleteView(int year, int month, int day) {
		Label exist = new Label("There is no available record for " + month + "/" + day + "/" + year);
		exist.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
		exist.setTextFill(Color.FIREBRICK);
		Node<Integer, ElectricityRecord> dayy = delete(year, month, day);
		if (dayy != null) {
			exist.setText("Record for " + month + "/" + day + "/" + year + " has been deleted successfully!");
			exist.setTextFill(Color.GREEN);
		}
		return exist;

	}

	public Parent searchView(int year, int month, int day) {
		Label exist = new Label("There is no available record for " + month + "/" + day + "/" + year);
		exist.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
		exist.setTextFill(Color.FIREBRICK);
		Node<Integer, ElectricityRecord> dayy = search(year, month, day);
		if (dayy != null) {
			VBox dataBox = new VBox(15);

			Label dataLabel = new Label("Electricity record for  " + month + "/" + day + "/" + year);
			dataLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
			dataLabel.setTextFill(Color.BLUEVIOLET);

			Text data = new Text("Israeli Lines: " + dayy.getData().getOccupation_lines() + " MWs \r\n\n"
					+ "Gaza Power Plant: " + dayy.getData().getPower_plant() + " MWs \r\n\n" + "Egyption Lines: "
					+ dayy.getData().getEgyption_lines() + " MWs \r\n\n" + "Total Daily Supply Available: "
					+ dayy.getData().getDaily_supply() + " MWs \r\n\n" + "Overall Demand: " + dayy.getData().getDemand()
					+ " MWs \r\n\n" + "Power Cuts: " + dayy.getData().getPower_cuts_hour_day() + " hours/day \r\n\n"
					+ "Temperature: " + dayy.getData().getTemp());

			data.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 15));
			data.setTextAlignment(TextAlignment.CENTER);

			dataBox.getChildren().addAll(dataLabel, data);
			dataBox.setAlignment(Pos.CENTER);
			return dataBox;
		}
		return exist;
	}

	// allMonths parameter identifies whether it is intended to traverse all years
	// with all months or a specific one, if it is intended for a specific month to
	// be traversed, a value of monthInt has to be specified, otherwise, it will be
	// set to 0. Similarly, allDays parameter identifies if it is intended to
	// traverse all days within each month or not, if not, a value for dayInt has to
	// be specified
	public Statistics traverseAllYears(boolean allMonths, int monthInt, boolean allDays, int dayInt) {

		// if years LinkedList is empty
		if (years.getHead() == null) {
			return null;
		}

		// if years LinkedList is not empty
		Node<Integer, CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>> curr_year = years.getHead();
		CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>> months = curr_year.getData();

		Statistics stat = null;
		Statistics monthsStat = null;
		int time = 0;

		do {
			if (months != null) {
				monthsStat = traverseMonthsOfYear(curr_year, allMonths, monthInt, allDays, dayInt);
				if (monthsStat != null)
					time++;
			}

			if (monthsStat != null) {
				if (time == 1)
					stat = monthsStat;
				else
					updateStat(stat, monthsStat);
			}

			curr_year = curr_year.getNext();
			months = curr_year.getData();
		} while (curr_year != years.getHead());

		if (stat != null) {
			stat.getAvg().setLabel("Average");
			stat.getMax().setLabel("Maximum");
			stat.getMin().setLabel("Minimun");
			stat.getTotal().setLabel("Total");
		}
		return stat;
	}

	public Statistics traverseMonthsOfYear(
			Node<Integer, CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>> year, boolean allMonths,
			int monthInt, boolean allDays, int dayInt) {

		CLinkedList months = year.getData();

		// if months LinkedList is empty
		if (months.getHead() == null)
			return null;

		// if months LinkedList is not empty
		Node<Integer, CLinkedList<Integer, ElectricityRecord>> curr_month = months.getHead();
		CLinkedList<Integer, ElectricityRecord> days = curr_month.getData();

		Statistics stat = null;
		Statistics daysStat = null;
		int time = 0;

		do {
			// if statistics is intended to be calculated for all months, the loop will be
			// iterating over all of them, otherwise, it will iterate only over specific
			// month where its label is equal to the one identified
			if (allMonths == true || (allMonths == false && curr_month.getLabel() == monthInt)) {
				if (days != null) {
					daysStat = traverseDaysOfMonth(curr_month, allDays, dayInt);
					if (daysStat != null)
						time++;
				}

				if (daysStat != null) {

					// if this is the first time that daysStat != null
					if (time == 1)
						stat = daysStat;
					else
						updateStat(stat, daysStat);
				}
			}
			curr_month = curr_month.getNext();
			days = curr_month.getData();
		} while (curr_month != months.getHead());

		if (stat != null) {
			stat.getAvg().setLabel("Average");
			stat.getMax().setLabel("Maximum");
			stat.getMin().setLabel("Minimun");
			stat.getTotal().setLabel("Total");
		}
		return stat;
	}

	public Statistics traverseDaysOfMonth(Node<Integer, CLinkedList<Integer, ElectricityRecord>> month, boolean all,
			int dayInt) {

		CLinkedList<Integer, ElectricityRecord> days = month.getData();

		// if days LinkedList is empty
		if (days.getHead() == null) {
			return null;
		}

		// if days LinkedList is not empty
		Node<Integer, ElectricityRecord> curr_day = days.getHead();
		ElectricityRecord rec = curr_day.getData();

		ElectricityRecord tempTotal = new ElectricityRecord(0, 0, 0, 0, 0, 0, 0);
		ElectricityRecord tempMin = new ElectricityRecord(rec.getOccupation_lines(), rec.getPower_plant(),
				rec.getEgyption_lines(), rec.getDaily_supply(), rec.getDemand(), rec.getPower_cuts_hour_day(),
				rec.getTemp());
		ElectricityRecord tempMax = new ElectricityRecord(rec.getOccupation_lines(), rec.getPower_plant(),
				rec.getEgyption_lines(), rec.getDaily_supply(), rec.getDemand(), rec.getPower_cuts_hour_day(),
				rec.getTemp());

		// initialize count variable
		int count = 0;

		do {
			if (all == true || (all == false && dayInt == curr_day.getLabel())) {

				// evaluate total for all attributes
				add(tempTotal, rec);

				// find minimum
				findMin(tempMin, rec);

				// find maximum
				findMax(tempMax, rec);

				// increment count
				count++;
			}
			// set curr_day node to be the next node
			curr_day = curr_day.getNext();
			rec = curr_day.getData();
		} while (curr_day != days.getHead());
		Statistics stat = new Statistics(count, tempTotal, tempMin, tempMax);
		return stat;
	}

	// this method finds minimum values between two Statistics' objects and sets
	// them for the first object
	private void findMin(ElectricityRecord min, ElectricityRecord rec) {

		if (rec.getOccupation_lines() < min.getOccupation_lines())
			min.setOccupation_lines(rec.getOccupation_lines());

		if (rec.getPower_plant() < min.getPower_plant())
			min.setPower_plant(rec.getPower_plant());

		if (rec.getEgyption_lines() < min.getEgyption_lines())
			min.setEgyption_lines(rec.getEgyption_lines());

		if (rec.getDaily_supply() < min.getDaily_supply())
			min.setDaily_supply(rec.getDaily_supply());

		if (rec.getDemand() < min.getDemand())
			min.setDemand(rec.getDemand());

		if (rec.getPower_cuts_hour_day() < min.getPower_cuts_hour_day())
			min.setPower_cuts_hour_day(rec.getPower_cuts_hour_day());

		if (rec.getTemp() < min.getTemp())
			min.setTemp(rec.getTemp());

	}

	// this method finds maximum values between two Statistics' objects and sets
	// them for the first object
	private void findMax(ElectricityRecord max, ElectricityRecord rec) {

		if (rec.getOccupation_lines() > max.getOccupation_lines())
			max.setOccupation_lines(rec.getOccupation_lines());

		if (rec.getPower_plant() > max.getPower_plant())
			max.setPower_plant(rec.getPower_plant());

		if (rec.getEgyption_lines() > max.getEgyption_lines())
			max.setEgyption_lines(rec.getEgyption_lines());

		if (rec.getDaily_supply() > max.getDaily_supply())
			max.setDaily_supply(rec.getDaily_supply());

		if (rec.getDemand() > max.getDemand())
			max.setDemand(rec.getDemand());

		if (rec.getPower_cuts_hour_day() > max.getPower_cuts_hour_day())
			max.setPower_cuts_hour_day(rec.getPower_cuts_hour_day());

		if (rec.getTemp() > max.getTemp())
			max.setTemp(rec.getTemp());

	}

	// this method finds sum of values between two Statistics' objects and sets them
	// for the first object
	private void add(ElectricityRecord total, ElectricityRecord rec) {
		total.setDaily_supply(total.getDaily_supply() + rec.getDaily_supply());
		total.setDemand(total.getDemand() + rec.getDemand());
		total.setEgyption_lines(total.getEgyption_lines() + rec.getEgyption_lines());
		total.setOccupation_lines(total.getOccupation_lines() + rec.getOccupation_lines());
		total.setPower_cuts_hour_day(total.getPower_cuts_hour_day() + rec.getPower_cuts_hour_day());
		total.setPower_plant(total.getPower_plant() + rec.getPower_plant());
		total.setTemp(total.getTemp() + rec.getTemp());
	}

	// this method finds all preferable values of minimum, maximum and total between
	// two Statistics' objects and sets them for the first object and changes the
	// count value
	private void updateStat(Statistics update, Statistics temp) {
		update.setCount(update.getCount() + temp.getCount());

		findMax(update.getMax(), temp.getMax());

		findMin(update.getMin(), temp.getMin());

		add(update.getTotal(), temp.getTotal());
	}

	public Scene statistics() {
		BorderPane root = new BorderPane();

		Label lblOpt = new Label("What do you want to find ?");
		lblOpt.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));

		RadioButton btnDay = new RadioButton("Statisitics for a given day across all months and years.");
		RadioButton btnMonth = new RadioButton("Statistics for a given month across all days and years.");
		RadioButton btnYear = new RadioButton("Statistics for a given year across all days and months.");
		RadioButton btnAll = new RadioButton("Statisitcs for all data.");

		ToggleGroup group = new ToggleGroup();

		btnDay.setToggleGroup(group);
		btnMonth.setToggleGroup(group);
		btnYear.setToggleGroup(group);
		btnAll.setToggleGroup(group);

		Font btnFont = Font.font("Times New Roman", FontWeight.NORMAL, 12);

		btnDay.setFont(btnFont);
		btnMonth.setFont(btnFont);
		btnYear.setFont(btnFont);
		btnAll.setFont(btnFont);

		GridPane btnPane = new GridPane();
		btnPane.setHgap(15);
		btnPane.setVgap(15);

		btnPane.add(lblOpt, 0, 0);

		btnPane.add(btnDay, 0, 1);
		btnPane.add(btnMonth, 0, 2);
		btnPane.add(btnYear, 0, 3);
		btnPane.add(btnAll, 0, 4);

		TextField tfYear = new TextField();
		tfYear.setPromptText("Type the year");
		tfYear.setDisable(true);

		ComboBox<Integer> days = daysB(root);
		btnPane.add(days, 1, 1);

		ComboBox<String> months = monthsB(root);
		btnPane.add(months, 1, 2);

		btnPane.add(tfYear, 1, 3);

		Label lblScreen = new Label("Statistics Screen");
		lblScreen.setFont(Font.font("Times New Roman", FontWeight.BOLD, 35));
		lblScreen.setTextAlignment(TextAlignment.CENTER);
		lblScreen.setTextFill(Color.CRIMSON);

		// setOnAction
		btnDay.setOnAction(e -> {
			days.setDisable(false);
			months.setDisable(true);
			tfYear.setDisable(true);
		});

		btnMonth.setOnAction(e -> {
			days.setDisable(true);
			months.setDisable(false);
			tfYear.setDisable(true);
		});

		btnYear.setOnAction(e -> {
			days.setDisable(true);
			months.setDisable(true);
			tfYear.setDisable(false);
		});

		btnAll.setOnAction(e -> {
			days.setDisable(true);
			months.setDisable(true);
			tfYear.setDisable(true);
			Statistics stat = traverseAllYears(true, 0, true, 0);
			showTable(stat, root);
		});

		tfYear.setOnAction(e -> {
			try {
				int yearInt = Integer.parseInt(tfYear.getText());
				Node<Integer, CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>> year = years
						.searchSorted(yearInt);
				if (year == null) {
					lblScreen.setText("No records for this year!");
					lblScreen.setFont(Font.font("Times New Roman", 20));
					root.setCenter(lblScreen);
				} else {
					Statistics stat = traverseMonthsOfYear(year, true, 0, true, 0);
					showTable(stat, root);
				}
			} catch (NumberFormatException b) {
				lblScreen.setText("Error! You can only type a number as a year");
				lblScreen.setTextFill(Color.RED);
				lblScreen.setFont(Font.font("Times New Roman", 20));
				root.setCenter(lblScreen);
			} catch (Exception b) {
				lblScreen.setText("Error!");
				lblScreen.setTextFill(Color.RED);
				lblScreen.setFont(Font.font("Times New Roman", 20));
				root.setCenter(lblScreen);
			}
		});

		root.setTop(btnPane);
		root.setCenter(lblScreen);
		root.setBottom(back);

		root.setPadding(new Insets(35));
		root.setStyle("-fx-background-color: white;");

		Scene scene = new Scene(root, 700, 600);
		return scene;
	}

	// this method is only used in statistics() method
	private ComboBox<String> monthsB(BorderPane root) {

		String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		ComboBox<String> monthsBox = new ComboBox<>(FXCollections.observableArrayList(months));
		monthsBox.setOnAction(e -> {

			Statistics stat = null;

			if (monthsBox.getValue() == months[0])
				stat = traverseAllYears(false, 1, true, 0);
			else if (monthsBox.getValue() == months[1])
				stat = traverseAllYears(false, 2, true, 0);
			else if (monthsBox.getValue() == months[2])
				stat = traverseAllYears(false, 3, true, 0);
			else if (monthsBox.getValue() == months[3])
				stat = traverseAllYears(false, 4, true, 0);
			else if (monthsBox.getValue() == months[4])
				stat = traverseAllYears(false, 5, true, 0);
			else if (monthsBox.getValue() == months[5])
				stat = traverseAllYears(false, 6, true, 0);
			else if (monthsBox.getValue() == months[6])
				stat = traverseAllYears(false, 7, true, 0);
			else if (monthsBox.getValue() == months[7])
				stat = traverseAllYears(false, 8, true, 0);
			else if (monthsBox.getValue() == months[8])
				stat = traverseAllYears(false, 9, true, 0);
			else if (monthsBox.getValue() == months[9])
				stat = traverseAllYears(false, 10, true, 0);
			else if (monthsBox.getValue() == months[10])
				stat = traverseAllYears(false, 11, true, 0);
			else if (monthsBox.getValue() == months[11])
				stat = traverseAllYears(false, 12, true, 0);

			showTable(stat, root);
		});

		monthsBox.setDisable(true);
		return monthsBox;
	}

	// this method is only used in statistics() method
	private ComboBox<Integer> daysB(BorderPane root) {

		Integer[] days = new Integer[31];
		for (int i = 0; i < days.length; i++) {
			days[i] = i + 1;
		}

		ComboBox<Integer> daysBox = new ComboBox<>(FXCollections.observableArrayList(days));
		daysBox.setOnAction(e -> {
			int option = daysBox.getValue();
			Statistics stat = traverseAllYears(true, 0, false, option);
			showTable(stat, root);
		});

		daysBox.setDisable(true);
		return daysBox;
	}

	public void showTable(Statistics stat, BorderPane root) {
		TableView table = new TableView();

		ObservableList<ElectricityRecord> list = FXCollections.observableArrayList(stat.getTotal(), stat.getAvg(),
				stat.getMax(), stat.getMin());

		table.setItems(list);

		TableColumn labelClm = new TableColumn("Kind of Statistics");
		labelClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("label"));
//		labelClm.setMaxWidth(80);

		TableColumn occuLinesClm = new TableColumn("Occupation Lines");
		occuLinesClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("occupation_lines"));
//		occuLinesClm.setMaxWidth();

		TableColumn powerPlantclm = new TableColumn("Gaze Power Plant");
		powerPlantclm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("power_plant"));
//		powerPlantclm.setMaxWidth(80);

		TableColumn EgyptionLinesClm = new TableColumn("Egyption Lines");
		EgyptionLinesClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("Egyption_lines"));
//		EgyptionLinesClm.setMaxWidth(80);

		TableColumn supplyClm = new TableColumn("Supply Avilable");
		supplyClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("daily_supply"));
//		supplyClm.setMaxWidth(80);

		TableColumn demandClm = new TableColumn("Demand");
		demandClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("demand"));
//		demandClm.setMaxWidth(80);

		TableColumn powerCutClm = new TableColumn("Power Cuts");
		powerCutClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("power_cuts_hour_day"));
//		powerCutClm.setMaxWidth(80);

		TableColumn tempClm = new TableColumn("Temperature");
		tempClm.setCellValueFactory(new PropertyValueFactory<ElectricityRecord, Double>("temp"));
//		tempClm.setMaxWidth(80);

		table.getColumns().addAll(labelClm, occuLinesClm, powerPlantclm, EgyptionLinesClm, supplyClm, demandClm,
				powerCutClm, tempClm);

		table.setMaxSize(1000, 270);
		root.setCenter(table);

	}

	public Scene save(Stage stage) {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 700, 600);

		Button save = new Button("Save file");
		save.setFont(btnFont);

		// create FileChooser object
		FileChooser saveFile = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		saveFile.getExtensionFilters().addAll(extFilter, extFilter2);

		save.setOnAction(e -> {
			File f = saveFile.showSaveDialog(stage);
			getFile(f);
			System.out.println(f);
			if (f != null) {
				Label label = new Label("Thank you!");
				label.setTextFill(Color.GREEN);
				label.setFont(Font.font("Times New Roman", 50));
				root.setCenter(label);
			}
		});

		root.setCenter(save);

		root.setPadding(new Insets(10));
		root.setStyle("-fx-background-color: white;");
		return scene;
	}

	public void getFile(File file) {
		try {
			PrintWriter writer = new PrintWriter(file);
			writer.println(titles);
			printList(writer);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printList(PrintWriter writer) {
		int count = 0;
		if (years != null && years.getHead() != null) {
			Node<Integer, CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>>> currYear = years.getHead();
			do {
				CLinkedList<Integer, CLinkedList<Integer, ElectricityRecord>> months = currYear.getData();
				if (months != null && months.getHead() != null) {
					Node<Integer, CLinkedList<Integer, ElectricityRecord>> currMonth = months.getHead();
					do {
						CLinkedList<Integer, ElectricityRecord> days = currMonth.getData();
						if (days != null && days.getHead() != null) {
							Node<Integer, ElectricityRecord> currDay = days.getHead();
							do {
								ElectricityRecord rec = currDay.getData();

								// print date and other data
								String date = currMonth.getLabel() + "/" + currDay.getLabel() + "/"
										+ currYear.getLabel();
								String data = date + "," + rec.getOccupation_lines() + "," + rec.getPower_plant() + ","
										+ rec.getEgyption_lines() + "," + rec.getDaily_supply() + "," + rec.getDemand()
										+ "," + rec.getPower_cuts_hour_day() + "," + rec.getTemp();

								writer.println(data);

								currDay = currDay.getNext();
								count++;
							} while (currDay != days.getHead());
						}
						currMonth = currMonth.getNext();
					} while (currMonth != months.getHead());
				}
				currYear = currYear.getNext();
			} while (currYear != years.getHead());
		}
		writer.close();
		System.out.println("count2 = " + count);
	}
}
