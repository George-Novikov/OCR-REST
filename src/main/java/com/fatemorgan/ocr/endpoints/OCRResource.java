package com.fatemorgan.ocr.endpoints;

import com.fatemorgan.ocr.dto.ErrorDTO;
import com.fatemorgan.ocr.tools.ImageProcessor;
import com.fatemorgan.ocr.tools.RecognitionService;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

@Path("/scan")
public class OCRResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OCRResource.class);

    @Inject
    RecognitionService recognitionService;

    @POST
    @Path("/pdf")
    @Consumes("application/pdf")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public Response scanPDF(@QueryParam("lang") @DefaultValue("eng") String lang,
                            @QueryParam("dpi") @DefaultValue("72") int dpi,
                            InputStream input){
        try {
            return Response.ok(recognitionService.scanPDF(lang, dpi, input)).build();
        } catch (IOException ioe){
            LOGGER.error(ioe.getMessage(), ioe);
            return Response.status(500).entity(new ErrorDTO(13, "Ошибка преобразования файла: " + ioe.getMessage())).build();
        } catch (TesseractException te){
            LOGGER.error(te.getMessage(), te);
            return Response.status(500).entity(new ErrorDTO(13, "Ошибка распознавания текста: " + te.getMessage())).build();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Response.status(500).entity(new ErrorDTO(13, e.getMessage())).build();
        }
    }

    @POST
    @Path("/image")
    @Consumes({"image/jpeg", "image/png", "image/gif", "image/svg+xml", "image/webp", "image/avif", "image/apng"})
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public Response scanImage(@QueryParam("lang") @DefaultValue("eng") String lang,
                              @QueryParam("resize") @DefaultValue("0") float multiplier,
                              @QueryParam("smooth") @DefaultValue("false") boolean isSmooth,
                              InputStream input){
        try {
            BufferedImage bufferedImage = ImageProcessor.streamToImage(input);
            if (multiplier > 0) bufferedImage = ImageProcessor.resize(bufferedImage, multiplier, isSmooth);
            return Response.ok(recognitionService.scanImage(lang, bufferedImage)).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.status(500).entity(new ErrorDTO(13, e.getMessage())).build();
        }
    }
}
