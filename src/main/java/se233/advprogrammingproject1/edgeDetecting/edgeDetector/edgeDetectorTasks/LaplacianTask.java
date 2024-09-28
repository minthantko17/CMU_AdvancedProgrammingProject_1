package se233.advprogrammingproject1.edgeDetecting.edgeDetector.edgeDetectorTasks;

import javafx.concurrent.Task;
import se233.advprogrammingproject1.controllers.EdgeDetectController;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.EdgeDetectPreviewImageView;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.detectors.LaplacianEdgeDetector;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.grayscale.Grayscale;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.util.Threshold;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

public class LaplacianTask extends Task<BufferedImage> {
    private final BufferedImage originalImage;
    private final int index;
    private int config;
    private CountDownLatch doneSignal;
    private LaplacianEdgeDetector led;


    public LaplacianTask(BufferedImage bufferedImage, int index, int config, CountDownLatch doneSignal) {

        BufferedImage tempBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.OPAQUE);
        Graphics2D graphics = tempBufferedImage.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, null);

        this.originalImage = tempBufferedImage;
        this.index = index;
        this.config = config;
        this.doneSignal = doneSignal;
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
            switch (config){
                case 0:
                    System.out.println("0");
                    double[][] kernel_3x3 = {{-1, -1, -1},
                            {-1, 8, -1},
                            {-1, -1, -1}};
                    led = new LaplacianEdgeDetector(pixels, kernel_3x3 );
                    break;
                case 1:
                    System.out.println("1");
                    double[][] kernel_5x5 = { { -1, -1, -1, -1, -1, },
                            { -1, -1, -1, -1, -1, },
                            { -1, -1, 24, -1, -1, },
                            { -1, -1, -1, -1, -1, },
                            { -1, -1, -1, -1, -1  } };
                    led = new LaplacianEdgeDetector(pixels, kernel_5x5 );
                    break;
                default:
                    System.out.println("default");
                    double[][] kernel_3x3Default = {{-1, -1, -1},
                            {-1, 8, -1},
                            {-1, -1, -1}};
                    led = new LaplacianEdgeDetector(pixels, kernel_3x3Default );
            }
            boolean[][] edges = led.getEdges();
            BufferedImage laplaceImage = Threshold.applyThresholdReversed(edges);
            return laplaceImage;
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
