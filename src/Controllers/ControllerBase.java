package Controllers;

import Game.ScenesLoader;

public abstract class ControllerBase {
    protected ScenesLoader loader;

    public void setLoader(ScenesLoader loader) {
        this.loader = loader;
        loader.changeResolutionScreen(isFullScreen);
    }

    public static boolean isFullScreen = true;
}
