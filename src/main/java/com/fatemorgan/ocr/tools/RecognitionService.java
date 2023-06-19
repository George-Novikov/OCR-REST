package com.fatemorgan.ocr.tools;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RecognitionService {
    private static final Tesseract TESSERACT = new Tesseract();

    public List<String> scanPDF(String language, int dpi, InputStream input) throws IOException, TesseractException {
        configureTesseract(language);

        PDDocument document = PDDocument.load(input);
        PDFRenderer renderer = new PDFRenderer(document);
        int pagesNumber = document.getNumberOfPages();

        List<String> pagesContent = new ArrayList<>();
        for (int i = 0; i < pagesNumber; i++){
            BufferedImage bufferedPage = renderer.renderImageWithDPI(i, dpi, ImageType.RGB);
            pagesContent.add(TESSERACT.doOCR(bufferedPage));
        }
        return pagesContent;
    }
    public String scanImage(String language, InputStream input) throws IOException, TesseractException {
        configureTesseract(language);
        BufferedInputStream bufferStream = new BufferedInputStream(input);
        BufferedImage bufferedImage = ImageIO.read(bufferStream);
        return TESSERACT.doOCR(bufferedImage);
    }
    private void configureTesseract(String language){
        File tessdata = LoadLibs.extractTessResources("tessdata");
        //URL dataPath = getClass().getClassLoader().getResource("/tessdata/" + language + ".traineddata");
        //File tessdata = new File(dataPath.getFile());
        TESSERACT.setDatapath(tessdata.getParent());
        TESSERACT.setLanguage(language);
    }
}
