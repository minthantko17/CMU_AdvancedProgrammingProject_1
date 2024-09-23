package se233.advprogrammingproject1.cropping;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import se233.advprogrammingproject1.controllers.CropController;
import se233.advprogrammingproject1.functions.CropFunctions;

import java.awt.image.BufferedImage;

public class CropTask extends Task<BufferedImage> {
    private final ImageView imageView;
    private final Rectangle rectangle;
    private final Group previewImgGroup;
    private final int index;
    private final int pageNumber;

    public CropTask(ImageView imageView, Rectangle rectangle, Group imageViewGroup, int index, int pageNumber) {
        this.imageView = imageView;
        this.rectangle = rectangle;
        this.previewImgGroup = imageViewGroup;
        this.index = index;
        this.pageNumber = pageNumber;
    }

    @Override
    protected BufferedImage call() throws Exception {
        System.out.println("In thread: " + Thread.currentThread().getName());
        return CropFunctions.crop(imageView, rectangle);
    }

    @Override
    protected void succeeded() {
        System.out.println("Finished: " + Thread.currentThread().getName());
        BufferedImage croppedBufferedImage = getValue();
        CropController.croppedBufferedImages.set(index,croppedBufferedImage);
        CropController.previewImageViewList.set(index, new PreviewImageView(croppedBufferedImage));
        Platform.runLater(()->{
            previewImgGroup.getChildren().clear();
            previewImgGroup.getChildren().addAll(CropController.previewImageViewList.get(pageNumber));
        });
    }
}
