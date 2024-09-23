package se233.advprogrammingproject1.cropping;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import se233.advprogrammingproject1.controllers.CropController;
import se233.advprogrammingproject1.functions.CropFunctions;

import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

public class CropTask extends Task<BufferedImage> {
    private final ImageView imageView;
    private final Rectangle rectangle;
    private final Group previewImgGroup;
    private final ProgressBar progressBar;
    private final int index;
    private final int pageNumber;
    private CountDownLatch doneSignal;

    public CropTask(ImageView imageView, Rectangle rectangle, Group imageViewGroup,
                    ProgressBar progressBar, int index, int pageNumber, CountDownLatch doneSignal) {
        this.imageView = imageView;
        this.rectangle = rectangle;
        this.previewImgGroup = imageViewGroup;
        this.progressBar = progressBar;
        this.index = index;
        this.pageNumber = pageNumber;
        this.doneSignal = doneSignal;
    }

    @Override
    protected BufferedImage call() throws Exception {
        System.out.println("In thread: " + Thread.currentThread().getName());
        for (int i = 1; i <= 10; i++) {
            System.out.println("i: "+i);
            Thread.sleep(500);
            updateProgress(i, 10);
//            progressBar.setProgress(i/10.0);
        }
        return CropFunctions.crop(imageView, rectangle);
    }

    @Override
    protected void succeeded() {
        System.out.println("Finished: " + Thread.currentThread().getName());
        BufferedImage croppedBufferedImage = getValue();
        CropController.croppedBufferedImages.set(index,croppedBufferedImage);
        CropController.previewImageViewList.set(index, new PreviewImageView(croppedBufferedImage));
        doneSignal.countDown();
//        Platform.runLater(()->{
//            previewImgGroup.getChildren().clear();
//            previewImgGroup.getChildren().addAll(CropController.previewImageViewList.get(pageNumber));
//        });
    }
}
