package Controllers;

import Auxiliary.*;
import Models.Vector2;
import Models.Ship;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.File;
import java.util.*;

import static java.lang.Math.random;

/**
 * Contains game logic.
 */
public class GameController extends ControllerBase {
    @FXML private AnchorPane gameField;
    @FXML private Label pointsLabel;
    @FXML private Pane shipSprite;
    @FXML private Label timeLabel;
    @FXML private Label HP_ship;


    private Ship ship;

    private Group mobs;
    private Group bullets;

    private CollisionsDetector collisionDetector;
    private Timeline mobsGenerator;
    private Timeline winTimer;

    private Thread AIThread;

    private int scores;
    private int difficulty;
    private int winTime;
    private int mobLastId = 0;
    private boolean isReplay;

    public GameController(){
        shipSprite = new Pane();
        ship = new Ship();
        mobs = new Group();
        gameField = new AnchorPane();
        bullets = new Group();
        mobsGenerator = new Timeline();
        pointsLabel = new Label();
        timeLabel = new Label();
        winTimer = new Timeline();
        winTime = 60;
        isReplay = false;
        AIThread = new Thread();
        HP_ship=new Label();

    }

    @FXML private void initialize(){
        gameField.setOnKeyPressed(new KeyboardHandler());

                /*
         *Sound
         */
        /*String path = "Media\\main_theme.gs";
        path = "D:\\Вучоба\\3 kypc (II)\\ТРиТПО\\Airplane\\src\\Media\\main_theme2.gs";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);*/

        ship.setPosition(shipSprite.getLayoutX() + shipSprite.getPrefWidth() / 2,
                shipSprite.getLayoutY() + shipSprite.getPrefHeight()/2);

        gameField.getChildren().add(bullets);
        gameField.getChildren().add(mobs);

        collisionDetector = new CollisionsDetector(mobs, bullets, shipSprite);
        collisionDetector.setOnMobBulletCollision(() -> pointsLabel.setText(String.format("Points: %d", ++scores)));
        collisionDetector.setOnMobShipCollision(() -> finishGame(false));
    }

    /**
     * Start new game.
     * @param difficulty difficulty rate in range 1200 to -INFINITY
     * @param AIMode AI enable state. True - enabled, False - disabled.
     */
    public void start(int difficulty, boolean AIMode){
        this.difficulty = difficulty;
        if(AIMode) initAIMode();
        initMobGenerator();
        collisionDetector.startDetection();
        initWinTimer();
    }

    private void initAIMode(){
        AIThread = new MagicAI(mobs, gameField);
        AIThread.setDaemon(true);
        AIThread.start();
    }

