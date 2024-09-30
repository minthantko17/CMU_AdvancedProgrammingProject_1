package se233.advprogrammingproject1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {
    public static Stage primaryStage;
    public static Scene primaryScene;
    public static List<String> filePath=new ArrayList<>();
    public static List<File> unzippedFileToProcess=new ArrayList<>();
    public static List<ImageView> imageViewsToProcess=new ArrayList<>();


    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public void start(Stage stage) throws IOException {
        primaryStage=stage;
        Launcher.primaryStage.setX(400);
        Launcher.primaryStage.setY(150);
        Image icon = new Image(getClass().getResourceAsStream("assets/appLogo.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Advance Programming Project 1");
        FXMLLoader fxmlLoader=new FXMLLoader(Launcher.class.getResource("MainMenu.fxml"));
        Scene mainScene=new Scene(fxmlLoader.load());
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }
//        String imagePath= "src/main/resources/se233/advprogrammingproject1/assets/TomAndJerry2.jpg";
//        BufferedImage bufImg= ImageIO.read(new File(imagePath));
//        System.out.println("Read Completed!");
//
//        int[][] pixels= Grayscale.imgToGrayPixels(bufImg);
//        LaplacianEdgeDetector led=new LaplacianEdgeDetector(pixels);
//        boolean[][] edges=led.getEdges();
//        BufferedImage laplaceImage= Threshold.applyThresholdReversed(edges);
//
//        String targetPath="src/main/java/se233/advprogrammingproject1/edgeDetecting/edgeDetector/demos/T_N_J.png";
//        ImageIO.write(laplaceImage, "png", new File(targetPath));


//    @Override
//    public void start(Stage stage) throws Exception {
//        RectangleBox rec= new RectangleBox(100, 100, 200, 150);
//        Pane pane = new Pane();
////        pane.getChildren().addAll(rec.getRectangle(), rec.getLeftHandle(), rec.getRightHandle(), rec.getTopHandle(), rec.getBottomHandle());
//        pane.getChildren().addAll(rec.getGp());
//        Scene scene = new Scene(pane, 500, 400);
//        stage.setScene(scene);
//        stage.show();
//    }

}
