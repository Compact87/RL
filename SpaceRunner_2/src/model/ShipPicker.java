package model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ShipPicker extends VBox {
    private ImageView circleImage;
    private ImageView shipImage;
    private SHIP ship;
    private String circleNotChosen="grey_circle.png";
    private String circleChosen="yellow_boxTick.png";

    private boolean isCircleChosen=false;
    public ShipPicker(SHIP ship){
        circleImage=new ImageView(circleNotChosen);
        shipImage=new ImageView(ship.getUrlShip());
        this.ship=ship;
        this.setPrefSize(20,50);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getChildren().add(circleImage);
        this.getChildren().add(shipImage);
    }
    public SHIP getShip(){
        return ship;
    }
    public boolean getIsCircleChosen(){
        return isCircleChosen;
    }
    public void setIsCircleChosen(boolean b){
        this.isCircleChosen=b;
        String imageToSet=this.isCircleChosen? circleChosen:circleNotChosen;
        circleImage.setImage(new Image(imageToSet));
    }
}
