# VuaDacSanAppMobile

Dự án **VuaDacSanAppMobile** là ứng dụng di động Android Native được xây dựng phục vụ việc kinh doanh, bán lẻ đặc sản vùng miền và quản lý vận hành doanh nghiệp toàn diện. Hệ thống tích hợp đầy đủ các phân hệ từ bán hàng cho khách hàng, phân quyền nhân sự, quản lý kho & kế toán, đến chăm sóc khách hàng và khuyến mãi trên nền tảng **SQLite Database**.

---

## 🏗 Cấu Trúc Dự Án

```text
VuaDacSanAppMobile/
├── .github/
│   └── workflows/                # CI/CD Workflows (nếu có cấu hình)
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   ├── com/example/baicuoiki/        # Phân hệ Bán hàng & Nghiệp vụ cốt lõi
│   │   │   │   │   ├── activity/                 # Màn hình Giỏ hàng, Đặt hàng, Nhà cung cấp, Lịch làm...
│   │   │   │   │   ├── adapter/                  # Adapter cho RecyclerView (Sản phẩm, Giỏ hàng...)
│   │   │   │   │   ├── model/                    # Data Models (Product, Order, Customer, Supplier...)
│   │   │   │   │   └── MainActivity.java         # Màn hình điều hướng chính
│   │   │   │   ├── com/example/dangnhap/         # Phân hệ Xác thực & Quản lý Nhân sự
│   │   │   │   │   ├── activities/               # LoginActivity, AdminActivity, NhanVienActivity
│   │   │   │   │   ├── adapters/                 # NhanVienAdapter
│   │   │   │   │   ├── dao/                      # NhanVienDAO, TaiKhoanDAO
│   │   │   │   │   └── models/                   # NhanVien, TaiKhoan
│   │   │   │   ├── com/example/kho_ketoan/       # Phân hệ Quản lý Kho & Kế toán
│   │   │   │   │   ├── activities/               # Quản lý Phiếu kho, Bảng lương, Thống kê doanh thu
│   │   │   │   │   ├── adapters/                 # Adapter phiếu kho, chi tiết phiếu, bảng lương
│   │   │   │   │   └── models/                   # PhieuKho, ChiTietPhieuKho, BangLuong
│   │   │   │   ├── com/example/qlkhuyenmai/      # Phân hệ Khuyến mãi & Chăm sóc khách hàng
│   │   │   │   │   ├── cskh/                     # Tiếp nhận & xử lý yêu cầu khiếu nại (CSKHActivity)
│   │   │   │   │   ├── KhuyenMaiMainActivity.java# Quản lý mã giảm giá, voucher khuyến mãi
│   │   │   │   │   └── KhuyenMaiDAO.java         # Xử lý dữ liệu khuyến mãi
│   │   │   │   └── database/
│   │   │   │       └── DatabaseHelper.java       # Quản trị SQLite (Tạo 13 bảng, quan hệ & seed data)
│   │   │   ├── res/                              # Giao diện XML, icon vector, màu sắc, strings
│   │   │   └── AndroidManifest.xml               # Khai báo quyền, Activity và ứng dụng
│   │   └── test/                                 # Unit Test (JUnit 4)
│   └── build.gradle.kts                          # Cấu hình build & dependencies cấp module app
├── gradle/
│   ├── libs.versions.toml                        # Quản lý phiên bản dependencies tập trung
│   └── wrapper/                                  # Gradle Wrapper
├── .gitignore                                    # Danh sách file và thư mục loại trừ
├── build.gradle.kts                              # Cấu hình build cấp root project
├── gradle.properties                             # Cấu hình JVM & AndroidX
├── gradlew / gradlew.bat                         # Gradle CLI runner cho macOS/Linux và Windows
├── settings.gradle.kts                           # Quản lý repositories và module con
└── README.md                                     # Tài liệu hướng dẫn dự án
```

---

## 📱 Các Phân Hệ & Tính Năng Nổi Bật

| Phân Hệ | Chức Năng Chính | Chi Tiết Nghiệp Vụ |
| :--- | :--- | :--- |
| **🛍️ Mua Sắm & Đặt Hàng** | Catalog Sản phẩm, Giỏ hàng, Đặt hàng | Xem chi tiết đặc sản, lọc danh mục, thêm/bớt số lượng giỏ hàng, áp mã khuyến mãi, lưu hóa đơn vào CSDL. |
| **🔐 Xác Thực & Phân Quyền** | Đăng nhập, Bảo mật, Quản lý tài khoản | Phân quyền 2 vai trò rõ ràng (**Admin** và **Nhân viên**), quản lý OTP, kiểm soát số lần đăng nhập sai. |
| **👥 Quản Lý Nhân Sự** | Hồ sơ nhân viên, Ca làm việc | Thêm/sửa/xóa nhân viên, gán tài khoản, xếp lịch làm việc theo ngày và theo ca, phân công nhiệm vụ. |
| **📦 Quản Lý Kho** | Phiếu nhập kho, Phiếu xuất kho | Quản lý mã phiếu, nhà cung cấp, chi tiết sản phẩm nhập/xuất, tự động cập nhật số lượng tồn kho. |
| **💰 Kế Toán & Lương** | Tính lương, Thống kê tài chính | Quản lý bảng lương theo tháng, tính tổng phụ cấp, khấu trừ, thống kê doanh thu - chi phí xuất/nhập kho. |
| **🎁 Khuyến Mãi & CSKH** | Voucher, Tiếp nhận yêu cầu hỗ trợ | Thiết lập mã giảm giá theo đơn tối thiểu, tiếp nhận và phản hồi phiếu khiếu nại của khách hàng. |

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Ứng Dụng (Khuyến Nghị)

