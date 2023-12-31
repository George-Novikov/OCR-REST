package com.fatemorgan.ocr.tools;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class RecognitionService {
    private static final Tesseract TESSERACT = new Tesseract();

    public List<String> scanPDF(String language, int dpi, float resizeMultiplier, boolean isSharp, InputStream input) throws IOException, TesseractException {
        configureTesseract(language);

        PDDocument document = PDDocument.load(input);
        PDFRenderer renderer = new PDFRenderer(document);
        int pagesNumber = document.getNumberOfPages();

        List<String> pagesContent = new ArrayList<>();
        for (int i = 0; i < pagesNumber; i++){
            BufferedImage bufferedPage = renderer.renderImageWithDPI(i, dpi, ImageType.RGB);
            if (resizeMultiplier != 0) bufferedPage = ImageProcessor.resize(bufferedPage, resizeMultiplier, false);
            if (isSharp) bufferedPage = ImageProcessor.sharpen(bufferedPage);
            pagesContent.add(TESSERACT.doOCR(bufferedPage));
        }
        document.close();
        return pagesContent;
    }
    public String scanImage(String language, BufferedImage bufferedImage) throws TesseractException {
        configureTesseract(language);
        return TESSERACT.doOCR(bufferedImage);
    }

    private void configureTesseract(String language){
        File tessdata = LoadLibs.extractTessResources("tessdata");

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("nix") || os.contains("nux")){
            TESSERACT.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
        } else {
            TESSERACT.setDatapath(tessdata.getParent());
        }
        TESSERACT.setLanguage(language);
    }
}
