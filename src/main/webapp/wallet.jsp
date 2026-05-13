<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Ví Điện Tử - Sàn Việc Vặt</title>
</head>
<body class="bg-light">

    <jsp:include page="header.jsp"></jsp:include>

    <input type="hidden" id="admin_ten_ngan_hang" value="${adminBank.tenNganHang}">
    <input type="hidden" id="admin_so_tai_khoan" value="${adminBank.soTaiKhoanNganHang}">
    <input type="hidden" id="admin_ten_chu_tai_khoan" value="${adminBank.tenChuTaiKhoan}">

    <div class="container mt-4 mb-5">
        
        <c:if test="${not empty message}">
            <div class="alert alert-${msgType} alert-dismissible fade show shadow-sm">
                <i class="fa-solid fa-bell"></i> ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="row">
            <div class="col-lg-5 mb-4">
                <div class="card bg-dark text-white shadow mb-3 border-0">
                    <div class="card-body text-center py-4">
                        <p class="mb-1 opacity-75 text-uppercase small fw-bold">Số dư hiện tại</p>
                        <h2 class="display-4 fw-bold text-warning mb-0">
                            <fmt:formatNumber value="${sessionScope.acc.soDuVi}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                        </h2>
                    </div>
                </div>

                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white p-0">
                        <ul class="nav nav-tabs nav-fill" id="walletTab" role="tablist">
                            <li class="nav-item">
                                <button class="nav-link active py-3 fw-bold" id="nap-tab" data-bs-toggle="tab" data-bs-target="#nap-content">NẠP TIỀN</button>
                            </li>
                            <li class="nav-item">
                                <button class="nav-link py-3 fw-bold" id="rut-tab" data-bs-toggle="tab" data-bs-target="#rut-content">RÚT TIỀN</button>
                            </li>
                            <li class="nav-item">
                                <button class="nav-link py-3 fw-bold" id="bank-tab" data-bs-toggle="tab" data-bs-target="#bank-content">BANK</button>
                            </li>
                        </ul>
                    </div>

                    <div class="card-body p-4">
                        <div class="tab-content" id="walletTabContent">
                            
                            <div class="tab-pane fade show active" id="nap-content" role="tabpanel">
                                <div id="step1-nap">
                                    <label class="form-label fw-bold text-secondary">Nhập số tiền muốn nạp (Min 10k)</label>
                                    <div class="input-group mb-4">
                                        <input type="text" id="amountIn" class="form-control form-control-lg fw-bold text-success" 
                                                oninput="formatCurrencyInput(this)">
                                        <span class="input-group-text fw-bold">VNĐ</span>
                                        <input type="hidden" id="amountIn_hidden">
                                    </div>
                                    <button onclick="taoQRNapTien('${sessionScope.acc.tenDangNhap}')" class="btn btn-primary w-100 fw-bold py-2 shadow-sm">
                                        <i class="fa-solid fa-qrcode me-2"></i> TẠO MÃ QR THANH TOÁN
                                    </button>
                                </div>

                                <div id="step2-nap" style="display:none;" class="text-center animate__animated animate__fadeIn">
                                    <h6 class="text-primary fw-bold mb-3"><i class="fa-solid fa-scan-qr"></i> QUÉT MÃ ĐỂ NẠP TIỀN</h6>
                                    <div class="border rounded p-2 d-inline-block mb-3 bg-white shadow-sm">
                                        <img id="img-qr-code" src="" alt="QR Code" class="img-fluid" style="max-width: 350px; width: 100%;">
                                    </div>
                                    <div class="card bg-light border-0 mb-3 text-start">
                                        <div class="card-body p-3">
                                            <h6 class="text-uppercase text-muted small fw-bold border-bottom pb-2 mb-2">Thông tin người nhận (Admin)</h6>
                                            <div class="d-flex justify-content-between mb-1">
                                                <span class="text-secondary small">Ngân hàng:</span>
                                                <span class="fw-bold text-dark" id="qr-bank-name">...</span>
                                            </div>
                                            <div class="d-flex justify-content-between align-items-center mb-1">
                                                <span class="text-secondary small">Số tài khoản:</span>
                                                <div class="d-flex align-items-center">
                                                    <span class="fw-bold text-primary font-monospace me-2" id="qr-bank-acc" style="font-size: 1.1rem;">...</span>
                                                    <button class="btn btn-sm btn-outline-secondary py-0 px-1" onclick="copyToClipboard()" title="Sao chép">
                                                        <i class="fa-regular fa-copy"></i>
                                                    </button>
                                                </div>
                                            </div>
                                            <div class="d-flex justify-content-between mb-2">
                                                <span class="text-secondary small">Chủ tài khoản:</span>
                                                <span class="fw-bold text-danger text-uppercase" id="qr-acc-holder">...</span>
                                            </div>
                                            <div class="alert alert-warning small p-2 mb-0 mt-2 border-warning">
                                                <i class="fa-solid fa-circle-exclamation me-1"></i> Nội dung CK: 
                                                <strong id="display-content" class="text-danger">...</strong>
                                            </div>
                                        </div>
                                    </div>
                                    <form action="wallet" method="post">
                                        <input type="hidden" name="action" value="confirm_deposit">
                                        <input type="hidden" name="amount" id="hidden-amount-submit">
                                        <button class="btn btn-success w-100 fw-bold py-2 mb-2 shadow-sm">
                                            <i class="fa-solid fa-check me-2"></i> TÔI ĐÃ CHUYỂN KHOẢN
                                        </button>
                                        <button type="button" onclick="location.reload()" class="btn btn-light text-muted btn-sm w-100">
                                            <i class="fa-solid fa-rotate-left"></i> Quay lại / Hủy
                                        </button>
                                    </form>
                                </div>
                            </div>

                            <div class="tab-pane fade" id="rut-content" role="tabpanel">
                                <c:choose>
                                    <c:when test="${empty sessionScope.acc.soTaiKhoanNganHang}">
                                        <div class="text-center py-5">
                                            <i class="fa-solid fa-triangle-exclamation text-warning fa-3x mb-3"></i>
                                            <h5>Chưa có thông tin nhận tiền!</h5>
                                            <p class="text-muted">Bạn cần cập nhật tài khoản ngân hàng trước khi rút tiền.</p>
                                            <button onclick="document.getElementById('bank-tab').click()" class="btn btn-primary btn-sm fw-bold px-4">Cập nhật ngay</button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="bg-light p-3 rounded mb-4 border shadow-sm">
                                            <div class="d-flex justify-content-between">
                                                <small class="text-muted">Tiền sẽ về:</small>
                                                <small class="fw-bold text-dark">${sessionScope.acc.tenNganHang}</small>
                                            </div>
                                            <div class="text-primary font-monospace fs-4 fw-bold my-1">${sessionScope.acc.soTaiKhoanNganHang}</div>
                                            <div class="small text-uppercase text-secondary fw-bold">${sessionScope.acc.tenChuTaiKhoan}</div>
                                        </div>
                                        <form action="wallet" method="post" onsubmit="return xacNhanRutTien()">
                                            <input type="hidden" name="action" value="request_withdraw">
                                            <label class="form-label fw-bold">Số tiền muốn rút (Min 50k)</label>
                                            <div class="input-group mb-3">
                                                <input type="text" id="amountOut" class="form-control form-control-lg fw-bold text-danger" 
                                                        oninput="formatCurrencyInput(this)">
                                                <span class="input-group-text">VNĐ</span>
                                                <input type="hidden" id="amountOut_hidden" name="amount">
                                            </div>
                                            <button class="btn btn-danger w-100 fw-bold py-2 shadow-sm">
                                                <i class="fa-solid fa-paper-plane"></i> GỬI YÊU CẦU RÚT
                                            </button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="tab-pane fade" id="bank-content" role="tabpanel">
                                <div class="alert alert-info small mb-3 border-info">
                                    <i class="fa-solid fa-circle-info"></i> Thông tin này dùng để Admin chuyển tiền cho bạn khi bạn Rút tiền.
                                </div>
                                <form action="wallet" method="post">
                                    <input type="hidden" name="action" value="update_bank">
                                    <div class="mb-3">
                                        <label class="form-label fw-bold">Tên Ngân Hàng</label>
                                        <select name="bankName" class="form-select" required>
                                            <option value="" disabled selected>Chọn ngân hàng...</option>
                                            <option value="MBBank" ${sessionScope.acc.tenNganHang == 'MBBank' ? 'selected' : ''}>MB Bank (Quân Đội)</option>
                                            <option value="Vietcombank" ${sessionScope.acc.tenNganHang == 'Vietcombank' ? 'selected' : ''}>Vietcombank</option>
                                            <option value="Vietinbank" ${sessionScope.acc.tenNganHang == 'Vietinbank' ? 'selected' : ''}>Vietinbank</option>
                                            <option value="BIDV" ${sessionScope.acc.tenNganHang == 'BIDV' ? 'selected' : ''}>BIDV</option>
                                            <option value="Techcombank" ${sessionScope.acc.tenNganHang == 'Techcombank' ? 'selected' : ''}>Techcombank</option>
                                            <option value="Agribank" ${sessionScope.acc.tenNganHang == 'Agribank' ? 'selected' : ''}>Agribank</option>
                                            <option value="ACB" ${sessionScope.acc.tenNganHang == 'ACB' ? 'selected' : ''}>ACB</option>
                                            <option value="TPBank" ${sessionScope.acc.tenNganHang == 'TPBank' ? 'selected' : ''}>TPBank</option>
                                        </select>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold">Số Tài Khoản</label>
                                        <input type="text" name="bankAcc" value="${sessionScope.acc.soTaiKhoanNganHang}" class="form-control font-monospace" placeholder="Nhập số tài khoản..." required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold">Tên Chủ Tài Khoản (Không dấu)</label>
                                        <input type="text" name="accHolder" id="accHolderInput" value="${sessionScope.acc.tenChuTaiKhoan}" class="form-control text-uppercase" placeholder="NGUYEN VAN A" required oninput="xuLyTenChuThe(this)">
                                        <div class="form-text text-muted">Hệ thống sẽ tự động chuyển thành in hoa không dấu.</div>
                                    </div>
                                    <button class="btn btn-dark w-100 fw-bold py-2"><i class="fa-solid fa-floppy-disk"></i> LƯU THÔNG TIN</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-7">
                <div class="card shadow-sm border-0 h-100">
                    <div class="card-header bg-white py-3 border-bottom d-flex justify-content-between align-items-center flex-wrap gap-2">
                        <h5 class="mb-0 fw-bold text-secondary"><i class="fa-solid fa-clock-rotate-left"></i> Lịch sử giao dịch</h5>
                        <div class="btn-group btn-group-sm shadow-sm">
                            <a href="wallet?type=ALL" class="btn btn-outline-secondary ${currentType == 'ALL' ? 'active' : ''}">Tất cả</a>
                            <a href="wallet?type=NAP_TIEN" class="btn btn-outline-success ${currentType == 'NAP_TIEN' ? 'active' : ''}">Nạp</a>
                            <a href="wallet?type=RUT_TIEN" class="btn btn-outline-warning ${currentType == 'RUT_TIEN' ? 'active' : ''}">Rút</a>
                            <a href="wallet?type=OTHERS" class="btn btn-outline-danger ${currentType == 'OTHERS' ? 'active' : ''}">Khác</a>
                        </div>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th class="ps-3">Loại GD</th>
                                        <th>Số tiền</th>
                                        <th>Trạng thái</th>
                                        <th>Ngày giờ</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${history}" var="h">
                                  
                                        <c:if test="${h.loaiGiaoDich != 'RUT_TIEN' || h.soTien > 100000}">
                                            <tr>
                                                <td class="ps-3">
                                                    <c:choose>
                                                        <c:when test="${h.loaiGiaoDich == 'NAP_TIEN'}">
                                                            <span class="badge bg-success bg-opacity-10 text-success border border-success rounded-pill px-3"><i class="fa-solid fa-arrow-down"></i> Nạp tiền</span>
                                                        </c:when>
                                                        <c:when test="${h.loaiGiaoDich == 'RUT_TIEN'}">
                                                            <span class="badge bg-warning bg-opacity-10 text-warning border border-warning text-dark rounded-pill px-3"><i class="fa-solid fa-arrow-up"></i> Rút tiền</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-danger bg-opacity-10 text-danger border border-danger rounded-pill px-3"><i class="fa-solid fa-money-bill-transfer"></i> Thanh toán</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="fw-bold font-monospace">
                                                    <c:if test="${h.loaiGiaoDich == 'NAP_TIEN' || h.loaiGiaoDich == 'NHAN_LUONG' || h.loaiGiaoDich == 'HOAN_TIEN'}">
                                                        <span class="text-success">+<fmt:formatNumber value="${h.soTien}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></span>
                                                    </c:if>
                                                    <c:if test="${h.loaiGiaoDich == 'RUT_TIEN'|| h.loaiGiaoDich == 'THANH_TOAN_VIEC'}">
                                                        <span class="text-danger">-<fmt:formatNumber value="${h.soTien}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${h.trangThai == 'CHO_DUYET'}"><small class="badge bg-light text-warning border border-warning">Chờ duyệt</small></c:when>
                                                        <c:when test="${h.trangThai == 'THANH_CONG'}"><small class="badge bg-light text-success border border-success">Thành công</small></c:when>
                                                        <c:otherwise><small class="badge bg-light text-danger border border-danger">Thất bại</small></c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-muted small"><fmt:formatDate value="${h.ngayTao}" pattern="dd/MM HH:mm"/></td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${empty history}"><tr><td colspan="4" class="text-center py-5 text-muted">Chưa có giao dịch nào.</td></tr></c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <c:if test="${totalPages > 1}">
                        <div class="card-footer bg-white py-3">
                            <nav aria-label="Page navigation">
                                <ul class="pagination justify-content-center mb-0 pagination-sm">
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <li class="page-item ${currentPage == i ? 'active' : ''}"><a class="page-link" href="wallet?type=${currentType}&page=${i}">${i}</a></li>
                                    </c:forEach>
                                </ul>
                            </nav>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="${pageContext.request.contextPath}/assets/js/wallet.js"></script>
    <jsp:include page="footer.jsp"></jsp:include>

</body>
</html>