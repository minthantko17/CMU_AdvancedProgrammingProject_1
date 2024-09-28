/**************************************************************************
 * @author Jason Altschuler
 *
 * @tags edge detection, image analysis, computer vision, AI, machine learning
 *
 * PURPOSE: Edge detector
 *
 * ALGORITHM: Prewitt edge detector algorithm
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


public class PrewittEdgeDetector extends GaussianEdgeDetector {
   
   /*********************************************************************
    * Convolution kernels
    *********************************************************************/

   private final static double[][] X_kernel = {{-1, 0, 1},
                                               {-1, 0, 1},
                                               {-1, 0, 1}};

   private final static double[][] Y_kernel = {{1, 1, 1},
                                               {0, 0, 0},
                                               {-1, -1, -1}};

   /*********************************************************************
    * Implemented abstract methods
    *********************************************************************/

   /**
    * @Override
    * {{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}}
    */
   public double[][] getXkernel() {
      return PrewittEdgeDetector.X_kernel;
   }
   
   /**
    * @Override
    * {{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}}
    */
   public double[][] getYkernel() {
      return PrewittEdgeDetector.Y_kernel;
   }

   
   /*********************************************************************
    * Constructor 
    **********************************************************************/
   
   /**
    * All work is done in constructor.
    * @param filePath path to image
    */
   public PrewittEdgeDetector(String filePath, int threshold) {
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
    * @param imageld
    */
   public PrewittEdgeDetector(int[][] image, int threshold) {
      findEdges(image, false, threshold);
   }
   
   /**
    * All work is done in constructor. 
    * <P> Gives option to use L1 or L2 norm.
    */
   public PrewittEdgeDetector(int[][] image, boolean L1norm, int threshold) {
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
      String img = "src/main/resources/se233/advprogrammingproject1/assets/TomAndJerry.jpg";
      BufferedImage originalImage = ImageIO.read(new File(img));
      int[][] pixels = Grayscale.imgToGrayPixels(originalImage);

      int threshold=30;
      // run SobelEdgeDetector
      final long startTime = System.currentTimeMillis();
      PrewittEdgeDetector sed = new PrewittEdgeDetector(pixels, threshold);
      final long endTime = System.currentTimeMillis();

      // print timing information
      final double elapsed = (double) (endTime - startTime) / 1000;
      System.out.println("Prewitt Edge Detector took " + elapsed + " seconds.");
      System.out.println("Threshold = " + sed.threshold);

      // display edges
      boolean[][] edges = sed.getEdges();
      BufferedImage edges_image = Threshold.applyThresholdReversed(edges);
      BufferedImage[] toShow = {originalImage, edges_image};
      String title = "Prewitt Edge Detector by Jason Altschuler";
      ImageViewer.showImages(toShow, title);
   }

}
