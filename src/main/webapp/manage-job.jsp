<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- THIẾT LẬP ĐỊNH DẠNG TIẾNG VIỆT ĐỂ SỐ HIỆN RA LÀ 100.000 VÀ 4,5 --%>
<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Ứng Viên - Sàn Việc Vặt</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        /* CSS Inline nhanh cho Modal */
        .rating-stars i { font-size: 2rem; margin: 0 5px; transition: 0.2s; }
        .rating-stars i:hover { transform: scale(1.2); }
    </style>
</head>
<body class="bg-light">

    <jsp:include page="header.jsp"></jsp:include>

    <div class="container mt-4 mb-5">
        <a href="my-posted-jobs" class="text-decoration-none text-muted mb-3 d-inline-block">
            <i class="fa-solid fa-arrow-left"></i> Quay lại danh sách việc
        </a>

        <div class="card border-primary mb-4 shadow-sm">
            <div class="card-body">
                <h6 class="text-uppercase text-muted small fw-bold">Đang quản lý công việc:</h6>
                <h4 class="text-primary fw-bold mb-3">${job.tieuDe}</h4>
                
                <div class="d-flex gap-4 text-muted">
                    <span>
                        <i class="fa-solid fa-money-bill-wave text-success"></i> 
                        <%-- UPDATE: maxFractionDigits="0" để bỏ số thập phân --%>
                        <fmt:formatNumber value="${job.giaTien}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                    </span>
                    <span><i class="fa-solid fa-users text-info"></i> ${applicants.size()} người ứng tuyển</span>
                    
                    <span>
                        <i class="fa-solid fa-circle-info text-dark"></i> Trạng thái: 
                        <c:choose>
                            <c:when test="${job.trangThai == 'MO'}"><span class="badge bg-success">Đang tìm người</span></c:when>
                            <c:when test="${job.trangThai == 'DA_GIAO'}"><span class="badge bg-warning text-dark">Đã giao việc</span></c:when>
                            <c:when test="${job.trangThai == 'CHO_XAC_NHAN'}"><span class="badge bg-info text-dark">Chờ nghiệm thu</span></c:when>
                            <c:when test="${job.trangThai == 'HOAN_THANH'}"><span class="badge bg-secondary">Đã hoàn thành</span></c:when>
                            <c:otherwise><span class="badge bg-light text-dark border">${job.trangThai}</span></c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>
        </div>

        <c:if test="${job.trangThai == 'CHO_XAC_NHAN'}">
            <div class="alert alert-warning border-warning shadow-sm d-flex justify-content-between align-items-center">
                <div>
                    <strong><i class="fa-solid fa-bell"></i> Thông báo nghiệm thu</strong><br>
                    Người làm đã báo cáo hoàn thành. Vui lòng kiểm tra và xác nhận để trả lương.
                </div>
                <button class="btn btn-success fw-bold animate__animated animate__pulse animate__infinite" 
                        data-bs-toggle="modal" data-bs-target="#rateModal">
                    <i class="fa-solid fa-check-to-slot"></i> Xác nhận & Đánh giá
                </button>
            </div>
        </c:if>

        <h5 class="fw-bold mb-3"><i class="fa-solid fa-user-check"></i> Danh sách ứng tuyển</h5>
        
        <div class="row g-3">
            <c:forEach items="${applicants}" var="app">
                <div class="col-md-6">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body">
                            <div class="d-flex align-items-center mb-3">
                                <div class="bg-light rounded-circle p-3 me-3 text-primary">
                                    <h4 class="m-0 fw-bold">${app.tenNguoiUngTuyen.charAt(0)}</h4>
                                </div>
                                <div>
                                    <h5 class="fw-bold mb-0">${app.tenNguoiUngTuyen}</h5>
                                    <small class="text-muted"><i class="fa-solid fa-phone"></i> ${app.sdtNguoiUngTuyen}</small>
                                    <br>
                                    
                                    <%-- UPDATE: Logic hiển thị sao làm tròn 1 chữ số thập phân --%>
                                    <small class="text-warning">
                                        <i class="fa-solid fa-star"></i> 
                                        <c:choose>
                                            <c:when test="${app.diemUyTin > 0}">
                                                <fmt:formatNumber value="${app.diemUyTin}" type="number" maxFractionDigits="1" minFractionDigits="1"/>
                                            </c:when>
                                            <c:otherwise>Mới</c:otherwise>
                                        </c:choose>
                                    </small>

                                </div>
                                <div class="ms-auto">
                                    <span class="small text-muted">${app.thoiGian}</span>
                                </div>
                            </div>
                            
                            <div class="bg-light p-3 rounded mb-3 fst-italic text-secondary">
                                <i class="fa-solid fa-quote-left me-2"></i>${app.loiNhan}
                            </div>

                            <div class="d-flex justify-content-end gap-2">
                                <c:choose>
                                    <%-- Nếu Job đang mở và ứng viên đang chờ -> Hiện nút duyệt --%>
                                    <c:when test="${job.trangThai == 'MO' && app.trangThai == 'CHO_DUYET'}">
                                        <button class="btn btn-outline-danger btn-sm">Từ chối</button>
                                        <button class="btn btn-primary btn-sm fw-bold" 
                                                onclick="duyetNguoi(${app.maUngTuyen}, '${app.tenNguoiUngTuyen}', ${app.nguoiUngTuyenId})">
                                            <i class="fa-solid fa-check"></i> Duyệt người này
                                        </button>
                                    </c:when>

                                    <%-- Nếu người này đã được chọn --%>
                                    <c:when test="${app.trangThai == 'DA_CHON'}">
                                        <span class="badge bg-success p-2"><i class="fa-solid fa-circle-check"></i> NGƯỜI LÀM CHÍNH</span>
                                    </c:when>

                                    <c:otherwise>
                                        <span class="badge bg-secondary">${app.trangThai}</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
            
            <c:if test="${empty applicants}">
                <div class="col-12 text-center py-5 text-muted">
                    <i class="fa-solid fa-inbox fa-3x mb-3 opacity-25"></i>
                    <p>Chưa có ai ứng tuyển công việc này.</p>
                </div>
            </c:if>
        </div>
    </div>

    <div class="modal fade" id="rateModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg">
                <div class="modal-header bg-warning text-dark border-0">
                    <h5 class="modal-title fw-bold">
                        <i class="fa-solid fa-star-half-stroke me-2"></i>Đánh giá & Trả lương
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                
                <form action="confirm-job" method="POST">
                    <div class="modal-body text-center p-4">
                        <p class="text-muted mb-3">Bạn đánh giá người làm này mấy sao?</p>
                        
                        <input type="hidden" name="jobId" value="${job.maCongViec}">
                        <input type="hidden" name="workerId" value="${job.nguoiLamId}"> 
                        <input type="hidden" name="rating" id="rating-value" value="5">

                        <div class="rating-stars mb-4 display-6 text-warning" style="cursor: pointer;">
                            <i class="fa-solid fa-star" onclick="setRating(1)"></i>
                            <i class="fa-solid fa-star" onclick="setRating(2)"></i>
                            <i class="fa-solid fa-star" onclick="setRating(3)"></i>
                            <i class="fa-solid fa-star" onclick="setRating(4)"></i>
                            <i class="fa-solid fa-star" onclick="setRating(5)"></i>
                        </div>

                        <div class="form-floating mb-3">
                            <textarea class="form-control" name="comment" placeholder="Nhận xét" id="floatingTextarea2" style="height: 100px" required></textarea>
                            <label for="floatingTextarea2">Nhận xét...</label>
                        </div>
                        
                        <div class="alert alert-info py-2 small">
                            <i class="fa-solid fa-circle-info me-1"></i> 
                            Số tiền 
                            <%-- UPDATE: maxFractionDigits="0" ở Modal nữa --%>
                            <strong><fmt:formatNumber value="${job.giaTien}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></strong> 
                            sẽ được chuyển ngay cho người làm.
                        </div>
                    </div>
                    
                    <div class="modal-footer border-0 justify-content-center pb-4">
                        <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-success px-4 fw-bold">
                            <i class="fa-solid fa-paper-plane me-1"></i> Xác nhận hoàn thành
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp"></jsp:include>
    
    <% 
        String errorMsg = (String) session.getAttribute("errorMsg");
        String errorVal = (errorMsg != null) ? errorMsg : "";
        if (errorMsg != null) { session.removeAttribute("errorMsg"); }
    %>
    <input type="hidden" id="server-error-msg" value="<%= errorVal %>">

    <script src="assets/js/manage-job.js"></script>
    
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

</body>
</html>