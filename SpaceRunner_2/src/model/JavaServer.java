package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import view.GameViewManager;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaServer extends Thread {
    public static String params[];
    public static char dir;
    public static boolean input=false;
    public static int done=0;

    public static Double toClient;
    public static List<Double> state;
    public static int reward;

    public static byte[] array;
    private static int port;
    Socket client;
    public JavaServer(int prt){port=prt;dir='s';}

    public void run()  {
        while(true){
        try {
            mojaMetoda();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        }
       
    }

    private void mojaMetoda() throws Exception{

        String fromClient;


        ServerSocket server = new ServerSocket(8080);
        System.out.println("wait for connection on port 8080");

        while(true) {

         client = server.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(),true);



            fromClient = in.readLine();
           // System.out.println("received: " + fromClient);

            if(fromClient.charAt(0)=='l'){
                input=true;
                dir='l';

                sendData();
                input=false;

            }
            else if(fromClient.charAt(0)=='r')
            {
                input=true;
                dir='r';
                sendData();
                input=false;
            }else if(fromClient.charAt(0)=='x')
            {
                input=true;
                dir='x';
                sendData();
                input=false;
            }else if(fromClient.charAt(0)=='w')
            {
                input=true;
                dir='w';
                print("sening reward = "+ reward);
                sendInfo(String.valueOf(reward));
                input=false;
            }else if(fromClient.charAt(0)=='d')
            {
                input=true;
                dir='d';
                sendInfo(String.valueOf(done));
                if(done==1)done=0;
                input=false;
            }else {

                System.out.println("nista");
                out.println(toClient);
            }

        }


    }
    public void sendData(){
        while(GameViewManager.imageReady==false);
        try {
            //System.out.println("Image is ready sending...");

            OutputStream outputStream = client.getOutputStream();
            //System.out.println(GameViewManager.imageReady);


            //image = ImageIO.read(new File("C:\\Users\\UrosV\\OneDrive\\Slike\\mladenPutty.png"));
            //image=SwingFXUtils.fromFXImage(GameViewManager.writableImage, null);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(SwingFXUtils.fromFXImage(GameViewManager.writableImage, null), "png", byteArrayOutputStream);

            byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
           // System.out.println("writing size..");
            outputStream.write(size);

           // System.out.println("writing stream");
            outputStream.write(byteArrayOutputStream.toByteArray());

            outputStream.flush();
          //  System.out.println("Flushed: " + System.currentTimeMillis());
            GameViewManager.imageReady=false;
          //  System.out.println(GameViewManager.imageReady);
            outputStream.close();
            client.close();
           /* File file = new File("snapshot.png");

            try {
                //ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                javax.imageio.ImageIO.write(SwingFXUtils.fromFXImage(GameViewManager.writableImage, null), "png", file);
                System.out.println("snapshot saved: " + file.getAbsolutePath());
                JavaServer.array = Files.readAllBytes(Path.of(file.getAbsolutePath()));

            } catch (IOException ex) {

            }*/
        }catch (IOException e) {
            e.printStackTrace();
        }

        GameViewManager.imageReady=false;

    }
    public void sendInfo(String s){

        try{
            DataOutputStream dout=new DataOutputStream(client.getOutputStream());
            dout.writeUTF(s);
            dout.close();
            client.close();

        }catch(IOException e){


        }
    }
    public void print(String string){
        System.out.println(string);

    }
}