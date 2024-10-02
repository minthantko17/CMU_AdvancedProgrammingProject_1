package se233.advprogrammingproject1.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.DirectoryChooser;
import se233.advprogrammingproject1.Launcher;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.edgeDetectorTasks.CannyTask;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.EdgeDetectImageGroup;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.EdgeDetectPreviewImageView;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.edgeDetectorTasks.LaplacianTask;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.edgeDetectorTasks.SobelTask;
import se233.advprogrammingproject1.functions.CropFunctions;
import se233.advprogrammingproject1.functions.EdgeDetectFunctions;
import se233.advprogrammingproject1.functions.MainMenuFunctions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static se233.advprogrammingproject1.Launcher.unzippedFileToProcess;

public class EdgeDetectController{

    @FXML
    public Pane edgeDetectPane;
    @FXML
    public Group edgeDetectGroup;
    @FXML
    public Button edgeDetectNextBtn;
    @FXML
    public Button edgeDetectPrevBtn;
    @FXML
    public Button edgeDetectSaveBtn;
    @FXML
    public Group edgeDetectPreviewImgGroup;
    @FXML
    public Button edgeDetectBtn;
    @FXML
    public HBox edgeDetectRectangleRatioHBOx;
    @FXML
    public HBox settingBox;
    @FXML
    public Button edgeDetectBackToMainBtn;
    @FXML
    public Button cannyEdgeDetectBtn;
    @FXML
    public Button laplacianEdgeDetectBtn;
    @FXML
    public Button sobelEdgeDetectBtn;
    @FXML
    public VBox edgeDetectProgressVBox;

    public static List<BufferedImage> bufferedImagesToDetect = new ArrayList<>();   //original image in buffer format
    public static List<BufferedImage> edgeDetectedBufferedImages = new ArrayList<>(); //buffered image after edge detected
    public static List<EdgeDetectImageGroup> edgeDetectImageGroupsList = new ArrayList<>();   //to show image list before edge detect
    public static List<EdgeDetectPreviewImageView> edgeDetectPreviewImageViewList = new ArrayList<>(); //to show preview image after edge detect

    int pageNumber;
    private static char edgeDetectAlgorithm='c';
    private static int edgeDetectConfig=0;
    int thresholdValue;
    int thresholdValueLbl;


    Button strongWeakBtn=new Button("Strong-Weak");
    Button cannyBtn=new Button("Canny");
    Button originalColorBtn=new Button("Org-Color");
    Button kernel33Btn=new Button("3 x 3");
    Button kernel55Btn=new Button("5 x 5");
    Slider slider=new Slider();
    Label label=new Label("0");
    private ObservableList<ProgressBar> progressBars;

