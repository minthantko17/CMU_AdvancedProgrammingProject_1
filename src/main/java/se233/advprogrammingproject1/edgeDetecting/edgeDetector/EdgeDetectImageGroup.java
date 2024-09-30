package se233.advprogrammingproject1.edgeDetecting.edgeDetector;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EdgeDetectImageGroup extends Group {
    private final ImageView edgeDetectImageView;

    public EdgeDetectImageGroup(Image image) {
        this.prefWidth(660);
        this.prefHeight(660);

        edgeDetectImageView = new ImageView();
        edgeDetectImageView.setImage(image);
        edgeDetectImageView.setSmooth(true);

        double xScale = 660.0 / image.getWidth();
        double yScale = 660.0 / image.getHeight();
        double scale = Math.min(xScale, yScale);

        edgeDetectImageView.setFitWidth(image.getWidth() * scale);
        edgeDetectImageView.setFitHeight(image.getHeight() * scale);

        if (xScale < yScale) {
            edgeDetectImageView.setLayoutY((660 - edgeDetectImageView.getFitHeight()) / 2);
        } else {
            edgeDetectImageView.setLayoutX((660 - edgeDetectImageView.getFitWidth()) / 2);
        }

        this.getChildren().addAll(edgeDetectImageView);
    }

    public ImageView getEdgeDetectImageView() {
        return edgeDetectImageView;
    }
}
