package view;



import javafx.application.Platform;
import javafx.beans.property.SetPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.SecureRandomSpi;
import java.util.ArrayList;
import java.util.List;

public class ViewManager {
    private static  final int HEIGHT=720;
    private static final int WIDTH=1000;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;
    enum subs {SHIP_CHOOSER,SCORES,CREDITS,HELP,EXIT};
    private static int SHIP_CHOOSER=0;
    private static int SCORES=1;
    private static int CREDITS=2;
    private static int HELP=3;
    private static int EXIT=4;
    Label name;
    private SpaceRunnerSubscene sceneToHide;
    private final static int MENU_BUTTONS_START_X=100;
    private final static int MENU_BUTTONS_START_Y=100;
    List<SpaceRunnerButton> menuButtons;
    List<SpaceRunnerSubscene> subScenes;
    private final String FONT_PATH ="/kenvector_future_thin.ttf";
    private SpaceRunnerSubscene creditsSubScene;
    private SpaceRunnerSubscene startSubScene;
    private SpaceRunnerSubscene exitSubScene;
    private SpaceRunnerSubscene helpSubScene;
    private SpaceRunnerSubscene scoresSubScene;
    private SpaceRunnerSubscene shipChoserScene;
    List<ShipPicker> shipList;
    private SHIP chosenShip;


    public ViewManager() throws FileNotFoundException {
        mainPane=new AnchorPane();
        mainScene=new Scene(mainPane,WIDTH,HEIGHT);
        mainStage=new Stage();
        mainStage.setScene(mainScene);
        menuButtons=new ArrayList<>();
        subScenes=new ArrayList<>();
        createSubScenes();
        createBackground();
        createButtons();
        addText();
        createSubScenes();
        addMenuListeners();


    }
    public Stage getMainStage(){
        return mainStage;
    }
    private void createButtons() {
        createStartButton();
        createScoresButton();
        createCreditsButton();
        createHelpButton();
        createExitButton();



    }
    private void createBackground(){
        Image backgroundImage=new Image("blue.png",256,256,false,true);
        BackgroundImage background= new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,null);
        mainPane.setBackground(new Background(background));
    }
    private void addMenuButtons(SpaceRunnerButton button){
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutY(MENU_BUTTONS_START_Y + menuButtons.size() * 100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);

    }
    private void createStartButton(){
        SpaceRunnerButton startButton=new SpaceRunnerButton("PLAY");
        addMenuButtons(startButton);
    }
    private void createScoresButton(){
        SpaceRunnerButton scoresButton=new SpaceRunnerButton("SCORES");
        addMenuButtons(scoresButton);
    }
    private void createHelpButton(){
        SpaceRunnerButton helpButton=new SpaceRunnerButton("HELP");
        addMenuButtons(helpButton);
    }
    private void createCreditsButton(){
        SpaceRunnerButton creditsButton=new SpaceRunnerButton("CREDITS");
        addMenuButtons(creditsButton);

    }
    private void createExitButton(){
        SpaceRunnerButton exitButton=new SpaceRunnerButton("EXIT");
        addMenuButtons(exitButton);

    }
    private void addText() throws FileNotFoundException {
        name=new Label("SPACE RUNNER");
        InputStream inputStream = getClass().getResourceAsStream(FONT_PATH);
        name.setFont(Font.loadFont(inputStream,50));
        name.setTextFill(Color.YELLOW);
        mainPane.getChildren().add(name);
        name.setOnMouseEntered(e->{
            DropShadow ds=new DropShadow();
            ds.setColor(Color.GREEN);
            name.setEffect(ds);
        });
        name.setOnMouseExited(e->{
            name.setEffect(null);
        });
        name.setPadding(new Insets(20,40,40,40));
    }
    public void createSubScenes() {

        SpaceRunnerSubscene s;
        for (int i = 0; i < menuButtons.size(); i++) {
            if (i == SHIP_CHOOSER) {
                createShipChoserSubScene();
            } else {
                s = new SpaceRunnerSubscene();
                addSubScenes(s);

            }


        }
    }
    private void createShipChoserSubScene(){
        shipChoserScene=new SpaceRunnerSubscene();


        InfoLabel chooseShip =new InfoLabel("CHOOSE YOUR SHIP");
        chooseShip.setLayoutX(50);
        chooseShip.setLayoutY(25);
        shipChoserScene.getPane().getChildren().add(chooseShip);
        shipChoserScene.getPane().getChildren().add(createShipsToChoose());
        addSubScenes(shipChoserScene);
        shipChoserScene.getPane().getChildren().add(createButtonToStart());

    }

    private void addSubScenes(SpaceRunnerSubscene s){
        subScenes.add(s);
        mainPane.getChildren().add(s);

    }
    private void addMenuListeners(){
        for(SpaceRunnerButton button:menuButtons){
            button.setOnAction(e->
            {
                if(e.getSource().equals(menuButtons.get(EXIT))) Platform.exit();

                showSubscene( subScenes.get(menuButtons.indexOf(button)));
            });

        }

    }
    private void showSubscene(SpaceRunnerSubscene subscene){
        if(sceneToHide !=null){
            sceneToHide.moveSubScene();
        }
        subscene.moveSubScene();
        sceneToHide=subscene;

    }
    private HBox createShipsToChoose(){
        HBox box=new HBox();
        box.setSpacing(20);
        shipList=new ArrayList<>();
        for(SHIP ship:SHIP.values()){
            ShipPicker shipToPick= new ShipPicker(ship);
            box.getChildren().add(shipToPick);
            shipList.add(shipToPick);
            shipToPick.setOnMouseClicked(e->{
                for(ShipPicker ship1: shipList){
                    ship1.setIsCircleChosen(false);
                }
                shipToPick.setIsCircleChosen(true);
                chosenShip=shipToPick.getShip();
            });

        }
        box.setLayoutX(300-(118*2));
        box.setLayoutY(100);

        return box;

    }
    private SpaceRunnerButton createButtonToStart(){
        SpaceRunnerButton startButton=new SpaceRunnerButton("START");
        startButton.setLayoutX(350);
        startButton.setLayoutY(300);

        startButton.setOnAction(e->{
            if(chosenShip!=null) {
                GameViewManager gameViewManager = new GameViewManager();
                gameViewManager.createNewGame(mainStage,chosenShip);
            }

        });
        return startButton;
    }
}
























