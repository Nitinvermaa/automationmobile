package com.gl.testngfw.utility;

import com.gl.testngfw.logging.FrameworkLogger;
import com.gl.testngfw.setup.InitializerScript;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openqa.selenium.OutputType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.opencv.core.Core.*;
import static org.opencv.core.CvType.CV_32FC1;
import static org.opencv.highgui.Highgui.imread;
import static org.opencv.imgproc.Imgproc.*;

/**
 * This class provides methods to support test scripts development when it is necessary to deal with
 * on-screen graphic objects like images. Refer to the particular methods description.
 */
public class ImageUtils {
    private static final Logger LOGGER = Logger.getLogger(ImageUtils.class.getName());
    private static final String TMP_IMG_FILE_NAME = "tmp_screenshot_img";
    private static final String USER_HOME = "user.home";
    private static final String PNG = ".png";
    private static final String SEPARATOR = "/";
    private static final AndroidDriver androidDriver = (AndroidDriver) InitializerScript.getDriver();

    static {
        final String osArch = System.getProperty("sun.arch.data.model");
        final String opencvDllName = "opencv_java249.dll";
        final String opencvDll = "opencv_dll";

        try {
            System.loadLibrary(opencvDllName);
        } catch (Exception ule) {
            if (!"64".equals(osArch) && !"32".equals(osArch)) {
                FrameworkLogger.logWarning("Incorrect type of OS architecture");
                FrameworkLogger.logError(ule);
            }
            try {
                InputStream in = ImageUtils.class.getResourceAsStream(
                        SEPARATOR + opencvDll + SEPARATOR + "x" + (osArch.contains("64") ? 64 : 86) +
                                SEPARATOR + opencvDllName);
                File tempDll = new File(System.getProperty(USER_HOME) + File.separator + opencvDllName);
                OutputStream out = FileUtils.openOutputStream(tempDll);
                IOUtils.copy(in, out);
                in.close();
                out.close();
                System.load(tempDll.getAbsolutePath());
                tempDll.deleteOnExit();
            } catch (IOException e) {
                FrameworkLogger.logError(e);
            }
        }
    }

    private ImageUtils() {
    }

