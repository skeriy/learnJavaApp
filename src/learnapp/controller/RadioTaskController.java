package learnapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fxrouter.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import learnapp.pojo.Practice;
import learnapp.service.DataService;
import learnapp.service.RouteService;
import learnapp.service.UtilService;

import java.io.IOException;


public class RadioTaskController {
    @FXML
    private AnchorPane practiceAnchorPane;

    @FXML
    private Button backToMenuBtn;

    @FXML
    private VBox radioBtnBox;

    @FXML
    private Text radioExText;

    @FXML
    private RadioButton rad1;

    @FXML
    private RadioButton rad2;

    @FXML
    private RadioButton rad3;

    @FXML
    private RadioButton rad4;


    @FXML
    public void onBackToMenu() throws IOException {
        FXRouter.goTo("Themes");
    }

    @FXML
    public void onCheck() {
        boolean result = radioAnswer.equals(succesAnswer);
        try {
            if (result) {
                System.out.println("OK");
                if (RouteService.getTheory().equals(RouteService.getMaxTheory())) {
                    System.out.println("SUBTHEME " + RouteService.getSubTheme() + " DONE! GO ALL SUBTHEMES!");
                    FXRouter.goTo("SubThemes");
                } else {
                    RouteService.incPractice();
                    RouteService.incTheory();
                    FXRouter.goTo("theory");
                }
            } else {
                System.out.println("NO");
                FXRouter.goTo("theory");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("In check radioAnswer: " + radioAnswer);
    }

    private String radioAnswer;
    private String succesAnswer;

    public void initialize() {
        initializeRadioButtons();
        UtilService.showNavigatePanel(practiceAnchorPane);
    }

    private void initializeRadioButtons() {
        JsonNode rootNode = DataService.getDataRootNode();
        JsonNode subThemeNode = rootNode.path("theme").path(RouteService.getTheme().toString()).path("sub_theme").path(RouteService.getSubTheme().toString());
        JsonNode practice = subThemeNode.path("practice").get(RouteService.getPractice().toString());

        Practice practiceObject = UtilService.convertJsonNodeToPractice(practice);

        succesAnswer = practiceObject.getSucces().get(0);

        ToggleGroup toggleGroup= new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                RadioButton button = (RadioButton) toggleGroup.getSelectedToggle();
                radioAnswer = button.getText();
                System.out.println("Button: " + radioAnswer);
            }
        });

        rad1.setToggleGroup(toggleGroup);
        rad2.setToggleGroup(toggleGroup);
        rad3.setToggleGroup(toggleGroup);
        rad4.setToggleGroup(toggleGroup);

        rad1.setText(practiceObject.getQuestions().get(0));
        rad2.setText(practiceObject.getQuestions().get(1));
        rad3.setText(practiceObject.getQuestions().get(2));
        rad4.setText(practiceObject.getQuestions().get(3));

        radioExText.setText(practiceObject.getText());

        System.out.println();

    }

}