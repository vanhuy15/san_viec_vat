/* ==========================================================================
   FILE: assets/js/job-action.js
   MÔ TẢ: Xử lý hành động của NGƯỜI TÌM VIỆC (Ứng tuyển, Báo cáo)
   ========================================================================== */

document.addEventListener("DOMContentLoaded", function() {
    // 1. HIỆN THÔNG BÁO CHUNG (Dành cho trang Tìm việc / Ứng tuyển)
    const urlParams = new URLSearchParams(window.location.search);
    const status = urlParams.get('status');

    if (status) {
        if (status === 'success') {
            Swal.fire('Thành công!', 'Đơn ứng tuyển đã được gửi.', 'success');
        } else if (status === 'fail') {
            Swal.fire('Chú ý!', 'Bạn đã ứng tuyển công việc này rồi.', 'info');
        } else if (status === 'self_apply_error') {
            Swal.fire('Thao tác sai!', 'Không thể tự ứng tuyển việc của mình.', 'warning');
        }
        
        // Nếu là các status của candidate thì mới xóa URL
        if (['success', 'fail', 'self_apply_error'].includes(status)) {
            const newUrl = window.location.pathname + window.location.search.replace(/[\?&]status=[^&]+/, '').replace(/^&/, '?');
            window.history.replaceState(null, null, newUrl);
        }
    }
});

/**
 * 2. HÀM XỬ LÝ ỨNG TUYỂN
 */
function ungTuyen(jobId) {
    Swal.fire({
        title: 'Xác nhận ứng tuyển?',
        text: "Hãy viết vài lời nhắn cho chủ việc.",
        input: 'textarea',
        inputPlaceholder: 'Ví dụ: Em rảnh sáng mai, có xe máy...',
        showCancelButton: true,
        confirmButtonColor: '#198754',
        confirmButtonText: 'Gửi yêu cầu',
        cancelButtonText: 'Hủy',
        inputValidator: (value) => {
            if (!value) return 'Bạn cần viết vài lời nhắn!';
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'apply-job'; 
            
            const inputId = document.createElement('input');
            inputId.type = 'hidden'; inputId.name = 'jobId'; inputId.value = jobId;
            form.appendChild(inputId);

            const inputMsg = document.createElement('input');
            inputMsg.type = 'hidden'; inputMsg.name = 'message'; inputMsg.value = result.value;
            form.appendChild(inputMsg);

            document.body.appendChild(form);
            form.submit();
        }
    });
}

/**
 * 3. HÀM BÁO CÁO HOÀN THÀNH
 */
function baoCaoHoanThanh(jobId) {
    Swal.fire({
        title: 'Xác nhận đã làm xong?',
        text: "Bạn xác nhận đã hoàn thành công việc này và muốn yêu cầu thanh toán?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#198754', 
        confirmButtonText: 'Đúng, tôi đã làm xong!',
        cancelButtonText: 'Chưa'
    }).then(function(result) {
        if (result.isConfirmed) {
            Swal.showLoading();
            fetch('report-completion', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                body: 'jobId=' + jobId
            })
            .then(function(response) {
                return response.text().then(function(message) {
                    return { ok: response.ok, message: message };
                });
            })
            .then(function(data) {
                if (data.ok) {
                    Swal.fire({ title: 'Thành công!', text: data.message, icon: 'success' })
                        .then(function() { window.location.reload(); });
                } else {
                    Swal.fire('Thất bại', data.message, 'error');
                }
            })
            .catch(function(err) {
                console.error(err);
                Swal.fire('Lỗi', 'Không kết nối được server', 'error');
            });
        }
    });
}