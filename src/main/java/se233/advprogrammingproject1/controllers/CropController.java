package se233.advprogrammingproject1.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import se233.advprogrammingproject1.Launcher;
import se233.advprogrammingproject1.cropping.CropImageGroup;
import se233.advprogrammingproject1.cropping.RectangleBoxGroup;
import se233.advprogrammingproject1.functions.CropFunctions;
import se233.advprogrammingproject1.cropping.PreviewImageView;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CropController {

    @FXML
    public Pane cropPane;
    @FXML
    public Group cropGroup;
    @FXML
    public Button nextBtn;
    @FXML
    public Group previewImgGroup;
    @FXML
    public ImageView previewImg;
    @FXML
    public HBox rectangleRatioHBox;

    @FXML
    public Button backToMainBtn;
    @FXML
    public Button ratioFreeBtn;
    @FXML
    public Button ratio11Btn;
    @FXML
    public Button ratio32Btn;
    @FXML
    public Button ratio23Btn;
    @FXML
    public Button ratio169Btn;
    @FXML
    public Button ratio916Btn;



    public static double ratioWidth;
    public static double ratioHeight;
    public static boolean isAspectRatio;
    public static List<BufferedImage> croppedBufferedImages = new ArrayList<>();
    List<CropImageGroup> cropImageGroupsList=new ArrayList<>();
    List<PreviewImageView> previewImageViewList=new ArrayList<>();
    int pageNumber = 0;

    @FXML
    public void initialize(){
        cropPane.setStyle("-fx-border-color: Black");
        cropPane.setStyle("-fx-background-color: Grey");
//        Image image1=new Image(Launcher.class.getResourceAsStream("assets/myImg1.jpg"));
//        Image image2=new Image(Launcher.class.getResourceAsStream("assets/myImg3.jpg"));
//
//        imageViews=new ArrayList<>();
//        imageViews.add(new ImageView(image1));
//        imageViews.add(new ImageView(image2));
        for(int i=0; i<Launcher.imageViewsToProcess.size(); i++){
            cropImageGroupsList.add(new CropImageGroup(Launcher.imageViewsToProcess.get(i).getImage()));
        }
        cropGroup.getChildren().addAll(cropImageGroupsList.get(pageNumber));
    }

    public void backToMainMenu() throws IOException {
        if(Launcher.filePath!=null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("MainMenu.fxml"));
            Launcher.primaryScene = new Scene(fxmlLoader.load());
            Launcher.primaryStage.setScene(Launcher.primaryScene);
            Launcher.primaryStage.show();
        }
    }


    public void cropBtnAction(){
        if(!croppedBufferedImages.isEmpty()){ croppedBufferedImages.clear(); }
        if(!previewImageViewList.isEmpty()){previewImageViewList.clear(); }
        for(int i =0; i<cropImageGroupsList.size(); i++){
            croppedBufferedImages.add(CropFunctions.crop(cropImageGroupsList.get(i).getImageView(), cropImageGroupsList.get(i).getRectangleBox().getRectangle()));
            previewImageViewList.add(new PreviewImageView(croppedBufferedImages.get(i)));
        }
        previewImgGroup.getChildren().clear();
        previewImgGroup.getChildren().addAll(previewImageViewList.get(pageNumber));
//        System.out.println("croppedBufferImages size: " + croppedBufferedImages.size());
    }
    public void saveBtnAction(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder to Save");
        File fileDir= directoryChooser.showDialog(Launcher.primaryStage);

        System.out.println("Before save loop croppedBufferImages size: " +croppedBufferedImages.size());
        System.out.println("Before save loop unzippedFiletoProcess size: " + Launcher.unzippedFileToProcess.size());

        for(int i =0; i<croppedBufferedImages.size(); i++){
            String orgFileName=Launcher.unzippedFileToProcess.get(i).getName();
            String extension = orgFileName.substring(orgFileName.lastIndexOf(".") + 1); //get extension format to save as
            String fileName="cropped_"+orgFileName;
            File outputFile=new File(fileDir, fileName);

            BufferedImage croppedImage=croppedBufferedImages.get(i);
            CropFunctions.saveImg(croppedImage, outputFile, extension);
        }
    }

    public void nextBtnAction(){
        if(pageNumber<cropImageGroupsList.size()-1) {
            pageNumber++;
        }
        System.out.println("pageNumber " + pageNumber);
        System.out.println("previewImageviewlistsize "+previewImageViewList.size());
        if(pageNumber<cropImageGroupsList.size()){
            System.out.println(pageNumber);
            cropGroup.getChildren().clear();
            cropGroup.getChildren().add(cropImageGroupsList.get(pageNumber));
        }
        if(!previewImageViewList.isEmpty()) {
            previewImgGroup.getChildren().clear();
            previewImgGroup.getChildren().addAll(previewImageViewList.get(pageNumber));
        }

    }

    public void prevBtnAction(){
        if (pageNumber>0){
            pageNumber--;
        }
        if(pageNumber>=0){
            System.out.println(pageNumber);
            cropGroup.getChildren().clear();
            cropGroup.getChildren().add(cropImageGroupsList.get(pageNumber));
        }
        if(!previewImageViewList.isEmpty()) {
            previewImgGroup.getChildren().clear();
            previewImgGroup.getChildren().addAll(previewImageViewList.get(pageNumber));
        }
    }

    public void setRatioFreeBtnAction(){
        isAspectRatio=false;
    }

    public void setRatio11BtnAction(){
        isAspectRatio=true;
        ratioWidth=1.0;
        ratioHeight=1.0;
        RectangleBoxGroup currentRectangleBox=cropImageGroupsList.get(pageNumber).getRectangleBox();
        if(currentRectangleBox.getRectangle().getWidth()>currentRectangleBox.getRectangle().getHeight()){
            currentRectangleBox.getRectangle().setWidth(currentRectangleBox.getRectangle().getHeight()*(ratioWidth/ratioHeight));
        }else{
            currentRectangleBox.getRectangle().setHeight(currentRectangleBox.getRectangle().getWidth()*(ratioHeight/ratioWidth));
        }
        CropFunctions.updateHandles(currentRectangleBox, currentRectangleBox.getRectangle());
    }

    public void setRatio32BtnAction(){
        isAspectRatio=true;
        ratioWidth=3.0;
        ratioHeight=2.0;
        RectangleBoxGroup currentRectangleBox=cropImageGroupsList.get(pageNumber).getRectangleBox();
        if(currentRectangleBox.getRectangle().getWidth()>currentRectangleBox.getRectangle().getHeight()){
            currentRectangleBox.getRectangle().setWidth(currentRectangleBox.getRectangle().getHeight()*(ratioWidth/ratioHeight));
        }else{
            currentRectangleBox.getRectangle().setHeight(currentRectangleBox.getRectangle().getWidth()*(ratioHeight/ratioWidth));
        }
        CropFunctions.updateHandles(currentRectangleBox, currentRectangleBox.getRectangle());
    }

    public void setRatio23BtnAction(){
        isAspectRatio=true;
        ratioWidth=2.0;
        ratioHeight=3.0;
        RectangleBoxGroup currentRectangleBox=cropImageGroupsList.get(pageNumber).getRectangleBox();
        if(currentRectangleBox.getRectangle().getWidth()>currentRectangleBox.getRectangle().getHeight()){
            currentRectangleBox.getRectangle().setWidth(currentRectangleBox.getRectangle().getHeight()*(ratioWidth/ratioHeight));
        }else{
            currentRectangleBox.getRectangle().setHeight(currentRectangleBox.getRectangle().getWidth()*(ratioHeight/ratioWidth));
        }
        CropFunctions.updateHandles(currentRectangleBox, currentRectangleBox.getRectangle());
    }

    public void setRatio169BtnAction(){
        isAspectRatio=true;
        ratioWidth=16.0;
        ratioHeight=9.0;
        RectangleBoxGroup currentRectangleBox=cropImageGroupsList.get(pageNumber).getRectangleBox();
        if(currentRectangleBox.getRectangle().getWidth()>currentRectangleBox.getRectangle().getHeight()){
            currentRectangleBox.getRectangle().setWidth(currentRectangleBox.getRectangle().getHeight()*(ratioWidth/ratioHeight));
        }else{
            currentRectangleBox.getRectangle().setHeight(currentRectangleBox.getRectangle().getWidth()*(ratioHeight/ratioWidth));
        }
        CropFunctions.updateHandles(currentRectangleBox, currentRectangleBox.getRectangle());
    }

    public void setRatio916BtnAction(){
        isAspectRatio=true;
        ratioWidth=9.0;
        ratioHeight=16.0;
        RectangleBoxGroup currentRectangleBox=cropImageGroupsList.get(pageNumber).getRectangleBox();
        if(currentRectangleBox.getRectangle().getWidth()>currentRectangleBox.getRectangle().getHeight()){
            currentRectangleBox.getRectangle().setWidth(currentRectangleBox.getRectangle().getHeight()*(ratioWidth/ratioHeight));
        }else{
            currentRectangleBox.getRectangle().setHeight(currentRectangleBox.getRectangle().getWidth()*(ratioHeight/ratioWidth));
        }
        CropFunctions.updateHandles(currentRectangleBox, currentRectangleBox.getRectangle());
    }



    //----junk methods----
    public void cropAndSaveImg(){
//        Crop.crop(((CropImageGroup)cropGroup.getChildren().get(0)).getImageView(), ((CropImageGroup)cropGroup.getChildren().get(0)).getRectangleBox().getRectangle());
//        Functions.crop(((CropImageGroup)cropGroup.getChildren().get(0)).getImageView(), ((CropImageGroup)cropGroup.getChildren().get(0)).getRectangleBox().getRectangle());
//        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG Files", "*.jpg"));
//        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Save Image");
        File fileDir= directoryChooser.showDialog(Launcher.primaryStage);

        String orgFileName;
        for(int i =0; i<cropImageGroupsList.size(); i++){
            orgFileName=Launcher.unzippedFileToProcess.get(i).getName();
            String extension = orgFileName.substring(orgFileName.lastIndexOf(".") + 1); //get extension format to save as
            String fileName="cropped_"+orgFileName;
            File outputFile=new File(fileDir, fileName);

            BufferedImage croppedImage= CropFunctions.crop(cropImageGroupsList.get(i).getImageView(), cropImageGroupsList.get(i).getRectangleBox().getRectangle());
            CropFunctions.saveImg(croppedImage, outputFile, extension);
        }
    }

    private void initiateRatios(){
//        ratioFreeImgView.setImage(new Image(Launcher.class.getResourceAsStream("assets/size_free.png")));
//        ratioSquareImgView.setImage(new Image(Launcher.class.getResourceAsStream("assets/size_1_1.png")));

    }
}
