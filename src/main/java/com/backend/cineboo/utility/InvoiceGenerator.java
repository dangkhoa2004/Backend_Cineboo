package com.backend.cineboo.utility;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.backend.cineboo.entity.*;
import com.backend.cineboo.repository.HoaDonRepository;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.barcodes.qrcode.EncodeHintType;
import com.itextpdf.barcodes.qrcode.ErrorCorrectionLevel;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.StyleConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Administrator
 */
public class InvoiceGenerator {
    private static BigDecimal getDecimal18Point2(BigDecimal value) {
        BigDecimal fractionalPart = value.remainder(BigDecimal.ONE);

        // Check if the fractional part is greater than or equal to 0.5
        if (fractionalPart.compareTo(BigDecimal.valueOf(0.5)) >= 0) {
            // Round up if >= 0.5
            return value.setScale(2, RoundingMode.HALF_UP);
        } else {
            // Floor if < 0.5
            return value.setScale(2, RoundingMode.FLOOR);
        }
    }

    public static void createInvoice(HoaDon hoaDon) throws IOException {
        if(hoaDon.getTrangThaiHoaDon()!=1){
            return;
        }
        InputStream inputFont = InvoiceGenerator.class.getClassLoader().getResourceAsStream("fonts/VietFontsWeb1_ttf/vuArial.ttf");
        InputStream inputFontBold = InvoiceGenerator.class.getClassLoader().getResourceAsStream("fonts/VietFontsWeb1_ttf/vuArialBold.ttf");

        FontProgram fontProgram = FontProgramFactory.createFont(IOUtils.toByteArray(inputFont));
        FontProgram fontBoldProgram = FontProgramFactory.createFont(IOUtils.toByteArray(inputFontBold));

        PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, true);
        PdfFont fontBold = PdfFontFactory.createFont(fontBoldProgram, PdfEncodings.IDENTITY_H, true);

        String maHoaDon = hoaDon.getMaHoaDon();
        Integer soLuong = hoaDon.getSoLuong();
        Voucher voucher = hoaDon.getVoucher();
        PTTT phuongThucThanhToan = hoaDon.getPttt();
        KhachHang khachHang = hoaDon.getKhachHang();
        BigDecimal tongSoTien = hoaDon.getTongSoTien();
        Phim phim = hoaDon.getPhim();
        List<ChiTietHoaDon> chiTietHoaDonList = hoaDon.getChiTietHoaDonList();


        String invoiceFileName = maHoaDon + khachHang.getMaKhachHang() + phim.getMaPhim();

        //Get Original Price to display later
        BigDecimal originalPrice = BigDecimal.ZERO;
        for(ChiTietHoaDon chiTietHoaDon: chiTietHoaDonList){
            //Damnit cant use lambda
            originalPrice=originalPrice.add(chiTietHoaDon.getGhe().getGiaTien());
        }
        originalPrice = InvoiceGenerator.getDecimal18Point2(originalPrice);


        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(invoiceFileName).append(".pdf");

