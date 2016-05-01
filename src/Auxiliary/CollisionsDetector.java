package Auxiliary;

import Models.Vector2;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Perform collisions detection.
 */
public class CollisionsDetector {
    private Timeline collisionDetection;
    public int helf_point=100;

    private Group mobs;
    private Group bullets;
    private Pane ship;

    private Runnable onMobBulletCollision;
    private Runnable onMobShipCollision;

    /**
     * @param mobs mobs Group.
     * @param bullets bullets Group.
     * @param shipSprite ship Pane.
     */
    public CollisionsDetector(Group mobs, Group bullets, Pane shipSprite){
        collisionDetection = new Timeline();
        this.mobs = mobs;
        this.bullets = bullets;
        this.ship = shipSprite;
    }

    /**
     * Initialize and start detection Timeline.
     */
    public void startDetection() {
        collisionDetection.getKeyFrames().addAll(
                new KeyFrame(new Duration(100), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        ArrayList<Node> toDelBullets = new ArrayList<Node>();
                        ArrayList<Node> toDelMobs = new ArrayList<Node>();
                        detectCollisions(toDelBullets, toDelMobs);
                        deleteCollided(toDelBullets, toDelMobs);
                    }
                }));
        collisionDetection.setCycleCount(Timeline.INDEFINITE);
        collisionDetection.play();
    }

    private void deleteCollided(ArrayList<Node> toDelBullets, ArrayList<Node> toDelMobs) {
        for(Node mob : toDelMobs){
            mobs.getChildren().remove(mob);
        }

        for(Node bullet : toDelBullets){
            bullets.getChildren().remove(bullet);
        }
    }
    private boolean flag=false;
    private  int damage=0;

    private void detectCollisions(ArrayList<Node> toDelBullets, ArrayList<Node> toDelMobs) {
        for (Node mob : mobs.getChildren()) {
            detectMobsBulletsCollision(toDelBullets, toDelMobs, mob);
            detectMobsShipCollisions(mob);
        }
        if(flag==true)
        {

            helf_point-=damage;
            flag=false;
        }

    }

    private void detectMobsShipCollisions(Node mob) {
        if(mob.getBoundsInParent().intersects(ship.getBoundsInParent())){

            damage=(int)(Math.random()*11+10);
            System.out.println(damage);
            if(helf_point<=damage)
            {
                System.out.println(String.format("Ship killed"));
                onMobShipCollision.run();
                //  toDelMobs.add(mob);


            }
            mobs.getChildren().remove(mob);
            flag=true;

        }
    }

    private void detectMobsBulletsCollision(ArrayList<Node> toDelBullets,
                                            ArrayList<Node> toDelMobs,
                                            Node mob) {
        Vector2 mobPos = new Vector2((mob.getBoundsInLocal().getMaxX() + mob.getBoundsInLocal().getMinX())/2,
                (mob.getBoundsInLocal().getMaxY() + mob.getBoundsInLocal().getMinY())/2);
        for(Node bullet : bullets.getChildren()){
            if(mob.getBoundsInParent().intersects(bullet.getBoundsInParent())){
                onMobBulletCollision.run();

                System.out.println(String.format("Mob %s killed at (%f, %f)",
                        mob.getId(), mobPos.getX(), mobPos.getY()));

                toDelBullets.add(bullet);
                toDelMobs.add(mob);
                return;
            }
        }
    }

    /**
     * Stop detection Timeline.
     */
    public void stopDetection(){
        collisionDetection.stop();
    }

    /**
     * Set handler on event when mob and bullet collides.
     * @param toRun Runnable object.
     */
    public void setOnMobBulletCollision(Runnable toRun){
        this.onMobBulletCollision = toRun;
    }

    /**
     * Set handler on event when mob and ship collides.
     * @param toRun Runnable object.
     */
    public void setOnMobShipCollision(Runnable toRun){
        this.onMobShipCollision = toRun;
    }


}
