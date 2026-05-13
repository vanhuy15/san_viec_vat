/* web/assets/js/script.js - File JS duy nhất cho toàn bộ website */

$(document).ready(function() {
    // ==========================================
    // 1. XỬ LÝ TOAST NOTIFICATION (Thông báo nổi)
    // ==========================================
    $('.toast').toast({
        delay: 5000 
    });
    $('.toast').toast('show');

    // ==========================================
    // 2. KHỞI TẠO BOOTSTRAP 5 COMPONENTS
    // ==========================================
    
    // Kích hoạt Tooltip
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Kích hoạt Dropdown (Fix lỗi trên một số thiết bị)
    var dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'));
    var dropdownList = dropdownElementList.map(function (dropdownToggleEl) {
        return new bootstrap.Dropdown(dropdownToggleEl);
    });
});

// ==========================================
// 3. CÁC HÀM XỬ LÝ SỰ KIỆN (GLOBAL FUNCTIONS)
// ==========================================

/* --- Hàm xác nhận xóa (Dùng chung) --- */
function confirmDelete(message) {
    return confirm(message || "Bạn có chắc chắn muốn xóa mục này không? Hành động này không thể hoàn tác.");
}

/* --- Hàm Validate Form Đăng Nhập (Từ auth.js cũ) --- */
function validateLogin() {
    let userField = document.getElementById("username");
    let passField = document.getElementById("password");

    // Kiểm tra xem trang hiện tại có form đăng nhập không để tránh lỗi null
    if (userField && passField) {
        let user = userField.value.trim();
        let pass = passField.value.trim();

        if (user === "") {
            alert("Vui lòng nhập tên đăng nhập!");
            userField.focus();
            return false;
        }
        
        if (pass === "") {
            alert("Vui lòng nhập mật khẩu!");
            passField.focus();
            return false;
        }
    }
    return true;
}