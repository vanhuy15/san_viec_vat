<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="now" class="java.util.Date" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi Tiết Công Việc - Sàn Việc Vặt</title>
</head>
<body class="bg-light">

    <jsp:include page="header.jsp"></jsp:include>

    <div class="container mt-4 mb-5">
        
        <%-- [ĐÃ XÓA] Phần thông báo Alert thừa --%>

        <a href="home" class="text-decoration-none text-muted mb-3 d-inline-block hover-link">
            <i class="fa-solid fa-arrow-left"></i> Quay lại danh sách việc
        </a>

        <c:if test="${job != null}">
            <div class="row">
                <div class="col-lg-8">
                    <div class="card shadow-sm border-0 mb-4 rounded-3">
                        <div class="card-body p-4">
                            <span class="badge bg-warning text-dark mb-2 px-3 py-2 rounded-pill border border-warning">
                                <i class="fa-solid fa-layer-group"></i> ${job.tenDanhMuc}
                            </span>
                            <h2 class="fw-bold mb-3 text-dark mt-2">${job.tieuDe}</h2>
                            <div class="d-flex flex-wrap gap-3 text-muted mb-4 small border-bottom pb-3">
                                <span><i class="fa-solid fa-location-dot text-danger"></i> ${job.diaDiem}</span>
                                <span class="border-start ps-3"><i class="fa-regular fa-clock"></i> Hạn chót: <strong class="text-dark"><fmt:formatDate value="${job.hanChot}" pattern="HH:mm dd/MM/yyyy"/></strong></span>
                                <span class="border-start ps-3">
                                    <i class="fa-solid fa-circle-info"></i> Trạng thái: 
                                    <c:choose>
                                        <c:when test="${job.trangThai == 'MO'}"><span class="text-success fw-bold">Đang tìm người</span></c:when>
                                        <c:when test="${job.trangThai == 'DA_GIAO'}"><span class="text-warning fw-bold">Đã giao việc</span></c:when>
                                        <c:when test="${job.trangThai == 'HOAN_THANH'}"><span class="text-primary fw-bold">Hoàn thành</span></c:when>
                                        <c:otherwise><span class="text-secondary fw-bold">${job.trangThai}</span></c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                            <h5 class="fw-bold mb-3"><i class="fa-solid fa-align-left text-primary"></i> Mô tả công việc</h5>
                            <div class="bg-light p-3 rounded text-secondary" style="line-height: 1.8; font-size: 1.05rem; white-space: pre-line;">
                                ${job.moTa}
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="card shadow border-0 mb-3 bg-white">
                        <div class="card-body text-center p-4">
                            <p class="text-muted mb-1 text-uppercase small fw-bold">Thù lao công việc</p>
                            <h2 class="text-success fw-bold display-5 mb-3">
                                <fmt:formatNumber value="${job.giaTien}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                            </h2>
                            
                            <c:if test="${sessionScope.acc == null}">
                                <a href="login.jsp" class="btn btn-outline-primary w-100 fw-bold py-2">
                                    <i class="fa-solid fa-right-to-bracket"></i> Đăng nhập để Ứng tuyển
                                </a>
                            </c:if>

                            <c:if test="${sessionScope.acc != null}">
                                <%-- LÀ CHỦ BÀI ĐĂNG --%>
                                <c:if test="${sessionScope.acc.maNguoiDung == job.nguoiThueId}">
                                    <div class="alert alert-info fw-bold py-2 small mb-2">
                                        <i class="fa-solid fa-user-check"></i> Đây là bài đăng của bạn
                                    </div>
                                    <a href="manage-job?id=${job.maCongViec}" class="btn btn-primary w-100 fw-bold shadow-sm py-2">
                                        <i class="fa-solid fa-list-check"></i> QUẢN LÝ ỨNG VIÊN
                                    </a>
                                </c:if>

                                <%-- LÀ NGƯỜI TÌM VIỆC --%>
                                <c:if test="${sessionScope.acc.maNguoiDung != job.nguoiThueId}">
                                    <c:choose>
                                        <c:when test="${job.trangThai == 'MO'}">
                                            <c:choose>
                                                <c:when test="${job.hanChot lt now}">
                                                    <div class="alert alert-warning text-center fw-bold small mb-2">
                                                        <i class="fa-solid fa-hourglass-end"></i> Đã hết hạn ứng tuyển
                                                    </div>
                                                    <button class="btn btn-secondary w-100 fw-bold btn-lg" disabled>
                                                        <i class="fa-solid fa-calendar-xmark"></i> ĐÃ QUÁ HẠN
                                                    </button>
                                                </c:when>

                                                <c:otherwise>
                                                    <button type="button" class="btn btn-success w-100 fw-bold btn-lg shadow-sm" 
                                                            data-bs-toggle="modal" data-bs-target="#applyModal">
                                                        <i class="fa-solid fa-hand-point-up"></i> ỨNG TUYỂN NGAY
                                                    </button>
                                                    <small class="text-muted d-block mt-2 fst-italic" style="font-size: 12px;">
                                                        * Cam kết hoàn thành đúng hạn.
                                                    </small>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn btn-secondary w-100 fw-bold" disabled>
                                                <i class="fa-solid fa-lock"></i> Đã đóng đơn
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:if>
                        </div>
                    </div>

                    <div class="card shadow-sm border-0 mb-3">
                        <div class="card-header bg-white fw-bold py-3">
                            <i class="fa-solid fa-circle-user"></i> Thông tin người thuê
                        </div>
                        <div class="card-body d-flex align-items-center">
                            <div class="me-3">
                                <a href="profile?id=${job.nguoiThueId}" class="text-decoration-none">
                                    <img src="https://ui-avatars.com/api/?name=${job.tenNguoiThue}&background=random&size=64" 
                                         class="rounded-circle shadow-sm hover-zoom" width="50" height="50"
                                         title="Xem hồ sơ">
                                </a>
                            </div>
                            <div>
                                <h6 class="fw-bold mb-0">
                                    <a href="profile?id=${job.nguoiThueId}" class="text-dark text-decoration-none">
                                        ${job.tenNguoiThue}
                                    </a>
                                </h6>
                                
                                <%-- [UPDATE] Format điểm uy tín thành 1 chữ số thập phân (Ví dụ: 5.0, 4.3) --%>
                                <div class="text-warning small mt-1">
                                    <c:if test="${job.diemUyTin > 0}">
                                        <span class="fw-bold">
                                            <fmt:formatNumber value="${job.diemUyTin}" type="number" maxFractionDigits="1" minFractionDigits="1"/>
                                        </span> 
                                        <i class="fa-solid fa-star"></i>
                                    </c:if>
                                    <c:if test="${job.diemUyTin == 0}">
                                        <span class="text-muted">Chưa có điểm</span>
                                    </c:if>
                                    <span class="text-muted text-dark ms-1 small">(Uy tín)</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <c:if test="${job.trangThai == 'HOAN_THANH'}">
                        <div class="card shadow-sm border-0">
                            <div class="card-header bg-success text-white fw-bold py-3">
                                <i class="fa-solid fa-check-double"></i> Kết quả công việc
                            </div>
                            <div class="card-body">
                                <c:if test="${review != null}">
                                    <div class="text-center mb-3">
                                        <h5 class="fw-bold text-dark">Người thuê đã đánh giá</h5>
                                        <div class="text-warning fs-4">
                                            <c:forEach begin="1" end="${review.soSao}"><i class="fa-solid fa-star"></i></c:forEach>
                                            <c:forEach begin="${review.soSao + 1}" end="5"><i class="fa-regular fa-star"></i></c:forEach>
                                        </div>
                                    </div>
                                    <div class="bg-light p-3 rounded border">
                                        <p class="mb-2 fst-italic text-secondary"><i class="fa-solid fa-quote-left"></i> ${review.nhanXet}</p>
                                        <div class="d-flex align-items-center mt-3 border-top pt-2">
                                            <div class="me-2">
                                                <a href="profile?id=${review.nguoiDanhGiaId}">
                                                    <img src="https://ui-avatars.com/api/?name=${review.tenNguoiDanhGia}&background=random&size=32" class="rounded-circle" width="30" height="30">
                                                </a>
                                            </div>
                                            <div class="flex-grow-1">
                                                <small class="fw-bold">
                                                    <a href="profile?id=${review.nguoiDanhGiaId}" class="text-primary text-decoration-none">
                                                        ${review.tenNguoiDanhGia}
                                                    </a>
                                                </small>
                                            </div>
                                            <small class="text-muted ms-auto"><fmt:formatDate value="${review.ngayDanhGia}" pattern="dd/MM/yyyy"/></small>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${review == null}">
                                    <div class="alert alert-warning text-center small mb-0">
                                        <i class="fa-solid fa-circle-exclamation"></i> Chưa có dữ liệu đánh giá.
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </c:if>
        
        <c:if test="${job == null}">
            <div class="alert alert-danger text-center mt-5">
                <h4><i class="fa-solid fa-triangle-exclamation"></i> Không tìm thấy công việc này!</h4>
                <a href="home" class="btn btn-dark mt-2">Về trang chủ</a>
            </div>
        </c:if>
    </div>

    <c:if test="${job != null && sessionScope.acc != null && sessionScope.acc.maNguoiDung != job.nguoiThueId}">
        <div class="modal fade" id="applyModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title"><i class="fa-solid fa-paper-plane"></i> Ứng Tuyển Công Việc</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <form action="apply-job" method="post">
                        <div class="modal-body">
                            <p>Bạn đang ứng tuyển vào vị trí: <strong>${job.tieuDe}</strong></p>
                            <input type="hidden" name="id" value="${job.maCongViec}">
                            
                            <div class="mb-3">
                                <label class="form-label fw-bold">Lời nhắn cho chủ nhà (Tùy chọn):</label>
                                <textarea name="message" class="form-control" rows="3" placeholder="Ví dụ: Em rảnh vào giờ này, có kinh nghiệm làm..."></textarea>
                            </div>
                            <div class="alert alert-warning small">
                                <i class="fa-solid fa-shield-halved"></i> Cam kết làm việc nghiêm túc, đúng giờ.
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-success fw-bold">Gửi Yêu Cầu</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </c:if>

    <script src="${pageContext.request.contextPath}/assets/js/job-action.js"></script>
    <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>