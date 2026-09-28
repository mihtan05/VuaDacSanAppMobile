# VuaDacSanAppMobile

Ứng dụng di động Android Native viết bằng **Java** và cơ sở dữ liệu **SQLite**, phục vụ quản lý và bán lẻ đặc sản.

---

## 🏗 Cấu Trúc Thư Mục Dự Án

```text
VuaDacSanAppMobile/
├── app/
│   ├── src/main/
│   │   ├── java/
│   │   │   ├── com/example/baicuoiki/    # Phân hệ mua sắm, giỏ hàng, đặt hàng & nghiệp vụ
│   │   │   ├── com/example/dangnhap/     # Phân hệ xác thực, phân quyền & quản lý nhân sự
│   │   │   ├── com/example/kho_ketoan/   # Phân hệ quản lý kho, bảng lương & thống kê
│   │   │   ├── com/example/qlkhuyenmai/  # Phân hệ khuyến mãi & chăm sóc khách hàng (CSKH)
│   │   │   └── database/                 # SQLite helper quản lý cơ sở dữ liệu
│   │   ├── res/                          # Tài nguyên giao diện (Layouts, Drawables, Values, Menus)
│   │   └── AndroidManifest.xml           # Khai báo cấu hình ứng dụng, Activity và quyền
│   └── build.gradle.kts                  # Cấu hình SDK & dependencies của module app
├── gradle/                               # Gradle wrapper & catalog thư viện
├── build.gradle.kts                      # Cấu hình cấp root project
├── settings.gradle.kts                   # Khai báo module và repositories
└── README.md                             # Tài liệu hướng dẫn dự án
```

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

### 1. Yêu Cầu Môi Trường
* **IDE**: Android Studio (Jellyfish, Koala hoặc mới hơn).
* **JDK**: Java Development Kit 11 trở lên.
* **Android SDK**: Min SDK 26 (Android 8.0) | Target SDK 35 | Compile SDK 36.
* **Thiết bị chạy**: Máy ảo Android (AVD) hoặc điện thoại thật bật *USB Debugging*.

### 2. Cài Đặt & Chạy Bằng Android Studio
1. **Clone mã nguồn về máy**:
   ```bash
   git clone https://github.com/mihtan05/VuaDacSanAppMobile.git
   ```
2. **Mở dự án**:
   - Mở Android Studio, chọn **Open** và dẫn tới thư mục `VuaDacSanAppMobile`.
3. **Đồng bộ dependencies**:
   - Chờ Android Studio tự động chạy **Sync Project with Gradle Files** để tải các thư viện cần thiết.
4. **Khởi chạy**:
   - Chọn cấu hình chạy là `app`.
   - Chọn máy ảo hoặc thiết bị thật đã kết nối.
   - Nhấn nút **Run** (biểu tượng ▶️) hoặc phím tắt `Shift + F10` để cài đặt và chạy ứng dụng.

### 3. Build File APK Trực Tiếp Bằng Lệnh (Command Line)
Nếu muốn xuất file cài đặt APK Debug mà không cần mở giao diện Android Studio:
* **Trên Windows:**
  ```powershell
  .\gradlew.bat assembleDebug
  ```
* **Trên macOS / Linux:**
  ```bash
  ./gradlew assembleDebug
  ```
File APK sau khi build nằm tại đường dẫn:
```text
app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔑 Tài Khoản Đăng Nhập Mặc Định

Cơ sở dữ liệu tự động tạo sẵn tài khoản quản trị khi ứng dụng khởi chạy lần đầu:
* **Tên đăng nhập**: `admin`
* **Mật khẩu**: `123`
* **Vai trò**: Quản trị viên (`admin`)

*(Người dùng có thể đăng ký tài khoản mới trực tiếp trên màn hình đăng ký của ứng dụng).*

---