    @FXML
    public void initialize() throws IOException {
        pageNumber = 0;
        edgeDetectPane.setStyle("-fx-border-color: Black");
        edgeDetectPane.setStyle("-fx-background-color: Lightblue");

        progressBars= FXCollections.observableArrayList();
        bufferedImagesToDetect.clear();
        edgeDetectedBufferedImages.clear();
        edgeDetectImageGroupsList.clear();
        edgeDetectPreviewImageViewList.clear();

        for(int i = 0; i < unzippedFileToProcess.size(); i++) {
            bufferedImagesToDetect.add(ImageIO.read(unzippedFileToProcess.get(i)));
            edgeDetectImageGroupsList.add(new EdgeDetectImageGroup(Launcher.imageViewsToProcess.get(i).getImage()));
        }
        System.out.println(bufferedImagesToDetect.size());
        edgeDetectGroup.getChildren().addAll(edgeDetectImageGroupsList.get(pageNumber));

        Insets insets=new Insets(4, 10, 4, 10);
        strongWeakBtn.setPadding(insets);
        strongWeakBtn.setPrefWidth(80);
        strongWeakBtn.setPrefHeight(25.6);
        strongWeakBtn.setStyle("-fx-background-color: white");
        cannyBtn.setPadding(insets);
        cannyBtn.setPrefWidth(80);
        cannyBtn.setPrefHeight(25.6);
        cannyBtn.setStyle("-fx-background-color: white");
        originalColorBtn.setPadding(insets);
        originalColorBtn.setPrefWidth(80);
        originalColorBtn.setPrefHeight(25.6);
        originalColorBtn.setStyle("-fx-background-color: white");
        kernel33Btn.setPadding(insets);
        kernel33Btn.setPrefWidth(80);
        kernel33Btn.setPrefHeight(25.6);
        kernel33Btn.setStyle("-fx-background-color: white");
        kernel55Btn.setPadding(insets);
        kernel55Btn.setPrefWidth(80);
        kernel55Btn.setPrefHeight(25.6);
        kernel55Btn.setStyle("-fx-background-color: white");
        slider.setPrefWidth(220);
        slider.setMax(250);
        slider.setMin(0);


        settingBox.setSpacing(10);
        settingBox.setAlignment(Pos.CENTER);
        settingBox.setPadding(new Insets(10));

        strongWeakBtn.setOnAction(e->{
            System.out.println("strong weak button clicked");
            edgeDetectConfig=0;
        });
        cannyBtn.setOnAction(e->{
            System.out.println("canny button clicked");
            edgeDetectConfig=1;
        });
        originalColorBtn.setOnAction(e->{
            System.out.println("original color button clicked");
            edgeDetectConfig=2;
        });

        kernel33Btn.setOnAction(e->{
            System.out.println("kernel33 button clicked");
            edgeDetectConfig=0;
        });
        kernel55Btn.setOnAction(e->{
            System.out.println("kernel55 button clicked");
            edgeDetectConfig=1;
        });

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                thresholdValueLbl =(int)slider.getValue();
                label.setText(Integer.toString(thresholdValueLbl));
            }
        });

        edgeDetectPrevBtn.setDisable(pageNumber == 0);
        edgeDetectNextBtn.setDisable(pageNumber + 1 == Launcher.imageViewsToProcess.size());
        edgeDetectSaveBtn.setDisable(true);



        //-----UI updates-----
        edgeDetectBackToMainBtn.setOnMousePressed(event->{
            edgeDetectBackToMainBtn.setStyle("-fx-background-color: lightgrey");
        });
        edgeDetectBackToMainBtn.setOnMouseReleased(event->{
            edgeDetectBackToMainBtn.setStyle("-fx-background-color: white");
        });
        edgeDetectNextBtn.setOnMousePressed((event->{
            edgeDetectNextBtn.setStyle("-fx-background-color: lightgrey");
        }));
        edgeDetectNextBtn.setOnMouseReleased(event->{
            edgeDetectNextBtn.setStyle("-fx-background-color: white");
        });
        edgeDetectPrevBtn.setOnMousePressed((event->{
            edgeDetectPrevBtn.setStyle("-fx-background-color: lightgrey");
        }));
        edgeDetectPrevBtn.setOnMouseReleased(event->{
            edgeDetectPrevBtn.setStyle("-fx-background-color: white");
        });
        edgeDetectBtn.setOnMousePressed((event->{
            edgeDetectBtn.setStyle("-fx-background-color: lightgrey");
        }));
        edgeDetectBtn.setOnMouseReleased(event->{
            edgeDetectBtn.setStyle("-fx-background-color: white");
        });
        edgeDetectSaveBtn.setOnMousePressed((event->{
            edgeDetectSaveBtn.setStyle("-fx-background-color: lightgrey");
        }));
        edgeDetectSaveBtn.setOnMouseReleased(event->{
            edgeDetectSaveBtn.setStyle("-fx-background-color: white");
        });

        cannyEdgeDetectBtn.setOnMouseClicked(event->{
            cannyEdgeDetectBtn.setStyle("-fx-background-color: gray");
            laplacianEdgeDetectBtn.setStyle("-fx-background-color: white");
            sobelEdgeDetectBtn.setStyle("-fx-background-color: white");
        });
        laplacianEdgeDetectBtn.setOnMouseClicked(event->{
            cannyEdgeDetectBtn.setStyle("-fx-background-color: white");
            laplacianEdgeDetectBtn.setStyle("-fx-background-color: gray");
            sobelEdgeDetectBtn.setStyle("-fx-background-color: white");
        });
        sobelEdgeDetectBtn.setOnMouseClicked(event->{
            cannyEdgeDetectBtn.setStyle("-fx-background-color: white");
            laplacianEdgeDetectBtn.setStyle("-fx-background-color: white");
            sobelEdgeDetectBtn.setStyle("-fx-background-color: gray");
        });

        strongWeakBtn.setOnMouseClicked(event->{
            strongWeakBtn.setStyle("-fx-background-color: lightgray");
            cannyBtn.setStyle("-fx-background-color: white");
            originalColorBtn.setStyle("-fx-background-color: white");
        });
        cannyBtn.setOnMouseClicked(event->{
            strongWeakBtn.setStyle("-fx-background-color: white");
            cannyBtn.setStyle("-fx-background-color: lightgray");
            originalColorBtn.setStyle("-fx-background-color: white");
        });
        originalColorBtn.setOnMouseClicked(event->{
            strongWeakBtn.setStyle("-fx-background-color: white");
            cannyBtn.setStyle("-fx-background-color: white");
            originalColorBtn.setStyle("-fx-background-color: lightgray");
        });

        kernel33Btn.setOnMouseClicked(event->{
            kernel33Btn.setStyle("-fx-background-color: lightgray");
            kernel55Btn.setStyle("-fx-background-color: white");
        });
        kernel55Btn.setOnMouseClicked(event->{
            kernel33Btn.setStyle("-fx-background-color: white");
            kernel55Btn.setStyle("-fx-background-color: lightgray");
        });

    }

    public void edgeDetectBackToMainMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("MainMenu.fxml"));
            Launcher.primaryStage.setTitle("Advance Programming Project 1");
            Launcher.primaryStage.setX(400);
            Launcher.primaryStage.setY(150);
            Launcher.primaryStage.setResizable(false);
            Launcher.primaryScene = new Scene(fxmlLoader.load());
            Launcher.primaryStage.setScene(Launcher.primaryScene);
            Launcher.primaryStage.show();
        } catch (IOException e) {
            MainMenuFunctions.showAlertBox("Error in loading Main Menu.", Alert.AlertType.ERROR);
        }
    }

    public void edgeDetectBtnAction() {
        if(!edgeDetectedBufferedImages.isEmpty()) { edgeDetectedBufferedImages.clear(); }
        if(!edgeDetectPreviewImageViewList.isEmpty()) { edgeDetectPreviewImageViewList.clear(); }

        //for progressbar
        progressBars.clear();
        edgeDetectProgressVBox.getChildren().clear();

        for(int i=0; i<edgeDetectImageGroupsList.size(); i++){
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

            edgeDetectProgressVBox.getChildren().add(hbox);
        }

        edgeDetectedBufferedImages=new ArrayList<>(Collections.nCopies(edgeDetectImageGroupsList.size(), null));
        edgeDetectPreviewImageViewList=new ArrayList<>(Collections.nCopies(edgeDetectImageGroupsList.size(), null));

        CountDownLatch countdown=new CountDownLatch(edgeDetectImageGroupsList.size());
        int numberOfThread = Math.min(5, edgeDetectImageGroupsList.size());

        try{
            ExecutorService executorService= Executors.newFixedThreadPool(numberOfThread);
            switch(edgeDetectAlgorithm) {
                case 'c':
                    System.out.println("c");
                    for(int i = 0; i < edgeDetectImageGroupsList.size(); i++) {
                        CannyTask cannyTask=new CannyTask(bufferedImagesToDetect.get(i), i, edgeDetectConfig, countdown);
                        progressBars.get(i).progressProperty().bind(cannyTask.progressProperty());
                        executorService.submit(cannyTask);
                    }
                    break;
                case 'l':
                    System.out.println("l");
                    for(int i = 0; i < edgeDetectImageGroupsList.size(); i++) {
                        LaplacianTask laplacianTask=new LaplacianTask(bufferedImagesToDetect.get(i), i, edgeDetectConfig, countdown);
                        progressBars.get(i).progressProperty().bind(laplacianTask.progressProperty());
                        executorService.submit(laplacianTask);
                    }
                    break;

                case 's':
                    thresholdValue=(int) slider.getValue();
                    System.out.println("s");
                    for (int i = 0; i < edgeDetectImageGroupsList.size(); i++) {
                        SobelTask sobelTask=new SobelTask(bufferedImagesToDetect.get(i), i, thresholdValue, countdown);
                        progressBars.get(i).progressProperty().bind(sobelTask.progressProperty());
                        executorService.submit(sobelTask);
                    }
                    break;
                default:
                    System.out.println("default with c");
                    for(int i = 0; i < edgeDetectImageGroupsList.size(); i++) {
                        CannyTask cannyTask=new CannyTask(bufferedImagesToDetect.get(i), i, edgeDetectConfig, countdown);
                        progressBars.get(i).progressProperty().bind(cannyTask.progressProperty());
                        executorService.submit(cannyTask);
                    }
                    break;
            }
            executorService.submit(()->{
                try {
                    System.out.println("above await: "+countdown.getCount());
                    countdown.await();
                    System.out.println("below await: "+countdown.getCount());
                    Platform.runLater(()->{
                        try {
                            if(edgeDetectPreviewImageViewList.get(pageNumber)==null){
                                throw new NullPointerException();
                            }
                            edgeDetectPreviewImgGroup.getChildren().clear();
                            edgeDetectPreviewImgGroup.getChildren().addAll(edgeDetectPreviewImageViewList.get(pageNumber));
                            System.out.println("done inside runlater");
                        } catch (NullPointerException e){
                            System.out.println("im here");
                            edgeDetectPreviewImgGroup.getChildren().clear();
                            edgeDetectPreviewImgGroup.getChildren().addAll(new EdgeDetectPreviewImageView(bufferedImagesToDetect.get(pageNumber)));
                            MainMenuFunctions.showAlertBox("There is an error in detecting edge.", Alert.AlertType.ERROR);
                        } finally {
                            edgeDetectSaveBtn.setDisable(false);
                            System.out.println("done");
                        }
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            executorService.shutdown();
        }catch (RasterFormatException e){
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void edgeDetectSaveBtnAction() {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Folder to Save");

            File defaultFolderToSave=new File(System.getProperty("user.home")+File.separator+"EdgeDetectedImage");
            if(!defaultFolderToSave.exists()){
                defaultFolderToSave.mkdir();
            }
            directoryChooser.setInitialDirectory(defaultFolderToSave);

            File fileDir= directoryChooser.showDialog(Launcher.primaryStage);
            System.out.println(directoryChooser);
            if(fileDir==null){
                throw new NullPointerException();
            }

            for(int i =0; i<edgeDetectedBufferedImages.size(); i++){
                String orgFileName=Launcher.unzippedFileToProcess.get(i).getName();
                String extension = orgFileName.substring(orgFileName.lastIndexOf(".") + 1); //get extension format to save as
                String fileName="edgeDetected_"+orgFileName;
                File outputFile=new File(fileDir, fileName);

                BufferedImage edgeDetectedImage=edgeDetectedBufferedImages.get(i);
                EdgeDetectFunctions.saveImg(edgeDetectedImage, outputFile, extension);
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

    public void edgeDetectNextBtnAction() {
        if(pageNumber < edgeDetectImageGroupsList.size() - 1) {
            pageNumber++;
        }
        System.out.println("pageNumber: " + pageNumber);
        System.out.println("edgeDetectPreviewImageviewlistsize " + edgeDetectPreviewImageViewList.size());
        if(pageNumber < edgeDetectImageGroupsList.size()) {
            System.out.println(pageNumber);
            edgeDetectGroup.getChildren().clear();
            edgeDetectGroup.getChildren().add(edgeDetectImageGroupsList.get(pageNumber));
        }
        if(!edgeDetectPreviewImageViewList.isEmpty() && edgeDetectPreviewImageViewList.get(pageNumber)!=null) {
            edgeDetectPreviewImgGroup.getChildren().clear();
            edgeDetectPreviewImgGroup.getChildren().addAll(edgeDetectPreviewImageViewList.get(pageNumber));
        }

        edgeDetectPrevBtn.setDisable(pageNumber == 0);
        edgeDetectNextBtn.setDisable(pageNumber + 1 == Launcher.imageViewsToProcess.size());
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
        if(!edgeDetectPreviewImageViewList.isEmpty() && edgeDetectPreviewImageViewList.get(pageNumber)!=null) {
            edgeDetectPreviewImgGroup.getChildren().clear();
            edgeDetectPreviewImgGroup.getChildren().addAll(edgeDetectPreviewImageViewList.get(pageNumber));
        }

        edgeDetectPrevBtn.setDisable(pageNumber == 0);
        edgeDetectNextBtn.setDisable(pageNumber + 1 == Launcher.imageViewsToProcess.size());
    }

    public void setCannyEdgeDetectBtnAction() {
        edgeDetectAlgorithm='c';
        settingBox.getChildren().clear();
        settingBox.getChildren().addAll(strongWeakBtn, cannyBtn, originalColorBtn);

    }

    public void setLaplacianEdgeDetectBtnAction() {
        edgeDetectAlgorithm='l';
        settingBox.getChildren().clear();
        settingBox.getChildren().addAll(kernel33Btn, kernel55Btn);
    }

    public void setSobelEdgeDetectBtnAction() {
        edgeDetectAlgorithm='s';
        settingBox.getChildren().clear();
        settingBox.getChildren().addAll(slider, label);
    }



}
