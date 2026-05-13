<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng Việc Mới</title>
</head>
<body class="bg-light">

    <jsp:include page="header.jsp"></jsp:include>

    <div class="container mt-4 mb-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="home">Trang chủ</a></li>
                        <li class="breadcrumb-item active" aria-current="page">Đăng việc mới</li>
                    </ol>
                </nav>

                <div class="card shadow-sm border-0 rounded-3">
                    <div class="card-header bg-warning text-dark py-3">
                        <h4 class="mb-0 fw-bold"><i class="fa-solid fa-pen-to-square"></i> NHẬP THÔNG TIN CÔNG VIỆC</h4>
                    </div>
                    <div class="card-body p-4">

                        <c:if test="${not empty message}">
                            <div class="alert alert-${msgType} text-center">
                                ${message}
                                <c:if test="${msgType == 'success'}">
                                    <br><a href="home" class="fw-bold text-decoration-none">Về trang chủ xem việc vừa đăng</a>
                                </c:if>
                            </div>
                        </c:if>

                        <form action="post-job" method="post">
                            
                            <div class="mb-3">
                                <label class="form-label fw-bold">Tiêu đề công việc (*)</label>
                                <input type="text" name="tieuDe" class="form-control form-control-lg" placeholder="Ví dụ: Cần 2 bạn dọn phòng trọ sáng nay" required>
                            </div>

                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-bold">Danh mục</label>
                                    <select name="maDanhMuc" class="form-select" required>
                                        <option value="1">Dọn dẹp vệ sinh</option>
                                        <option value="2">Hỗ trợ học tập/IT</option>
                                        <option value="3">Bê vác/Chuyển đồ</option>
                                        <option value="4">Chăm sóc thú cưng</option>
                                        <option value="5">Việc vặt khác</option>
                                    </select>
                                </div>
                                
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-bold">Số lượng người (*)</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fa-solid fa-users"></i></span>
                                        <input type="number" name="soLuong" class="form-control" value="1" min="1" max="10" required>
                                    </div>
                                    <div class="form-text text-muted small">Mặc định là 1 người.</div>
                                </div>

                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-bold">Thù lao (mỗi người) (*)</label>
                                    <div class="input-group">
                                        <input type="number" name="giaTien" class="form-control" placeholder="Nhập số tiền..." required min="10000" step="1000">
                                        <span class="input-group-text">đ</span>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Địa điểm làm việc (*)</label>
                                    <input type="text" name="diaDiem" class="form-control" placeholder="Số phòng, KTX..." required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Hạn chót ứng tuyển (*)</label>
                                    <input type="datetime-local" name="hanChot" class="form-control" required>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-bold">Mô tả chi tiết (*)</label>
                                <textarea name="moTa" class="form-control" rows="5" placeholder="Mô tả rõ công việc cần làm, yêu cầu đặc biệt..." required></textarea>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-dark btn-lg fw-bold">
                                    <i class="fa-solid fa-cloud-arrow-up"></i> ĐĂNG VIỆC NGAY
                                </button>
                                <a href="home" class="btn btn-outline-secondary">Hủy bỏ</a>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <c:if test="${msgType == 'success'}">
        <script>
            Swal.fire({
                title: 'Đăng thành công!',
                text: 'Công việc của bạn đã được hiển thị lên sàn.',
                icon: 'success',
                confirmButtonText: 'Tuyệt vời'
            });
        </script>
    </c:if>
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>