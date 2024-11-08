package com.backend.cineboo;

import com.backend.cineboo.service.PublicMeBaby;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
//		PublicMeBaby ngrokService = new PublicMeBaby();
//		String ngrokUrl = ngrokService.startNgrok();
//		System.out.println("Tạo tài khoản PayOS");
//		System.out.println("Tạo kênh thanh toán. Vào Cài Đặt");
//		System.out.println("Điển URL phía dưới vào mục Webhook");
//		System.out.println("URL public: " + ngrokUrl+"/payos/confirm-webhook");
	}

}
