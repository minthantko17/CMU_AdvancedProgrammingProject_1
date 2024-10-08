/**************************************************************************
 * @author Jason Altschuler
 *
 * @tags edge detection, image analysis, computer vision, AI, machine learning
 *
 * PURPOSE: Edge detector
 *
 * ALGORITHM: Sobel edge detector algorithm
 *
 * For full documentation, see the README
  ************************************************************************/

package se233.advprogrammingproject1.edgeDetecting.edgeDetector.detectors;

import se233.advprogrammingproject1.edgeDetecting.edgeDetector.grayscale.Grayscale;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import se233.advprogrammingproject1.edgeDetecting.edgeDetector.ui.ImageViewer;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.util.Threshold;


public class SobelEdgeDetector extends GaussianEdgeDetector {
   
   /*********************************************************************
    * Convolution kernels
    *********************************************************************/
   private final static double[][] X_kernel = {{-1, 0, 1},
                                               {-2, 0, 2},
                                               {-1, 0, 1}};

   private final static double[][] Y_kernel = {{1, 2, 1},
                                               {0, 0, 0},
                                               {-1, -2, -1}};

   /*********************************************************************
    * Implemented abstract methods
    *********************************************************************/

   /**
    * @Override
    * {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}}
    */
   public double[][] getXkernel() {
      return SobelEdgeDetector.X_kernel;
   }
   
   /**
    * @Override
    * {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}}
    */
   public double[][] getYkernel() {
      return SobelEdgeDetector.Y_kernel;
   }

   
   /*********************************************************************
    * Constructor 
    **********************************************************************/
   
   /**
    * All work is done in constructor.
    * @param filePath path to image
    */
   public SobelEdgeDetector(String filePath, int threshold) {
      // read image and get pixels
      BufferedImage originalImage;
      try {
         originalImage = ImageIO.read(new File(filePath));
         findEdges(Grayscale.imgToGrayPixels(originalImage), false, threshold);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   /**
    * All work is done in constructor.
    * <P> Uses L2 norm by default.
    * @param image
    */
   public SobelEdgeDetector(int[][] image, int threshold) {
      findEdges(image, false, threshold);
   }
   
   /**
    * All work is done in constructor. 
    * <P> Gives option to use L1 or L2 norm.
    */
   public SobelEdgeDetector(int[][] image, boolean L1norm, int threshold) {
      findEdges(image, L1norm, threshold);
   }


   /*********************************************************************
    * Unit testing
    * @throws IOException 
    *********************************************************************/

   /**
    * Example run. 
    * <P> Displays detected edges next to orignal image.
    * @param args
    * @throws IOException
    */
   public static void main(String[] args) throws IOException {
      // read image and get pixels
//      String img = args[0];
      String img = "src/main/resources/se233/advprogrammingproject1/assets/TomAndJerry2.jpg";
      BufferedImage originalImage = ImageIO.read(new File(img));
      int[][] pixels = Grayscale.imgToGrayPixels(originalImage);
      int threshold=50;

      // run SobelEdgeDetector
      final long startTime = System.currentTimeMillis();
      SobelEdgeDetector sed = new SobelEdgeDetector(pixels, threshold);
      final long endTime = System.currentTimeMillis();

      // print timing information
      final double elapsed = (double) (endTime - startTime) / 1000;
      System.out.println("Sobel Edge Detector took " + elapsed + " seconds.");
      System.out.println("Threshold = " + sed.threshold);

      // display edges
      boolean[][] edges = sed.getEdges();
      BufferedImage edges_image = Threshold.applyThresholdReversed(edges);
      BufferedImage[] toShow = {originalImage, edges_image};
      String title = "Sobel Edge Detector by Jason Altschuler";
      ImageViewer.showImages(toShow, title);
   }

}