    /**
     * Captures current screen displayed on the android device and saves it as .png to the local drive
     *
     * @param saveToPath - directory to which the captured screen should be saved
     */
    public static void captureScreen(String saveToPath, String fileName) {
        File file = androidDriver.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(saveToPath + "/" + fileName + ".png"));
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "", e);
        }
    }

    /**
     * Captures specified area of the current screen displayed on the android device
     * and saves it as .png to the local drive
     *
     * @param saveToPath - directory to which the captured screen should be saved, i.e. C:/Projects/imgs
     * @param fileName   - name of the file, without extension
     * @param x          - top left point x coordinate of area to be captured
     * @param y          - top left point y coordinate of area to be captured
     * @param w          - width of the area to be captured
     * @param h          - height of the area to be captured
     */
    public static void captureScreenArea(String saveToPath, String fileName, int x, int y, int w, int h) throws IOException {
        String path = checkLastSlash(saveToPath);
        captureScreen(path, fileName);

        File file = new File(path + File.separator + fileName + PNG);
        BufferedImage img = null;
        img = ImageIO.read(file);
        FrameworkLogger.logStep(String.format("Cropping specified on-screen area: x - %s, y - %s, " +
                "width - %s, height - %s", x, y, w, h));
        img = cropBufferedImage(img, new Rect(x, y, w, h));
        ImageIO.write(img, PNG, file);
    }

    /**
     * Returns color of the specified pixel
     *
     * @param imgPathName - path to the image, i.e.
     * @param x           - x coordinate of the pixel
     * @param y           - y coordinate of the pixel
     * @return Color object
     */
    public static Color getImagePixelColor(String imgPathName, int x, int y) throws IOException {
        File file = new File(imgPathName);
        BufferedImage img = null;
        img = ImageIO.read(file);
        return getPixelColor(img, x, y);
    }

    /**
     * Returns color of the specified pixel on screen;
     *
     * @param x - x coordinate of the pixel
     * @param y - y coordinate of the pixel
     * @return Color object
     */
    public static Color getScreenPixelColor(int x, int y) throws IOException {
        File file = new File(System.getProperty(USER_HOME) + File.separator + TMP_IMG_FILE_NAME + PNG);
        captureScreen(file.getParent(), TMP_IMG_FILE_NAME);

        BufferedImage img = null;
        img = ImageIO.read(file);

        if (!file.delete()) {
            FrameworkLogger.logWarning("Cannot delete file: " + file.getPath());
        }

        return getPixelColor(img, x, y);
    }

    /**
     * Checks if the images are match or not, return true or false respectively.
     * Decision is made based similarity threshold value passed to this method as argument. Correlation between
     * two histograms method is used for image comparison.
     *
     * @param pathToImage1 - path to image1
     * @param pathToImage2 - path to image2
     * @param threshold    - similarity threshold
     * @return boolean: true if images match, false if images don't math
     */
    public static boolean compareImagesByHistograms(String pathToImage1, String pathToImage2, double threshold) throws IOException {
        Double similarity = calcHistogramsSimilarity(pathToImage1, pathToImage2, 0);
        if (similarity == null) {
            FrameworkLogger.logWarning("Image comparison is aborted due to the error");
            return false;
        } else if (similarity >= threshold) {
            FrameworkLogger.logStep(String.format("Images recognized as identical. Similarity (%s) >= Threshold (%s)",
                    similarity, threshold));
            return true;
        } else {
            FrameworkLogger.logStep(String.format("Images recognized as different. Similarity (%s) <= Threshold (%s)",
                    similarity, threshold));
            return false;
        }
    }

    /**
     * Checks if the images are match or not, return true or false respectively.
     * Decision is made based on predefined similarity threshold value for each comparison method. Correlation between
     * two histograms method is used for image comparison.
     *
     * @param pathToImage1 - path to image1
     * @param pathToImage2 - path to image2
     * @return boolean: true if images match, false if images don't math
     */
    public static boolean compareImagesByHistograms(String pathToImage1, String pathToImage2) throws IOException {
        final double threshold = 1;
        return compareImagesByHistograms(pathToImage1, pathToImage2, threshold);
    }

    /**
     * Checks if the images are match or not, return true or false respectively.
     * Decision is made based on predefined similarity threshold value.
     *
     * @param pathToImage1 - path to image1
     * @param pathToImage2 - path to image2
     * @return boolean: true if images match, false if images don't math
     */
    public static boolean compareImagesByPixels(String pathToImage1, String pathToImage2) throws IOException {
        final double threshold = 0.98;
        return compareImagesByPixels(pathToImage1, pathToImage2, threshold);
    }

    /**
     * Checks if the images are match or not, return true or false respectively.
     * Decision is made based on similarity threshold value passed as argument.
     *
     * @param pathToImage1 - path to image1
     * @param pathToImage2 - path to image2
     * @param threshold    - similarity threshold
     * @return boolean: true if images match, false if images don't math
     */
    public static boolean compareImagesByPixels(String pathToImage1, String pathToImage2, double threshold) throws IOException {
        Mat img1 = loadImage(pathToImage1);
        Mat img2 = loadImage(pathToImage2);

        if (img1 == null || img2 == null) {
            FrameworkLogger.logWarning("Image comparison is aborted due to the error.");
            return false;
        }

        if (!sizesCheckAndAdjustment(img1, img2, pathToImage1, pathToImage2)) {
            FrameworkLogger.logWarning("Image comparison is aborted due to the error.");
            return false;
        }

        double[][] pixelsMatchMap = getPixelsMatchMap(img1, img2);
        double similarity = calcSimilarityByPixels(pixelsMatchMap);

        if (similarity >= threshold) {
            FrameworkLogger.logStep(String.format("Images recognized as identical. Similarity (%s) >= Threshold (%s)",
                    similarity, threshold));
            return true;
        } else {
            FrameworkLogger.logStep(String.format("Images recognized as different. Similarity (%s) <= Threshold (%s)",
                    similarity, threshold));
            return false;
        }
    }

    /**
     * Returns color of the specified pixel in BufferedImages passed as input parameter
     *
     * @param img - image
     * @param x   - x coordinate of the pixel
     * @param y   - y coordinate of the pixel
     * @return Color object
     */
    private static Color getPixelColor(BufferedImage img, int x, int y) {
        if (x > img.getWidth() - 1 || y > img.getHeight() - 1) {
            FrameworkLogger.logWarning("Specified point out of image dimensions");
            return null;
        }
        return new Color(img.getRGB(x, y));
    }

    /**
     * Return similarity coefficient between two images. Value is depend on method used for similarity calculation.
     * This method requires two images of the same size
     *
     * @param pathToImage1 - path to image1
     * @param pathToImage2 - path to image2
     * @param method       - comparison method. Possible values:
     *                     0 - Correlation - Imgproc.CV_COMP_CORREL
     *                     1 - Chi-square - Imgproc.CV_COMP_CHISQR
     *                     2 - Intersection - Imgproc.CV_COMP_INTERSECT
     *                     3 - Bhattacharyya - Imgproc.CV_COMP_BHATTACHARYYA or Imgproc.CV_COMP_HELLINGER
     * @return Double, similarity coefficient. Method returns null in case something unexpected happened.
     */
    private static Double calcHistogramsSimilarity(String pathToImage1, String pathToImage2, int method) throws IOException {

        FrameworkLogger.logStep(String.format("Calculating similarity between images %s and %s",
                pathToImage1, pathToImage2));

        // Read images
        Mat img1 = loadImage(pathToImage1);
        Mat img2 = loadImage(pathToImage2);
        if (img1 == null || img2 == null) {
            return null;
        }

        // Check if image have the same size or not. If yes - just proceed, if not matchTemplate algorithm will be
        // applied. If images have incompatible size, method will be interrupted and return null.
        if (!sizesCheckAndAdjustment(img1, img2, pathToImage1, pathToImage2)) {
            return null;
        }

        Mat hsv1 = new Mat();
        Mat hsv2 = new Mat();

        // Convert images to HSV format
        cvtColor(img1, hsv1, COLOR_BGR2HSV);
        cvtColor(img2, hsv2, COLOR_BGR2HSV);

        // Initialize arguments to calculate the histograms (bins, ranges, and channels)
        final int hBins = 50;
        final int sBins = 60;
        final int hRangeMin = 0;
        final int hRangeMax = 180;
        final int sRangeMin = 0;
        final int sRangeMax = 256;
        MatOfInt histSize = new MatOfInt(hBins, sBins);
        MatOfFloat ranges = new MatOfFloat(hRangeMin, hRangeMax, sRangeMin, sRangeMax);
        MatOfInt channels = new MatOfInt(0, 1);

        // Calculation and normalization of images histograms
        Mat hist1 = new Mat();
        Mat hist2 = new Mat();
        calcHist(Arrays.asList(hsv1), channels, new Mat(), hist1, histSize, ranges);
        Core.normalize(hist1, hist1, 0, 1, NORM_MINMAX, -1);
        calcHist(Arrays.asList(hsv2), channels, new Mat(), hist2, histSize, ranges);
        Core.normalize(hist2, hist2, 0, 1, NORM_MINMAX, -1);

        // Calculation of images similarity coefficient. There are 4 methods that could be applied.
        // Refer to method description above.
        return compareHist(hist1, hist2, method);
    }

    /**
     * com.ril.amiko.utility method. Checks sizes of the Mat images and adjusts size of bigger image to smaller image in case
     * sizes are different. Sizes adjusted throw launching findTemplateInSource method
     *
     * @param img1         - first image
     * @param img2         - second image
     * @param pathToImage1 - path to first image
     * @param pathToImage2 - path to second image
     * @return bool: true if either sizes were the same or adjustment was completed successfully; false otherwise
     */
    private static boolean sizesCheckAndAdjustment(Mat img1, Mat img2, String pathToImage1, String pathToImage2) throws IOException {
        if (!img1.size().equals(img2.size())) {
            // Incompatible sizes check
            if ((img1.width() > img2.width() && img1.height() < img2.height()) ||
                    (img1.width() < img2.width() && img1.height() > img2.height())) {
                FrameworkLogger.logWarning("Images have incompatible sizes.");
                return false;
            }
            // finding "bigger" image and then finding location of "smaller" image inside "bigger" one.
            // Also "bigger" image will be trimmed to "smaller" image dimensions for consequent comparison
            else {
                FrameworkLogger.logStep(String.format("Images have different sizes \n" +
                                "%s: width - %s; height - %s\n %s: width - %s; height - %s.\n" +
                                "Find Template In Source method will be applied.",
                        pathToImage1, img1.width(), img1.height(), pathToImage2, img2.width(), img2.height()
                ));
                // if img1 is "bigger" one
                if (img1.width() >= img2.width() && img1.height() >= img2.height()) {
                    cropImage(img2, img1, pathToImage2, pathToImage1);
                }
                // else if img2 is "bigger" one
                else if (img1.width() <= img2.width() && img1.height() <= img2.height()) {
                    cropImage(img1, img2, pathToImage1, pathToImage2);
                }
                // else images have incompatible sizes. Shouldn't be the case due to "incompatible sizes" check
                // performed above
                else {
                    FrameworkLogger.logWarning("Images have incompatible sizes.");
                    return false;
                }
            }
        }
        return true;
    }

    private static void cropImage(Mat img1, Mat img2, String pathToImage1, String pathToImage2) throws IOException {
        Rect foundLoc = findTemplateInSourceCoeffNormed(img2, img1);
        FrameworkLogger.logStep(String.format("%s was found inside %s in following area: x: %s, y: %s, " +
                        "width: %s, height: %s", pathToImage1, pathToImage2, foundLoc.x, foundLoc.y,
                foundLoc.width, foundLoc.height
        ));
        highlightArea(pathToImage2, foundLoc);
        cropMat(img2, foundLoc).assignTo(img2);
    }

    /**
     * Returns match map with similarity coefficient between pixels of two images
     *
     * @param img1 - image 1
     * @param img2 - image 2
     * @return double[][] pixels match map with values in 0 - 1 range
     */
    private static double[][] getPixelsMatchMap(Mat img1, Mat img2) {
        final int rowsCount = img1.rows();
        final int colsCount = img1.cols();
        final int channelsCount = 3; // B G R
        final int normalizationCoef = 255;
        double[][] res = new double[rowsCount][colsCount];

        for (int i = 0; i < rowsCount; i++)
            for (int j = 0; j < colsCount; j++)
                for (int channel = 0; channel < channelsCount; channel++) {
                    res[i][j] += (1 - Math.abs(img1.get(i, j)[channel] - img2.get(i, j)[channel]) / normalizationCoef) /
                            channelsCount;
                }
        return res;
    }

    /**
     * Returns similarity coefficient calculated using pixelsMatchMap
     *
     * @param pixelsMatchMap - double[][] pixels mathc map
     * @return double, similarity coefficient
     */
    private static double calcSimilarityByPixels(double[][] pixelsMatchMap) {
        int pixelsCount = pixelsMatchMap.length * pixelsMatchMap[0].length;
        double similarity = 0;
        for (double[] aPixelsMatchMap : pixelsMatchMap)
            for (double anAPixelsMatchMap : aPixelsMatchMap)
                similarity += anAPixelsMatchMap;
        return similarity / pixelsCount;
    }

    /**
     * Returns the best match center point of the template in the source image
     *
     * @param srcImg      - source image
     * @param templateImg - template image
     * @param method      - matchTemplate method. Possible values:
     *                    0 - Imgproc.TM_SQDIFF
     *                    1 - Imgproc.TM_SQDIFF_NORMED
     *                    2 - Imgproc.TM_CCORR
     *                    3 - Imgproc.TM_CCORR_NORMED
     *                    4 - Imgproc.TM_CCOEFF
     *                    5 - Imgproc.TM_CCOEFF_NORMED
     * @return Rect (org.opencv.core.Rect) found area in source image
     */
    private static Rect findTemplateInSource(Mat srcImg, Mat templateImg, int method) {
        org.opencv.core.Point resPoint;
        Mat resMat = new Mat();
        Rect resRect = new Rect();

        resMat.create(srcImg.rows() - templateImg.rows() + 1,
                srcImg.cols() - templateImg.cols() + 1, CV_32FC1);
        matchTemplate(srcImg, templateImg, resMat, method);
        normalize(resMat, resMat, 0, 1, NORM_MINMAX, -1, new Mat());

        MinMaxLocResult minMaxLocResult = minMaxLoc(resMat, new Mat());

        if (method == Imgproc.TM_SQDIFF || method == Imgproc.TM_SQDIFF_NORMED) {
            resPoint = minMaxLocResult.minLoc;
        } else {
            resPoint = minMaxLocResult.maxLoc;
        }

        resRect.x = (int) resPoint.x;
        resRect.y = (int) resPoint.y;
        resRect.width = templateImg.width();
        resRect.height = templateImg.height();

        return resRect;
    }

    /**
     * Returns the best match center point of the template in the source image.
     * CCOEFF_NORMED method is used for that.
     *
     * @param srcImg      - source image
     * @param templateImg - template image
     * @return Rect (org.opencv.core.Rect) found area in source image
     */
    private static Rect findTemplateInSourceCoeffNormed(Mat srcImg, Mat templateImg) {
        return findTemplateInSource(srcImg, templateImg, 5);
    }

    /**
     * Returns cropped image. Crop area is specified in input parameters
     *
     * @param img  - image to be cropped
     * @param rect - rectangle specifying area that will be cropped
     * @return BufferedImage, cropped part of the input img image
     */
    private static BufferedImage cropBufferedImage(BufferedImage img, Rect rect) {
        return img.getSubimage(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Returns cropped image represented as Mat object
     *
     * @param img  - image to be cropped
     * @param rect - rectangle specifying area that will be cropped
     * @return Mat, cropped part of the input image
     */
    private static Mat cropMat(Mat img, Rect rect) {
        return img.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width);
    }

    /**
     * Creates new image files in the same directory as input file, named as ..._area.png
     *
     * @param pathToImage - image pathname
     * @param rect        - area to be highlighted in the newly created image
     */
    private static void highlightArea(String pathToImage, Rect rect) throws IOException {
        File file = new File(pathToImage);
        File newFile = new File(file.getParent() + "\\" + FilenameUtils.removeExtension(file.getName())
                + "_area." + FilenameUtils.getExtension(file.getName()));
        FrameworkLogger.logStep(String.format("New image with highlighted area will be created in %s and named as %s",
                newFile.getParent(), newFile.getName()));

        BufferedImage img = null;
        img = ImageIO.read(file);

        final int lineWidth = 5;
        Graphics2D graph = img.createGraphics();
        graph.setColor(Color.YELLOW);
        graph.setStroke(new BasicStroke(lineWidth));
        graph.drawRect(rect.x, rect.y, rect.width, rect.height);

        ImageIO.write(img, PNG, newFile);
    }

    /**
     * Checks if the path parameter ends with slash, and if not, adds it.
     *
     * @param saveToPath - path to be checked
     * @return String, checked and corrected (if needed) path
     */
    private static String checkLastSlash(String saveToPath) {
        if ((saveToPath.charAt(saveToPath.length() - 1) == '/' ||
                saveToPath.charAt(saveToPath.length() - 1) == '\\') &&
                saveToPath.length() > 3) {
            return saveToPath.substring(0, saveToPath.length() - 1);
        } else
            return saveToPath;
    }

    /**
     * Loads image from specified location and returns Mat object
     *
     * @param pathToImage - image location
     * @return Mat image object
     */
    private static Mat loadImage(String pathToImage) {
        Mat img = imread(pathToImage);
        if (img.width() == 0 & img.height() == 0) {
            FrameworkLogger.logWarning(String.format("Unable to read image %s", pathToImage));
            return null;
        } else {
            return img;
        }
    }
}
