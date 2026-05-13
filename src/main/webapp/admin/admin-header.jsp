<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="root" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Quản Trị - Sàn Việc Vặt</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="${root}/assets/css/admin.css" rel="stylesheet">
</head>
<body>

<div class="sidebar d-flex flex-column p-3">
    <a href="${root}/admin-dashboard" class="d-flex align-items-center mb-4 text-white text-decoration-none px-2">
        <i class="fa-solid fa-bolt text-warning fs-3 me-2"></i>
        <span class="fs-4 fw-bold">ADMIN PANEL</span>
    </a>
    
    <hr class="text-secondary mt-0">
    
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <a href="${root}/admin-dashboard" class="nav-link ${fn:contains(pageContext.request.servletPath, 'dashboard') ? 'active' : ''}">
                <i class="fa-solid fa-chart-line"></i> <span>Tổng quan</span>
            </a>
        </li>
        <li>
            <a href="${root}/admin-revenue" class="nav-link ${fn:contains(pageContext.request.servletPath, 'revenue') ? 'active' : ''}">
                <i class="fa-solid fa-sack-dollar"></i> <span>Doanh thu (5%)</span>
            </a>
        </li>
        <li>
            <a href="${root}/admin-transactions" class="nav-link ${fn:contains(pageContext.request.servletPath, 'transaction') ? 'active' : ''}">
                <i class="fa-solid fa-money-bill-transfer"></i> <span>Duyệt Nạp/Rút</span>
                <c:if test="${cntTrans > 0}">
                    <span class="badge bg-danger ms-auto shadow-sm">${cntTrans}</span>
                </c:if>
            </a>
        </li>
        <li>
            <a href="${root}/admin-users" class="nav-link ${fn:contains(pageContext.request.servletPath, 'users') ? 'active' : ''}">
                <i class="fa-solid fa-users"></i> <span>Quản lý Người dùng</span>
            </a>
        </li>
        <li>
            <a href="${root}/admin-jobs" class="nav-link ${fn:contains(pageContext.request.servletPath, 'jobs') ? 'active' : ''}">
                <i class="fa-solid fa-briefcase"></i> <span>Quản lý Bài đăng</span>
            </a>
        </li>
        <li>
             <a href="${root}/admin-reports" class="nav-link ${fn:contains(pageContext.request.servletPath, 'reports') ? 'active' : ''}">
                <i class="fa-solid fa-flag"></i> <span>Khiếu nại / Báo cáo</span>
                <c:if test="${cntReports > 0}">
                    <span class="badge bg-warning text-dark ms-auto shadow-sm">${cntReports}</span>
                </c:if>
            </a>
        </li>
    </ul>
    
    <hr class="text-secondary">
    
    <div class="dropdown sidebar-user mt-auto">
        <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle p-2 rounded" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
            <img src="https://ui-avatars.com/api/?name=${sessionScope.acc.hoTen}&background=random&color=fff&size=128&bold=true" width="38" height="38" class="rounded-circle me-2 shadow-sm">
            
            <div class="sidebar-user-info">
                <strong class="text-truncate d-block">
                    <c:choose>
                        <c:when test="${not empty sessionScope.acc.hoTen}">
                            ${sessionScope.acc.hoTen}
                        </c:when>
                        <c:otherwise>
                            ${sessionScope.acc.tenDangNhap}
                        </c:otherwise>
                    </c:choose>
                </strong>
                <small class="text-white-50" style="font-size: 0.75rem;">Quản trị viên</small>
            </div>
        </a>
        
        <ul class="dropdown-menu dropdown-menu-dark text-small shadow" aria-labelledby="dropdownUser1">
            <li><a class="dropdown-item text-danger" href="${root}/logout"><i class="fa-solid fa-right-from-bracket me-2"></i>Đăng xuất</a></li>
        </ul>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>