package learnapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fxrouter.FXRouter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import learnapp.pojo.Practice;
import learnapp.service.DataService;
import learnapp.service.ProgressService;
import learnapp.service.RouteService;
import learnapp.service.UtilService;

import java.io.IOException;
import java.util.ArrayList;

public class InputTextController {
    @FXML
    private AnchorPane practiceAnchorPane;

    @FXML
    private HBox navigateHBox;

    @FXML
    private VBox codeVBox;

    @FXML
    private Button backToMenuBtn;

    @FXML
    private Button checkBtn;

    @FXML
    private TextFlow inputTextExText;

    @FXML
    public void onBackToMenu() throws IOException {
        FXRouter.goTo("SubThemes");
    }

    @FXML
    public void onCheck() {
        checkInputs();
    }

    private ObservableList<TextField> allTextFields = FXCollections.observableArrayList();
    private Practice practiceObject;

    public void initialize() {
        JsonNode rootNode = DataService.getDataRootNode();
        JsonNode subThemeNode = rootNode.path("theme").path(RouteService.getTheme().toString()).path("sub_theme").path(RouteService.getSubTheme().toString());
        JsonNode practice = subThemeNode.path("practice").get(RouteService.getPractice().toString());

        practiceObject = UtilService.convertJsonNodeToPractice(practice);

        int rowCount = practiceObject.getRowsMap().size();

        for (int i = 1; i <= rowCount; i++) {
            HBox row = new HBox();
            if (practiceObject.getPasteElements().contains(i)) {
                TextField textField = new TextField();
                textField.setPrefWidth(50);
                allTextFields.add(textField);
                ArrayList<String> subStrings = practiceObject.getRowsMap().get(i);
                row.getChildren().addAll(new Text(subStrings.get(0)), textField, new Text(subStrings.get(1)));
            } else {
                row.getChildren().add(new Text(practiceObject.getRowsMap().get(i).get(0)));
            }
            row.setAlignment(Pos.CENTER_LEFT);
            codeVBox.getChildren().add(row);
        }

        inputTextExText.getChildren().add(new Text(practiceObject.getText()));

        UtilService.showNavigatePanel(navigateHBox);
    }

    private void checkInputs() {
        boolean result = true;
        for (int i = 0; i < allTextFields.size(); i++) {
            if (!allTextFields.get(i).getText().equals(practiceObject.getSucces().get(i))) {
                result = false;
            }
        }

        ProgressService.updateProgress(result);
    }
}
