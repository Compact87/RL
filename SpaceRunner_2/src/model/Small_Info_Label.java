package model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Small_Info_Label extends Label {

    private final static String FONT_PATH="/kenvector_future_thin.ttf";
    public Small_Info_Label(String text){
        setPrefWidth(130);
        setPrefHeight(50);
        BackgroundImage backgroundImage=new BackgroundImage(
                new Image("infoLabel.png",130,50,false,true)
                , BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);
        setBackground(new Background(backgroundImage));
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10,10,10,10));
        setLabelFont();
        setText(text);

    }
    private void setLabelFont(){
        try{
        InputStream inputStream = getClass().getResourceAsStream(FONT_PATH);
        setFont(Font.loadFont(inputStream,15));
        }catch (Exception e){setFont(Font.font("Verdana",15));}
    }
}
