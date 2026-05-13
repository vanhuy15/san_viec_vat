/* ==========================================================================
   FILE: assets/js/manage-job.js
   MÔ TẢ: Xử lý logic cho trang Quản lý công việc (Chủ việc)
   ========================================================================== */

document.addEventListener("DOMContentLoaded", function() {
    
    // 1. XỬ LÝ CÁC THÔNG BÁO TỰ ĐỘNG (Dựa vào URL param)
    const urlParams = new URLSearchParams(window.location.search);
    const status = urlParams.get('status');
    const jobId = urlParams.get('id');

    if (status) {
        if (status === 'approved') {
            Swal.fire('Thành công!', 'Đã giao việc và trừ tiền cọc.', 'success');
        } 
        else if (status === 'completed') {
            Swal.fire({
                title: 'Thành công!',
                text: 'Đã xác nhận hoàn thành, trả lương và đánh giá người làm!',
                icon: 'success',
                confirmButtonColor: '#198754'
            });
        }

        // Xóa param status trên URL để F5 không hiện lại, giữ lại param id
        if (['approved', 'completed'].includes(status)) {
            let newUrl = window.location.pathname;
            if (jobId) newUrl += "?id=" + jobId;
            window.history.replaceState(null, null, newUrl);
        }
    }

    // 2. XỬ LÝ LỖI TỪ SERVER (Dựa vào thẻ input hidden)
    const errorInput = document.getElementById("server-error-msg");
    if (errorInput && errorInput.value.trim() !== "") {
        Swal.fire({
            title: 'Thất bại',
            text: errorInput.value,
            icon: 'error',
            confirmButtonColor: '#dc3545'
        });
    }
});

/**
 * 3. HÀM DUYỆT NGƯỜI LÀM (Gửi Ajax để trừ tiền và đổi trạng thái)
 */
function duyetNguoi(maUngTuyen, tenWorker, workerId) {
    const urlParams = new URLSearchParams(window.location.search);
    const jobId = urlParams.get('id');

    if (!jobId) {
        Swal.fire('Lỗi', 'Không tìm thấy ID công việc trên URL', 'error');
        return;
    }

    Swal.fire({
        title: 'Duyệt ' + tenWorker + '?',
        text: "Hệ thống sẽ TRỪ TIỀN ví ngay lập tức để tạm giữ. Bạn chắc chắn chứ?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Đồng ý & Trừ tiền',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            
            // Hiện loading
            Swal.fire({
                title: 'Đang xử lý...',
                html: 'Vui lòng chờ trong giây lát.',
                allowOutsideClick: false,
                didOpen: () => { Swal.showLoading(); }
            });

            // Gọi Servlet
            fetch('approve-worker', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                },
                body: 'id=' + jobId + '&workerId=' + workerId
            })
            .then(response => response.text().then(text => ({ status: response.status, message: text })))
            .then(data => {
                if (data.status === 200) {
                    // Thành công -> Reload trang với param approved
                    window.location.href = window.location.pathname + "?id=" + jobId + "&status=approved";
                } else {
                    Swal.fire('Thất bại!', data.message, 'error');
                }
            })
            .catch(error => {
                console.error(error);
                Swal.fire('Lỗi server', 'Không thể kết nối.', 'error');
            });
        }
    });
}

/**
 * 4. HÀM CHỌN SAO ĐÁNH GIÁ (Xử lý giao diện Modal)
 */
function setRating(starCount) {
    // Cập nhật giá trị vào input ẩn
    const ratingInput = document.getElementById('rating-value');
    if(ratingInput) ratingInput.value = starCount;
    
    // Đổi màu các ngôi sao
    let stars = document.querySelectorAll('.rating-stars i');
    stars.forEach((star, index) => {
        if (index < starCount) {
            star.classList.remove('fa-regular'); // Bỏ sao rỗng
            star.classList.add('fa-solid');      // Thêm sao đặc
            star.style.color = '#ffc107';        // Màu vàng
        } else {
            star.classList.remove('fa-solid');
            star.classList.add('fa-regular');    // Thêm sao rỗng
            star.style.color = '#ccc';           // Màu xám
        }
    });
}