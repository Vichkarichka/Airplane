package Game;

import Controllers.StartSceneController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.File;

//import java.net.URL;


public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        /*
         *Sound
         */
        /*String path = "Media\\main_theme.gs";
        path = "D:\\main_theme.gs";
        //path = "D:\\";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);*/


        ScenesLoader loader = new ScenesLoader(this, primaryStage);
        loader.setScenePath("Views/StartScene.fxml");
        loader.loadScene();
        StartSceneController controller = loader.getSceneController();
        controller.setLoader(loader);

        primaryStage.setTitle("Cap & 4 kamikadze");

        /*
         * Add new application icon.
         */
        //Image applicationIcon = new Image(getClass().getResourceAsStream("icon.png"));
        //primaryStage.getIcons().add(applicationIcon);
        Image applicationIcon = new Image(getClass().getResourceAsStream("/Media/st6b.png"));
        primaryStage.getIcons().add(applicationIcon);

        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
