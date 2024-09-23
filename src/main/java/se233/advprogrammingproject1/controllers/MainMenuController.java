package se233.advprogrammingproject1.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import se233.advprogrammingproject1.Launcher;
import se233.advprogrammingproject1.functions.CropFunctions;
import se233.advprogrammingproject1.functions.MainMenuFunctions;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainMenuController {
    @FXML
    Button toCropPageBtn;
    @FXML
    Button chooseFileBtn;

    public void redirectToCrop() throws IOException {
        if(Launcher.filePath!=null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("CropScene.fxml"));
            Launcher.primaryScene = new Scene(fxmlLoader.load());
            Launcher.primaryStage.setScene(Launcher.primaryScene);
            Launcher.primaryStage.show();
            CropController.isAspectRatio=false;
        }
    }

    public void chooseFile(){
        Launcher.filePath = MainMenuFunctions.openFile();
//        System.out.println(Launcher.filePath.get(0));
        if(Launcher.filePath!=null){
            Launcher.unzippedFileToProcess = MainMenuFunctions.unzipAndGetFile(Launcher.filePath, "temp");
            Launcher.imageViewsToProcess = MainMenuFunctions.loadImageViewsToProcess(Launcher.unzippedFileToProcess);
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
