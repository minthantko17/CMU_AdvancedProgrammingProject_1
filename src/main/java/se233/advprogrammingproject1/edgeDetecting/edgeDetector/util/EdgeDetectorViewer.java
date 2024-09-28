/**************************************************************************
 * @author Jason Altschuler
 * 
 * Displays results of a few edge detectors.
 *************************************************************************/

package se233.advprogrammingproject1.edgeDetecting.edgeDetector.util;

import se233.advprogrammingproject1.edgeDetecting.edgeDetector.detectors.GaussianEdgeDetector;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.detectors.PrewittEdgeDetector;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.detectors.RobertsCrossEdgeDetector;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.detectors.SobelEdgeDetector;
import se233.advprogrammingproject1.edgeDetecting.edgeDetector.grayscale.Grayscale;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import se233.advprogrammingproject1.edgeDetecting.edgeDetector.ui.ImageViewer;

public class EdgeDetectorViewer {
   
   public static void main(String[] args) {
      try {
         test("stani.jpg");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * Displays results from Sobel, Prewitt, and Roberts Cross edge detectors.
    * @param imageFile
    * @throws IOException
    */
   public static void test(String imageFile) throws IOException {
      // read image and get pixels
      BufferedImage originalImage = ImageIO.read(new File(imageFile));
      int[][] pixels = Grayscale.imgToGrayPixels(originalImage);
      int threshold=30;

      // run various Gaussian edge detectors
      GaussianEdgeDetector[] edgeDetectors = new GaussianEdgeDetector[4];
      edgeDetectors[0] = new SobelEdgeDetector(pixels, threshold);
      edgeDetectors[1] = new PrewittEdgeDetector(pixels, threshold);
      edgeDetectors[2] = new RobertsCrossEdgeDetector(pixels, threshold);
      
      // get edges
      boolean[][] sobelEdges   = edgeDetectors[0].getEdges();
      boolean[][] prewittEdges = edgeDetectors[1].getEdges();
      boolean[][] robertsCrossEdges   = edgeDetectors[2].getEdges();

      // make images out of edges
      BufferedImage sobelImage = Threshold.applyThresholdReversed(sobelEdges);
      BufferedImage prewittImage = Threshold.applyThresholdReversed(prewittEdges);
      BufferedImage robertsCrossImage = Threshold.applyThresholdReversed(robertsCrossEdges);

      // display edges
      BufferedImage[] toShow = {originalImage, sobelImage, prewittImage, robertsCrossImage};
      String title = "Edge Detection by Jason Altschuler";
      ImageViewer.showImages(toShow, title, 2, 3);
   }

}
