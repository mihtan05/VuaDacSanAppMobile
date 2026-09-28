# VuaDacSanAppMobile

Dự án **VuaDacSanAppMobile** là ứng dụng di động Android Native viết bằng **Java**, sử dụng hệ cơ sở dữ liệu **SQLite** cục bộ. Ứng dụng cung cấp giải pháp kép: giao diện mua hàng cho khách hàng và bộ công cụ quản lý vận hành (sản phẩm, nhân sự, lịch làm việc, kho - kế toán, khuyến mãi và chăm sóc khách hàng) cho quản trị viên.

---

## 🏗 Cấu Trúc Thư Mục Dự Án

```text
VuaDacSanAppMobile/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   ├── com/example/baicuoiki/        # Nghiệp vụ mua sắm & tiện ích
│   │       │   │   ├── activity/                 # CartActivity, CheckoutActivity, CustomerActivity, LichLamActivity, OrderActivity, OrderDetailActivity, ProductActivity, ProductDetailActivity, SupplierActivity
│   │       │   │   ├── adapter/                  # CartAdapter, CustomerAdapter, LichLamViecAdapter, OrderAdapter, OrderDetailAdapter, ProductAdapter, SupplierAdapter
│   │       │   │   ├── model/                    # CartItem, CartManager, Customer, LichLamViec, Order, OrderDetail, Product, Supplier
│   │       │   │   ├── ExcelHelper.java          # Tiện ích xuất dữ liệu ra file Excel (.xlsx)
│   │       │   │   ├── MainActivity.java         # Màn hình chính phân quyền hiển thị (Admin / Khách hàng)
│   │       │   │   └── RegisterActivity.java     # Đăng ký tài khoản khách hàng mới
│   │       │   ├── com/example/dangnhap/         # Xác thực & Quản lý nhân sự
│   │       │   │   ├── activities/               # LoginActivity, AdminActivity, NhanVienActivity
│   │       │   │   ├── adapters/                 # NhanVienAdapter
│   │       │   │   ├── dao/                      # NhanVienDAO, TaiKhoanDAO
│   │       │   │   └── models/                   # NhanVien, TaiKhoan
│   │       │   ├── com/example/kho_ketoan/       # Quản lý kho, tính lương & thống kê
│   │       │   │   ├── activities/               # KhoKeToanMainActivity, PhieuKhoActivity, AddEditPhieuKhoActivity, ChiTietPhieuKhoActivity, BangLuongActivity, ThongKeActivity
│   │       │   │   ├── adapters/                 # PhieuKhoAdapter, ChiTietPhieuKhoAdapter, BangLuongAdapter
│   │       │   │   └── models/                   # PhieuKho, ChiTietPhieuKho, BangLuong
│   │       │   ├── com/example/qlkhuyenmai/      # Khuyến mãi & Chăm sóc khách hàng
│   │       │   │   ├── cskh/                     # CSKHActivity, YeuCauAdapter, YeuCauDAO, YeuCauHoTro
│   │       │   │   ├── KhuyenMaiMainActivity.java# Quản lý voucher, mã giảm giá
│   │       │   │   └── KhuyenMaiDAO.java         # Thao tác CSDL khuyến mãi
│   │       │   └── database/
│   │       │       └── DatabaseHelper.java       # SQLite helper khởi tạo & quản trị 13 bảng CSDL
│   │       ├── res/                              # Layouts XML, Drawables, Values (Colors, Strings, Themes), Menus
│   │       └── AndroidManifest.xml               # Khai báo các Activity và quyền (Internet, Storage)
│   └── build.gradle.kts                          # Cấu hình SDK, dependencies của app
├── gradle/
│   ├── libs.versions.toml                        # Khai báo phiên bản thư viện
│   └── wrapper/                                  # Gradle Wrapper
├── .gitignore                                    # File cấu hình bỏ qua Git (.idea, build, .gradle...)
├── build.gradle.kts                              # Cấu hình root project
├── gradle.properties                             # Cấu hình JVM & AndroidX
├── gradlew / gradlew.bat                         # Gradle CLI Script cho macOS/Linux và Windows
└── settings.gradle.kts                           # Khai báo settings & repositories
```

---

## 📱 Các Chức Năng Hiện Có Trong Ứng Dụng

