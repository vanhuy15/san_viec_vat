<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký Tài Khoản</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <link href="assets/css/auth.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container mt-5 mb-5 auth-container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card auth-card shadow-lg border-0 rounded-4 animate__animated animate__fadeInDown">
                    <div class="card-header bg-dark text-white text-center py-4">
                        <h4 class="fw-bold mb-0 text-warning text-uppercase">
                            <i class="fa-solid fa-user-plus me-2"></i> Đăng Ký Thành Viên
                        </h4>
                    </div>
                    <div class="card-body p-4 p-md-5 bg-white">
                        
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger text-center animate__animated animate__shakeX">
                                <i class="fa-solid fa-triangle-exclamation"></i> ${error}
                            </div>
                        </c:if>

                        <form action="register" method="post" id="registerForm" onsubmit="return validateRegister()">
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold text-secondary">Tên đăng nhập (*)</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                                        <input type="text" name="user" id="username" class="form-control" placeholder="Viết liền không dấu">
                                    </div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold text-secondary">Họ và tên (*)</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fa-solid fa-id-card"></i></span>
                                        <input type="text" name="fullname" id="fullname" class="form-control" placeholder="Nguyễn Văn A">
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold text-secondary">Email</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                                    <input type="email" name="email" class="form-control" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold text-secondary">Số điện thoại</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fa-solid fa-phone"></i></span>
                                    <input type="tel" name="phone" id="phone" class="form-control">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold text-secondary">Mật khẩu (*)</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                                        <input type="password" name="pass" id="password" class="form-control">
                                    </div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold text-secondary">Nhập lại mật khẩu (*)</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fa-solid fa-key"></i></span>
                                        <input type="password" name="re-pass" id="re-password" class="form-control">
                                    </div>
                                </div>
                            </div>
                            
                            <div class="d-grid mt-4">
                                <button type="submit" class="btn btn-warning btn-lg fw-bold shadow-sm rounded-pill text-uppercase">
                                    <i class="fa-solid fa-paper-plane me-2"></i> Hoàn tất đăng ký
                                </button>
                            </div>
                        </form>
                    </div>
                    <div class="card-footer text-center bg-light py-3 border-top-0">
                        <span class="text-muted">Đã có tài khoản?</span> 
                        <a href="login.jsp" class="auth-link ms-1">Đăng nhập ngay</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="assets/js/auth.js"></script>

    <script>
        var status = '${status}';
        if (status === 'success') {
            Swal.fire({
                title: 'Đăng ký thành công!',
                text: 'Chào mừng bạn gia nhập cộng đồng.',
                icon: 'success',
                showConfirmButton: false,
                timer: 2000,
                timerProgressBar: true
            }).then(() => {
                window.location.href = 'login.jsp';
            });
        }
    </script>

</body>
</html>