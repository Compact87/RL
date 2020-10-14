package model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

import java.io.InputStream;

public class InfoLabel  extends Label{
    public final static String FONT_PATH="/kenvector_future_thin.ttf ";
    public final static String BACKGROUND_IMAGE="/yellow_button_pressed.png";
    public InfoLabel(String text){
        setPrefWidth(380);
        setPrefHeight(49);

        setText(text);
        setWrapText(true);
        setLabelFont();
        setAlignment(Pos.CENTER);
        BackgroundImage backgroundImage=new BackgroundImage(new Image(BACKGROUND_IMAGE,380,49,false,true),
                BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);
        setBackground(new Background(backgroundImage));
    }

    private void setLabelFont()  {
        try{
            InputStream inputStream = getClass().getResourceAsStream(FONT_PATH);
        setFont(Font.loadFont(inputStream,23));
        }catch (Exception f){
            setFont(Font.font("Verdana",23));
        }
    }
}
