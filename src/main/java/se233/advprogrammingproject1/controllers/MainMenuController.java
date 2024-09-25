package se233.advprogrammingproject1.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import se233.advprogrammingproject1.Launcher;
import se233.advprogrammingproject1.functions.MainMenuFunctions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class MainMenuController {
    @FXML
    Button toCropPageBtn;
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

    public void redirectToCrop() throws IOException {
        if(!Launcher.filePath.isEmpty() && !Launcher.unzippedFileToProcess.isEmpty() && !Launcher.imageViewsToProcess.isEmpty()) {
            tempListView=listView;
            System.out.println("tempListView: "+tempListView.getItems().size());
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("CropScene.fxml"));
            Launcher.primaryScene = new Scene(fxmlLoader.load());
            Launcher.primaryStage.setScene(Launcher.primaryScene);
            Launcher.primaryStage.show();
            CropController.isAspectRatio=false;
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
        List<File> tempFilePath=MainMenuFunctions.openFile();
        if(tempFilePath!=null){
            System.out.println("in IF of chooseFile");
            String filePath;
            String fileName;
            for(File file: tempFilePath){
                filePath = file.getAbsolutePath();
                fileName = Paths.get(filePath).getFileName().toString();
                Launcher.filePath.add(filePath);
                listView.getItems().add(fileName);
            }
            addFilesToProcess();
        }
    }

    public void addFilesToProcess(){
        if(!Launcher.filePath.isEmpty()){
            previewFilesPane.setVisible(true);
            clearAllBtn.setDisable(false);
            Launcher.unzippedFileToProcess=MainMenuFunctions.unzipAndGetFile(Launcher.filePath, "temp");
            Launcher.imageViewsToProcess=MainMenuFunctions.loadImageViewsToProcess(Launcher.unzippedFileToProcess);
        }
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
