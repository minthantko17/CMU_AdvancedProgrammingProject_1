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
        primaryStage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("assets/appLogo.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Advance Programming Project 1");
        FXMLLoader fxmlLoader=new FXMLLoader(Launcher.class.getResource("MainMenu.fxml"));
        Scene mainScene=new Scene(fxmlLoader.load());
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }
}
