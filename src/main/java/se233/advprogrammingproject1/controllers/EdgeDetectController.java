package se233.advprogrammingproject1.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import se233.advprogrammingproject1.Launcher;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.EdgeDetectImageGroup;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.EdgeDetectPreviewImageView;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.grayscale.Grayscale;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static se233.advprogrammingproject1.Launcher.unzippedFileToProcess;

public class EdgeDetectController{

    @FXML
    public Pane edgeDetectPane;
    @FXML
    public Group edgeDetectGroup;
    @FXML
    public Button edgeDetectNextButton;
    @FXML
    public Group edgeDetectPreviewImgGroup;
    @FXML
    public ImageView edgePreviewImg;
    @FXML
    public HBox edgeDetectRectangleRatioHBOx;

    @FXML
    public Button edgeDetectBackToMainMenu;
    @FXML
    public Button cannyEdgeDetectBtn;
    @FXML
    public Button laplacianEdgeDetectBtn;
    @FXML
    public Button sobelEdgeDetectBtn;

    public static List<BufferedImage> edgeDetectedBufferedImagesList = new ArrayList<>();
    List<int[][]> pixelList = new ArrayList<>();


    List<EdgeDetectImageGroup> edgeDetectImageGroupsList = new ArrayList<>();
    List<EdgeDetectPreviewImageView> edgeDetecPreviewImageViewList = new ArrayList<>();
    int pageNumber = 0;

    @FXML
    public void initialize() throws IOException {
        edgeDetectPane.setStyle("-fx-border-color: Black");
        edgeDetectPane.setStyle("-fx-background-color: Grey");
        for(int i = 0; i < unzippedFileToProcess.size(); i++) {
            edgeDetectedBufferedImagesList.add(ImageIO.read(unzippedFileToProcess.get(i)));
            pixelList.add(Grayscale.imgToGrayPixels(edgeDetectedBufferedImagesList.get(i)));
            edgeDetectImageGroupsList.add(new EdgeDetectImageGroup(Launcher.imageViewsToProcess.get(i).getImage()));
        }
        edgeDetectGroup.getChildren().addAll(edgeDetectImageGroupsList.get(pageNumber));
    }

    public void edgeDetectBackToMainMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("MainMenu.fxml"));
            Launcher.primaryScene = new Scene(fxmlLoader.load());
            Launcher.primaryStage.setScene(Launcher.primaryScene);
            Launcher.primaryStage.show();
        } catch (IOException e) {
            System.out.println("error");
        }
    }

//    public void edgeDetectBtnAction() {
//        if(!edgeDetectedBufferedImages.isEmpty()) { edgeDetectedBufferedImages.clear(); }
//        if(!edgeDetecPreviewImageViewList.isEmpty()) { edgeDetecPreviewImageViewList.clear(); }
//        for (int i = 0; i < edgeDetectImageGroupsList.size(); i++) {
//            edgeDetectedBufferedImages.add(EdgeDetectFunctions.edgeDetect(edgeDetectImageGroupsList.get(i).getEdgeDetectImageView(), edgeDetectImageGroupsList.get(i).getEdgeDetectRectangleBox().getRectangle()));
//            edgeDetecPreviewImageViewList.add(new EdgeDetectPreviewImageView(edgeDetectedBufferedImages.get(i)));
//        }
//        edgeDetectPreviewImgGroup.getChildren().clear();
//        edgeDetectPreviewImgGroup.getChildren().addAll(edgeDetecPreviewImageViewList.get(pageNumber));
//    }
//
//    public void edgeDetectSaveBtnAction() {
//        DirectoryChooser directoryChooser = new DirectoryChooser();
//        directoryChooser.setTitle("Select Folder to Save");
//        File fileDir = directoryChooser.showDialog(Launcher.primaryStage);
//
//        System.out.println("Before save loop edgeDetectedBufferImages size: " + edgeDetectedBufferedImages.size());
//        System.out.println("Before save loop unzippedFiletoProcess size: " + unzippedFileToProcess.size());
//
//        for (int i = 0; i < edgeDetectedBufferedImages.size(); i++) {
//            String orgFileName = unzippedFileToProcess.get(i).getName();
//            String extension = orgFileName.substring(orgFileName.lastIndexOf(".") + 1);
//            String fileName = "edgeDetected_" + orgFileName;
//            File outputFile = new File(fileDir, fileName);
//
//            BufferedImage edgeDetectedImage = edgeDetectedBufferedImages.get(i);
//            EdgeDetectFunctions.saveImg(edgeDetectedImage, outputFile, extension);
//        }
//    }

    public void edgeDetectNextBtnAction() {
        if(pageNumber < edgeDetectImageGroupsList.size() - 1) {
            pageNumber++;
        }
        System.out.println("pageNumber: " + pageNumber);
        System.out.println("edgeDetectPreviewImageviewlistsize " + edgeDetecPreviewImageViewList.size());
        if(pageNumber < edgeDetectImageGroupsList.size()) {
            System.out.println(pageNumber);
            edgeDetectGroup.getChildren().clear();
            edgeDetectGroup.getChildren().add(edgeDetectImageGroupsList.get(pageNumber));
        }
        if(!edgeDetecPreviewImageViewList.isEmpty()) {
            edgeDetectPreviewImgGroup.getChildren().clear();
            edgeDetectPreviewImgGroup.getChildren().addAll(edgeDetecPreviewImageViewList.get(pageNumber));
        }
    }

    public void edgeDetectPrevBtnAction() {
        if (pageNumber > 0) {
            pageNumber--;
        }
        if (pageNumber >= 0) {
            System.out.println(pageNumber);
            edgeDetectGroup.getChildren().clear();
            edgeDetectGroup.getChildren().add(edgeDetectImageGroupsList.get(pageNumber));
        }
        if(!edgeDetecPreviewImageViewList.isEmpty()) {
            edgeDetectPreviewImgGroup.getChildren().clear();
            edgeDetectPreviewImgGroup.getChildren().addAll(edgeDetecPreviewImageViewList.get(pageNumber));
        }
    }

    /*public static String img;
    public static BufferedImage originalImage = ImageIO.read(new File(img));
    public static*/

    public void edgeDetectBtnAction() {

    }

    public void setCannyEdgeDetectBtnAction() {

    }

    public void setLaplacianEdgeDetectBtnAction() {

    }

    public void setSobelEdgeDetectBtnAction() {

    }

    public void edgeDetectCannyWeak() {

    }

    public void edgeDetectCannyColor() {

    }

    public void edgeDetectCannyOriginal() {

    }

    public void edgeDetectSaveBtnAction() {

    }

}
