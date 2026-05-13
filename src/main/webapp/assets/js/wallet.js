/* ==============================================
   FILE: assets/js/wallet.js
   MÔ TẢ: Xử lý các logic của trang Ví (QR Code, Format tiền, Input tên)
   ============================================== */

/** Helper: Chuyển tên ngân hàng trong DB thành mã ngắn VietQR */
function mapBankName(dbBankName) {
    if (!dbBankName) return "";
    const map = {
        "MBBank": "MB", "MB": "MB", "Vietcombank": "VCB", "Vietinbank": "ICB", 
        "BIDV": "BIDV", "Agribank": "VBA", "Techcombank": "TCB", "ACB": "ACB", 
        "TPBank": "TPB", "VPBank": "VPB", "Sacombank": "STB"
    };
    return map[dbBankName] || dbBankName;
}

/** 1. ĐỊNH DẠNG TIỀN (Thêm dấu phẩy khi nhập) */
function formatCurrencyInput(input) {
    let rawValue = input.value.replace(/\D/g, ""); 
    if (rawValue === "") { input.value = ""; return; }
    input.value = new Intl.NumberFormat('en-US').format(rawValue);
    
    let hiddenInput = document.getElementById(input.id + "_hidden");
    if (hiddenInput) { hiddenInput.value = rawValue; }
}

/** 2. TẠO QR NẠP TIỀN & HIỂN THỊ THÔNG TIN ADMIN */
function taoQRNapTien(username) {
    // A. Lấy thông tin Admin từ input ẩn trong JSP
    let dbTenNganHang = document.getElementById("admin_ten_ngan_hang").value;
    let dbSoTaiKhoan = document.getElementById("admin_so_tai_khoan").value;
    let dbTenChuTaiKhoan = document.getElementById("admin_ten_chu_tai_khoan").value;
    
    // B. Lấy số tiền thực
    let rawAmount = document.getElementById("amountIn_hidden").value;
    let amount = parseInt(rawAmount);

    // Validate
    if (!amount || amount < 10000) {
        Swal.fire({ icon: 'error', title: 'Số tiền không hợp lệ', text: 'Vui lòng nạp tối thiểu 10,000 VNĐ' });
        return;
    }
    if (!dbSoTaiKhoan) {
        Swal.fire({ icon: 'error', title: 'Lỗi hệ thống', text: 'Admin chưa cập nhật tài khoản nhận tiền!' });
        return;
    }

    // C. Tạo QR
    let bankIdVietQR = mapBankName(dbTenNganHang);
    let content = "NAPTIEN " + username.toUpperCase(); 
    let qrUrl = `https://img.vietqr.io/image/${bankIdVietQR}-${dbSoTaiKhoan}-compact.png?amount=${amount}&addInfo=${encodeURIComponent(content)}&accountName=${encodeURIComponent(dbTenChuTaiKhoan)}`;

    // D. CẬP NHẬT GIAO DIỆN BƯỚC 2
    
    // 1. Ảnh QR (Đã xóa dòng hiển thị tiền 'display-amount' vì bạn yêu cầu bỏ)
    document.getElementById("img-qr-code").src = qrUrl;
    
    // 2. Điền thông tin ngân hàng Admin
    document.getElementById("qr-bank-name").innerText = dbTenNganHang;
    document.getElementById("qr-bank-acc").innerText = dbSoTaiKhoan;
    document.getElementById("qr-acc-holder").innerText = dbTenChuTaiKhoan;
    
    // 3. Nội dung chuyển khoản
    document.getElementById("display-content").innerText = content;
    
    // 4. Set giá trị cho form submit cuối cùng
    document.getElementById("hidden-amount-submit").value = amount;

    // E. HIỂN THỊ BƯỚC 2, ẨN BƯỚC 1
    document.getElementById("step1-nap").style.display = "none";
    document.getElementById("step2-nap").style.display = "block";
}

/** Helper: Copy số tài khoản */
function copyToClipboard() {
    let text = document.getElementById("qr-bank-acc").innerText;
    navigator.clipboard.writeText(text).then(() => {
        Swal.fire({
            icon: 'success',
            title: 'Đã sao chép!',
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1500
        });
    });
}

/** 3. XÁC NHẬN RÚT TIỀN */
function xacNhanRutTien() {
    let rawAmount = document.getElementById("amountOut_hidden").value;
    let amount = parseInt(rawAmount);
    if (!amount || amount < 50000) {
        Swal.fire({ icon: 'error', title: 'Không thể rút', text: 'Số tiền rút tối thiểu là 50,000 VNĐ' });
        return false; 
    }
    return true; 
}

/** 4. XỬ LÝ TÊN CHỦ THẺ */
function xuLyTenChuThe(input) {
    let str = input.value.toUpperCase();
    str = str.normalize("NFD").replace(/[\u0300-\u036f]/g, "").replace(/Đ/g, "D");
    input.value = str;
}