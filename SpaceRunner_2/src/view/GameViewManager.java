package view;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.Base64;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

import model.JavaClient;
import model.JavaServer;
import model.SHIP;
import model.Small_Info_Label;

import javafx.scene.image.Image;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

public class GameViewManager {
    private int port;
    private OutputStream outputStream;
    private DataOutputStream dos;
    private Socket socket;


    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private int angle;
    private AnimationTimer gameTimer;

    private static final int GAME_WIDTH=600;
    private static final int GAME_HEIGHT=800;

    private Stage menuStage;
    private ImageView ship;

    private GridPane gridPane1;
    private GridPane gridPane2;
    private String BACKGROUND_IMAGE="blue.png";

    private final static String METEOR_BROWN_IMAGE="/meteorBrown_small1.png";
    private final static String METEOR_GREY_IMAGE="/meteorGrey_small2.png";

    private ImageView[] brownMeteors;
    private ImageView[] greyMeteors;
    Random randomPositionGenerator;

    private ImageView star;
    private Small_Info_Label pointsLabel;
    private ImageView[] playerLifes;
    private int playerLife;
    private int points;
    private final static String GOLD_STAR_IMAGE="star_gold.png";

    private final static int STAR_RADIUS=22;
    private final static int SHIP_RADIUS=27;
    private final static int METEOR_RADIUS=20;

    private JavaServer js;
    private JavaClient jc;

    public static volatile WritableImage writableImage;
    public static Image placeholderImage;

    private javafx.embed.swing.SwingFXUtils SwingFXUtils;
    public int i=0;
    public static File file;
    public static volatile boolean imageReady;
    public GameViewManager(){
        initializeStage();
        initializeListeners();
        randomPositionGenerator=new Random();
        imageReady=true;
    }

    private void initializeListeners() {
        gameScene.setOnKeyPressed(e->{
            if(e.getCode()== KeyCode.LEFT){
                isLeftKeyPressed=true;
            }else if(e.getCode()==KeyCode.RIGHT){
                isRightKeyPressed=true;
            }
        });
        gameScene.setOnKeyReleased(e->{
            if(e.getCode()== KeyCode.LEFT){
                isLeftKeyPressed=false;
            }else if(e.getCode()==KeyCode.RIGHT){
                isRightKeyPressed=false;
            }
        });
    }

    private void initializeStage() {
        gamePane=new AnchorPane();
        gameScene=new Scene(gamePane,GAME_WIDTH,GAME_HEIGHT);
        gameStage=new Stage();
        gameStage.setScene(gameScene);
    }
    public void createNewGame(Stage menuStage, SHIP chosenShip) {

        this.menuStage=menuStage;
        this.menuStage.hide();
        createBackground();
        createGameElements(chosenShip);
        CreateShip(chosenShip);
        imageReady=false;
        createGameLoop();
        startComm();
        gameStage.show();

    }
    public void CreateShip(SHIP chosenShip){
        ship=new ImageView(chosenShip.getUrlShip());
        ship.setLayoutX(GAME_WIDTH-200);
        ship.setLayoutY(GAME_HEIGHT-90);
        gamePane.getChildren().add(ship);

    }
    public void createGameLoop(){

        gameTimer=new AnimationTimer() {
            int i=0;
            @Override
            public void handle(long l) {
                if(JavaServer.dir!='s') {

                    if(JavaServer.dir=='t'){
                        resetGame();
                        JavaServer.dir='s';
                        print("reset");

                    }else {
                    moveBackground();
                    moveShip();
                    moveGameElements();
                    checkIfElementsAreBehindTheShipAndRelocate();
                    checkIfElementsCollide();
                    checkElementsDistance();
                    takeSnapShot(gameScene, i);
                    i++;
                    JavaServer.dir='s';}
                }

            }
        };
        gameTimer.start();
    }

    private void checkElementsDistance() {
        double val;
        JavaServer.state=new ArrayList<>();
        for(int i=0;i<brownMeteors.length;i++){
           val =calculateDistance(ship.getLayoutX()+49, brownMeteors[i].getLayoutX()+20, ship.getLayoutY()+37,brownMeteors[i].getLayoutY()+20);
            JavaServer.state.add(val);
            }
        for(int i=0;i<greyMeteors.length;i++){
            val =calculateDistance(ship.getLayoutX()+49, greyMeteors[i].getLayoutX()+20, ship.getLayoutY()+37,greyMeteors[i].getLayoutY()+20);
            JavaServer.state.add(val);
        }
        val=calculateDistance(ship.getLayoutX()+49, star.getLayoutX()+15, ship.getLayoutY()+37,star.getLayoutY()+15);
        JavaServer.state.add(val);

    }



