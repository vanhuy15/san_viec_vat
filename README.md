# 🎓 Sàn Việc Vặt (Student Micro Tasks)

Một ứng dụng nền web (Web Application) được xây dựng trên nền tảng **Java Servlet/JSP**, nhằm mục đích kết nối sinh viên với các công việc vặt ngắn hạn, giúp sinh viên có thêm thu nhập và các cá nhân/tổ chức dễ dàng tìm người hỗ trợ các công việc đơn giản.

## 🚀 Các Tính Năng Nổi Bật

### 👨‍🎓 Dành Cho Sinh Viên / Người Dùng
* **Tài khoản & Hồ sơ:** Đăng ký, đăng nhập, quên mật khẩu, cập nhật thông tin cá nhân.
* **Đăng việc (Người thuê):** Đăng tải các công việc vặt cần tìm người làm, quản lý các công việc đã đăng.
* **Nhận việc (Người làm):** Tìm kiếm, xem chi tiết công việc và nộp đơn ứng tuyển.
* **Quản lý tiến độ:** Xét duyệt người ứng tuyển, hủy việc, hoàn thành việc và xác nhận hoàn thành.
* **Ví tiền ảo:** Quản lý số dư, lịch sử giao dịch.
* **Đánh giá & Báo cáo:** Đánh giá chất lượng sau khi hoàn thành, báo cáo vi phạm.

### 🛡️ Dành Cho Quản Trị Viên (Admin)
* **Bảng điều khiển (Dashboard):** Xem tổng quan thống kê toàn hệ thống.
* **Quản lý người dùng:** Xem danh sách, khóa/mở khóa tài khoản người dùng vi phạm.
* **Quản lý công việc:** Kiểm duyệt các tin đăng công việc.
* **Quản lý giao dịch:** Theo dõi doanh thu, lịch sử nạp/rút tiền.
* **Quản lý khiếu nại:** Xử lý các báo cáo/phản hồi từ người dùng.

## 🛠️ Công Nghệ Sử Dụng

* **Ngôn ngữ:** Java 8
* **Nền tảng Back-end:** Java Servlet & JSP, JSTL
* **Cơ sở dữ liệu:** MySQL (sử dụng MySQL Connector-J 8.x)
* **Quản lý Build/Dependencies:** Maven
* **Web Server:** Hỗ trợ chạy nhúng cực nhẹ qua Jetty Plugin (hoặc Apache Tomcat)

## ⚙️ Hướng Dẫn Cài Đặt & Chạy Project

### Yêu Cầu Hệ Thống:
* **Java:** JDK 1.8 (Java 8) trở lên.
* **Database:** XAMPP (hoặc MySQL Server độc lập).
* **Git:** Để tải mã nguồn về máy.

### Bước 1: Thiết lập Database
1. Bật module **MySQL** trên phần mềm XAMPP Control Panel.
2. Mở trình duyệt truy cập `http://localhost/phpmyadmin` (hoặc dùng MySQL Workbench).
3. Tạo một database mới có tên là: `san_viec_vat`.
4. Import file SQL chứa cấu trúc bảng dữ liệu vào database vừa tạo.
*(Lưu ý: Mặc định ứng dụng kết nối qua cổng 3306 với tài khoản root, mật khẩu rỗng. Bạn có thể điều chỉnh lại tại `src/main/java/com/viecvat/context/DBContext.java` nếu cần).*

### Bước 2: Chạy dự án (Không cần phải cài Tomcat)
Vì dự án đã được cấu hình tích hợp **Jetty Maven Plugin** và **Maven Wrapper**, bạn có thể chạy trực tiếp bằng dòng lệnh vô cùng dễ dàng mà không cần cài đặt rườm rà:

1. Tải project về máy:
   ```bash
   git clone https://github.com/vanhuy15/san_viec_vat.git
   ```
2. Mở Terminal (PowerShell/CMD) tại thư mục chứa code vừa tải về.
3. Chạy lệnh sau để khởi động Server:
   ```bash
   .\mvnw jetty:run
   ```
4. Sau khi Terminal hiển thị quá trình khởi động thành công (`Started Jetty Server`), mở trình duyệt và truy cập:
   👉 **http://localhost:8080**