Ứng dụng tự động điều hướng và hiển thị giao diện theo **Vai trò người dùng** sau khi đăng nhập:

### 1. Phân Hệ Xác Thực (`com.example.dangnhap`)
- **Đăng nhập (`LoginActivity`)**: Kiểm tra thông tin trong bảng `TAI_KHOAN`, lưu trạng thái phiên và vai trò (`admin` hoặc `customer`) qua `SharedPreferences`.
- **Đăng ký (`RegisterActivity`)**: Cho phép người dùng đăng ký tài khoản khách hàng mới, lưu đồng thời vào bảng `TAI_KHOAN` và `KHACH_HANG`.

### 2. Giao Diện Dành Cho Khách Hàng (`customer`)
- **Trang chủ (`MainActivity`)**: Hiển thị danh sách sản phẩm đặc sản dưới dạng lưới (`GridView`), có ô tìm kiếm sản phẩm theo tên.
- **Chi tiết sản phẩm (`ProductDetailActivity`)**: Xem thông tin chi tiết, giá bán, mô tả, chọn số lượng và thêm vào giỏ hàng.
- **Giỏ hàng (`CartActivity`)**: Quản lý sản phẩm đang chọn, tăng/giảm số lượng hoặc xóa món.
- **Thanh toán (`CheckoutActivity`)**: Nhập thông tin nhận hàng, chọn phương thức thanh toán, áp dụng mã khuyến mãi và tạo hóa đơn lưu vào CSDL.
- **Đơn hàng (`OrderActivity`, `OrderDetailActivity`)**: Xem danh sách hóa đơn đã đặt và chi tiết từng đơn hàng.
- **Hỗ trợ khách hàng (`CSKHActivity`)**: Gửi yêu cầu trợ giúp/khiếu nại về đơn hàng và sản phẩm.

### 3. Giao Diện Quản Trị Viên (`admin`)
Khi đăng nhập bằng tài khoản Admin, trang chủ `MainActivity` mở khóa toàn bộ menu quản trị:
- **Quản lý Sản phẩm (`ProductActivity`)**: Xem danh sách, thêm, chỉnh sửa thông tin và xóa sản phẩm đặc sản.
- **Quản lý Nhà cung cấp (`SupplierActivity`)**: Thêm, sửa, xóa thông tin các nhà cung cấp đặc sản.
- **Lịch làm việc (`LichLamActivity`)**: 
  - Phân công ca trực cho nhân viên theo ngày.
  - Hỗ trợ thêm lịch làm việc hàng loạt.
  - **Xuất lịch làm việc ra file Excel (`.xlsx`)** vào thư mục `Downloads` thông qua `ExcelHelper` (Apache POI).
- **Quản lý Nhân sự (`NhanVienActivity`)**: Danh sách nhân viên, thông tin CCCD, ngày sinh, vai trò và tài khoản liên kết.
- **Quản lý Khách hàng (`CustomerActivity`)**: Xem danh sách, thêm, cập nhật thông tin khách hàng.
- **Phân hệ Kho & Kế toán (`KhoKeToanMainActivity`)**:
  - **Phiếu kho (`PhieuKhoActivity`)**: Tạo và quản lý phiếu nhập kho/xuất kho kèm chi tiết số lượng, đơn giá từng sản phẩm.
  - **Bảng lương (`BangLuongActivity`)**: Quản lý lương nhân viên theo tháng (Lương cơ bản + Phụ cấp - Khấu trừ = Tổng lương).
  - **Thống kê (`ThongKeActivity`)**: Thống kê tổng tiền nhập kho, xuất kho và tổng chi phí lương theo từng tháng.
- **Quản lý Khuyến mãi (`KhuyenMaiMainActivity`)**: Tạo và quản lý mã giảm giá, giá trị giảm, đơn hàng tối thiểu và ngày hết hạn.
- **Chăm sóc Khách hàng (`CSKHActivity`)**: Xem danh sách khiếu nại, phản hồi nội dung và cập nhật trạng thái yêu cầu hỗ trợ.

---

## 🗄 Cơ Sở Dữ Liệu SQLite (`QuanLyHeThong.db`)

Dữ liệu được quản lý tập trung qua file `DatabaseHelper.java` với **13 bảng quan hệ**:

