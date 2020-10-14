package model;

import javafx.embed.swing.SwingFXUtils;
import view.GameViewManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Base64;
public class JavaClient extends Thread{
    private int port;
    private OutputStream outputStream;
    private DataOutputStream dos;
    private Socket socket;
    public JavaClient(int prt){port=prt;}
    public static BufferedImage image;
    public void run(){

        try {
            socket = new Socket("localhost", port);
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("running");
        int index=0;
        while(true){
            System.out.println(GameViewManager.imageReady);

            while(GameViewManager.imageReady==false);

                 try {System.out.println("Image is ready sending...");

                //System.out.println(GameViewManager.imageReady);


               //image = ImageIO.read(new File("C:\\Users\\UrosV\\OneDrive\\Slike\\mladenPutty.png"));
                 //image=SwingFXUtils.fromFXImage(GameViewManager.writableImage, null);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(SwingFXUtils.fromFXImage(GameViewManager.writableImage, null), "png", byteArrayOutputStream);

                byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
                System.out.println("writing size..");
                outputStream.write(size);
                System.out.println("writing stream");
                outputStream.write(byteArrayOutputStream.toByteArray());
                outputStream.flush();
                System.out.println("Flushed: " + System.currentTimeMillis());
                GameViewManager.imageReady=false;
                System.out.println(GameViewManager.imageReady);

                     File file = new File("snapshot"+index+".png");
                     index++;
                     try {
                         //ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                         javax.imageio.ImageIO.write(SwingFXUtils.fromFXImage(GameViewManager.writableImage, null), "png", file);
                         System.out.println("snapshot saved: " + file.getAbsolutePath());
                         JavaServer.array = Files.readAllBytes(Path.of(file.getAbsolutePath()));

                     } catch (IOException ex) {

                     }
                }catch (IOException e) {
                     e.printStackTrace();
                 }

                GameViewManager.imageReady=false;
        }
    }
    public static String encodeImage(byte[] imageByteArray) {
        return Base64.getEncoder().encodeToString(imageByteArray);
    }
}
