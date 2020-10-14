package model;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class SpaceRunnerSubscene extends SubScene {
  //  private final static String FONT_PATH="/kenvector_future_thin.ttf";
    private final static String BACKGROUND_IMAGE="/yellow_panel.png";
    private  boolean isHidden=true;
    public SpaceRunnerSubscene() {
        super(new AnchorPane(),600,400);
        prefWidth(600);
        prefHeight(400);

        BackgroundImage image= new BackgroundImage(new Image(BACKGROUND_IMAGE,600,400,false,true),
                BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);
        AnchorPane root2= (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        setLayoutX(1000);
        setLayoutY(150);
    }
    public void moveSubScene(){

        TranslateTransition transition=new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this);
        if(isHidden){
            transition.setToX(-600);
            isHidden=false;
        }else{
            transition.setToX(+600);
            isHidden=true;
        }
        transition.play();
    }
    public AnchorPane getPane(){
        return (AnchorPane) this.getRoot();

    }
}
