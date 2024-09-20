package se233.advprogrammingproject1.functions;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import se233.advprogrammingproject1.Launcher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainMenuFunctions {
    // for main menu
    public static List<String> openFile(){
        List<String> list = new ArrayList<>();
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Select Image or ImageZip");

        //filter image and zip file to choose
        FileChooser.ExtensionFilter extensionFilter= new FileChooser.ExtensionFilter("Image or Zip", "*.zip", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif");
        fileChooser.getExtensionFilters().addAll(extensionFilter);

        List<File> file=fileChooser.showOpenMultipleDialog(Launcher.primaryStage);
        if (file==null){ return null; }
//        System.out.println(file.toString());

        for(File i: file){
            list.add(i.toString());
        }

        return list;
    }

    public static List<File> unzipAndGetFile(List<String> filePath, String targetPath){
        List<File> imageFiles=new ArrayList<>();
        File dir=new File(targetPath);
//        if(!dir.exists()){ dir.mkdirs(); }

        List<File> inputFile=new ArrayList<>();
        for(int i=0; i<filePath.size(); i++){
            inputFile.add(new File(filePath.get(i)));

            if(checkImage(inputFile.get(i))){
                imageFiles.add(inputFile.get(i));
            } else if (checkZip(inputFile.get(i))) {
                try(ZipInputStream zipInputStream=new ZipInputStream(new FileInputStream(filePath.get(i)))){
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
                            if(checkImage(newFile)) {
                                imageFiles.add(newFile);
                            }
                        }
                        zipInputStream.closeEntry();
                        zipEntry=zipInputStream.getNextEntry();
                    }

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
//        File inputFile=new File(filePath);
        return imageFiles;
    }

    public static List<ImageView> loadImageViewsToProcess(List<File> imageFiles){
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
        if(fileName.endsWith(zipExtension)){
            return true;
        }
        return false;
    }
}
