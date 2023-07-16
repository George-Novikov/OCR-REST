package com.fatemorgan.ocr.tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageProcessor {
    public static BufferedImage resize(BufferedImage originalImage, float multiplier, boolean isSmooth){
        int scaleType = Image.SCALE_DEFAULT;
        if (isSmooth) scaleType = Image.SCALE_SMOOTH;

        int targetWidth = Math.round((float) originalImage.getWidth() * multiplier);
        int targetHeight = Math.round((float) originalImage.getHeight() * multiplier);

        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, scaleType);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(scaledImage, 0, 0, null);
        return outputImage;
    }

    public static BufferedImage sharpen(BufferedImage bufferedImage){
        float[] kernelMatrix = new float[] {
                0, -1, 0,
                -1, 5, -1,
                0, -1, 0 };
        Kernel kernel = new Kernel(3, 3, kernelMatrix);
        BufferedImageOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(bufferedImage, null);
    }

    public static BufferedImage streamToImage(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        BufferedImage bufferedImage = ImageIO.read(bufferedStream);
        bufferedStream.close();
        return bufferedImage;
    }
}
