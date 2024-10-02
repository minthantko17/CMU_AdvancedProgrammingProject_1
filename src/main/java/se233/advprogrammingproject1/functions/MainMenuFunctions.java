package se233.advprogrammingproject1.functions;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import se233.advprogrammingproject1.Launcher;
import se233.advprogrammingproject1.exceptions.UnsupportedFormatException;

import java.io.*;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainMenuFunctions {
    // for main menu
    public static List<File> openFile() throws IllegalFormatException {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Select Image or ImageZip");

        //filter image and zip file to choose
        FileChooser.ExtensionFilter extensionFilter= new FileChooser.ExtensionFilter("Image or Zip", "*.zip", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().addAll(extensionFilter);

        List<File> file=fileChooser.showOpenMultipleDialog(Launcher.primaryStage); //return null when cancel btn is clicked
        if (file==null){
            throw new NullPointerException("No File selected 😞");
        }
        return  file;
    }

    public static List<File> unzipAndGetFile(List<String> filePath, String targetPath) throws IOException, IndexOutOfBoundsException, UnsupportedFormatException {
        List<File> imageFiles=new ArrayList<>();
        File dir=new File(targetPath);

        List<File> inputFile=new ArrayList<>();
        for(int i=0; i<filePath.size(); i++){
            inputFile.add(new File(filePath.get(i)));

            if(checkImage(inputFile.get(i))){
                imageFiles.add(inputFile.get(i));
            } else if (checkZip(inputFile.get(i))) {
                unzip(imageFiles, filePath.get(i), targetPath);
            }else{
                System.out.println("Unsupported File");
                throw new UnsupportedFormatException();
            }
        }
        System.out.println("imagefile size: "+imageFiles.size());
        return imageFiles;
    }

    public static List<ImageView> loadImageViewsToProcess(List<File> imageFiles) throws NullPointerException{
        List<ImageView> imageViews=new ArrayList<>();
        for(File i : imageFiles){
            Image image= new Image(i.toURI().toString());
            imageViews.add(new ImageView(image));
        }
        return imageViews;
    }


    //---------Helper Methods----------
    public static boolean checkImage(File file){
        String[] imageExtensions = new String[] { "png", "jpg", "jpeg", "bmp", "gif" };
        String fileName = file.getName().toLowerCase();
        for (String ext : imageExtensions) {
            if (fileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkZip(File file){
        String zipExtension = "zip";
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(zipExtension);
    }

    public static void showAlertBox(String message, Alert.AlertType alertType){
        Alert alert=new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void unzip(List<File> imageFiles, String filePathInput, String targetPath){
        boolean unsupportIncluded=false;
        try(ZipInputStream zipInputStream=new ZipInputStream(new FileInputStream(filePathInput))){
            ZipEntry zipEntry=zipInputStream.getNextEntry();
            byte[] buffer = new byte[1024];

            while (zipEntry != null) {
                String fileName=targetPath + File.separator + zipEntry.getName();
                File newFile=new File(fileName);

                if(zipEntry.isDirectory()){
                    newFile.mkdirs();
                }else{
                    new File(newFile.getParent()).mkdirs();
                    try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(newFile))) {
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }

                    if(checkImage(newFile)){
                        imageFiles.add(newFile);
                    }else{
                        unsupportIncluded=true;
                    }
                }
                zipInputStream.closeEntry();
                zipEntry=zipInputStream.getNextEntry();
            }
            System.out.println("imagefile size insize method: "+imageFiles.size());
            if(unsupportIncluded){
                throw new UnsupportedFormatException();
            }
        } catch (UnsupportedFormatException e) {
//            System.err.println("Unsupported File included");
            showAlertBox("Unsupported File(s) included in .zip folders are skipped.", Alert.AlertType.INFORMATION);
        }catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Input Output Exception");
        }
    }
}
