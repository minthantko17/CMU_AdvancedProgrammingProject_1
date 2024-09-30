package se233.advprogrammingproject1.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Popup;
import jdk.swing.interop.SwingInterOpUtils;
import se233.advprogrammingproject1.Launcher;
import se233.advprogrammingproject1.cropping.CropImageGroup;
import se233.advprogrammingproject1.cropping.CropTask;
import se233.advprogrammingproject1.cropping.RectangleBoxGroup;
import se233.advprogrammingproject1.functions.CropFunctions;
import se233.advprogrammingproject1.cropping.PreviewImageView;
import se233.advprogrammingproject1.functions.MainMenuFunctions;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CropController {

    @FXML
    Pane cropPane;
    @FXML
    Group cropGroup;
    @FXML
    Button nextBtn;
    @FXML
    Button prevBtn;
    @FXML
    Button saveBtn;
    @FXML
    Group previewImgGroup;
    @FXML
    Button cropBtn;
    @FXML
    HBox rectangleRatioHBox;
    @FXML
    Button backToMainBtn;
    @FXML
    Button ratioFreeBtn;
    @FXML
    Button ratio11Btn;
    @FXML
    Button ratio32Btn;
    @FXML
    Button ratio23Btn;
    @FXML
    Button ratio169Btn;
    @FXML
    Button ratio916Btn;
    @FXML
    ScrollPane scrollPane;
    @FXML
    VBox progressVBox;

    public static double ratioWidth;
    public static double ratioHeight;
    public static boolean isAspectRatio;
    List<CropImageGroup> cropImageGroupsList=new ArrayList<>();
    public static List<BufferedImage> croppedBufferedImages = new ArrayList<>();
    public static List<PreviewImageView> previewImageViewList=new ArrayList<>();
    int pageNumber;
    private ObservableList<ProgressBar> progressBars;

    @FXML
    public void initialize() throws IndexOutOfBoundsException{
        pageNumber=0;

        progressBars= FXCollections.observableArrayList();
        cropImageGroupsList.clear();
        croppedBufferedImages.clear();
        previewImageViewList.clear();

        for(int i=0; i<Launcher.imageViewsToProcess.size(); i++){
            cropImageGroupsList.add(new CropImageGroup(Launcher.imageViewsToProcess.get(i).getImage()));
        }
        cropGroup.getChildren().addAll(cropImageGroupsList.get(pageNumber));

        prevBtn.setDisable(pageNumber == 0);
        nextBtn.setDisable(pageNumber + 1 == Launcher.imageViewsToProcess.size());
        saveBtn.setDisable(true);


        //-----some UI updates-----
        backToMainBtn.setOnMousePressed(event->{
            backToMainBtn.setStyle("-fx-background-color: lightgrey");
        });
        backToMainBtn.setOnMouseReleased(event->{
            backToMainBtn.setStyle("-fx-background-color: white");
        });

        nextBtn.setOnMousePressed((event->{
            nextBtn.setStyle("-fx-background-color: lightgrey");
        }));
        nextBtn.setOnMouseReleased(event->{
            nextBtn.setStyle("-fx-background-color: white");
        });

        prevBtn.setOnMousePressed((event->{
            prevBtn.setStyle("-fx-background-color: lightgrey");
        }));
        prevBtn.setOnMouseReleased(event->{
            prevBtn.setStyle("-fx-background-color: white");
        });

        cropBtn.setOnMousePressed((event->{
            cropBtn.setStyle("-fx-background-color: lightgrey");
        }));
        cropBtn.setOnMouseReleased(event->{
            cropBtn.setStyle("-fx-background-color: white");
        });

        saveBtn.setOnMousePressed((event->{
            saveBtn.setStyle("-fx-background-color: lightgrey");
        }));
        saveBtn.setOnMouseReleased(event->{
            saveBtn.setStyle("-fx-background-color: white");
        });

        ratioFreeBtn.setOnMouseClicked(event->{
            ratioFreeBtn.setStyle("-fx-background-color: gray");
            ratio11Btn.setStyle("-fx-background-color: white");
            ratio32Btn.setStyle("-fx-background-color: white");
            ratio23Btn.setStyle("-fx-background-color: white");
            ratio169Btn.setStyle("-fx-background-color: white");
            ratio916Btn.setStyle("-fx-background-color: white");
        });

        ratio11Btn.setOnMouseClicked(event->{
            ratioFreeBtn.setStyle("-fx-background-color: white");
            ratio11Btn.setStyle("-fx-background-color: gray");
            ratio32Btn.setStyle("-fx-background-color: white");
            ratio23Btn.setStyle("-fx-background-color: white");
            ratio169Btn.setStyle("-fx-background-color: white");
            ratio916Btn.setStyle("-fx-background-color: white");
        });

        ratio32Btn.setOnMouseClicked(event->{
            ratioFreeBtn.setStyle("-fx-background-color: white");
            ratio11Btn.setStyle("-fx-background-color: white");
            ratio32Btn.setStyle("-fx-background-color: gray");
            ratio23Btn.setStyle("-fx-background-color: white");
            ratio169Btn.setStyle("-fx-background-color: white");
            ratio916Btn.setStyle("-fx-background-color: white");
        });

        ratio23Btn.setOnMouseClicked(event->{
            ratioFreeBtn.setStyle("-fx-background-color: white");
            ratio11Btn.setStyle("-fx-background-color: white");
            ratio32Btn.setStyle("-fx-background-color: white");
            ratio23Btn.setStyle("-fx-background-color: gray");
            ratio169Btn.setStyle("-fx-background-color: white");
            ratio916Btn.setStyle("-fx-background-color: white");
        });

        ratio169Btn.setOnMouseClicked(event->{
            ratioFreeBtn.setStyle("-fx-background-color: white");
            ratio11Btn.setStyle("-fx-background-color: white");
            ratio32Btn.setStyle("-fx-background-color: white");
            ratio23Btn.setStyle("-fx-background-color: white");
            ratio169Btn.setStyle("-fx-background-color: gray");
            ratio916Btn.setStyle("-fx-background-color: white");
        });

        ratio916Btn.setOnMouseClicked(event->{
            ratioFreeBtn.setStyle("-fx-background-color: white");
            ratio11Btn.setStyle("-fx-background-color: white");
            ratio32Btn.setStyle("-fx-background-color: white");
            ratio23Btn.setStyle("-fx-background-color: white");
            ratio169Btn.setStyle("-fx-background-color: white");
            ratio916Btn.setStyle("-fx-background-color: gray");
        });

    }

    public void backToMainMenu(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("MainMenu.fxml"));
            Launcher.primaryStage.setTitle("Advance Programming Project 1");
            Launcher.primaryStage.setX(400);
            Launcher.primaryStage.setY(150);
            Launcher.primaryScene = new Scene(fxmlLoader.load());
            Launcher.primaryStage.setScene(Launcher.primaryScene);
            Launcher.primaryStage.show();
        } catch (IOException e) {
            MainMenuFunctions.showAlertBox("Error in loading Main Menu.", Alert.AlertType.ERROR);
        }
    }

    public void cropBtnAction(){
        if(!croppedBufferedImages.isEmpty()){ croppedBufferedImages.clear(); }
        if(!previewImageViewList.isEmpty()){previewImageViewList.clear(); }

        progressBars.clear();
        progressVBox.getChildren().clear();

        //create Label+ProgressBar according to input image number
        for(int i=0; i<cropImageGroupsList.size(); i++){
            String imageName = Launcher.unzippedFileToProcess.get(i).getName();
            Label imageLabel = new Label(imageName);
            imageLabel.setPrefWidth(120);

            ProgressBar progressBar = new ProgressBar(0);
            progressBar.setPrefWidth(150);
            progressBars.add(progressBar);

            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPadding(new Insets(5, 5, 5, 5));
            hbox.getChildren().addAll(imageLabel, progressBar);

            progressVBox.getChildren().add(hbox);
        }

        //create countdown to show preview after finishing all image processing
        CountDownLatch countDownLatch=new CountDownLatch(cropImageGroupsList.size());

        croppedBufferedImages=new ArrayList<>(Collections.nCopies(cropImageGroupsList.size(), null));
        previewImageViewList=new ArrayList<>(Collections.nCopies(cropImageGroupsList.size(), null));

        int numberOfThread = Math.min(5, cropImageGroupsList.size());
        try{
            ExecutorService executorService= Executors.newFixedThreadPool(numberOfThread);

            for(int i=0; i<cropImageGroupsList.size();i++){
                CropImageGroup currentCropImageGroup = cropImageGroupsList.get(i);
                CropTask cropTask=new CropTask(currentCropImageGroup.getImageView(),
                        currentCropImageGroup.getRectangleBox().getRectangle(),
                        previewImgGroup,
                        progressBars.get(i),
                        i, pageNumber, countDownLatch
                );
                progressBars.get(i).progressProperty().bind(cropTask.progressProperty());
                executorService.submit(cropTask);
            }

            executorService.submit(()->{
                try {
                    countDownLatch.await();
                    Platform.runLater(()->{
                        try {
                            if(previewImageViewList.get(pageNumber)==null){
                                throw new NullPointerException();
                            }
                            previewImgGroup.getChildren().clear();
                            previewImgGroup.getChildren().addAll(previewImageViewList.get(pageNumber));
                            System.out.println("previewImgGroup size af: "+previewImgGroup.getChildren().size());
                        } catch (NullPointerException e){
                            System.out.println("im here");
                            previewImgGroup.getChildren().clear();
                            previewImgGroup.getChildren().addAll(new PreviewImageView(SwingFXUtils.fromFXImage(cropImageGroupsList.get(pageNumber).getImageView().getImage(), null)));
                            MainMenuFunctions.showAlertBox("Some Image can't be cropped.\nPlease check your cropping area.", Alert.AlertType.ERROR);
                        } finally {
                            saveBtn.setDisable(false);
                        }
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            System.out.println("before shutdown");
            executorService.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void saveBtnAction(){
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Folder to Save");

            File defaultFolderToSave=new File(System.getProperty("user.home")+File.separator+"CroppedImages");
            if(!defaultFolderToSave.exists()){
                defaultFolderToSave.mkdir();
            }
            directoryChooser.setInitialDirectory(defaultFolderToSave);

            File fileDir= directoryChooser.showDialog(Launcher.primaryStage);
            System.out.println(directoryChooser);
            if(fileDir==null){
                throw new NullPointerException();
            }

            for(int i =0; i<croppedBufferedImages.size(); i++){
                String orgFileName=Launcher.unzippedFileToProcess.get(i).getName();
                String extension = orgFileName.substring(orgFileName.lastIndexOf(".") + 1); //get extension format to save as
                String fileName="cropped_"+orgFileName;
                File outputFile=new File(fileDir, fileName);

                BufferedImage croppedImage=croppedBufferedImages.get(i);
                CropFunctions.saveImg(croppedImage, outputFile, extension);
            }
            MainMenuFunctions.showAlertBox("Successfully Saved Image(s)\nSaved at: "+ fileDir, Alert.AlertType.INFORMATION);
        }catch (NullPointerException e){
            System.out.println("Save location is not selected");
        }catch (IOException e) {
            MainMenuFunctions.showAlertBox("Images(s) are not saved", Alert.AlertType.INFORMATION);
        }catch (Exception e){
            System.out.println("Error in save.\n"+e.getMessage());
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
        if(!previewImageViewList.isEmpty() && previewImageViewList.get(pageNumber) != null) {
            previewImgGroup.getChildren().clear();
            previewImgGroup.getChildren().addAll(previewImageViewList.get(pageNumber));
        }

        prevBtn.setDisable(pageNumber == 0);
        nextBtn.setDisable(pageNumber + 1 == Launcher.imageViewsToProcess.size());
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
        if(!previewImageViewList.isEmpty() && previewImageViewList.get(pageNumber) != null) {
            previewImgGroup.getChildren().clear();
            previewImgGroup.getChildren().addAll(previewImageViewList.get(pageNumber));
        }

        prevBtn.setDisable(pageNumber == 0);
        nextBtn.setDisable(pageNumber + 1 == Launcher.imageViewsToProcess.size());
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
//    public void cropAndSaveImg(){
////        Crop.crop(((CropImageGroup)cropGroup.getChildren().get(0)).getImageView(), ((CropImageGroup)cropGroup.getChildren().get(0)).getRectangleBox().getRectangle());
////        Functions.crop(((CropImageGroup)cropGroup.getChildren().get(0)).getImageView(), ((CropImageGroup)cropGroup.getChildren().get(0)).getRectangleBox().getRectangle());
////        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG Files", "*.jpg"));
////        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
//        DirectoryChooser directoryChooser = new DirectoryChooser();
//        directoryChooser.setTitle("Save Image");
//        File fileDir= directoryChooser.showDialog(Launcher.primaryStage);
//
//        String orgFileName;
//        for(int i =0; i<cropImageGroupsList.size(); i++){
//            orgFileName=Launcher.unzippedFileToProcess.get(i).getName();
//            String extension = orgFileName.substring(orgFileName.lastIndexOf(".") + 1); //get extension format to save as
//            String fileName="cropped_"+orgFileName;
//            File outputFile=new File(fileDir, fileName);
//
//            BufferedImage croppedImage= CropFunctions.crop(cropImageGroupsList.get(i).getImageView(), cropImageGroupsList.get(i).getRectangleBox().getRectangle());
//            CropFunctions.saveImg(croppedImage, outputFile, extension);
//        }
//    }
//
//    private void initiateRatios(){
////        ratioFreeImgView.setImage(new Image(Launcher.class.getResourceAsStream("assets/size_free.png")));
////        ratioSquareImgView.setImage(new Image(Launcher.class.getResourceAsStream("assets/size_1_1.png")));
//
//    }
//
//    public void cropBtnAction(){
//        if(!croppedBufferedImages.isEmpty()){ croppedBufferedImages.clear(); }
//        if(!previewImageViewList.isEmpty()){previewImageViewList.clear(); }
//        for(int i =0; i<cropImageGroupsList.size(); i++){
//            croppedBufferedImages.add(CropFunctions.crop(cropImageGroupsList.get(i).getImageView(), cropImageGroupsList.get(i).getRectangleBox().getRectangle()));
//            previewImageViewList.add(new PreviewImageView(croppedBufferedImages.get(i)));
//        }
//        previewImgGroup.getChildren().clear();
//        previewImgGroup.getChildren().addAll(previewImageViewList.get(pageNumber));
////        System.out.println("croppedBufferImages size: " + croppedBufferedImages.size());
//    }
}
