package se233.advprogrammingproject1.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import se233.advprogrammingproject1.Launcher;
import se233.advprogrammingproject1.exceptions.UnsupportedFormatException;
import se233.advprogrammingproject1.functions.MainMenuFunctions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.IllegalFormatException;
import java.util.List;

public class MainMenuController {
    @FXML
    Button toCropPageBtn;
    @FXML
    Button toEdgeDetectPageBtn;
    @FXML
    Button chooseFileBtn;
    @FXML
    Pane dragAreaPane;
    @FXML
    Pane previewFilesPane;
    @FXML
    Button clearAllBtn;
    @FXML
    ListView<String> listView;

    public static ListView<String> tempListView= new ListView<>();

    public void initialize() throws FileNotFoundException {
        if(!Launcher.filePath.isEmpty() && !Launcher.unzippedFileToProcess.isEmpty() && !Launcher.imageViewsToProcess.isEmpty()) {
            System.out.println("tempListView: "+tempListView.getItems().size());
            listView.getItems().addAll(tempListView.getItems());
            previewFilesPane.setVisible(true);
            clearAllBtn.setDisable(false);
        }else{
            previewFilesPane.setVisible(false);
            clearAllBtn.setDisable(true);
        }

    }

    public void redirectToCrop(){
        try {
            if (Launcher.filePath.isEmpty() && Launcher.unzippedFileToProcess.isEmpty() && Launcher.imageViewsToProcess.isEmpty()) {
                throw new FileNotFoundException("No file selected.");
            }
            tempListView = listView;
            System.out.println("tempListView: " + tempListView.getItems().size());
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("CropScene2.fxml"));
            Launcher.primaryStage.setX(150);
            Launcher.primaryStage.setY(10);
            Launcher.primaryScene = new Scene(fxmlLoader.load());
            Launcher.primaryStage.setScene(Launcher.primaryScene);
            Launcher.primaryStage.show();
            CropController.isAspectRatio = false;
        } catch (FileNotFoundException e) {
            MainMenuFunctions.showAlertBox(e.getMessage(), Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            MainMenuFunctions.showAlertBox("There is an error while switching scene.", Alert.AlertType.ERROR);
        }
    }

