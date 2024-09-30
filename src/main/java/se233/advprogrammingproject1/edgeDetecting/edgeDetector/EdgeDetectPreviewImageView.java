package se233.advprogrammingproject1.edgeDetecting.edgeDetector;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EdgeDetectPreviewImageView extends ImageView {
    public EdgeDetectPreviewImageView(BufferedImage image) {
        this.prefWidth(320);
        this.prefHeight(320);


        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.OPAQUE);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);

        Image edgeDetectPrevImage = SwingFXUtils.toFXImage(bufferedImage, null);
        this.setImage(edgeDetectPrevImage);
        this.setSmooth(true);

        double xScale=320/edgeDetectPrevImage.getWidth();
        double yScale=320/edgeDetectPrevImage.getHeight();
        double scale=Math.min(xScale,yScale);

        this.setFitWidth(edgeDetectPrevImage.getWidth()*scale);
        this.setFitHeight(edgeDetectPrevImage.getHeight()*scale);

        if(xScale < yScale){
            this.setLayoutY((320 - this.getFitHeight()) / 2);     // Horizontally center if image has fitWidth
        }else{
            this.setLayoutX((320 - this.getFitWidth()) / 2);      // Vertically center if image has fitHeight
        }
    }

}
