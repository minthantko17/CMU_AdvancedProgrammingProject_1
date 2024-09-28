package se233.advprogrammingproject1.functions;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import se233.advprogrammingproject1.controllers.CropController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EdgeDetectFunctions {
    /*public static BufferedImage edgeDetect(ImageView imageView, Rectangle recBounds) {

    }*/

    public static void saveImg(BufferedImage croppedImage, File fileName, String extension){
        try{
            ImageIO.write(croppedImage, extension, fileName);
            System.out.println("Image saved to " + fileName.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fitRecToSmallerImageViewSide(ImageView imageView, Rectangle rectangle){
        if(imageView.getFitWidth()<imageView.getFitHeight()){
            if(rectangle.getWidth()<=rectangle.getHeight()
                    && (imageView.getFitHeight() < (imageView.getFitWidth() * (CropController.ratioHeight / CropController.ratioWidth)))){
                System.out.println("m1");
                rectangle.setHeight(imageView.getFitHeight());
                rectangle.setWidth(rectangle.getHeight()*(CropController.ratioWidth / CropController.ratioHeight));
            }else{
                System.out.println("m2");
                rectangle.setWidth(imageView.getFitWidth());
                rectangle.setHeight(rectangle.getWidth() * (CropController.ratioHeight / CropController.ratioWidth));
            }
        }else{
            if(rectangle.getWidth()>=rectangle.getHeight()
                    && imageView.getFitWidth() < (imageView.getFitHeight() * (CropController.ratioWidth / CropController.ratioHeight))){
                System.out.println("in n1");
                rectangle.setWidth(imageView.getFitWidth());
                rectangle.setHeight(rectangle.getWidth() * (CropController.ratioHeight / CropController.ratioWidth));
            }else{
                System.out.println("in n2");
                rectangle.setHeight(imageView.getFitHeight());
                rectangle.setWidth(rectangle.getHeight()*(CropController.ratioWidth / CropController.ratioHeight));
            }
        }



        /*public static void updateEdgeDetector() {

        }*/
    }



}