    private void moveShip(){
        i++;
        if(JavaServer.input && JavaServer.dir=='r'){/*JavaServer.input && JavaServer.dir=='r'*/
            if(angle <30){
                angle +=5;
            }
            ship.setRotate(angle);
            if(ship.getLayoutX()<522){
                ship.setLayoutX(ship.getLayoutX()+3);
                JavaServer.toClient=ship.getLayoutX();
            }
            JavaServer.input=false;

        }
        if(JavaServer.input && JavaServer.dir=='l'){//JavaServer.input && JavaServer.dir=='l'
            if(angle >-30){
                angle -=5;
            }
            ship.setRotate(angle);
            if(ship.getLayoutX()>-20){
                ship.setLayoutX(ship.getLayoutX()-3);
                JavaServer.toClient=ship.getLayoutX();
            }
            JavaServer.input=false;

        }

        if(!JavaServer.input){
            if(angle<0){
                angle +=5;
            }else if(angle>0){
                angle -=5;
            }
            ship.setRotate(angle);
        }
    }

    private void createBackground(){
        gridPane1=new GridPane();
        gridPane2=new GridPane();
        for(int i=0;i<12;i++){
            InputStream inputStream = getClass().getResourceAsStream(BACKGROUND_IMAGE);
            ImageView backgroundImage1= new ImageView(BACKGROUND_IMAGE);
            ImageView backgroundImage2=new ImageView(BACKGROUND_IMAGE);
            GridPane.setConstraints(backgroundImage1,i%3,i/3);
            GridPane.setConstraints(backgroundImage2,i%3,i/3);
            gridPane1.getChildren().add(backgroundImage1);
            gridPane2.getChildren().add(backgroundImage2);

        }
        gridPane2.setLayoutY(-1024);
        gamePane.getChildren().addAll(gridPane1,gridPane2);
    }
    private void moveBackground(){
        gridPane1.setLayoutY(gridPane1.getLayoutY()+0.5);
        gridPane2.setLayoutY(gridPane2.getLayoutY()+0.5);
        if(gridPane1.getLayoutY()>=1024)gridPane1.setLayoutY(-1024);
        if(gridPane2.getLayoutY()>=1024)gridPane2.setLayoutY(-1024);
    }
    private void createGameElements(SHIP chosenShip){
        playerLife=2;
        star=new ImageView(GOLD_STAR_IMAGE);
        setNewElementPosition(star);
        gamePane.getChildren().add(star);
        pointsLabel = new Small_Info_Label("POINTS: 00");
        pointsLabel.setLayoutX(460);
        pointsLabel.setLayoutY(20);
        gamePane.getChildren().add(pointsLabel);
        playerLifes=new ImageView[3];
        for(int i=0; i<playerLifes.length;i++){
            playerLifes[i]=new ImageView(chosenShip.getUrlLife());
            playerLifes[i].setLayoutX(455+(i*50));
            playerLifes[i].setLayoutY(80);
            gamePane.getChildren().add(playerLifes[i]);

        }



        brownMeteors=new ImageView[3];
        for(int i =0;i<brownMeteors.length;i++){
            brownMeteors[i]=new ImageView(METEOR_BROWN_IMAGE);
            setNewElementPosition(brownMeteors[i]);
            gamePane.getChildren().add(brownMeteors[i]);
        }
        greyMeteors=new ImageView[3];
        for(int i =0;i<greyMeteors.length;i++){
           greyMeteors[i]=new ImageView(METEOR_GREY_IMAGE);
            setNewElementPosition(greyMeteors[i]);
            gamePane.getChildren().add(greyMeteors[i]);
        }
    }
    private void setNewElementPosition(ImageView image){
        image.setLayoutX(randomPositionGenerator.nextInt(600));
        image.setLayoutY(-(randomPositionGenerator.nextInt(370)+100));


    }
    private void moveGameElements(){
        //star.setLayoutY(star.getLayoutY()+5);

        for(int i=0;i<brownMeteors.length;i++){

            brownMeteors[i].setLayoutY(brownMeteors[i].getLayoutY()+20);
            brownMeteors[i].setRotate(brownMeteors[i].getRotate()+4);
        }
        for(int i=0;i<greyMeteors.length;i++){

            greyMeteors[i].setLayoutY(greyMeteors[i].getLayoutY()+20);
            greyMeteors[i].setRotate(greyMeteors[i].getRotate()+4);
        }

    }
    private void checkIfElementsAreBehindTheShipAndRelocate(){
        if(star.getLayoutY()>1200){
            setNewElementPosition(star);

        }

        for(int i=0;i<brownMeteors.length;i++){
            if(brownMeteors[i].getLayoutY()>900){
                setNewElementPosition(brownMeteors[i]);
            }
        }
        for(int i=0;i<greyMeteors.length;i++){
            if(greyMeteors[i].getLayoutY()>900){
                setNewElementPosition(greyMeteors[i]);
            }
        }

    }
    private void checkIfElementsCollide(){
        boolean colision=false;
        if((SHIP_RADIUS + STAR_RADIUS)>calculateDistance(ship.getLayoutX()+49, star.getLayoutX()+15, ship.getLayoutY()+37,star.getLayoutY()+15))
        {  setNewElementPosition(star);

            points=10;
            String textToSet="Points :";
            if(points<10){
                textToSet=textToSet+"0";
            }
            pointsLabel.setText(textToSet+points);
        }
        for(int i=0;i<brownMeteors.length;i++){
            if((SHIP_RADIUS +METEOR_RADIUS)>calculateDistance(ship.getLayoutX()+49, brownMeteors[i].getLayoutX()+20, ship.getLayoutY()+37,brownMeteors[i].getLayoutY()+20))
            {
                print("COLISION");
                removeLife();


                //setNewElementPosition(brownMeteors[i]);
                colision=true;

            }
        }
        for(int i=0;i<greyMeteors.length;i++){
            if(SHIP_RADIUS +METEOR_RADIUS>calculateDistance(ship.getLayoutX()+49, greyMeteors[i].getLayoutX()+20, ship.getLayoutY()+37,greyMeteors[i].getLayoutY()+20))
            {
                print("COLISION");
                removeLife();
                colision=true;
               // setNewElementPosition(greyMeteors[i]);

            }

        }
        if(colision==true){
            JavaServer.reward=0;
            resetGame();
        }else{
            JavaServer.reward=1;

        }

    }
    private void removeLife(){
        gamePane.getChildren().remove(playerLifes[playerLife]);
       // playerLife--;
        if(playerLife<0){
            gameStage.close();
            gameTimer.stop();
            menuStage.show();
            JavaServer.reward=points;
        }
    }
    private int calculateDistance(double x1, double x2, double y1, double y2){
        int t=(int)Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));

        return t;

    }
    private void startComm(){
       js=new JavaServer(8080);
     //  jc=new JavaClient(8090);
        js.start();
     //   jc.start();
    }
    private void takeSnapShot(Scene scene, int index){
        while(GameViewManager.imageReady==true);
         //   System.out.println("Taking snapshot..");
         writableImage =
               new WritableImage((int)scene.getWidth(), (int)scene.getHeight());
        scene.snapshot(writableImage);
         //   System.out.println("took it");

         file = new File("snapshot"+index+".png");
        imageReady=true;
        //System.out.println(imageReady);

        /*try {
            //ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            javax.imageio.ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            System.out.println("snapshot saved: " + file.getAbsolutePath());
             JavaServer.array =Files.readAllBytes(Path.of(file.getAbsolutePath()));
            imageReady=true;
        } catch (IOException ex) {

        }*/




        }
        public void resetGame(){

            setNewElementPosition(star);

            for(int i=0;i<brownMeteors.length;i++)
                setNewElementPosition(brownMeteors[i]);


            for(int i=0;i<greyMeteors.length;i++)
             setNewElementPosition(greyMeteors[i]);

            ship.setLayoutX(GAME_WIDTH-200);
            ship.setLayoutY(GAME_HEIGHT-90);

            JavaServer.done=1;
            print("reset");

        }
        public void print(String string){
            System.out.println(string);

        }

    }
















