# Quản Lý Shop Quần Áo

Ứng dụng quản lý shop quần áo được xây dựng bằng Java Swing và MySQL với giao diện hiện đại, thân thiện với người dùng.

![Shop Manager](/src/main/resources/images/readme/preview.png)

## Tính năng chính

- **Giao diện hiện đại**: Sử dụng FlatLaf và thiết kế UI tùy chỉnh
- **Đăng nhập an toàn**: Hệ thống xác thực người dùng với phân quyền
- **Dashboard**: Tổng quan với các thống kê và biểu đồ trực quan
- **Quản lý sản phẩm**: Thêm, sửa, xóa, tìm kiếm sản phẩm, kèm ảnh sản phẩm
- **Quản lý danh mục**: Phân loại sản phẩm theo danh mục
- **Quản lý đơn hàng**: Theo dõi và xử lý đơn hàng
- **Quản lý khách hàng**: Thông tin và lịch sử mua hàng của khách
- **Quản lý nhà cung cấp**: Thông tin chi tiết về nhà cung cấp
- **Quản lý nhập hàng**: Theo dõi giao dịch nhập xuất kho
- **Thống kê báo cáo**: Biểu đồ và phân tích dữ liệu
- **Xuất Excel**: Xuất dữ liệu sản phẩm và khách hàng ra file Excel
- **Responsive design**: Giao diện thích ứng với các kích thước màn hình

## Yêu cầu hệ thống

- Java JDK 11 hoặc cao hơn
- MySQL (XAMPP)
- Maven

## Cài đặt

1. Clone repository này về máy của bạn
2. Mở XAMPP Control Panel và khởi động MySQL
3. Mở MySQL và thực thi script SQL trong file `src/main/resources/database.sql`
4. Mở project trong IDE của bạn (IntelliJ IDEA hoặc Eclipse)
5. Chạy lệnh Maven để tải các dependency:
   ```
   mvn clean install
   ```

## Chạy ứng dụng

### Sử dụng JAR file (Khuyên dùng)

1. Tạo file JAR bằng Maven:
   ```
   mvn clean package
   ```

2. Chạy ứng dụng từ file JAR:
   ```
   java -jar target/clothing-shop-manager-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

### Sử dụng Maven

1. Chạy ứng dụng từ cửa sổ Terminal/Command Prompt:
   ```
   mvn compile exec:java -Dexec.mainClass="com.shopmanager.ui.LoginForm"
   ```

2. Hoặc chạy MainWindow trực tiếp:
   ```
   mvn compile exec:java -Dexec.mainClass="com.shopmanager.ui.MainWindow"
   ```

### Sử dụng IDE

1. Mở project trong IDE (IntelliJ IDEA hoặc Eclipse)
2. Chạy file `src/main/java/com/shopmanager/ui/LoginForm.java` để mở form đăng nhập
3. Hoặc chạy file `src/main/java/com/shopmanager/ui/MainWindow.java` trực tiếp

### Thông tin đăng nhập

- Username: `admin`
- Password: `admin123`

## Công nghệ sử dụng

- **Java Swing**: Framework UI
- **MySQL**: Hệ quản trị cơ sở dữ liệu
- **JDBC**: Kết nối Java với MySQL
- **Maven**: Quản lý dependency và build project
- **FlatLaf**: Modern Look and Feel cho Java Swing
- **JFreeChart**: Tạo các biểu đồ thống kê
- **JCalendar**: Date picker component
- **Apache POI**: Xuất dữ liệu ra Excel

## Cấu trúc dự án

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── shopmanager/
│   │           ├── models/       # Data models (User, Product, Order, etc.)
│   │           ├── ui/           # UI components
│   │           │   ├── components/  # Base components like BasePanel
│   │           │   └── [Panel classes] # Specific panels (ProductPanel, etc.)
│   │           └── utils/        # Utility classes (DatabaseConnection, ExcelExporter, etc.)
│   └── resources/
│       ├── database.sql    # SQL script to create database
│       └── images/         # Images for the application
│           ├── ui/         # UI images like logo, icons
│           └── products/   # Product images storage
```

## Đặc điểm UI

- Bo tròn các góc, hiệu ứng hover và focus trên textfields
- Giao diện thống nhất với bảng màu hài hòa
- Biểu đồ thống kê hỗ trợ hiển thị dữ liệu trực quan
- Bảng dữ liệu dễ đọc và tương tác
- Logo tùy chỉnh thay thế logo mặc định
- Header với gradient màu hiện đại
- Thanh tìm kiếm bo tròn với nút tìm kiếm riêng
- Nút đăng xuất có hiệu ứng hover
- Hiển thị ngày/giờ với kiểu dáng hiện đại

## Tính năng quản lý sản phẩm

- **Hiển thị ảnh sản phẩm**: Mỗi sản phẩm có thể kèm theo hình ảnh minh họa
- **Chỉnh sửa ảnh**: Chọn và cập nhật ảnh sản phẩm từ máy tính
- **Xem ảnh lớn**: Nhấp vào ảnh sản phẩm để xem kích thước lớn hơn
- **Lưu trữ ảnh tự động**: Ảnh được lưu trong thư mục resources/images/products với tên file duy nhất
- **Xuất Excel**: Xuất danh sách sản phẩm ra file Excel
- **Tìm kiếm nhanh**: Tìm kiếm sản phẩm theo tên, loại, giá, kích thước...

## Phát triển

Để đóng góp vào dự án, bạn có thể fork repository này và tạo pull request.

### Các hướng phát triển tiếp theo:
- Thêm tính năng xuất báo cáo PDF
- Tích hợp thanh toán trực tuyến
- Phát triển phiên bản mobile companion app
- Tích hợp hệ thống quét mã vạch/QR code
- Tính năng đánh giá và xếp hạng sản phẩm

## Copyright

© 2025 AT19N0105_MaiHaiDang. All rights reserved. 