| Tên Bảng | Mô Tả Dữ Liệu |
| :--- | :--- |
| `TAI_KHOAN` | Lưu `tenDangnhap`, `matKhau`, `trangThai`, `soLanDangNhapSai`, `maOTP`, `thoiGianHetHanOTP` |
| `NHAN_VIEN` | Thông tin nhân viên, họ tên, SĐT, email, CCCD, ngày sinh, vai trò |
| `SAN_PHAM` | Tên đặc sản, hình ảnh, mô tả, đơn vị tính, đơn giá, tồn kho, HSD, mã NCC |
| `NHA_CUNG_CAP` | Thông tin đối tác cung cấp (Tên, SĐT, email, địa chỉ) |
| `LICH_LAM_VIEC` | Ca làm việc, ngày làm, nhiệm vụ được phân công cho nhân viên |
| `PHIEU_KHO` | Phiếu nhập/xuất, ngày lập, tổng tiền, nhân viên lập, mã NCC, trạng thái |
| `CHI_TIET_PHIEU_KHO` | Chi tiết từng sản phẩm, số lượng và đơn giá trong phiếu kho |
| `BANG_LUONG` | Bảng tính lương theo tháng (Lương CB, phụ cấp, khấu trừ, tổng lương) |
| `KHACH_HANG` | Hồ sơ khách hàng, SĐT, email, địa chỉ, tài khoản liên kết |
| `HOA_DON` | Hóa đơn bán hàng, ngày tạo, phương thức thanh toán, tổng tiền, trạng thái |
| `CHI_TIET_HOA_DON` | Danh sách sản phẩm, số lượng và giá bán trong mỗi hóa đơn |
| `KHUYEN_MAI` | Mã voucher, loại mã, giá trị giảm, đơn tối thiểu, ngày kết thúc |
| `YEU_CAU_HO_TRO` | Phiếu phản hồi/khiếu nại của khách hàng và nội dung trả lời từ CSKH |

### Tài Khoản Quản Trị Mặc Định (Seed Data)
Cơ sở dữ liệu tự động tạo sẵn 1 tài khoản Admin khi cài đặt lần đầu:
- **Tên đăng nhập**: `admin`
- **Mật khẩu**: `123`
- **Vai trò**: `admin` (Mã nhân viên: `NV01`)

---

## 🛠 Thư Viện & Công Nghệ Thực Tế

| Thành phần | Phiên bản / Chi tiết | Mục đích sử dụng |
| :--- | :--- | :--- |
| **Java** | Version 11 | Ngôn ngữ phát triển toàn bộ mã nguồn |
| **Android SDK** | Min SDK 26, Target SDK 35, Compile SDK 36 | Nền tảng phát triển ứng dụng di động Android Native |
| **SQLite (SQLiteOpenHelper)** | Database Version 3 | CSDL quan hệ lưu trữ dữ liệu offline trực tiếp trên thiết bị |
| **Material Components** | 1.12.0 | Giao diện chuẩn (Buttons, Cards, Dialogs, Bottom Navigation...) |
| **Glide** | 4.16.0 | Tải và hiển thị hình ảnh sản phẩm mượt mà |
| **Apache POI** | 5.2.3 (poi, poi-ooxml) | Hỗ trợ xuất dữ liệu (Lịch làm việc) ra file định dạng Excel (`.xlsx`) |

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

1. Mở thư mục dự án bằng **Android Studio**.
2. Đợi Android Studio hoàn tất quá trình **Sync Gradle** tải các thư viện.
3. Kết nối máy ảo Android (API 26+) hoặc máy thật qua cổng USB (bật *USB Debugging*).
4. Nhấn nút **Run** (biểu tượng ▶️) để biên dịch và cài đặt ứng dụng lên máy.

Hoặc build file APK Debug trực tiếp từ dòng lệnh:
```powershell
.\gradlew.bat assembleDebug
```
File APK sinh ra tại thư mục: `app/build/outputs/apk/debug/app-debug.apk`.

---

## 👨‍💻 Thông Tin Dự Án
- **Repository**: [https://github.com/mihtan05/VuaDacSanAppMobile](https://github.com/mihtan05/VuaDacSanAppMobile)
- **Tác giả**: [mihtan05](https://github.com/mihtan05)
