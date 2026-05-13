<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quên Mật Khẩu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="assets/css/auth.css" rel="stylesheet"> 
</head>
<body>

    <div class="container auth-container">
        <div class="card auth-card shadow-lg border-0 rounded-4 overflow-hidden animate__animated animate__fadeInDown" style="max-width: 500px; margin: 50px auto;">
            <div class="p-5 bg-white">
                <div class="text-center mb-4">
                    <h3 class="fw-bold text-danger text-uppercase"><i class="fa-solid fa-key"></i> Lấy Lại Mật Khẩu</h3>
                    <p class="text-muted small">Nhập thông tin xác thực để đặt lại mật khẩu mới.</p>
                </div>

                <%-- Hiển thị thông báo lỗi --%>
                <c:if test="${not empty mess}">
                    <div class="alert alert-danger small">${mess}</div>
                </c:if>

                <form action="forgot-password" method="post">
                    <div class="mb-3">
                        <label class="form-label fw-bold small">Tên đăng nhập</label>
                        <input type="text" name="username" class="form-control bg-light" required>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold small">Email đăng ký</label>
                            <input type="email" name="email" class="form-control bg-light" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold small">Số điện thoại</label>
                            <input type="text" name="phone" class="form-control bg-light" required>
                        </div>
                    </div>

                    <hr>

                    <div class="mb-3">
                        <label class="form-label fw-bold small">Mật khẩu mới</label>
                        <input type="password" name="newpass" class="form-control bg-light" required>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label fw-bold small">Nhập lại mật khẩu</label>
                        <input type="password" name="confirmpass" class="form-control bg-light" required>
                    </div>

                    <button type="submit" class="btn btn-danger w-100 fw-bold py-2 rounded-pill shadow-sm">ĐỔI MẬT KHẨU</button>
                </form>

                <div class="text-center mt-4">
                    <a href="login.jsp" class="text-secondary text-decoration-none small"><i class="fa-solid fa-arrow-left"></i> Quay lại Đăng nhập</a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>