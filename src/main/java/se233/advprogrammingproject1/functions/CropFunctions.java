package se233.advprogrammingproject1.functions;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import se233.advprogrammingproject1.Launcher;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CropFunctions {

    public static BufferedImage crop(ImageView imageView, Rectangle recBounds) {
        double scaleX = imageView.getImage().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getImage().getHeight() / imageView.getFitHeight();

        // Map the rectangle coordinates back to the original image size
        double originalX = (recBounds.getX()-imageView.getLayoutX()) * scaleX;
        double originalY = (recBounds.getY()-imageView.getLayoutY()) * scaleY;
        double originalWidth = recBounds.getWidth() * scaleX;
        double originalHeight = recBounds.getHeight() * scaleY;

        // Ensure the original image is loaded
        BufferedImage originalImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);

        // Crop the original image
        BufferedImage croppedImage = originalImage.getSubimage(
                (int) originalX,
                (int) originalY,
                (int) originalWidth,
                (int) originalHeight
        );
        return croppedImage;
    }

    public static void saveImg(BufferedImage croppedImage, File fileName, String extension){
        try{
            ImageIO.write(croppedImage, extension, fileName);
            System.out.println("Image saved to " + fileName.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
