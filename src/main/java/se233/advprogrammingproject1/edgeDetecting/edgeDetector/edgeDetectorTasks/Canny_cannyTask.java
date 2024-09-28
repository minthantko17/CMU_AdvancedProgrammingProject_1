package se233.advprogrammingproject1.edgeDetecting.edgeDetector.edgeDetectorTasks;

import javafx.concurrent.Task;
import se233.advprogrammingproject1.controllers.EdgeDetectController;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.EdgeDetectPreviewImageView;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.detectors.CannyEdgeDetector;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.grayscale.Grayscale;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.util.Threshold;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.concurrent.CountDownLatch;

public class Canny_cannyTask extends Task<BufferedImage> {
    private final int[][] pixels;
//    private final BufferedImage bufferedImage;
    private final int index;
    private CountDownLatch doneSignal;
    public Canny_cannyTask(int[][] pixels, int index, CountDownLatch countdown){
        this.pixels = pixels;
//        this.bufferedImage = bufferedImage;
        this.index = index;
        this.doneSignal = countdown;
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
//            int[][] pixels= Grayscale.imgToGrayPixels(bufferedImage);
            CannyEdgeDetector canny = new CannyEdgeDetector.Builder(pixels)
                    .minEdgeSize(10)
                    .thresholds(15, 35)
                    .L1norm(false)
                    .build();
            boolean[][] edges = canny.getEdges();
            BufferedImage cannyImage = Threshold.applyThresholdReversed(edges);
            return cannyImage;

        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } catch(RasterFormatException e){
            System.err.println("in rasterformat exception");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        finally {
            doneSignal.countDown();
        }
        return null;
    }

    @Override
    protected void succeeded() {
        System.out.println("inside succeeded");
        BufferedImage edgeDetectedBufferImage = getValue();
        EdgeDetectController.edgeDetectedBufferedImages.set(index, edgeDetectedBufferImage);
        EdgeDetectController.edgeDetectPreviewImageViewList.set(index, new EdgeDetectPreviewImageView(edgeDetectedBufferImage));
        System.out.println("done succeeded");
    }
}
