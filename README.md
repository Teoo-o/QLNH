HƯỚNG DẪN CÀI ĐẶT VÀ VẬN HÀNH HỆ THỐNG QUẢN LÝ NHÀ HÀNG (QLNH)

1. YÊU CẦU HỆ THỐNG
Trước khi cài đặt, hãy đảm bảo máy tính đã cài đặt các công cụ sau:

JDK 17 trở lên.

Maven (để quản lý thư viện Java).

Microsoft SQL Server (phiên bản 2017 trở lên).

IDE: IntelliJ IDEA hoặc Eclipse.

Trình duyệt: Chrome/Edge/Firefox.

2. CÀI ĐẶT CƠ SỞ DỮ LIỆU (DATABASE SETTINGS)
Mở SQL Server Management Studio (SSMS).

Tạo một Database mới tên là: QLNH.

Mở file Script SQL và chạy (Execute/F5) để tạo cấu trúc bảng và dữ liệu mẫu.

Đảm bảo SQL Server cho phép đăng nhập bằng quyền SQL Server Authentication (thông tin tài khoản sa).

3. CẤU HÌNH VÀ CHẠY BACKEND (SPRING BOOT)
Mở dự án bằng IntelliJ IDEA.

Tìm đến file cấu hình: src/main/resources/application.properties.

Chỉnh sửa thông tin kết nối Database phù hợp với máy của bạn:

Properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=QLNH;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=1234567890
spring.jpa.hibernate.ddl-auto=update
server.port=8080
Mở file QlnhApplication.java (trong gói com.nhahang.qlnh) và nhấn Run.

Kiểm tra: Truy cập http://localhost:8080/api/kho trên trình duyệt. Nếu hiện ra dữ liệu JSON nguyên liệu là Backend đã chạy thành công.

4. CẤU HÌNH VÀ CHẠY FRONTEND (CLIENT)
Vì Frontend sử dụng gọi API bất đồng bộ (fetch), bạn cần cấu hình đúng địa chỉ của Server.

Tìm file config.js trong thư mục Frontend.

Đảm bảo biến API_BASE_URL trỏ đúng về Port của Backend:

JavaScript
const API_BASE_URL = "http://localhost:8080/api";
Cách chạy:

Cách 1: Click chuột phải vào file dangnhap.html chọn Open with Live Server (Khuyên dùng trong VS Code).

Cách 2: Mở trực tiếp file dangnhap.html bằng trình duyệt.

5. CÁC TÀI KHOẢN TRUY CẬP MẪU (TEST ACCOUNTS)
Hệ thống sử dụng cơ chế phân quyền dựa trên Chức vụ:

Quản lý (Admin): Tài khoản: admin | Mật khẩu: 123 (Toàn quyền).

Bếp trưởng: Tài khoản: nv04 | Mật khẩu: 123 (Có quyền vào kho).

Nhân viên phục vụ: Tài khoản: nv01 | Mật khẩu: 123 (Chỉ dùng Bàn/Thanh toán).

*Vô bảng NHAN_VIEN để xem tài khoản và mật khẩu của toàn bộ nhân viên*

6. CÁC ĐIỂM NHẤN KỸ THUẬT TRONG DỰ ÁN
Thuật toán Find-or-Create: Tự động nhận diện khách quen qua Số điện thoại tại mục Đặt bàn để tối ưu dữ liệu.

Chặn đặt trùng lịch: Hệ thống tự động tính toán khoảng đệm +/- 2 giờ để ngăn chặn việc đặt trùng bàn.

Trừ kho tự động: Khi thanh toán hóa đơn, hệ thống tự động quét định mức và trừ số lượng nguyên liệu thực tế trong kho.

Module hóa Frontend: Sử dụng Object AppUtil trong config.js để quản lý tập trung việc Phân quyền và Định dạng tiền tệ VNĐ.

Xử lý số liệu: Tiền tệ được làm tròn số nguyên, định lượng kho làm tròn 2 chữ số thập phân.

7. CẤU TRÚC THƯ MỤC CHÍNH
src/main/java/.../entity: Định nghĩa các thực thể CSDL (Mapping JPA).

src/main/java/.../repository: Các lớp tương tác dữ liệu (Spring Data JPA).

src/main/java/.../controller: Xử lý logic nghiệp vụ và cung cấp REST API.

Thư mục Frontend: Chứa toàn bộ file .html, .css, .js của giao diện.
