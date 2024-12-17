package com.backend.cineboo.utility;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.backend.cineboo.entity.*;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.barcodes.qrcode.EncodeHintType;
import com.itextpdf.barcodes.qrcode.ErrorCorrectionLevel;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
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
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.apache.commons.io.IOUtils;
import org.springframework.web.client.RestTemplate;
import vn.payos.type.PaymentLinkData;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.backend.cineboo.utility.ESC_POS_Commands.*;

/**
 * @author Administrator
 */
public class InvoiceGenerator {


    private static final RestTemplate restTemplate = new RestTemplate();

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

    public static String createInvoice(HoaDon hoaDon) throws IOException {

        String maHoaDonToOrderCode = hoaDon.getMaHoaDon().replaceAll("[^\\d-]|-(?=\\D)", "");
        Long orderCode = Long.valueOf(maHoaDonToOrderCode);
        String url = "http://localhost:8080/payos/get/" + orderCode;
        PaymentLinkData paymentLinkData = restTemplate.getForEntity(url, PaymentLinkData.class).getBody();
        System.out.println(paymentLinkData);
        System.out.println(paymentLinkData.getStatus());
        if (!paymentLinkData.getStatus().equals("PAID")) {
            //Meaning its Expired or Cancelled and shits
            return null;
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
        Phim phim = hoaDon.getChiTietHoaDonList().get(0).getId_GheAndSuatChieu().getId_SuatChieu().getPhim();
        SuatChieu suatChieu = hoaDon.getChiTietHoaDonList().get(0).getId_GheAndSuatChieu().getId_SuatChieu();

        List<ChiTietHoaDon> chiTietHoaDonList = hoaDon.getChiTietHoaDonList();


        String invoiceFileName = maHoaDon + khachHang.getMaKhachHang() + phim.getMaPhim();

        //Get Original Price to display later
        BigDecimal originalPrice = BigDecimal.ZERO;
        for (ChiTietHoaDon chiTietHoaDon : chiTietHoaDonList) {
            //Damnit cant use lambda

            originalPrice = originalPrice.add(chiTietHoaDon.getId_GheAndSuatChieu().getId_Ghe().getGiaTien());
        }
        originalPrice = InvoiceGenerator.getDecimal18Point2(originalPrice);


        String nameBuilder = invoiceFileName + ".pdf";
        String directoryPath = "invoices/"; // Replace with your directory path
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
        // Creating a PdfDocument object
        PdfWriter writer;
        String absolutePath = directoryPath + nameBuilder;
        try {


            // Load the font using PdfFontFactory
            writer = new PdfWriter(absolutePath);
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

            //Changing LocalDateTime to readable format
            LocalDateTime paymentTime = hoaDon.getThoiGianThanhToan();
            LocalDateTime airingTime = suatChieu.getThoiGianChieu();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String thoiGianThanhToan = paymentTime.format(formatter);
            String thoiGianChieu = airingTime.format(formatter);

            // Invoice Details
            doc.add(new Paragraph("Mã thanh toán: " + maHoaDon).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Khách hàng: " + khachHang.getMaKhachHang()).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Tên phim: " + phim.getTenPhim()).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Thời lượng: " + phim.getThoiLuong()).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Lịch chiếu: " + thoiGianChieu).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Địa điểm: " + hoaDon.getChiTietHoaDonList().get(0).getId_GheAndSuatChieu().getId_Ghe().getPhongChieu().getMaPhong()).setFontSize(7f)).setFont(font);


            doc.add(new Paragraph("Thời gian thanh toán: " + thoiGianThanhToan).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Vé đã mua:").setFontSize(7f));

            // Creating a table
            float[] pointColumnWidths = {50F, 50F};
            Table table = new Table(pointColumnWidths);
            table.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table.setTextAlignment(TextAlignment.CENTER);
            table.setBorder(Border.NO_BORDER);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.setAutoLayout();
            // Adding column names to the table

            table.addCell(new Cell().add("Số ghế")
                    .setFontSize(6f)
                    .setFont(fontBold)
                    .setBorderTop(Border.NO_BORDER)
                    .setBorderBottom(Border.NO_BORDER)
                    .setBorderLeft(Border.NO_BORDER));
            table.addCell(new Cell().add("Đơn giá (VNĐ)")
                    .setFontSize(6f)
                    .setFont(fontBold)
                    .setBorderTop(Border.NO_BORDER)
                    .setBorderBottom(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER));

            // Adding rows to the table
            for (int i = 0; i < chiTietHoaDonList.size(); i++) {
                table.addCell(new Cell().add(chiTietHoaDonList
                                .get(i)
                                .getId_GheAndSuatChieu()
                                .getId_Ghe()
                                .getMaGhe())
                        .setFontSize(5f)
                        .setFont(font)
                        .setBorderTop(Border.NO_BORDER)
                        .setBorderBottom(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER));

                table.addCell(new Cell().add(chiTietHoaDonList
                                .get(i)
                                .getId_GheAndSuatChieu()
                                .getId_Ghe().getGiaTien()
                                .toString()).setFontSize(5f)
                        .setFont(font)
                        .setBorderTop(Border.NO_BORDER)
                        .setBorderBottom(Border.NO_BORDER)
                        .setBorderRight(Border.NO_BORDER));
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
                    if (discountAmount.compareTo(maxAmount) > 0) {
                        doc.add(new Paragraph("Giảm: " + maxAmount + " VNĐ").setFontSize(7f)).setFont(font);
                    } else {
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
            InvoiceGenerator.addQR(maHoaDon, pdf, 1, 95f, 240f);
            doc.close();

        } catch (FileNotFoundException exception) {
            Logger.getLogger(InvoiceGenerator.class.getName()).log(Level.SEVERE, "Error generating invoice PDF", exception);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return absolutePath;
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

    public static String createTicket(HoaDon hoaDon, Long idGheAndSuatChieu) throws IOException {

        String maHoaDonToOrderCode = hoaDon.getMaHoaDon().replaceAll("[^\\d-]|-(?=\\D)", "");
        Long orderCode = Long.valueOf(maHoaDonToOrderCode);
        String url = "http://localhost:8080/payos/get/" + orderCode;
        PaymentLinkData paymentLinkData = restTemplate.getForEntity(url, PaymentLinkData.class).getBody();
        System.out.println(paymentLinkData);
        System.out.println(paymentLinkData.getStatus());
        if (!paymentLinkData.getStatus().equals("PAID")) {
            //Meaning its Expired or Cancelled and shits
            return null;
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
        Phim phim = hoaDon.getChiTietHoaDonList().get(0).getId_GheAndSuatChieu().getId_SuatChieu().getPhim();
        SuatChieu suatChieu = hoaDon.getChiTietHoaDonList().get(0).getId_GheAndSuatChieu().getId_SuatChieu();

        List<ChiTietHoaDon> chiTietHoaDonList = hoaDon.getChiTietHoaDonList();


        String ticketName = maHoaDon + chiTietHoaDonList.size() + idGheAndSuatChieu;

        //Get Original Price to display later


        String nameBuilder = ticketName + ".pdf";
        String directoryPath = "tickets/"; // Replace with your directory path
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
        // Creating a PdfDocument object
        PdfWriter writer;
        String absolutePath = directoryPath + nameBuilder;
        try {


            // Load the font using PdfFontFactory
            writer = new PdfWriter(absolutePath);
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
            Paragraph superTitle2 = new Paragraph("Tap and Book")
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
            Paragraph title = new Paragraph("Vé xem phim")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(font)
                    .setFontColor(new DeviceRgb(8, 73, 117))
                    .setFontSize(10f);  // Title font size
            title.setRole(PdfName.H1);
            doc.add(title);

            //Changing LocalDateTime to readable format
            LocalDateTime paymentTime = hoaDon.getThoiGianThanhToan();
            LocalDateTime airingTime = suatChieu.getThoiGianChieu();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String thoiGianThanhToan = paymentTime.format(formatter);
            String thoiGianChieu = airingTime.format(formatter);

            // Invoice Details
            doc.add(new Paragraph("Mã thanh toán: " + maHoaDon).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Khách hàng: " + khachHang.getMaKhachHang()).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Tên phim: " + phim.getTenPhim()).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Thời lượng: " + phim.getThoiLuong()).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Lịch chiếu: " + thoiGianChieu).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Địa điểm: " + hoaDon.getChiTietHoaDonList().get(0).getId_GheAndSuatChieu().getId_Ghe().getPhongChieu().getMaPhong()).setFontSize(7f)).setFont(font);


            doc.add(new Paragraph("Thời gian thanh toán: " + thoiGianThanhToan).setFontSize(7f)).setFont(font);
            doc.add(new Paragraph("Thông tin vé:").setFontSize(7f));

            // Creating a table
            float[] pointColumnWidths = {50F, 50F};
            Table table = new Table(pointColumnWidths);
            table.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table.setTextAlignment(TextAlignment.CENTER);
            table.setBorder(Border.NO_BORDER);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.setAutoLayout();
            // Adding column names to the table

            table.addCell(new Cell().add("Số ghế")
                    .setFontSize(6f)
                    .setFont(fontBold)
                    .setBorderTop(Border.NO_BORDER)
                    .setBorderBottom(Border.NO_BORDER)
                    .setBorderLeft(Border.NO_BORDER));
            table.addCell(new Cell().add("Đơn giá (VNĐ)")
                    .setFontSize(6f)
                    .setFont(fontBold)
                    .setBorderTop(Border.NO_BORDER)
                    .setBorderBottom(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER));
            BigDecimal price = BigDecimal.ZERO;
            // Adding rows to the table
            for (int i = 0; i < chiTietHoaDonList.size(); i++) {
                if (chiTietHoaDonList.get(i).getId_GheAndSuatChieu().getId() != idGheAndSuatChieu) {
                    continue;//Skip if not the right ticket
                }
                price = chiTietHoaDonList.get(i).getGiaTien();
                table.addCell(new Cell().add(chiTietHoaDonList
                                .get(i)
                                .getId_GheAndSuatChieu()
                                .getId_Ghe()
                                .getMaGhe())
                        .setFontSize(5f)
                        .setFont(font)
                        .setBorderTop(Border.NO_BORDER)
                        .setBorderBottom(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER));

                table.addCell(new Cell().add(chiTietHoaDonList
                                .get(i)
                                .getId_GheAndSuatChieu()
                                .getId_Ghe().getGiaTien()
                                .toString()).setFontSize(5f)
                        .setFont(font)
                        .setBorderTop(Border.NO_BORDER)
                        .setBorderBottom(Border.NO_BORDER)
                        .setBorderRight(Border.NO_BORDER));
            }
            doc.add(table);

            // Draw another line separator
//                doc.add(separator);//No separator

            // Quantity and total amount
            //Add original price
            doc.add(new Paragraph("Tổng: " + price.toString()).setFontSize(7f)).setFont(font);


            doc.add(new Paragraph("Phương thức thanh toán: " + phuongThucThanhToan.getTenPTTT()).setFontSize(10f)).setFont(font);

            // Total amount
            Paragraph total = new Paragraph("Thành tiền: " + price + " VNĐ").setFontSize(10f).setFont(fontBold);
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
            InvoiceGenerator.addQR(idGheAndSuatChieu.toString(), pdf, 1, 95f, 240f);
            doc.close();

        } catch (FileNotFoundException exception) {
            Logger.getLogger(InvoiceGenerator.class.getName()).log(Level.SEVERE, "Error generating invoice PDF", exception);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return absolutePath;
    }



    public static String thermalTicketGenerator(HoaDon hoaDon) {
        String maHoaDonToOrderCode = hoaDon.getMaHoaDon().replaceAll("[^\\d-]|-(?=\\D)", "");
        Long orderCode = Long.valueOf(maHoaDonToOrderCode);
        String url = "http://localhost:8080/payos/get/" + orderCode;
        PaymentLinkData paymentLinkData = restTemplate.getForEntity(url, PaymentLinkData.class).getBody();
        System.out.println(paymentLinkData);
        System.out.println(paymentLinkData.getStatus());
        if (!paymentLinkData.getStatus().equals("PAID")) {
            //Meaning its Expired or Cancelled and shits
            return null;
        }
//        <-- code to check if hoadon is paid, uncomment later
        String nameBuilder = "thermal_ticket_"+hoaDon.getMaHoaDon()+ ".bin";
        String directoryPath = "thermal_tickets/"; // Replace with your directory path
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
        String absolutePath = directoryPath+nameBuilder;
        // ESC/POS commands (raw bytes)


        String maHoaDon = hoaDon.getMaHoaDon();
        PTTT phuongThucThanhToan = hoaDon.getPttt();
        List<ChiTietHoaDon> chiTietHoaDonList = hoaDon.getChiTietHoaDonList();
        KhachHang khachHang = hoaDon.getKhachHang();
        for(ChiTietHoaDon chiTietHoaDon :chiTietHoaDonList){
            if(chiTietHoaDon.getId_GheAndSuatChieu()==null){
                return null;
            }
        }
        Phim phim = hoaDon.getChiTietHoaDonList().get(0).getId_GheAndSuatChieu().getId_SuatChieu().getPhim();
        SuatChieu suatChieu = hoaDon.getChiTietHoaDonList().get(0).getId_GheAndSuatChieu().getId_SuatChieu();
        String ten = khachHang.getHo() + " " + khachHang.getTenDem() + " " + khachHang.getTen();

        int printed = 0;
        int ticketCount = chiTietHoaDonList.size();


        try {
            ////////////START HERE/////////////////////////////////////////////
            try (OutputStream outputStream = new FileOutputStream(absolutePath)) {
                outputStream.write(initializePrinter);
                for (ChiTietHoaDon chiTietHoaDon : chiTietHoaDonList) {
                    String ticketName = maHoaDon + chiTietHoaDonList.size() + chiTietHoaDon.getId_GheAndSuatChieu().getId();
                    outputStream.write(alignCenter);
                    outputStream.write("==============================================\n".getBytes(StandardCharsets.UTF_8));
                    outputStream.write(boldOn);
                    outputStream.write("CINEBOO\n".getBytes(StandardCharsets.UTF_8));
                    outputStream.write("Tap and Book\n".getBytes(StandardCharsets.UTF_8));
                    outputStream.write(boldOff);
                    outputStream.write("==============================================\n\n".getBytes(StandardCharsets.UTF_8));

                    outputStream.write(alignCenter);
                    outputStream.write("Ve Xem Phim\n".getBytes(StandardCharsets.UTF_8));
                    outputStream.write("--- Thong Tin Ve ---\n".getBytes(StandardCharsets.UTF_8));
                    outputStream.write(alignLeft);
                    outputStream.write("----------------------------------------------\n".getBytes(StandardCharsets.UTF_8));
                    outputStream.write(("Ma Ve:          " + ticketName + "\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.write(("Khach Hang:     " + TextUtilities.removeDiacritics(ten) + "\n\n").getBytes(StandardCharsets.UTF_8));

                    outputStream.write(("Ten Phim:       " + TextUtilities.removeDiacritics(phim.getTenPhim()) + "\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.write(("Thoi Gian Chieu: " + suatChieu.getThoiGianChieu() + "\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.write(("Ma Ghe:         " + chiTietHoaDon.getId_GheAndSuatChieu().getId_Ghe().getMaGhe() + "\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.write(("Phong Chieu:    " + chiTietHoaDon.getId_GheAndSuatChieu().getId_Ghe().getPhongChieu().getMaPhong() + "\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.write("----------------------------------------------\n".getBytes(StandardCharsets.UTF_8));


                    outputStream.write(("Gia Tien:       " + chiTietHoaDon.getId_GheAndSuatChieu().getId_Ghe().getGiaTien() + "\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.write(("So Luong:       " + 1 + "\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.write(("Thanh Tien:     " + chiTietHoaDon.getId_GheAndSuatChieu().getId_Ghe().getGiaTien() + "\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.write(("Phuong Thuc Thanh Toan: " + TextUtilities.removeDiacritics(phuongThucThanhToan.getTenPTTT()) + "\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.write(("Thoi Gian Thanh Toan: " + hoaDon.getThoiGianThanhToan() + "\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.write("----------------------------------------------\n\n".getBytes(StandardCharsets.UTF_8));

                    outputStream.write(alignCenter);
                    outputStream.write(boldOn);
                    outputStream.write("Cam on Quy Khach!\n".getBytes(StandardCharsets.UTF_8));
                    outputStream.write(boldOff);
                    outputStream.write("==============================================\n".getBytes(StandardCharsets.UTF_8));
                    outputStream.write(cutPaperPartial);
                    printed++;
                }
                outputStream.write(cutPaperFull);
                if (printed != ticketCount) {
                    return null;
                }
                return absolutePath;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String generateBase64QRCode(String text) throws Exception {
        // Set QR code hints
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // Medium error correction level

        // Create BarcodeQRCode object
        BarcodeQRCode barcodeQRCode = new BarcodeQRCode(text, hints);

        // Generate the QR code as a ToolkitImage
        Image qrImage = barcodeQRCode.createAwtImage(Color.BLACK, Color.WHITE);

        // Set desired size for the QR code
        int scaleFactor = 5; // Adjust this for the desired size
        int width = qrImage.getWidth(null) * scaleFactor;
        int height = qrImage.getHeight(null) * scaleFactor;

        // Convert the ToolkitImage to a BufferedImage with scaled size
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g2d.drawImage(qrImage, 0, 0, width, height, null);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.25f); // Adjust quality as needed
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(bufferedImage, null, null), param);
        ios.flush();
        ios.close();
        // Convert the BufferedImage to byte array

        byte[] imageBytes = baos.toByteArray();

        // Encode byte array to Base64 and return it as a data URL
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }
}