    private void initWinTimer(){
        winTimer.getKeyFrames().add(
                new KeyFrame(new Duration(1000), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        timeLabel.setText(String.format("Time: %02d:%02d",
                                (int) Math.floor(winTime/60), (int) Math.floor(winTime%60)));
                        winTime -= 1;
                        if(winTime == 0){
                            finishGame(true);
                        }
                    }
                })
        );
        winTimer.setCycleCount(Timeline.INDEFINITE);
        winTimer.play();
    }

    private void finishGame(boolean isWin){
        //loader.changeResolutionScreen();
        stopGame(isWin);
        loadFinishScene(isWin);
    }

    private void stopGame(boolean isWin) {
        stopTimelines();
        System.out.println("Game finished");
    }

    private void loadFinishScene(boolean isWin){
        loader.setScenePath("Views/GameFinishedScene.fxml");
        loader.loadScene();
        GameFinishedController controller = loader.getSceneController();
        controller.setLoader(loader);
        controller.setGameFinishResult(isWin);
    }



    private void initMobGenerator() {
        mobsGenerator.getKeyFrames().add(
                new KeyFrame(new Duration(1200 - difficulty), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.print("Mob generator: ");
                        Vector2 mobPos = generateRandomMobPosition();
                        createMob(mobPos);
                    }
                })
        );
        mobsGenerator.setCycleCount(Timeline.INDEFINITE);
        mobsGenerator.play();
    }

    private void createMob(Vector2 mobStartPos) {
        Circle circle = new Circle(mobStartPos.getX(), mobStartPos.getY(), 15, Color.web("white", .70));
        circle.setId(Integer.toString(mobLastId++));
        initMobMovePath(circle, mobStartPos);
        mobs.getChildren().add(circle);
        System.out.println(String.format("Mob %s created at pos: (%f, %f)",
                circle.getId(), mobStartPos.getX(), mobStartPos.getY()));
    }



    private Vector2 generateRandomMobPosition() {
        Vector2 mobStartPos = new Vector2();
        int side = (int) Math.floor(random() * 4);

        Vector2 windowSize = new Vector2(gameField.getPrefWidth(), gameField.getPrefHeight());

        switch(side) {
            case 0: //top
                mobStartPos.setX(random() * windowSize.getX());
                mobStartPos.setY(20);
                break;
            case 1: // right
                mobStartPos.setX(windowSize.getX());
                mobStartPos.setY(random() * windowSize.getY());
                break;
            case 2: //bottom
                mobStartPos.setX(random() * windowSize.getX());
                mobStartPos.setY(windowSize.getY());
                break;
            case 3: //left
                mobStartPos.setX(20);
                mobStartPos.setY(random() * windowSize.getY());
                break;
        }
        return mobStartPos;
    }

    @FXML protected void handleMenuButtonClick(){
        stopGame(false);
        returnToMainMenu();
    }
    @FXML protected void handleBackButtonClick(){
        returnToMainMenu();
    }

    private void returnToMainMenu() {
        loader.setScenePath("Views/StartScene.fxml");
        loader.loadScene();
        StartSceneController controller = loader.getSceneController();
        controller.setLoader(loader);
    }

    private void stopTimelines() {
        collisionDetector.stopDetection();
        mobsGenerator.stop();
        winTimer.stop();
        AIThread.interrupt();
        try {
            AIThread.join();
        } catch (InterruptedException ie){
            System.out.println("AIThread exception: " + ie.getMessage());
        }
    }

    @FXML protected void handleGameFieldMouseClicked(MouseEvent mouseEvent){
        Vector2 mouseCoords = new Vector2(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        shipFires(mouseCoords);
    }

    @FXML protected void handleGameFieldMouseMoved(MouseEvent mouseEvent){
        Vector2 mouseCoords = new Vector2(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        shipMoves(mouseCoords);
    }

    private void shipMoves(Vector2 mouseCoords) {
        double deltaX = Math.abs(mouseCoords.getX() - ship.getPosition().getX());
        double deltaY = Math.abs(mouseCoords.getY() - ship.getPosition().getY());
        double angle = Math.toDegrees(Math.atan(deltaX / deltaY));
        if (mouseCoords.getX() < ship.getPosition().getX()) {
            angle = -angle;
        }
        if(mouseCoords.getY() > ship.getPosition().getY()){
            angle = 180 - angle;
        }
        shipSprite.setRotate(angle);
    }

    @FXML protected void handleKeyPressed(KeyEvent keyEvent) {
        keyPressed(keyEvent.getCode());
    }

    private void keyPressed(KeyCode keyCode) {
        final int movStep = 10;
        if(keyCode == KeyCode.W){
            ship.shiftPosition(0, -movStep);
            shipMoves();
        }
        if(keyCode == KeyCode.A){
            ship.shiftPosition(-movStep, 0);
            shipMoves();
        }
        if(keyCode == KeyCode.S){
            ship.shiftPosition(0, movStep);
            shipMoves();
        }
        if(keyCode == KeyCode.D){
            ship.shiftPosition(movStep, 0);
            shipMoves();
        }
        if(keyCode == KeyCode.ESCAPE){
            handleMenuButtonClick();
        }
    }

    private void shipMoves() {
        HP_ship .setText(String.format("HP: %d", collisionDetector.helf_point));

        shipSprite.setLayoutX(ship.getPosition().getX() - shipSprite.getPrefWidth()/2);
        shipSprite.setLayoutY(ship.getPosition().getY() - shipSprite.getPrefHeight()/2);
    }

    private void shipFires(Vector2 direction) {
        Circle circle = new Circle(ship.getPosition().getX(), ship.getPosition().getY(), 5, Color.web("red", .7));
        initBulletMovePath(ship.getPosition(), direction, circle);
        bullets.getChildren().add(circle);
    }

    private void initMobMovePath(final Circle circle, Vector2 mobStartPos) {
        System.out.println(String.format("New mob's %s (%f, %f) path",
                circle.getId(), mobStartPos.getX(), mobStartPos.getY()));
        initMovPath(mobStartPos, ship.getPosition(), circle, 50, mobs);
    }

    private void initBulletMovePath(Vector2 startPosition, Vector2 endPosition, final Circle circle) {
        initMovPath(startPosition, endPosition, circle, 200, bullets);
    }

    private void initMovPath(Vector2 startPosition, Vector2 endPosition, final Node movedNode,
                             int speed, Group deleteFromGroup) {
        Timeline timeline = new Timeline();
        Vector2 direction = Vector2.subtract(endPosition, startPosition);
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(Vector2.distance(startPosition, endPosition) / speed),
                        event -> deleteFromGroup.getChildren().remove(movedNode),
                        new KeyValue(movedNode.translateXProperty(), direction.getX()),
                        new KeyValue(movedNode.translateYProperty(), direction.getY())
                )
        );
        timeline.play();
    }

    private class KeyboardHandler implements EventHandler<KeyEvent>{
        @Override
        public void handle(KeyEvent event) {
            keyPressed(event.getCode());
        }
    }
}