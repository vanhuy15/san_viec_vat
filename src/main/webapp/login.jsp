<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập - Sàn Việc Vặt</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="assets/css/auth.css" rel="stylesheet"> 
</head>
<body>

    <div class="container auth-container">
        <div class="card auth-card shadow-lg border-0 rounded-4 overflow-hidden animate__animated animate__fadeInDown" style="max-width: 900px; margin: 0 auto;">
            <div class="row g-0">
                <div class="col-lg-6 d-none d-lg-block bg-auth-image"></div>
                <div class="col-lg-6 p-5 bg-white">
                    <div class="text-center mb-4">
                        <h3 class="fw-bold text-warning text-uppercase"><i class="fa-solid fa-bolt"></i> Sàn Việc Vặt</h3>
                        <p class="text-muted">Đăng nhập để bắt đầu kiếm tiền ngay!</p>
                    </div>

                    <%-- HIỂN THỊ THÔNG BÁO LỖI / KHÓA TÀI KHOẢN --%>
                    <c:if test="${not empty mess}">
                        <div class="alert alert-danger alert-dismissible fade show shadow-sm border-start border-4 border-danger d-flex align-items-center animate__animated animate__headShake" role="alert">
                            <i class="fa-solid fa-circle-exclamation fa-lg me-2"></i>
                            <div class="fw-medium">${mess}</div>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <form action="login" method="post">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Tên đăng nhập</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light border-end-0"><i class="fa-solid fa-user text-muted"></i></span>
                                <input type="text" name="username" class="form-control bg-light border-start-0" 
                                       placeholder="Nhập username..." value="${username}" required>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label fw-bold">Mật khẩu</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light border-end-0"><i class="fa-solid fa-lock text-muted"></i></span>
                                <input type="password" name="password" class="form-control bg-light border-start-0" 
                                       placeholder="Nhập mật khẩu..." value="${password}" required>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="remember" id="remember">
                                <label class="form-check-label text-muted small" for="remember">Ghi nhớ đăng nhập</label>
                            </div>
                            <a href="forgot-password.jsp" class="text-decoration-none small text-warning fw-bold">Quên mật khẩu?</a>
                        </div>

                        <button type="submit" class="btn btn-warning w-100 fw-bold py-2 rounded-pill shadow-sm">ĐĂNG NHẬP</button>
                    </form>

                    <div class="text-center mt-4">
                        <p class="small text-muted mb-0">Chưa có tài khoản? <a href="register.jsp" class="text-warning fw-bold text-decoration-none">Đăng ký ngay</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="assets/js/script.js"></script>
</body>
</html>