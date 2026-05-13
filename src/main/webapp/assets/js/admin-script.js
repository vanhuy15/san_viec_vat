/* ==============================================
   FILE: assets/js/admin-script.js
   MÔ TẢ: Xử lý logic JS cho Admin Dashboard
   ============================================== */

document.addEventListener("DOMContentLoaded", function() {
    
    // --- 1. HIỆU ỨNG SỐ CHẠY ---
    const counters = document.querySelectorAll('.h2.fw-bold');
    counters.forEach(counter => {
        const originalText = counter.innerText;
        const target = +originalText.replace(/\D/g, ''); 
        if (target > 0) {
            let count = 0;
            const increment = target / 70; 
            const updateCount = () => {
                count += increment;
                if (count < target) {
                    counter.innerText = Math.ceil(count).toLocaleString('vi-VN');
                    requestAnimationFrame(updateCount);
                } else {
                    counter.innerText = originalText;
                }
            };
            updateCount();
        }
    });

    // --- 2. VẼ BIỂU ĐỒ ---
    const canvas = document.getElementById('jobChart'); 
    if (canvas) {
        const ctx = canvas.getContext('2d');
        const rawLabels = canvas.dataset.labels || "[]";
        const rawNew = canvas.dataset.new || "[]";
        const rawDone = canvas.dataset.done || "[]";

        let labels = [], dataNew = [], dataDone = [];
        try {
            labels = JSON.parse(rawLabels.replace(/'/g, '"'));
            dataNew = JSON.parse(rawNew);
            dataDone = JSON.parse(rawDone);
        } catch (e) { console.error("Chart Data Parse Error", e); }

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [
                    { 
                        label: 'Việc đăng mới', 
                        data: dataNew, 
                        backgroundColor: '#4e73df', 
                        hoverBackgroundColor: '#2e59d9',
                        borderRadius: 5, 
                        barPercentage: 0.6
                    },
                    { 
                        label: 'Việc hoàn thành', 
                        data: dataDone, 
                        backgroundColor: '#1cc88a',
                        hoverBackgroundColor: '#17a673',
                        borderRadius: 5,
                        barPercentage: 0.6
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { 
                    legend: { position: 'bottom', labels: { usePointStyle: true } },
                    tooltip: { mode: 'index', intersect: false }
                },
                scales: { 
                    y: { beginAtZero: true, ticks: { stepSize: 1 }, grid: { borderDash: [2], drawBorder: false } },
                    x: { grid: { display: false } }
                }
            }
        });
    }
});

// --- 3. HÀM MAP TÊN NGÂN HÀNG SANG MÃ VIETQR ---
function mapBankName(fullName) {
    if (!fullName) return "";
    // Danh sách map mã ngân hàng phổ biến tại VN
    const map = { 
        "MBBank": "MB", "MB": "MB", "Quân Đội": "MB",
        "Vietcombank": "VCB", "Ngoại Thương": "VCB",
        "Vietinbank": "ICB", "Công Thương": "ICB",
        "BIDV": "BIDV", 
        "Agribank": "VBA", 
        "Techcombank": "TCB", 
        "ACB": "ACB", "Á Châu": "ACB",
        "TPBank": "TPB", 
        "VPBank": "VPB", 
        "Sacombank": "STB",
        "HDBank": "HDB",
        "VIB": "VIB"
    };
    return map[fullName] || fullName; // Nếu không tìm thấy thì trả về tên gốc
}

// --- 4. HÀM HIỂN THỊ POPUP QR (TÍCH HỢP VIETQR API) ---
function showWithdrawQR(userBankName, userBankAcc, userAccName, amount, content) {
    if(!userBankName || !userBankAcc) {
        Swal.fire({ icon: 'error', title: 'Thiếu thông tin', text: 'User chưa cập nhật ngân hàng!' });
        return;
    }

    // A. Tìm Template trong JSP
    const template = document.getElementById('qr-popup-template');
    if (!template) {
        console.error("Lỗi: Không tìm thấy template QR trong JSP");
        return;
    }

    // B. Tạo bản sao Template
    const clone = template.content.cloneNode(true);
    
    // C. Tạo Link VietQR API (Đây là phần tích hợp API)
    // Cú pháp: https://img.vietqr.io/image/<BANK_ID>-<ACCOUNT>-<TEMPLATE>.png
    let bankId = mapBankName(userBankName);
    let qrUrl = `https://img.vietqr.io/image/${bankId}-${userBankAcc}-compact.png?amount=${amount}&addInfo=${encodeURIComponent(content)}&accountName=${encodeURIComponent(userAccName)}`;

    // D. Điền dữ liệu vào bản sao
    clone.getElementById('tpl-qr-img').src = qrUrl;
    clone.getElementById('tpl-bank-name').textContent = userBankName;
    clone.getElementById('tpl-bank-acc').textContent = userBankAcc;
    clone.getElementById('tpl-acc-name').textContent = userAccName;
    clone.getElementById('tpl-amount').textContent = new Intl.NumberFormat('vi-VN').format(amount) + ' đ';

    // E. Chuyển đổi thành HTML String để ném vào SweetAlert
    const div = document.createElement('div');
    div.appendChild(clone);
    const htmlContent = div.innerHTML;

    // F. Hiển thị Popup
    Swal.fire({
        title: 'THÔNG TIN CHUYỂN KHOẢN',
        html: htmlContent, 
        showConfirmButton: false, 
        showCloseButton: true,
        width: 450,
        padding: '1.25em'
    });
}

// --- 5. HÀM XỬ LÝ DUYỆT / HỦY GIAO DỊCH ---
function handleTransaction(id, action, type) {
    let actionText = action === 'approve' ? 'DUYỆT' : 'HỦY';
    let btnColor = action === 'approve' ? '#1cc88a' : '#e74a3b';
    let warningText = "";

    if (action === 'approve') {
        if (type === 'NAP_TIEN') warningText = "Tiền sẽ được cộng vào ví User.";
        else if (type === 'RUT_TIEN') warningText = "Xác nhận bạn ĐÃ CHUYỂN KHOẢN cho User xong.";
    } else {
        if (type === 'RUT_TIEN') warningText = "Tiền sẽ được HOÀN LẠI vào ví User.";
        else warningText = "Giao dịch sẽ bị hủy bỏ.";
    }

    Swal.fire({
        title: `Xác nhận ${actionText}?`,
        text: warningText,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: btnColor,
        cancelButtonColor: '#858796',
        confirmButtonText: 'Đồng ý',
        cancelButtonText: 'Thoát'
    }).then((result) => {
        if (result.isConfirmed) {
            let form = document.createElement('form');
            form.method = 'POST';
            form.action = 'admin-transactions';

            let inputId = document.createElement('input');
            inputId.type = 'hidden';
            inputId.name = 'id';
            inputId.value = id;
            form.appendChild(inputId);

            let inputAction = document.createElement('input');
            inputAction.type = 'hidden';
            inputAction.name = 'action';
            inputAction.value = action;
            form.appendChild(inputAction);
            
            const urlParams = new URLSearchParams(window.location.search);
            let currentType = urlParams.get('type') || 'ALL';
            let inputType = document.createElement('input');
            inputType.type = 'hidden';
            inputType.name = 'type';
            inputType.value = currentType;
            form.appendChild(inputType);

            document.body.appendChild(form);
            form.submit();
        }
    });
}

// --- 6. CÁC HÀM ĐIỀU HƯỚNG ---
function filterTransactions(type) { window.location.href = 'admin-transactions?type=' + type; }
function filterJobs(status) { window.location.href = 'admin-jobs?status=' + status; }
function resolveReport(reportId, action) {
    let actionTitle = action === 1 ? 'HOÀN TIỀN (NGƯỜI THUÊ)' : 'TRẢ LƯƠNG (NGƯỜI LÀM)';
    let btnColor = action === 1 ? '#1cc88a' : '#e74a3b'; 

    Swal.fire({
        title: 'Xử lý khiếu nại',
        text: `Bạn chọn: ${actionTitle}`,
        icon: 'warning',
        input: 'textarea',
        inputPlaceholder: 'Nhập lý do...',
        showCancelButton: true,
        confirmButtonColor: btnColor,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = `admin-reports?action=resolve&id=${reportId}&type=${action}&note=${encodeURIComponent(result.value)}`;
        }
    });
}