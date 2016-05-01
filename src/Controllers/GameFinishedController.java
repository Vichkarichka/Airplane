package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.media.*;

import java.io.File;

public class GameFinishedController extends ControllerBase{
    @FXML private Label gameResultLabel;

    public GameFinishedController(){
        gameResultLabel = new Label();
    }

    public void setGameFinishResult(boolean isWin) {
        if (isWin) {
            gameResultLabel.setText("WIN");
            gameResultLabel.setTextFill(Paint.valueOf("LIME"));
            String path = "src/Resources/J.mp3";
            Media sound = new Media(new File(path).toURI().toString());
            MediaPlayer mp = new MediaPlayer(sound);
            mp.play();
        } else {
            gameResultLabel.setText("LOOSE");
            gameResultLabel.setTextFill(Paint.valueOf("RED"));
            String path = "src/Resources/GTA.mp3";
            Media sound = new Media(new File(path).toURI().toString());
            MediaPlayer mp = new MediaPlayer(sound);
            mp.play();
        }
    }

    public void handleContinueButtonClick(){
        loader.setScenePath("Views/StartScene.fxml");
        loader.loadScene();
        StartSceneController controller = loader.getSceneController();
        controller.setLoader(loader);
    }


}
