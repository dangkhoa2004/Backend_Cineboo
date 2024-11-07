package com.backend.cineboo.controller;

import com.backend.cineboo.entity.HoaDon;
import com.backend.cineboo.repository.HoaDonRepository;
import com.backend.cineboo.utility.RepoUtility;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.exception.PayOSException;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

import java.time.Instant;
import java.time.LocalDateTime;
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


    @GetMapping(value = "/success")
    public String Success() {
        return "success";
    }

    @GetMapping(value = "/cancel")
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
                details.append(hoaDon.getId());
                details.append("_");
                details.append(hoaDon.getMaHoaDon());
                details.append("_");
                details.append(hoaDon.getKhachHang().getMaKhachHang());


                // Calculate expiration time (5 minutes after the current time)
                Long currentTimestamp = Instant.now().getEpochSecond(); // Get current time in seconds and cast to int
                Long expiredAt = currentTimestamp + 300; // Add 5 minutes (300 seconds)
                //The bloody thing will not take expiration value
                //So NO EXPIRATION!

                final String description = details.toString();
                final String returnUrl = baseUrl + "/payos/success";
                final String cancelUrl = baseUrl + "/payos/cancel";
                final Integer quantity = hoaDon.getSoLuong();
//                final int price = hoaDon.getTongSoTien().intValue();
                final int price = 1000;
                // Gen order code
                String currentTimeString = String.valueOf(new Date().getTime());
                String maHoaDon = hoaDon.getMaHoaDon().replaceAll("[^\\d-]|-(?=\\D)", "");
                long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
                orderCode = Long.valueOf(maHoaDon);
                ItemData item = ItemData.builder().name(productName).quantity(quantity).price(price).build();


                PaymentData paymentData = PaymentData.builder().orderCode(orderCode).amount(price).description(description)
                        .returnUrl(returnUrl).cancelUrl(cancelUrl).expiredAt(expiredAt).item(item).build();


                // This one creates REAL payment link
                CheckoutResponseData data = payOS.createPaymentLink(paymentData);
                String checkoutUrl = data.getCheckoutUrl();
                Map urls = new HashMap<>();
                urls.put("paymentData", paymentData);
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

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelPayment(
            @PathVariable Long orderId,
            @RequestParam(value = "cancellationReason", required = false) String cancellationReason) {

        try {
            // Default to a generic cancellation reason if none is provided
            String reason = (cancellationReason != null && !cancellationReason.isEmpty())
                    ? cancellationReason
                    : "No specific reason provided";

            // Call the cancelPaymentLink method with the resolved reason
            PaymentLinkData cancel = payOS.cancelPaymentLink(orderId, reason);
            String maHoaDon = "HD00" + orderId;
            HoaDon hoaDon = hoaDonRepository.getByMaHoaDon(maHoaDon).orElse(null);
            if (cancel != null) {
                //After a successful delete operation on Bank side
                //Change status From Database too
                if (hoaDon != null) {
                    hoaDon.setTrangThaiHoaDon(2);
                    hoaDonRepository.save(hoaDon);
                } else {
                    cancel.setCancellationReason(cancel.getCancellationReason() + ". Đã huỷ hoá Đơn Online, nhưng Hoá đơn không tồn tại trong Database");
                }
                return ResponseEntity.ok(cancel);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("No response from cancel operation");
            }

        } catch (PayOSException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many cancel requests sent to API");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while canceling the order");
        }
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable Long orderId) {

        try {
            // Default to a generic cancellation reason if none is provided


            // Call the cancelPaymentLink method with the resolved reason
            PaymentLinkData getPayment = payOS.getPaymentLinkInformation(orderId);

            if (getPayment != null) {
                return ResponseEntity.ok(getPayment);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("No response from get operation");
            }

        } catch (PayOSException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many cancel requests sent to API");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while canceling the order");
        }
    }

    @PostMapping(path = "/confirm-webhook")
    public ResponseEntity confirmWebhook(@RequestBody String requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Chuyển đổi requestBody thành JsonNode
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            JsonNode dataNode = jsonNode.get("data");
            System.out.println(jsonNode);
            // Access the "id" field inside the "data" node
            String maHoaDon = "HD00" + dataNode.get("orderCode").asText();
            HoaDon hoaDon = hoaDonRepository.getByMaHoaDon(maHoaDon).orElse(null);
            if (hoaDon != null) {
                hoaDon.setThoiGianThanhToan(LocalDateTime.now());
                hoaDon.setTrangThaiHoaDon(1);
                hoaDon.getChiTietHoaDonList().stream().forEach(e -> e.setTrangThaiChiTietHoaDon(1));
                //Thêm điểm vào ĐÂY
                //Thay vào số 0
                hoaDon.setDiem(hoaDon.getDiem() + 0);
                hoaDonRepository.save(hoaDon);
            } else {
                throw new Exception("Thanh toán thành công nhưng Hoá đơn không tồn tại trong DB");
            }
            // Trả về phản hồi đã được đóng gói trong ResponseEntity\
            return ResponseEntity.status(HttpStatus.OK).body(jsonNode);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
        }

    }
}