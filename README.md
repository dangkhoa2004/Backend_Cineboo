Dưới đây là một mẫu README cho dự án Spring Boot của bạn:

---

# CineBoo Backend - Đặt Vé Xem Phim 🎬

Đây là dự án backend cho ứng dụng **Đặt Vé Xem Phim - CineBoo** được xây dựng bằng Spring Boot và MySQL. Dự án bao gồm các tính năng cần thiết cho hệ thống đặt vé xem phim trực tuyến, như quản lý phim, suất chiếu, rạp chiếu, ghế ngồi, và người dùng.

## Mục lục

- [Giới thiệu](#giới-thiệu)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Cấu hình](#cấu-hình)
- [Cách chạy dự án](#cách-chạy-dự-án)
- [API Endpoints](#api-endpoints)
- [Thông tin thêm](#thông-tin-thêm)

## Giới thiệu

**CineBoo** là ứng dụng đặt vé xem phim giúp người dùng có thể:
- Đăng ký và đăng nhập tài khoản.
- Xem thông tin phim, rạp chiếu, và suất chiếu.
- Chọn ghế và đặt vé xem phim.

## Công nghệ sử dụng

- **Java** và **Spring Boot** - Xây dựng backend và quản lý API.
- **MySQL** - Cơ sở dữ liệu cho ứng dụng.
- **JWT** - Quản lý xác thực người dùng.
- **Maven** - Quản lý thư viện và dependencies.

## Cấu trúc dự án

```
Backend_Cineboo/
├── .mvn/                     # Wrapper Maven để build dự án.
├── src/
│   ├── main/
│   │   ├── java/com/backend/cineboo/
│   │   │   ├── config/         # Cấu hình toàn bộ ứng dụng (CORS, Security,...).
│   │   │   ├── controller/     # Lớp điều khiển (API endpoint).
│   │   │   ├── dto/            # Các lớp dữ liệu truyền tải (Data Transfer Object).
│   │   │   ├── entity/         # Các lớp mô hình ánh xạ với bảng cơ sở dữ liệu (JPA Entities).
│   │   │   ├── repository/     # Lớp giao tiếp với cơ sở dữ liệu (Spring Data JPA).
│   │   │   ├── scheduledJobs/  # Các tác vụ chạy định kỳ (Scheduler).
│   │   │   ├── service/        # Xử lý logic nghiệp vụ (business logic).
│   │   │   ├── utility/        # Các lớp tiện ích dùng chung.
│   │   │   └── Application.java # Entry point chính của Spring Boot.
│   │   ├── resources/
│   │   │   ├── css/            # Tệp CSS (nếu cần dùng cho email templates hoặc giao diện server-rendered).
│   │   │   ├── fonts/          # Font hoặc tài nguyên liên quan.
│   │   │   ├── static/         # Tài nguyên tĩnh (ảnh, file tải về,...).
│   │   │   ├── templates/      # File HTML (nếu sử dụng Thymeleaf hoặc template engine khác).
│   │   │   └── application.properties # Cấu hình chính cho ứng dụng.
│   ├── test/
│   │   ├── java/com/backend/cineboo/ # Các lớp kiểm thử (unit test, integration test).
├── target/                    # Thư mục chứa file build (JAR/WAR) sau khi biên dịch.
├── .gitattributes             # File cấu hình Git (encoding, line endings,...).
├── .gitignore                 # Danh sách file/thư mục cần bỏ qua khi commit.
├── mvnw                       # File script Maven Wrapper cho Linux/Mac.
├── mvnw.cmd                   # File script Maven Wrapper cho Windows.
├── nbactions.xml              # Cấu hình cho NetBeans (nếu dùng IDE này).
├── pom.xml                    # File cấu hình Maven (dependencies, plugins, build,...).
└── README.md                  # Tài liệu mô tả dự án.
```

## Cấu hình

- **Cơ sở dữ liệu**: MySQL (tên database: **cineboo**)
- **JWT Secret Key**: `'VID2019'`
- **Phiên bản MySQL Connector**: 8.0.18
- **Cấu hình Spring Boot**:
    - URL API login: `localhost:8080/api/user/login`
    - Body yêu cầu cho API login:
      ```json
      {
        "username": "<tên người dùng>",
        "password": "<mật khẩu>"
      }
      ```

## Cách chạy dự án

1. **Cài đặt MySQL** và tạo cơ sở dữ liệu với tên `cineboo`.
2. **Cấu hình MySQL** trong `application.yml`:
    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/cineboo
        username: <tên người dùng MySQL>
        password: <mật khẩu MySQL>
      jpa:
        hibernate:
          ddl-auto: update
        show-sql: true
    ```
3. **Chạy ứng dụng**:
    ```bash
    mvn spring-boot:run
    ```

## API Endpoints

Một số endpoint cơ bản:
- `POST /api/user/login`: Đăng nhập
- `POST /api/user/register`: Đăng ký
- `GET /api/movies`: Lấy danh sách phim
- `GET /api/cinemas`: Lấy danh sách rạp chiếu
- `POST /api/bookings`: Đặt vé xem phim

## Thông tin thêm

Ứng dụng sử dụng kiến trúc RESTful và tích hợp JWT để bảo mật các API endpoint. Mọi yêu cầu liên quan đến đặt vé hoặc thông tin cá nhân sẽ yêu cầu xác thực.

---

Hy vọng README này sẽ giúp ích cho dự án của bạn!
