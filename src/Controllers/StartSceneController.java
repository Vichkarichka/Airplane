package Controllers;

import Auxiliary.ConnectionFactory;
import Auxiliary.DatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class StartSceneController extends ControllerBase {
    @FXML private Slider difficultySlider;
    @FXML private CheckBox AICheckbox;
    @FXML public TextField profileTextField;
    @FXML public Button profileButton;
    @FXML public Label profileLabel;
    @FXML public TableView profileTable;
    public static String name = "";


    public StartSceneController(){
        difficultySlider = new Slider();
        AICheckbox = new CheckBox();
    }

    @FXML protected void handleExitClick() {
        System.exit(0);
    }

    @FXML protected void handleStartClick(){
        GameController controller = preloadGameScene();
        controller.start((int) difficultySlider.getValue(), AICheckbox.isSelected());
       /* controller.loader.changeResolutionScreen();*/
    }
    @FXML protected void handleSesttingsClick(){
        GameController controller = pGameScene();
       /* controller.loader.changeResolutionScreen();*/
        try {
            Connection connection = null;
            connection = ConnectionFactory.GetConnection();
            String query1 = " select * from Profiles";
            PreparedStatement pst1 = connection.prepareStatement(query1);
            ResultSet rs = pst1.executeQuery();
           // profileTable.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){};
    }
    @FXML protected void handleChangeProfile(){
        Connection connection = null;
        connection = ConnectionFactory.GetConnection();
        name = profileTextField.getText();
        if (name == null){
            profileTextField.setText("Enter your name");
        }
        else {
            profileTextField.setText(null);
            profileTextField.setVisible(false);
            profileButton.setVisible(false);
            profileLabel.setText("Hello, " + name + "!");
        }
    }




    private void writeResults(Map<String, Integer> toOut, String resName, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (Map.Entry<String, Integer> entry : toOut.entrySet()) {
                writer.write(String.format("%s\t%d%n", entry.getKey(), entry.getValue()));
            }
            writer.close();
        }catch (IOException e){
            System.out.println(resName + "map out error\n");
        }
    }
    private String buildStatisticInfoString(Map<String, Integer> statistic) {
        String infoBoxMsg = new String(String.format("%-30s\t%-30s\n", "Event type", "        Call times"));
        for(Map.Entry<String, Integer> entry : statistic.entrySet()){
            infoBoxMsg += String.format("%-30s\t%-30d\n", entry.getKey(), entry.getValue());
        }
        return infoBoxMsg;
    }
    private GameController preloadGameScene() {
        loader.setScenePath("Views/GameScene.fxml");
        loader.loadScene();
        GameController controller = loader.getSceneController();
        controller.setLoader(loader);
        return controller;
    }
    private GameController pGameScene() {
        loader.setScenePath("Views/SettingsScene.fxml");
        loader.loadScene();
        GameController controller = loader.getSceneController();
        controller.setLoader(loader);
        return controller;
    }
}
