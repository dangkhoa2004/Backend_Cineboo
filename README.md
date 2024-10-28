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
cineboo.backend
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.cineboo.backend
│   │   │       ├── config          # Cấu hình chung cho ứng dụng (security, JWT, etc.)
│   │   │       ├── controller      # Xử lý các request API
│   │   │       ├── entity          # Các mô hình (model) của cơ sở dữ liệu
│   │   │       ├── repository      # Tầng giao tiếp với cơ sở dữ liệu
│   │   │       ├── service         # Xử lý logic nghiệp vụ
│   │   │       └── request         # Các DTO để nhận dữ liệu từ client
│   │   └── resources
│   │       ├── application.yml     # Cấu hình ứng dụng
│   │       └── schema.sql          # Cấu trúc database (nếu có)
└── pom.xml                          # Cấu hình Maven và dependencies
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
