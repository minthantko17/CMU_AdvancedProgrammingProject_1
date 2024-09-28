package se233.advprogrammingproject1.edgeDetecting.edgeDetector.edgeDetectorTasks;

import javafx.concurrent.Task;
import se233.advprogrammingproject1.controllers.EdgeDetectController;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.EdgeDetectPreviewImageView;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.detectors.CannyEdgeDetector;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.grayscale.Grayscale;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.util.Threshold;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.concurrent.CountDownLatch;

public class CannyTask extends Task<BufferedImage> {
//    private final int[][] pixels;
    private final BufferedImage originalImage;
    private final int index;
    private CountDownLatch doneSignal;
    private int config;
    public CannyTask(BufferedImage bufferedImage, int index, int config, CountDownLatch countdown){

        BufferedImage tempBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.OPAQUE);
        Graphics2D graphics = tempBufferedImage.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, null);

        this.originalImage = tempBufferedImage;
        this.index = index;
        this.doneSignal = countdown;
        this.config = config;
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
            int[][] pixels= Grayscale.imgToGrayPixels(originalImage);
            CannyEdgeDetector canny = new CannyEdgeDetector.Builder(pixels)
                    .minEdgeSize(10)
                    .thresholds(15, 35)
                    .L1norm(false)
                    .build();

            boolean[][] edges = canny.getEdges();
            boolean[][] weakEdges = canny.getWeakEdges();
            boolean[][] strongEdges = canny.getStrongEdges();

            switch (config){
                case 0:
                    BufferedImage strongweakImage = Threshold.applyThresholdWeakStrongCanny(weakEdges, strongEdges);
                    return strongweakImage;
                case 1:
                    BufferedImage cannyImage = Threshold.applyThresholdReversed(edges);
                    return cannyImage;
                case 2:
                    System.out.println("inside case 2");
                    BufferedImage edgesOriginalColor = Threshold.applyThresholdOriginal(edges, originalImage);
                    System.out.println("done in case 2");
                    return edgesOriginalColor;
                default:
                    System.out.println("inside default");
                    BufferedImage strongweakImageAsDefault = Threshold.applyThresholdWeakStrongCanny(weakEdges, strongEdges);
                    return strongweakImageAsDefault;
            }
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
        System.out.println("prevS: "+EdgeDetectController.edgeDetectPreviewImageViewList.size());
        EdgeDetectController.edgeDetectPreviewImageViewList.set(index, new EdgeDetectPreviewImageView(edgeDetectedBufferImage));
        System.out.println("done succeeded");
    }
}

