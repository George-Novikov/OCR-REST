package com.fatemorgan.ocr.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/scan")
public class OCRResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OCRResource.class);

    @POST
    @Path("/pdf")
    @Consumes("application/pdf")
    @Produces(MediaType.TEXT_HTML)
    public Response scanPDF(InputStream input){
        
    }
}