        // Creating a PdfDocument object
        PdfWriter writer;
        try {


            // Load the font using PdfFontFactory
            writer = new PdfWriter(nameBuilder.toString());
            PdfDocument pdf = new PdfDocument(writer);

            // Set Page size to small (58cm width)
            PageSize customPageSize = new PageSize(58 * 2.83f, 100 * 3.83f);  // Adjust for scaling
            pdf.setDefaultPageSize(customPageSize);

            // Creating a Document object
            Document doc = new Document(pdf);
            doc.setMargins(2f, 5f, 2f, 5f);


            // Title: "CineBoo-Booking with a Touch"
            Paragraph superTitle = new Paragraph("CineBoo")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(font)
                    .setFontColor(new DeviceRgb(8, 73, 117))
                    .setFontSize(11f);  // Slightly larger for the main title;
            superTitle.setRole(PdfName.Title);
            doc.add(superTitle);
            Paragraph superTitle2 = new Paragraph("Tap & Book")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(font)
                    .setFontColor(new DeviceRgb(8, 73, 117))
                    .setFontSize(11f);  // Slightly larger for the main title;

            superTitle.setRole(PdfName.Title);
            doc.add(superTitle2);

            // Draw line separator
            SolidLine line = new SolidLine(0.5f);
            line.setColor(new DeviceRgb(8, 73, 117));
            LineSeparator separator = new LineSeparator(line);
            separator.setMarginTop(5f);
            separator.setMarginBottom(5f);
                doc.add(separator);

            // Main Title: "Hoá đơn đặt vé phim"
            Paragraph title = new Paragraph("Hoá đơn đặt vé")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(font)
                    .setFontColor(new DeviceRgb(8, 73, 117))
                    .setFontSize(10f);  // Title font size
            title.setRole(PdfName.H1);
            doc.add(title);

            // Draw line separator
//            SolidLine line = new SolidLine(1f);
//            line.setColor(Color.BLUE);
//            LineSeparator separator = new LineSeparator(line);
//            separator.setMarginTop(10);
//            separator.setMarginBottom(10);
//                doc.add(separator); //No seperator for now

            // Invoice Details
            doc.add(new Paragraph("Mã thanh toán: " + maHoaDon).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Khách hàng: " + khachHang.getMaKhachHang()).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Tên phim: " + phim.getTenPhim()).setFontSize(7f)).setFont(font);
            LocalDateTime paymentTime  = hoaDon.getThoiGianThanhToan();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = paymentTime.format(formatter);
            doc.add(new Paragraph("Thời gian thanh toán: " + formatDateTime).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Vé đã mua:").setFontSize(7f));

            // Creating a table
            float[] pointColumnWidths = {50F, 50F, 50F};
            Table table = new Table(pointColumnWidths);
            table.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table.setTextAlignment(TextAlignment.CENTER);
            table.setBorder(Border.NO_BORDER);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.setAutoLayout();
            // Adding column names to the table

            table.addCell(new Cell().add("Số ghế").setFontSize(6f).setFont(fontBold)) ;
            table.addCell(new Cell().add("Phòng chiếu").setFontSize(6f).setFont(fontBold));
            table.addCell(new Cell().add("Đơn giá (VNĐ)").setFontSize(6f).setFont(fontBold));

            // Adding rows to the table
            for (int i = 0; i < chiTietHoaDonList.size(); i++) {
                table.addCell(new Cell().add(chiTietHoaDonList.get(i).getGhe().getMaGhe()).setFontSize(5f).setFont(font));
                table.addCell(new Cell().add(chiTietHoaDonList.get(i).getGhe().getPhongChieu().getMaPhong()).setFontSize(5f).setFont(font));
                table.addCell(new Cell().add(chiTietHoaDonList.get(i).getGhe().getGiaTien().toString()).setFontSize(5f).setFont(font));
            }
            doc.add(table);

            // Draw another line separator
//                doc.add(separator);//No separator

            // Quantity and total amount
            doc.add(new Paragraph("Số lượng: " + soLuong).setFontSize(7f)).setFont(font);
            //Add original price
            doc.add(new Paragraph("Tổng: " + originalPrice).setFontSize(7f)).setFont(font);
            if (voucher != null) {
                doc.add(new Paragraph("Voucher: " + voucher.getMaVoucher()).setFontSize(7f)).setFont(font);
                if (voucher.getTruTienSo() != null) {
                    doc.add(new Paragraph("Giảm: " + voucher.getTruTienSo() + " VNĐ").setFontSize(7f)).setFont(font);
                }
                if (voucher.getTruTienPhanTram() != null) {
                    doc.add(new Paragraph("Phần trăm giảm: " + voucher.getTruTienPhanTram() + " %").setFontSize(7f)).setFont(font);
                    BigDecimal maxAmount = voucher.getGiamToiDa();
                    BigDecimal discountPercentage = BigDecimal.valueOf(voucher.getTruTienPhanTram()).divide(BigDecimal.valueOf(100)); // Assuming it is a percentage
                    BigDecimal discountAmount = originalPrice.multiply(discountPercentage);
                    if(discountAmount.compareTo(maxAmount)>0){
                        doc.add(new Paragraph("Giảm: " + maxAmount + " VNĐ").setFontSize(7f)).setFont(font);
                    }else{
                        doc.add(new Paragraph("Giảm: " + discountAmount + " VNĐ").setFontSize(7f)).setFont(font);
                    }
                }
            }
            doc.add(new Paragraph("Phương thức thanh toán: " + phuongThucThanhToan.getTenPTTT()).setFontSize(10f)).setFont(font);

            // Total amount
            Paragraph total = new Paragraph("Thành tiền: " + tongSoTien.intValue() + " VNĐ").setFontSize(10f).setFont(fontBold);
            doc.add(total);

            // Draw line separator

            doc.add(separator);
            // Last text
            Paragraph endTitle = new Paragraph("Cảm ơn Quý khách")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(font)
                    .setFontColor(new DeviceRgb(8, 73, 117))
                    .setFontSize(7f);  // Slightly larger for the main title;
            doc.add(endTitle);
            // Closing the document
            //Or not, place barcode too
            InvoiceGenerator.addQR(maHoaDon,pdf,1,95f,240f);
            doc.close();

        } catch (FileNotFoundException exception) {
            Logger.getLogger(InvoiceGenerator.class.getName()).log(Level.SEVERE, "Error generating invoice PDF", exception);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void addQR(String mQRCode, PdfDocument mPdfDocument, int mPage, float x, float y) {

        PdfPage mPdfPage = mPdfDocument.getPage(mPage);
        BarcodeQRCode mBarcodeQRCode;
        Map<EncodeHintType, Object> mHints = new HashMap<>();
        mHints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        mHints.put(EncodeHintType.MIN_VERSION_NR, 2);
        mBarcodeQRCode = new BarcodeQRCode(mQRCode, mHints);

        PdfCanvas over = new PdfCanvas(mPdfPage);

        // Save the original canvas state to avoid affecting other elements
        over.saveState();

        // Translate the canvas to the desired position
        over.concatMatrix(1, 0, 0, 1, x, y);

        // Place the barcode on the translated canvas
        mBarcodeQRCode.placeBarcode(over, new DeviceRgb(8, 73, 117), 1.5f);

        // Restore the canvas state to avoid affecting other elements
        over.restoreState();
    }

}