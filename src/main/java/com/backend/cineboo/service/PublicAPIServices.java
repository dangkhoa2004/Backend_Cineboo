package com.backend.cineboo.service;

import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
public class PublicAPIServices {

//    @Value(value = "${NGROK_TOKEN}")
    private String ngrokToken = "2pkK74s9r01kNrCyHi5QKweIszp_7UJcsQ7JEjbhFv91auotBc";

    public String startNgrok() throws Exception {
        // Ensure the token is set
        if (ngrokToken == null || ngrokToken.isEmpty()) {
            throw new IllegalArgumentException("Ngrok Auth Token is required.");
        }

        // Set the NGROK_AUTH_TOKEN environment variable for the Ngrok process
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.environment().put("NGROK_AUTH_TOKEN", ngrokToken);

        // Start the ngrok process to expose the local port 8080
        processBuilder.command("ngrok", "http", "8080");
        Process process = processBuilder.start();

        // Wait for Ngrok to initialize and establish the tunnel
        Thread.sleep(2000);  // Allow some time for Ngrok to start

        // Retrieve the Ngrok public URL from the API
        return getNgrokPublicUrl();
    }

    private String getNgrokPublicUrl() throws Exception {
        // Query the Ngrok API for the public URL
        URL url = new URL("http://localhost:4040/api/tunnels");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Extract the public URL from the response
        String jsonResponse = response.toString();
        String publicUrl = jsonResponse.split("\"public_url\":\"")[1].split("\"")[0];

        return publicUrl;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() throws Exception {
        PublicAPIServices ngrokService = new PublicAPIServices();
        String ngrokUrl = ngrokService.startNgrok();
        System.out.println("Tạo tài khoản PayOS");
        System.out.println("Tạo kênh thanh toán. Vào Cài Đặt");
        System.out.println("Điển URL phía dưới vào mục Webhook");
        System.out.println("URL public: " + ngrokUrl + "/payos/confirm-webhook");
    }

}
