<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="root" value="${pageContext.request.contextPath}" />

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
<link href="${root}/assets/css/style.css" rel="stylesheet"> 

<div class="toast-container position-fixed top-0 end-0 p-3" style="z-index: 1100;">
    <%-- SUCCESS MESSAGE --%>
    <c:if test="${not empty sessionScope.succMsg}">
        <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header bg-success text-white">
                <strong class="me-auto"><i class="fas fa-check-circle"></i> Thành công</strong>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body bg-white text-dark">
                ${sessionScope.succMsg}
            </div>
        </div>
        <c:remove var="succMsg" scope="session"/>
    </c:if>

    <%-- ERROR MESSAGE --%>
    <c:if test="${not empty sessionScope.failedMsg}">
        <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header bg-danger text-white">
                <strong class="me-auto"><i class="fas fa-exclamation-triangle"></i> Thất bại</strong>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body bg-white text-dark">
                ${sessionScope.failedMsg}
            </div>
        </div>
        <c:remove var="failedMsg" scope="session"/>
    </c:if>
</div>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
    <div class="container">
        <a class="navbar-brand fw-bold text-uppercase" href="${root}/home">
            <i class="fa-solid fa-bolt text-warning"></i> Sàn Việc Vặt
        </a>
        
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <div class="d-flex align-items-center gap-3 ms-auto">
                <c:choose>
                    <c:when test="${not empty sessionScope.acc}">
                        <a href="${root}/wallet" class="wallet-badge text-decoration-none">
                            <i class="fa-solid fa-wallet"></i> 
                            <fmt:formatNumber value="${sessionScope.acc.soDuVi}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                        </a>
                        
                        <a href="${root}/post-job.jsp" class="btn btn-post-job btn-sm rounded-pill px-3 py-2 text-decoration-none">
                            <i class="fa-solid fa-plus-circle"></i> Đăng việc
                        </a>
                        
                        <div class="dropdown">
                            <a class="nav-link dropdown-toggle text-white d-flex align-items-center" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <img src="https://ui-avatars.com/api/?name=${sessionScope.acc.hoTen}&background=random&color=fff" class="rounded-circle me-2 border border-2 border-white" width="35" height="35">
                                <span class="fw-bold d-none d-md-inline">${sessionScope.acc.hoTen}</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow border-0 mt-2" aria-labelledby="userDropdown">
                                <li><h6 class="dropdown-header text-muted">Xin chào, ${sessionScope.acc.tenDangNhap}</h6></li>
                                
                                <li><a class="dropdown-item py-2" href="${root}/profile"><i class="fa-solid fa-user me-2 text-primary"></i> Hồ sơ của tôi</a></li>
                                
                                <li><a class="dropdown-item py-2" href="${root}/wallet"><i class="fa-solid fa-money-bill-transfer me-2 text-success"></i> Ví & Giao dịch</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item py-2" href="${root}/my-posted-jobs"><i class="fa-solid fa-briefcase me-2 text-primary"></i> Việc tôi đã đăng</a></li>
                                <li><a class="dropdown-item py-2" href="${root}/my-applications"><i class="fa-solid fa-hand-holding-hand me-2 text-info"></i> Việc đã ứng tuyển</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger fw-bold py-2" href="${root}/logout"><i class="fa-solid fa-right-from-bracket me-2"></i> Đăng xuất</a></li>
                            </ul>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <c:set var="req" value="${pageContext.request}" />
                        <c:set var="uri" value="${req.requestURI}" />
                        
                        <c:if test="${not uri.contains('login.jsp')}">
                            <a href="${root}/login.jsp" class="btn btn-outline-light btn-sm fw-bold px-3">Đăng nhập</a>
                        </c:if>
                        <c:if test="${not uri.contains('register.jsp')}">
                            <a href="${root}/register.jsp" class="btn btn-warning btn-sm fw-bold px-3 text-dark">Đăng ký ngay</a>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>