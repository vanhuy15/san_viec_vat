<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Việc Đã Đăng</title>
</head>
<body class="bg-light">

    <jsp:include page="header.jsp"></jsp:include>

    <div class="container mt-4 mb-5">
        
        <c:if test="${not empty sessionScope.msg}">
            <div class="alert alert-success alert-dismissible fade show">
                <i class="fa-solid fa-check-circle"></i> ${sessionScope.msg}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% session.removeAttribute("msg"); %>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show">
                <i class="fa-solid fa-circle-exclamation"></i> ${sessionScope.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% session.removeAttribute("error"); %>
        </c:if>

        <h3 class="fw-bold mb-4 text-primary"><i class="fa-solid fa-briefcase"></i> Quản Lý Việc Đã Đăng</h3>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="bg-light">
                            <tr>
                                <th class="py-3 ps-4">Tên công việc</th>
                                <th>Giá tiền</th>
                                <th>Ngày đăng</th>
                                <th>Trạng thái</th>
                                <th class="text-end pe-4">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${myJobs}" var="job">
                                <tr>
                                    <td class="ps-4">
                                        <div class="fw-bold text-dark">
                                            <a href="job-detail?id=${job.maCongViec}" class="text-decoration-none text-dark">${job.tieuDe}</a>
                                        </div>
                                        <small class="text-muted"><i class="fa-solid fa-tag"></i> ${job.tenDanhMuc}</small>
                                    </td>
                                    <td class="fw-bold text-success">
                                        <fmt:formatNumber value="${job.giaTien}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                    </td>
                                    <td><fmt:formatDate value="${job.ngayDang}" pattern="dd/MM/yyyy"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${job.trangThai == 'MO'}">
                                                <span class="badge bg-success">Đang tìm người</span>
                                            </c:when>
                                            <c:when test="${job.trangThai == 'DA_GIAO'}">
                                                <span class="badge bg-warning text-dark">Đang thực hiện</span>
                                            </c:when>
                                            <c:when test="${job.trangThai == 'CHO_XAC_NHAN'}">
                                                <span class="badge bg-info text-dark">Chờ xác nhận</span>
                                            </c:when>
                                            <c:when test="${job.trangThai == 'HOAN_THANH'}">
                                                <span class="badge bg-primary">Hoàn thành</span>
                                            </c:when>
                                            <c:when test="${job.trangThai == 'CHO_DUYET'}">
                                                <span class="badge bg-danger">Đang khiếu nại / Chờ duyệt</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">${job.trangThai}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end pe-4">
                                        <c:if test="${job.trangThai == 'MO'}">
                                            <a href="manage-job?id=${job.maCongViec}" class="btn btn-outline-primary btn-sm fw-bold shadow-sm">
                                                <i class="fa-solid fa-users-viewfinder"></i> Xem ứng viên
                                            </a>
                                        </c:if>

                                        <c:if test="${job.trangThai == 'DA_GIAO'}">
                                            <div class="text-muted small fst-italic border p-2 rounded bg-light d-inline-block">
                                                <div class="spinner-border spinner-border-sm text-secondary" role="status"></div>
                                                Thợ đang làm việc...
                                            </div>
                                        </c:if>

                                        <c:if test="${job.trangThai == 'CHO_XAC_NHAN'}">
                                            <div class="d-flex flex-column align-items-end gap-2">
                                                <span class="badge bg-warning text-dark mb-1 animate__animated animate__flash">
                                                    <i class="fa-solid fa-bell"></i> Thợ đã báo xong!
                                                </span>
                                                <div class="d-flex gap-2">
                                                    <button type="button" class="btn btn-outline-danger btn-sm fw-bold" 
                                                            data-bs-toggle="modal" 
                                                            data-bs-target="#reportModal_${job.maCongViec}" title="Báo cáo nếu làm ẩu">
                                                        <i class="fa-solid fa-flag"></i> Khiếu nại
                                                    </button>
    
                                                    <button type="button" class="btn btn-success btn-sm fw-bold shadow" 
                                                            data-bs-toggle="modal" 
                                                            data-bs-target="#completeModal_${job.maCongViec}">
                                                        <i class="fa-solid fa-hand-holding-dollar"></i> Nghiệm thu & Trả tiền
                                                    </button>
                                                </div>
                                            </div>
                                        </c:if>
                                        
                                        <c:if test="${job.trangThai == 'HOAN_THANH'}">
                                            <button class="btn btn-light border btn-sm text-success fw-bold" disabled>
                                                <i class="fa-solid fa-check-double"></i> Đã thanh toán
                                            </button>
                                        </c:if>
                                        
                                        <c:if test="${job.trangThai == 'CHO_DUYET'}">
                                            <button class="btn btn-warning btn-sm text-dark" disabled>
                                                <i class="fa-solid fa-user-shield"></i> Chờ Admin xử lý
                                            </button>
                                        </c:if>
                                    </td>
                                </tr>

                                <div class="modal fade" id="completeModal_${job.maCongViec}" tabindex="-1" aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header bg-success text-white">
                                                <h5 class="modal-title">Xác nhận & Đánh giá</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                            </div>
                                            <form action="complete-job" method="post">
                                                <div class="modal-body">
                                                    <p>Bạn có chắc công việc <strong>${job.tieuDe}</strong> đã xong?</p>
                                                    <p class="text-muted small">Tiền sẽ được chuyển cho người làm ngay lập tức.</p>
                                                    
                                                    <input type="hidden" name="jobId" value="${job.maCongViec}">
                                                    
                                                    <div class="mb-3">
                                                        <label class="fw-bold">Số sao:</label>
                                                        <select name="rating" class="form-select">
                                                            <option value="5">⭐⭐⭐⭐⭐ (5 sao)</option>
                                                            <option value="4">⭐⭐⭐⭐ (4 sao)</option>
                                                            <option value="3">⭐⭐⭐ (3 sao)</option>
                                                            <option value="2">⭐⭐ (2 sao)</option>
                                                            <option value="1">⭐ (1 sao)</option>
                                                        </select>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label class="fw-bold">Nhận xét:</label>
                                                        <textarea name="review" class="form-control" rows="3" required placeholder="Nhận xét về người làm..."></textarea>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                    <button type="submit" class="btn btn-success">Xác nhận</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <div class="modal fade" id="reportModal_${job.maCongViec}" tabindex="-1" aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header bg-danger text-white">
                                                <h5 class="modal-title"><i class="fa-solid fa-triangle-exclamation"></i> Gửi Khiếu Nại</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                            </div>
                                            <form action="report-job" method="post">
                                                <div class="modal-body">
                                                    <p>Bạn đang khiếu nại công việc: <strong>${job.tieuDe}</strong></p>
                                                    <div class="alert alert-warning small">
                                                        <i class="fa-solid fa-circle-info"></i> Sau khi gửi, Admin sẽ xem xét và liên hệ. Tiền vẫn được giữ an toàn trong hệ thống.
                                                    </div>
                                                    
                                                    <input type="hidden" name="jobId" value="${job.maCongViec}">
                                                    
                                                    <div class="mb-3">
                                                        <label class="fw-bold text-danger">Lý do khiếu nại:</label>
                                                        <textarea name="reason" class="form-control border-danger" rows="4" required placeholder="Vui lòng mô tả chi tiết: Người làm không làm đúng yêu cầu, thái độ kém, v.v..."></textarea>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                                    <button type="submit" class="btn btn-danger">Gửi báo cáo</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                </c:forEach>
                            
                            <c:if test="${empty myJobs}">
                                <tr>
                                    <td colspan="5" class="text-center py-5">
                                        <i class="fa-solid fa-clipboard-list fa-3x text-muted mb-3"></i>
                                        <p class="text-muted">Bạn chưa đăng công việc nào.</p>
                                        <a href="post-job.jsp" class="btn btn-warning fw-bold">Đăng việc ngay</a>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>