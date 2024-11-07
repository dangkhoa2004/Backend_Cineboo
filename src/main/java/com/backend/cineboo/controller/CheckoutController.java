package com.backend.cineboo.controller;

import com.backend.cineboo.entity.HoaDon;
import com.backend.cineboo.repository.HoaDonRepository;
import com.backend.cineboo.utility.RepoUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payos")
public class CheckoutController {

    @Autowired
    private final PayOS payOS;

    @Autowired
    HoaDonRepository hoaDonRepository;

    public CheckoutController(PayOS payOS) {
        super();
        this.payOS = payOS;
    }


    @RequestMapping(value = "/success")
    public String Success() {
        return "success";
    }

    @RequestMapping(value = "/cancel")
    public String Cancel() {
        return "cancel";
    }


    @Operation(summary = "Chuyển hướng tới trang thanh toán",
            description = "Điều hướng Client(API Caller) trực tiếp thay vì trả về ResponseEntity chứa URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirection")})
    @GetMapping(value = "/create-payment-link/redirect")
    public void redirectToCheckout(@PathVariable Long hoaDonId, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ResponseEntity response = urlToCheckout(hoaDonId, request);
        if (response.getStatusCode().is2xxSuccessful()) {
            String checkoutUrl = (String) response.getBody();
            httpServletResponse.setHeader("Location", checkoutUrl);
            httpServletResponse.setStatus(302);
        }
    }

    @Operation(summary = "Trả về URLs liên quan tới thanh toán",
            description = "Trả về ResponseEntity cho Client(API Caller)\n\n" +
                    "Chỉ chứa thông tin cơ bản cần thiết\n\n" +
                    "Liên hệ lập trình viên để đưa thêm thông tin vào\n\n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tạo URL thanh toán thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi khởi tạo URL thanh toán")

    })
    @GetMapping(value = "/create-payment-link/url/{hoaDonId}")
    public ResponseEntity urlToCheckout(@PathVariable Long hoaDonId, HttpServletRequest request) {
        ResponseEntity response = RepoUtility.findById(hoaDonId, hoaDonRepository);
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                HoaDon hoaDon = (HoaDon) response.getBody();
                final String baseUrl = getBaseUrl(request);
                final String productName = hoaDon.getPhim().getTenPhim() + hoaDon.getPhim().getGioiHanDoTuoi().getTenDoTuoi();

                StringBuilder details = new StringBuilder();

                hoaDon.getChiTietHoaDonList().stream()
                        .map(e -> e.getGhe().getMaGhe())
                        .map(maGhe -> String.valueOf(Integer.parseInt(maGhe.replaceAll("[^\\d-]|-(?=\\D)", "")))) // Convert to integer to strip leading zeros
                        .forEach(maGhe -> details.append("G").append(maGhe)); // Append each processed `maGhe` with a space


                // Calculate expiration time (5 minutes after the current time)
                Long currentTimestamp = Instant.now().getEpochSecond(); // Get current time in seconds and cast to int
                Long expiredAt = currentTimestamp + 300; // Add 5 minutes (300 seconds)
                //The bloody thing will not take expiration value
                //So NO EXPIRATION!

                final String description = details.toString();
                final String returnUrl = baseUrl + "/success";
                final String cancelUrl = baseUrl + "/cancel";
                final Integer quantity = hoaDon.getSoLuong();
//                final int price = hoaDon.getTongSoTien().intValue();
                final int price = 1000;
                // Gen order code
                String currentTimeString = String.valueOf(new Date().getTime());
                String maHoaDon = hoaDon.getMaHoaDon().replaceAll("[^\\d-]|-(?=\\D)", "");
                long orderCode =  Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
                orderCode = Long.valueOf(String.valueOf(orderCode)+maHoaDon);
                ItemData item = ItemData.builder().name(productName).quantity(quantity).price(price).build();


                PaymentData paymentData = PaymentData.builder().orderCode(orderCode).amount(price).description(description)
                        .returnUrl(returnUrl).cancelUrl(cancelUrl).expiredAt(expiredAt).item(item).build();


                // This one creates REAL payment link
                CheckoutResponseData data = payOS.createPaymentLink(paymentData);
                String checkoutUrl = data.getCheckoutUrl();
                Map urls = new HashMap<>();
                urls.put("paymentData",paymentData);
                urls.put("payment", checkoutUrl);
                urls.put("success", returnUrl);
                urls.put("cancel", cancelUrl);
                return ResponseEntity.status(HttpStatus.OK).body(urls);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Tạo URL thanh toán thất bại");
            }
        }
        return response;
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String url = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url += ":" + serverPort;
        }
        url += contextPath;
        return url;
    }
}