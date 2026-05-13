<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang Chủ - Sàn Việc Vặt Sinh Viên</title>
</head>
<body class="bg-light d-flex flex-column min-vh-100">

    <jsp:include page="header.jsp"></jsp:include>

    <div class="bg-dark text-white py-5 mb-5" 
         style="background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)), url('https://images.unsplash.com/photo-1523240795612-9a054b0db644?ixlib=rb-1.2.1&auto=format&fit=crop&w=1600&q=80'); background-size: cover; background-position: center;">
        
        <div class="container text-center py-5">
            <h1 class="display-4 fw-bold text-warning mb-3">SÀN VIỆC VẶT SINH VIÊN</h1>
            <p class="lead text-white-50 mb-4">Kết nối sinh viên cần việc làm thêm và Người cần hỗ trợ việc vặt.</p>
            
            <a href="post-job" class="btn btn-warning fw-bold px-5 py-3 rounded-pill shadow-lg">
                <i class="fa-solid fa-plus-circle me-2"></i> Đăng việc ngay
            </a>
        </div>
    </div>

    <div class="container mb-5">
        
        <div class="d-flex justify-content-between align-items-center mb-4 border-start border-4 border-warning ps-3">
            <h3 class="fw-bold mb-0 text-uppercase">Việc mới nhất</h3>
        </div>

        <div class="row g-4">
            <c:forEach items="${listJobs}" var="j">
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 job-card shadow-sm">
                        <div class="card-body d-flex flex-column p-4">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <span class="badge bg-light text-secondary border rounded-pill px-3 py-2 fw-normal">
                                    ${j.tenDanhMuc}
                                </span>
                                <h5 class="text-success fw-bolder mb-0">
                                    <fmt:formatNumber value="${j.giaTien}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                </h5>
                            </div>

                            <h5 class="card-title fw-bold text-truncate mb-2" title="${j.tieuDe}">
                                <a href="job-detail?id=${j.maCongViec}" class="text-decoration-none text-dark stretched-link job-card-title">${j.tieuDe}</a>
                            </h5>

                            <div class="mb-2">
                                <c:choose>
                                    <c:when test="${j.soLuongDaTuyen >= j.soLuongCan}">
                                        <span class="badge bg-secondary rounded-pill">
                                            <i class="fa-solid fa-users me-1"></i> ${j.soLuongDaTuyen} / ${j.soLuongCan}
                                        </span>
                                        <span class="badge bg-danger rounded-pill ms-1">Đã đủ</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-primary rounded-pill">
                                            <i class="fa-solid fa-users me-1"></i> Tuyển: ${j.soLuongDaTuyen} / ${j.soLuongCan}
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <p class="small text-muted mb-2 text-truncate">
                                <i class="fa-solid fa-location-dot text-danger me-1"></i> ${j.diaDiem}
                            </p>
                            
                            <p class="card-text small text-secondary text-truncate mb-4">
                                ${j.moTa}
                            </p>

                            <div class="mt-auto pt-3 border-top d-flex align-items-center justify-content-between">
                                <div class="d-flex align-items-center">
                                    <div class="rounded-circle bg-warning text-white d-flex justify-content-center align-items-center fw-bold me-2" 
                                         style="width: 32px; height: 32px; font-size: 13px;">
                                        ${j.tenNguoiThue.charAt(0)}
                                    </div>
                                    <div class="lh-1">
                                        <div class="small fw-bold text-truncate" style="max-width: 120px;">${j.tenNguoiThue}</div>
                                        <small class="text-muted" style="font-size: 10px;">
                                            <fmt:formatDate value="${j.ngayDang}" pattern="dd/MM/yyyy"/>
                                        </small>
                                    </div>
                                </div>
                                <span class="small fw-bold text-dark text-decoration-none" style="z-index: 2; position: relative;">
                                    Chi tiết <i class="fa-solid fa-arrow-right ms-1"></i>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${empty listJobs}">
                <div class="col-12 text-center py-5">
                    <h5 class="text-muted">Chưa có công việc nào.</h5>
                </div>
            </c:if>
        </div>

        <c:if test="${totalPage > 1}">
            <nav class="mt-5">
                <ul class="pagination justify-content-center">
                    
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link shadow-sm" href="home?page=${currentPage - 1}">
                            <i class="fa-solid fa-chevron-left"></i>
                        </a>
                    </li>

                    <c:forEach begin="1" end="${totalPage}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link shadow-sm" href="home?page=${i}">
                                ${i}
                            </a>
                        </li>
                    </c:forEach>

                    <li class="page-item ${currentPage == totalPage ? 'disabled' : ''}">
                        <a class="page-link shadow-sm" href="home?page=${currentPage + 1}">
                            <i class="fa-solid fa-chevron-right"></i>
                        </a>
                    </li>
                </ul>
            </nav>
        </c:if>

    </div>

    <jsp:include page="footer.jsp"></jsp:include>

</body>
</html>