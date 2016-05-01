package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class StartSceneController extends ControllerBase {
    @FXML private Slider difficultySlider;
    @FXML private CheckBox AICheckbox;

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
