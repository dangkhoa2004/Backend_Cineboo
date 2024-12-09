package com.backend.cineboo.service;

import io.github.jonathanlink.PDFLayoutTextStripper;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.*;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PrinterServices {
    public final static String DEFAULT_PRINTER = "CutePDF Writer";
    public void printReceipt(String text,String printerName) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();

        // Set the printer to CutePDF (make sure CutePDF is installed and available)
        job.setPrintService(findPrintService("CutePDF Writer"));  // Adjust if necessary

        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            PageFormat pf = job.defaultPage();
            Paper paper = pf.getPaper();
            double paperWidth = 8 * 72d; // 8 inches width
            double paperHeight = 11 * 72d; // 11 inches height (letter size paper)
            paper.setSize(paperWidth, paperHeight);
            pageFormat.setPaper(paper);

            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            // Set font and graphics for text printing
            graphics.setFont(new Font("Arial", Font.PLAIN, 10));
            graphics.setColor(Color.BLACK);

            // Split text into lines based on the width of the page
            int yPosition = 100;
            String[] lines = text.split("\n");
            for (String line : lines) {
                graphics.drawString(line, 100, yPosition); // Print each line at a new y-position
                yPosition += 15; // Increase yPosition for next line (adjust as needed)
            }

            return Printable.PAGE_EXISTS;
        });

        try {
            job.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }


    private PrintService findPrintService(String printerName) {
        PrintService[] services = PrinterJob.lookupPrintServices();
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }

    public void printMeAPDF(String pathToInvoicePDF, String printerName) throws IOException, PrinterException {
        File file = new File(pathToInvoicePDF);
        PDDocument document = Loader.loadPDF(file);
        PrintService myPrintService = findPrintService(printerName);

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Ticket_" + file.getName());
        job.setPageable(new PDFPageable(document));
        job.setPrintService(myPrintService);
        job.print();
    }


    public String extractTextFromPDF(String pathToInvoicePDF) throws IOException {
        File file = new File(pathToInvoicePDF);
        PDDocument document = Loader.loadPDF(file);
        PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
        String string = pdfTextStripper.getText(document);
        return string;
    }
}