### 1. Yêu Cầu Môi Trường
* **Android Studio**: Phiên bản Jellyfish, Koala hoặc mới hơn.
* **JDK**: Java Development Kit 11 trở lên.
* **Android SDK**: Min SDK 26 (Android 8.0) | Target SDK 35 | Compile SDK 36.
* **Thiết bị**: Máy ảo Android (AVD) hoặc máy thật bật chế độ *USB Debugging*.

### 2. Mở Dự Án Trên Android Studio
1. Khởi động Android Studio.
2. Chọn **Open** và dẫn tới thư mục dự án `BaiCuoiKi` (hoặc `VuaDacSanAppMobile`).
3. Chờ Android Studio tự động tải dependencies và thực hiện **Gradle Sync**.

### 3. Khởi Chạy Ứng Dụng
1. Chọn cấu hình chạy là `app`.
2. Chọn thiết bị đích (máy ảo hoặc máy thật đã kết nối).
3. Bấm nút **Run** (biểu tượng ▶️) hoặc phím tắt `Shift + F10` để biên dịch và cài đặt APK.

---

## 💻 Chạy Bằng Lệnh Gradle (Command Line)

Nếu bạn muốn build và kiểm thử trực tiếp thông qua terminal:

### 1. Biên dịch gói ứng dụng (Build Debug APK)
* **Trên Windows (PowerShell/CMD):**
  ```powershell
  .\gradlew.bat assembleDebug
  ```
* **Trên Linux/macOS:**
  ```bash
  ./gradlew assembleDebug
  ```
File APK hoàn thiện sẽ được xuất tại: `app/build/outputs/apk/debug/app-debug.apk`.

### 2. Chạy Kiểm Thử Đơn Vị (Unit Test)
```powershell
.\gradlew.bat test
```

### 3. Cài Đặt Trực Tiếp Vào Thiết Bị Đã Kết Nối
```powershell
.\gradlew.bat installDebug
```

---

## 🗄 Cơ Sở Dữ Liệu & Tài Khoản Mặc Định

Hệ thống sử dụng **SQLite Database** (`QuanLyHeThong.db`) được khởi tạo tự động khi ứng dụng chạy lần đầu.

### 1. Tài Khoản Đăng Nhập Mặc Định

| Tên Đăng Nhập | Mật Khẩu | Vai Trò | Mã Nhân Viên | Quyền Hạn |
| :--- | :--- | :--- | :--- | :--- |
| **`admin`** | **`123`** | `admin` | `NV01` | Toàn quyền quản trị hệ thống, quản lý nhân viên, kho, lương, khuyến mãi |

> [!NOTE]
> Ngoài tài khoản quản trị, bạn có thể đăng ký tài khoản khách hàng mới ngay trên giao diện ứng dụng hoặc tạo thêm nhân viên từ trang quản trị của Admin.

### 2. Danh Sách 13 Bảng Dữ Liệu
* **`TAI_KHOAN`**: Quản lý thông tin đăng nhập, mật khẩu, trạng thái khóa, mã OTP.
* **`NHAN_VIEN`**: Quản lý thông tin cá nhân, CCCD, chức vụ, tài khoản liên kết.
* **`SAN_PHAM`**: Danh mục đặc sản, giá bán, số lượng tồn kho, hạn sử dụng, nhà cung cấp.
* **`NHA_CUNG_CAP`**: Danh sách đối tác phân phối đặc sản.
* **`LICH_LAM_VIEC`**: Lịch trực ca, nhiệm vụ của nhân viên.
* **`PHIEU_KHO` & `CHI_TIET_PHIEU_KHO`**: Giao dịch nhập/xuất kho hàng hóa.
* **`BANG_LUONG`**: Bảng tính lương theo tháng cho từng nhân sự.
* **`KHACH_HANG`**: Hồ sơ khách hàng và lịch sử mua sắm.
* **`HOA_DON` & `CHI_TIET_HOA_DON`**: Đơn hàng, phương thức thanh toán, trạng thái giao vận.
* **`KHUYEN_MAI`**: Mã voucher, tỉ lệ chiết khấu, thời hạn sử dụng.
* **`YEU_CAU_HO_TRO`**: Phiếu tiếp nhận yêu cầu CSKH và phản hồi từ nhân viên.

---

## 🛠 Công Nghệ & Thư Viện Sử Dụng

| Tên Công Nghệ / Thư Viện | Phiên Bản | Mục Đích Sử Dụng |
| :--- | :--- | :--- |
| **Java** | 11 | Ngôn ngữ lập trình chính của ứng dụng |
| **Android SDK** | Compile 36 / Target 35 / Min 26 | Nền tảng phát triển ứng dụng di động Android Native |
| **SQLite (SQLiteOpenHelper)** | v3 | Cơ sở dữ liệu quan hệ cục bộ lưu trữ toàn bộ nghiệp vụ |
| **Material Components** | 1.12.0+ | Giao diện chuẩn Material Design (Buttons, Dialogs, Cards...) |
| **Glide** | 4.16.0 | Tối ưu hóa tải và hiển thị hình ảnh sản phẩm mượt mà |
| **Apache POI** | 5.2.3 | Hỗ trợ đọc/ghi và xuất báo cáo dữ liệu sang định dạng Excel |
| **Log4j & Woodstox** | Latest | Hỗ trợ xử lý định dạng XML và ghi nhật ký hệ thống |

---

## 👨‍💻 Tác Giả & Đóng Góp
- **Repository**: [https://github.com/mihtan05/VuaDacSanAppMobile](https://github.com/mihtan05/VuaDacSanAppMobile)
- **Tác giả**: [mihtan05](https://github.com/mihtan05)
