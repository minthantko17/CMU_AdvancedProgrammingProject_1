package se233.advprogrammingproject1.functions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EdgeDetectFunctions {
    //this does the same as CropFunctions.saveImg xD
    public static void saveImg(BufferedImage edgeDetectedImage, File fileName, String extension) throws IOException {
            ImageIO.write(edgeDetectedImage, extension, fileName);
            System.out.println("Image saved to: " + fileName.getAbsolutePath());
    }
}
