import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(true);
        showMainMenu(primaryStage);
    }

    private static void setGridConstraints(GridPane pane) {

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(20);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(40);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(20);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(25);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(25);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(25);
        pane.getColumnConstraints().addAll(column1, column2, column3); // each get 50% of width
        pane.getRowConstraints().addAll(row1, row2, row3);
        pane.setPadding(new Insets(10, 20, 10, 20));
        pane.setVgap(20);
        pane.setHgap(50);
        pane.setAlignment(Pos.CENTER);
     //   pane.setGridLinesVisible(true);
    }

    private static void showScansion(Stage primaryStage, String line) {
        GridPane pane = new GridPane();
        Scene scansionMenu = new Scene(pane, 1000, 750);
        setGridConstraints(pane);

        Text welcomeText = new Text("The lines scan as follows:");
        welcomeText.setStyle("-fx-font: 24 times");
        pane.add(welcomeText, 1, 0, 1, 1);
        GridPane.setHalignment(welcomeText, HPos.CENTER);

        String scansion = "";
        for(String oneLine : line.split("\n")) {
            try {
                scansion += Driver.scanLine(oneLine) + "\n";
            }
            catch (Exception wordNotFound) {
                System.out.print(wordNotFound.getStackTrace() + " " + wordNotFound.toString());
            }


        }

        TextArea scansionText = new TextArea(scansion);
        pane.add(scansionText, 0, 1, 3, 2);
        scansionText.setStyle("-fx-font: 26 times");
        scansionText.setEditable(false);
        GridPane.setHalignment(scansionText, HPos.CENTER);

        Button backButton = new Button("Back");
        backButton.setMaxSize(200, 50);
        backButton.setAlignment(Pos.CENTER);

        backButton.setOnMouseClicked(event -> {
            showMainMenu(primaryStage);
        });
        GridPane.setHalignment(backButton, HPos.CENTER);
        pane.add(backButton, 1, 4, 1, 6);


        primaryStage.setScene(scansionMenu);
    }

    private static void showMainMenu(Stage primaryStage) {
        GridPane pane = new GridPane();
        Scene mainMenu = new Scene(pane, 1000, 750);

        setGridConstraints(pane);
   //    pane.setPrefSize(300, 300);
        // never size the gridpane larger than its preferred size:
        pane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        Text welcomeText = new Text("Dactylic Hexameter Poetry Scanner (Latin)");
        welcomeText.setScaleX(1.5);
        welcomeText.setScaleY(1.5);
        GridPane.setHalignment(welcomeText, HPos.CENTER);
        pane.add(welcomeText, 1, 0, 1, 1);

  //      pane.setGridLinesVisible(true);

//
//        Button helpButton = new Button ("Help");
//
//        Label total = new Label("Income:");
//        pane.add(total, 0, 1);
        TextArea totalField = new TextArea("Type lines here");
        totalField.setStyle("-fx-font: 20 times");
        totalField.setMaxHeight(400);
        pane.add(totalField, 0, 1, 3, 1);

//        Label percent = new Label("% Tax:");
//        pane.add(percent,0,2);
//        TextField percentField = new TextField();
//        pane.add(percentField, 1, 2);
//

        Button startButton = new Button("Scan");
        startButton.setMaxSize(200, 50);
        GridPane.setHalignment(startButton, HPos.CENTER);
        pane.add(startButton, 1, 2);
        startButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
              showScansion(primaryStage, cleanText(totalField.getText()));
        });

        primaryStage.setScene(mainMenu);
        primaryStage.show();
    }

    private static String cleanText(String text) {
        String newText = "";
        String[] lines = text.split("\n");
        for(int j = 0; j < lines.length; j++) {
            String line = lines[j];
            String newLine = "";
            int indexOfLastSymbol = -1;
            for(int i = 0 ; i < line.length(); i++) {
                if(!Driver.contains(Driver.letters, line.substring(i, i+1)) && !line.substring(i, i+1).equals(" ")) {
                    newLine += line.substring(indexOfLastSymbol + 1, i);
                    indexOfLastSymbol = i;
                }
                else if(i == line.length() - 1) newLine += line.substring(indexOfLastSymbol + 1);
            }
            if(j != lines.length - 1) {
                newText += newLine + "\n";
            }
            else {
                newText += newLine;
            }
        }
        return newText;
    }

}
