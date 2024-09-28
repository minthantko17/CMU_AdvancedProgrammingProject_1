package se233.advprogrammingproject1.edgeDetecting.edgeDetector.edgeDetectorTasks;

import javafx.concurrent.Task;
import se233.advprogrammingproject1.controllers.EdgeDetectController;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.EdgeDetectPreviewImageView;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.detectors.SobelEdgeDetector;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.grayscale.Grayscale;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.util.Threshold;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

public class SobelTask extends Task<BufferedImage> {
    private final BufferedImage originalImage;
    private final int index;
    private CountDownLatch doneSignal;
    private int threshold;
    public SobelTask(BufferedImage bufferedImage, int index, int threshold, CountDownLatch countdown){

        BufferedImage tempBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.OPAQUE);
        Graphics2D graphics = tempBufferedImage.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, null);

        this.originalImage = bufferedImage;
        this.index = index;
        this.doneSignal = countdown;
        this.threshold = threshold;
    }
    @Override
    protected BufferedImage call() throws Exception {
        try {
            System.out.println("in thread: "+Thread.currentThread().getName());
            for (int i = 1; i <= 10; i++) {
                System.out.println("i: "+i);
                Thread.sleep(100);
                updateProgress(i, 10);
            }
            int[][] pixels = Grayscale.imgToGrayPixels(originalImage);
            SobelEdgeDetector sed = new SobelEdgeDetector(pixels, threshold);
            boolean[][] edges = sed.getEdges();
            BufferedImage sobelImage = Threshold.applyThresholdReversed(edges);

            return sobelImage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            doneSignal.countDown();
        }
    }

    @Override
    protected void succeeded() {
        System.out.println("inside succeeded");
        BufferedImage edgeDetectedBufferImage = getValue();
        EdgeDetectController.edgeDetectedBufferedImages.set(index, edgeDetectedBufferImage);
        System.out.println("prevS: "+EdgeDetectController.edgeDetectPreviewImageViewList.size());
        EdgeDetectController.edgeDetectPreviewImageViewList.set(index, new EdgeDetectPreviewImageView(edgeDetectedBufferImage));
        System.out.println("done succeeded");
    }
}
