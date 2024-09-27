package se233.advprogrammingproject1.cropping;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import se233.advprogrammingproject1.controllers.CropController;
import se233.advprogrammingproject1.functions.CropFunctions;
import se233.advprogrammingproject1.functions.MainMenuFunctions;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
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
        try {
            for (int i = 1; i <= 10; i++) {
                System.out.println("i: "+i);
                Thread.sleep(200);
                updateProgress(i, 10);
    //            progressBar.setProgress(i/10.0);
            }
            return CropFunctions.crop(imageView, rectangle);
        }catch (RasterFormatException e) {
            System.err.println("Error: Cropping rectangle exceeds image bounds.\nThe Image is not cropped.");
            MainMenuFunctions.showAlertBox("Some Image cannot be cropped.\nPlease check your cropping area", Alert.AlertType.ERROR);
            return CropFunctions.crop(imageView, new Rectangle(imageView.getLayoutX(), imageView.getLayoutY(),
                    imageView.getFitWidth(), imageView.getFitHeight())); //return original image size
        } catch (NullPointerException e) {
            System.err.println("Error: ImageView does not contain an image.");
        }  catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            doneSignal.countDown();
        }
        return  null;
    }

    @Override
    protected void succeeded(){
        try {
            System.out.println("Finished: " + Thread.currentThread().getName());
            BufferedImage croppedBufferedImage = getValue();
            if (croppedBufferedImage == null) {
                throw new NullPointerException("Some Image can't be cropped.\nPlease check your cropping area.");
            }
            CropController.croppedBufferedImages.set(index,croppedBufferedImage);
            CropController.previewImageViewList.set(index, new PreviewImageView(croppedBufferedImage));
        } catch (NullPointerException e){
            CropController.croppedBufferedImages.set(index, SwingFXUtils.fromFXImage(imageView.getImage(), null));
            CropController.previewImageViewList.set(index, new PreviewImageView(SwingFXUtils.fromFXImage(imageView.getImage(), null)));
            MainMenuFunctions.showAlertBox(e.getMessage(), Alert.AlertType.ERROR);
        }
//        Platform.runLater(()->{
//            previewImgGroup.getChildren().clear();
//            previewImgGroup.getChildren().addAll(CropController.previewImageViewList.get(pageNumber));
//        });
    }
}
