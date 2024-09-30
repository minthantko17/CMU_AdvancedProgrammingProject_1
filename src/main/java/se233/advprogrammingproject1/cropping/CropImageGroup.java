package se233.advprogrammingproject1.cropping;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class CropImageGroup extends Group {
    private final ImageView imageView;
    RectangleBoxGroup rectangleBox;

    public CropImageGroup(Image image) {
        this.prefWidth(660);
        this.prefHeight(660);

        imageView = new ImageView();
        imageView.setImage(image);
        imageView.setSmooth(true);

        // Calculate scaling and centering
        double xScale = 660.0 / image.getWidth();
        double yScale = 660.0 / image.getHeight();
        double scale = Math.min(xScale, yScale); // Keep aspect ratio intact

        imageView.setFitWidth(image.getWidth() * scale);
        imageView.setFitHeight(image.getHeight() * scale);

        // Center the image
        if(xScale < yScale){
            imageView.setLayoutY((660 - imageView.getFitHeight()) / 2);     // Horizontally center if image has fitWidth
        }else{
            imageView.setLayoutX((660 - imageView.getFitWidth()) / 2);      // Vertically center if image has fitHeight
        }

//        rectangleBox=new RectangleBoxGroup(imageView,imageView.getLayoutX()+7,imageView.getLayoutY()+7, imageView.getFitWidth()-14,imageView.getFitHeight()-14);
        rectangleBox=new RectangleBoxGroup(imageView,imageView.getLayoutX(),imageView.getLayoutY(),
                50,50);

        this.getChildren().addAll(imageView, rectangleBox);
    }


    public ImageView getImageView() {
        return imageView;
    }

    public RectangleBoxGroup getRectangleBox() {
        return rectangleBox;
    }
}