    public void redirectToEdgeDetect() throws IOException {
        try {
            if (Launcher.filePath.isEmpty() && Launcher.unzippedFileToProcess.isEmpty() && Launcher.imageViewsToProcess.isEmpty()) {
                throw new FileNotFoundException("No file selected.");
            }
            tempListView=listView;
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("EdgeDetectScene2.fxml"));
            Launcher.primaryStage.setX(150);
            Launcher.primaryStage.setY(10);
            Launcher.primaryScene = new Scene(fxmlLoader.load());
            Launcher.primaryStage.setScene(Launcher.primaryScene);
            Launcher.primaryStage.show();
        } catch (FileNotFoundException e) {
            MainMenuFunctions.showAlertBox(e.getMessage(), Alert.AlertType.INFORMATION);
        }catch (IOException e) {
            MainMenuFunctions.showAlertBox("There is an error while switching scene.", Alert.AlertType.ERROR);
        }
    }

    public void dragOverAction(DragEvent event){
        Dragboard db = event.getDragboard();
        boolean isAccepted=false;
        for(File file: db.getFiles()){
            if(MainMenuFunctions.checkImage(file) || MainMenuFunctions.checkZip(file)){
                isAccepted=true;
            }else{
                isAccepted=false;
                break;
            }
        }
        if(db.hasFiles() && isAccepted){
            event.acceptTransferModes(TransferMode.COPY);
        }else{
            event.consume();
        }
    }

    public void dragDropAction(DragEvent event){
        Dragboard db=event.getDragboard();
        boolean success=false;
        if(db.hasFiles()){
            success=true;
            String filePath;
            String fileName;
            for(int i=0; i<db.getFiles().size(); i++){
                File file = db.getFiles().get(i);
                filePath = file.getAbsolutePath();
                fileName = Paths.get(filePath).getFileName().toString();
                Launcher.filePath.add(filePath);
                listView.getItems().add(fileName);
            }
        }
        event.setDropCompleted(success);
        addFilesToProcess();
        event.consume();
    }

    public void clearAllBtnAction(){
        tempListView.getItems().clear();
        listView.getItems().clear();
        Launcher.filePath.clear();
        Launcher.unzippedFileToProcess.clear();
        Launcher.imageViewsToProcess.clear();
        previewFilesPane.setVisible(false);
        clearAllBtn.setDisable(true);
    }

    public void chooseFile(){
//        List<String> tempFilePath = MainMenuFunctions.openFile();
        try {
            List<File> tempFilePath = MainMenuFunctions.openFile();
            System.out.println("in IF of chooseFile");
            String filePath;
            String fileName;
            for (File file : tempFilePath) {
                filePath = file.getAbsolutePath();
                fileName = Paths.get(filePath).getFileName().toString();
                Launcher.filePath.add(filePath);
                listView.getItems().add(fileName);
            }
            addFilesToProcess();
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
        } catch (IllegalFormatException e){
            System.out.println("Illegal format");
        }

    }

    public void addFilesToProcess() {
        try {
            if (Launcher.filePath.isEmpty()) {
                throw new FileNotFoundException("The input file path is empty. Please try again.");
            }
            previewFilesPane.setVisible(true);
            clearAllBtn.setDisable(false);
            Launcher.unzippedFileToProcess = MainMenuFunctions.unzipAndGetFile(Launcher.filePath, "temp");
            Launcher.imageViewsToProcess = MainMenuFunctions.loadImageViewsToProcess(Launcher.unzippedFileToProcess);
        } catch (FileNotFoundException | UnsupportedFormatException e) {
            MainMenuFunctions.showAlertBox(e.getMessage(), Alert.AlertType.ERROR);
            Launcher.filePath.clear();
            Launcher.unzippedFileToProcess.clear();
            Launcher.imageViewsToProcess.clear();
            previewFilesPane.setVisible(false);
            clearAllBtn.setDisable(true);
        } catch(IndexOutOfBoundsException e){
            MainMenuFunctions.showAlertBox("There is an error in loading multiple file.", Alert.AlertType.ERROR);
        } catch(NullPointerException e){
            MainMenuFunctions.showAlertBox("There is an error in processing to ImageView.", Alert.AlertType.ERROR);
        } catch(IOException e){
            MainMenuFunctions.showAlertBox("There is an error in processing Input/Outupt.", Alert.AlertType.ERROR);
        }

    }


    // ----non functional methods----
    public void mousePressToCropBtn(){
        toCropPageBtn.setStyle("-fx-background-color: #7e9e9e");
    }

    public void mouseReleaseToCropBtn(){
        toCropPageBtn.setStyle("-fx-background-color: #a9d5d5");
    }

    public void mousePressToEdgeDetectBtn(){
        toEdgeDetectPageBtn.setStyle("-fx-background-color: #7e9e9e");
    }

    public void mouseReleaseToEdgeDetectBtn(){
        toEdgeDetectPageBtn.setStyle("-fx-background-color: #a9d5d5");
    }


//    inputListView.setOnDragDropped(event->{
//        Dragboard db=event.getDragboard();
//        boolean success=false;
//        if(db.hasFiles()){
//            success=true;
//            String filePath;
//            String fileName;
//            int totalFiles=db.getFiles().size();
//            for(int i=0;i<totalFiles;i++){
//                File file = db.getFiles().get(i);
//                filePath = file.getAbsolutePath();
//                fileName = Paths.get(filePath).getFileName().toString();
//                inputFilePath.add(filePath);
//                inputListView.getItems().add(fileName);
//            }
//        }
//        event.setDropCompleted(success);
//        event.consume();
//    });
}
