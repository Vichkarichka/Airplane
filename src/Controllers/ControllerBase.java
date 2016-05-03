package Controllers;

import Game.ScenesLoader;
import javafx.scene.control.CheckBox;
import javafx.event.ActionEvent;

public abstract class ControllerBase {
    protected ScenesLoader loader;

    public void setLoader(ScenesLoader loader) {
        this.loader = loader;
        loader.changeResolutionScreen(isFullScreen);
    }

    /*
    * Add CheckBoxScreen edit
    * */
    public void editBoxSound(ActionEvent event) {
        System.out.println("CheckBoxSound!");
    }
    /*
    * Add CheckBoxScreen edit
    * */
    public void editBoxScreen(ActionEvent event) {
        System.out.println("CheckBoxScreen!");
    }

    public static boolean isFullScreen = true;
}
