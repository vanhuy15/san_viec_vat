/* assets/js/my-applications.js */

/**
 * Hàm xử lý khi người làm bấm "Báo xong"
 * Sử dụng SweetAlert2 để xác nhận trước khi gửi request
 */
function baoCaoHoanThanh(jobId) {
    Swal.fire({
        title: 'Xác nhận hoàn thành?',
        text: "Bạn xác nhận đã làm xong công việc này? Chủ nhà sẽ nhận được thông báo để kiểm tra.",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#0d6efd', // Màu xanh primary
        cancelButtonColor: '#6c757d',  // Màu xám secondary
        confirmButtonText: 'Đúng, báo xong!',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            // Tạo một form ẩn để submit (thay thế cho form HTML cũ)
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'report-completion'; // Servlet xử lý báo cáo hoàn thành
            
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'jobId';
            input.value = jobId;
            
            form.appendChild(input);
            document.body.appendChild(form);
            form.submit();
        }
    });
}

// Nếu bạn muốn hiển thị Toast message đẹp hơn khi load trang (thay thế alert của Bootstrap)
document.addEventListener("DOMContentLoaded", function() {
    // Code xử lý các hiệu ứng khác nếu cần
});