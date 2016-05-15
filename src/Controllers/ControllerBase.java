package Controllers;

import Game.ScenesLoader;
import javafx.scene.control.CheckBox;
import javafx.event.ActionEvent;

public abstract class ControllerBase {
    protected ScenesLoader loader;
 protected static boolean flag_sound=true;
    public void setLoader(ScenesLoader loader) {
        this.loader = loader;
        loader.changeResolutionScreen(isFullScreen);
    }

    /*
    * Add CheckBoxScreen edit
    * */
    public void editBoxSound(ActionEvent event) {

        if(flag_sound==true)
        {
            flag_sound=false;
        }
        else
        {
            flag_sound=true;
        }

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
