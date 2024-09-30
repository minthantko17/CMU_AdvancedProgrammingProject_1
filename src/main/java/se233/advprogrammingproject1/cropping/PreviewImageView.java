package se233.advprogrammingproject1.cropping;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PreviewImageView extends ImageView {
    public PreviewImageView(BufferedImage image){
        this.prefWidth(320);
        this.prefHeight(320);


        BufferedImage bufferedImage=new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.OPAQUE);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);

        Image prevImage= SwingFXUtils.toFXImage(bufferedImage, null);
        this.setImage(prevImage);
        this.setSmooth(true);

        double xScale=320/prevImage.getWidth();
        double yScale=320/prevImage.getHeight();
        double scale=Math.min(xScale,yScale);

        this.setFitWidth(prevImage.getWidth()*scale);
        this.setFitHeight(prevImage.getHeight()*scale);

        if(xScale < yScale){
            this.setLayoutY((320 - this.getFitHeight()) / 2);     // Horizontally center if image has fitWidth
        }else{
            this.setLayoutX((320 - this.getFitWidth()) / 2);      // Vertically center if image has fitHeight
        }

    }
}
