package se233.advprogrammingproject1.functions;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import se233.advprogrammingproject1.controllers.CropController;
import se233.advprogrammingproject1.cropping.RectangleBoxGroup;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.*;

public class CropFunctions {

    public static BufferedImage crop(ImageView imageView, Rectangle recBounds)
            throws NullPointerException, RasterFormatException, IllegalArgumentException {

        double scaleX = imageView.getImage().getWidth() / imageView.getFitWidth();
        double scaleY = imageView.getImage().getHeight() / imageView.getFitHeight();

        // Map the rectangle coordinates back to the original image size
        double originalX = (recBounds.getX()-imageView.getLayoutX()) * scaleX;
        double originalY = (recBounds.getY()-imageView.getLayoutY()) * scaleY;
        double originalWidth = recBounds.getWidth() * scaleX;
        double originalHeight = recBounds.getHeight() * scaleY;

        // Load original image
        BufferedImage originalImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);

        // Crop original image
        BufferedImage croppedImage = originalImage.getSubimage(
                (int) originalX,
                (int) originalY,
                (int) originalWidth,
                (int) originalHeight
        );
        return croppedImage;
    }

    public static void saveImg(BufferedImage croppedImage, File fileName, String extension) throws IOException {
        ImageIO.write(croppedImage, extension, fileName);
        System.out.println("Image saved to " + fileName.getAbsolutePath());
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
    }

    //update circle position according to rectangle
    public static void updateHandles(RectangleBoxGroup rectangleBoxGroup, Rectangle rectangle){
        rectangleBoxGroup.getTopLeftHandle().setCenterX(rectangle.getX());
        rectangleBoxGroup.getTopLeftHandle().setCenterY(rectangle.getY());
        rectangleBoxGroup.getTopRightHandle().setCenterX(rectangle.getX() + rectangle.getWidth());
        rectangleBoxGroup.getTopRightHandle().setCenterY(rectangle.getY());
        rectangleBoxGroup.getBottomLeftHandle().setCenterX(rectangle.getX());
        rectangleBoxGroup.getBottomLeftHandle().setCenterY(rectangle.getY() + rectangle.getHeight());
        rectangleBoxGroup.getBottomRightHandle().setCenterX(rectangle.getX() + rectangle.getWidth());
        rectangleBoxGroup.getBottomRightHandle().setCenterY(rectangle.getY() + rectangle.getHeight());
        rectangleBoxGroup.getTopHandle().setCenterX(rectangle.getX() + rectangle.getWidth() / 2);
        rectangleBoxGroup.getTopHandle().setCenterY(rectangle.getY());
        rectangleBoxGroup.getBottomHandle().setCenterX(rectangle.getX() + rectangle.getWidth() / 2);
        rectangleBoxGroup.getBottomHandle().setCenterY(rectangle.getY() + rectangle.getHeight());
        rectangleBoxGroup.getLeftHandle().setCenterX(rectangle.getX());
        rectangleBoxGroup.getLeftHandle().setCenterY(rectangle.getY() + rectangle.getHeight() / 2);
        rectangleBoxGroup.getRightHandle().setCenterX(rectangle.getX() + rectangle.getWidth());
        rectangleBoxGroup.getRightHandle().setCenterY(rectangle.getY() + rectangle.getHeight() / 2);
    }

}
