<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Việc Tôi Đã Ứng Tuyển</title>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/my-applications.css">
</head>

<body class="bg-light">

    <jsp:include page="header.jsp"></jsp:include>

    <div class="container mt-4 mb-5" style="min-height: 60vh;">

        <c:if test="${not empty sessionScope.msg}">
            <div class="alert alert-${sessionScope.msgType == 'error' ? 'danger' : 'success'} alert-dismissible fade show">
                ${sessionScope.msg}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% 
                session.removeAttribute("msg"); 
                session.removeAttribute("msgType"); 
            %>
        </c:if>

        <h3 class="fw-bold mb-4 text-primary">
            <i class="fa-solid fa-hand-holding-hand"></i> LỊCH SỬ ỨNG TUYỂN
        </h3>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">

                    <table class="table table-hover align-middle mb-0">
                        <thead class="bg-light text-secondary">
                            <tr>
                                <th class="py-3 ps-4">Tên công việc</th>
                                <th>Người thuê</th>
                                <th>Ngày nộp đơn</th>
                                <th>Lời nhắn của bạn</th>
                                <th class="text-end pe-4">Trạng thái / Hành động</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach items="${myApps}" var="app">
                                <tr>
                                    <td class="ps-4">
                                        <a href="job-detail?id=${app.maCongViec}"
                                           class="fw-bold text-decoration-none text-primary"
                                           style="font-size: 1.1rem;">
                                            ${app.tenCongViec}
                                            <i class="fa-solid fa-arrow-up-right-from-square small ms-1"></i>
                                        </a>
                                        <br>
                                        <small class="text-muted">
                                            Mã việc: #${app.maCongViec}
                                        </small>
                                    </td>

                                    <td>
                                        <div class="d-flex align-items-center">
                                            <div class="bg-primary bg-opacity-10 text-primary rounded-circle user-icon-box me-2">
                                                <i class="fa-solid fa-user"></i>
                                            </div>
                                            <span class="fw-bold text-dark">
                                                ${app.tenNguoiThue}
                                            </span>
                                        </div>
                                    </td>

                                    <td class="text-muted">
                                        <fmt:formatDate value="${app.thoiGian}" pattern="HH:mm dd/MM/yyyy"/>
                                    </td>

                                    <td class="fst-italic text-secondary">
                                        <div style="max-width: 200px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"
                                             title="${app.loiNhan}">
                                            "${app.loiNhan}"
                                        </div>
                                    </td>

                                    <td class="text-end pe-4">
                                        <c:choose>

                                            <c:when test="${app.trangThai == 'CHO_DUYET'}">
                                                <span class="badge bg-secondary bg-opacity-75 text-white px-3 py-2 rounded-pill">
                                                    <i class="fa-regular fa-clock me-1"></i>
                                                    Đang chờ duyệt
                                                </span>
                                            </c:when>

                                            <c:when test="${app.trangThai == 'TU_CHOI'}">
                                                <span class="badge bg-danger bg-opacity-75 text-white px-3 py-2 rounded-pill">
                                                    <i class="fa-solid fa-ban me-1"></i>
                                                    Bị từ chối
                                                </span>
                                            </c:when>

                                            <c:when test="${app.trangThai == 'DA_CHON'}">

                                                <%-- [SỬA] Cho phép hiện nút nếu Job đã giao HOẶC Job còn mở nhưng mình đã được chọn --%>
                                                <c:if test="${app.trangThaiCongViec == 'DA_GIAO' 
                                                             || (app.trangThaiCongViec == 'MO' && app.trangThai == 'DA_CHON')}">

                                                    <div class="action-btn-group">
                                                        <button onclick="baoCaoHoanThanh(${app.maCongViec})"
                                                                class="btn btn-primary btn-sm fw-bold shadow-sm px-3 rounded-pill">
                                                            <i class="fa-solid fa-check-double me-1"></i>
                                                            Báo xong
                                                        </button>

                                                        <form action="cancel-job"
                                                              method="post"
                                                              onsubmit="return confirm('CẢNH BÁO: Hủy việc sẽ hoàn lại tiền cho chủ nhà. Bạn chắc chắn chứ?');">
                                                            <input type="hidden" name="jobId" value="${app.maCongViec}">
                                                            <button class="btn btn-outline-danger btn-sm fw-bold shadow-sm px-3 rounded-pill">
                                                                <i class="fa-solid fa-ban me-1"></i>
                                                                Hủy
                                                            </button>
                                                        </form>
                                                    </div>
                                                </c:if>

                                                <c:if test="${app.trangThaiCongViec == 'CHO_XAC_NHAN'}">
                                                    <span class="badge bg-warning text-dark border border-warning px-3 py-2 rounded-pill">
                                                        <i class="fa-solid fa-hourglass-half me-1"></i>
                                                        Chờ chủ xác nhận
                                                    </span>
                                                </c:if>

                                                <c:if test="${app.trangThaiCongViec == 'HOAN_THANH'}">
                                                    <span class="badge bg-success px-3 py-2 rounded-pill">
                                                        <i class="fa-solid fa-circle-check me-1"></i>
                                                        Đã hoàn thành
                                                    </span>
                                                </c:if>

                                                <c:if test="${app.trangThaiCongViec == 'DA_HUY'}">
                                                    <span class="badge bg-dark px-3 py-2 rounded-pill">
                                                        <i class="fa-solid fa-xmark me-1"></i>
                                                        Đã hủy
                                                    </span>
                                                </c:if>

                                            </c:when>

                                            <c:otherwise>
                                                <span class="badge bg-light text-dark border">
                                                    ${app.trangThai}
                                                </span>
                                            </c:otherwise>

                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>

                            <c:if test="${empty myApps}">
                                <tr>
                                    <td colspan="5" class="text-center py-5">
                                        <div class="text-muted opacity-50 mb-3">
                                            <i class="fa-brands fa-searchengin fa-3x"></i>
                                        </div>
                                        <p class="text-muted fs-5">
                                            Bạn chưa ứng tuyển công việc nào.
                                        </p>
                                        <a href="home"
                                           class="btn btn-warning fw-bold text-dark shadow-sm px-4 rounded-pill">
                                            Tìm việc ngay
                                        </a>
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

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="${pageContext.request.contextPath}/assets/js/my-applications.js"></script>

</body>
</html>
