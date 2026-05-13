<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hồ Sơ: ${u.hoTen}</title>
</head>
<body class="bg-light">
    
    <jsp:include page="header.jsp"></jsp:include> <div class="container mt-4 mb-5">
        <div class="row">
            <div class="col-md-4">
                <div class="card border-0 shadow-sm text-center p-4">
                    <div class="mb-3 position-relative">
                        <img src="https://ui-avatars.com/api/?name=${u.hoTen}&size=128&background=random" class="rounded-circle shadow">
                        
                        <c:if test="${sessionScope.acc.maNguoiDung == u.maNguoiDung}">
                            <button class="btn btn-sm btn-light border position-absolute bottom-0 end-0 rounded-circle shadow-sm" 
                                    style="right: 25% !important;" 
                                    data-bs-toggle="modal" data-bs-target="#editProfileModal" title="Chỉnh sửa hồ sơ">
                                <i class="fa-solid fa-pen text-primary"></i>
                            </button>
                        </c:if>
                    </div>

                    <h4 class="fw-bold mb-0">${u.hoTen}</h4>
                    <p class="text-muted small">Thành viên từ <fmt:formatDate value="${u.ngayTao}" pattern="yyyy"/></p>
                    
                    <div class="d-flex justify-content-center gap-2 mb-3 text-warning">
                        <span class="fw-bold fs-5">
                            <fmt:formatNumber value="${u.diemUyTin}" type="number" maxFractionDigits="1" minFractionDigits="1"/>
                        </span> 
                        <i class="fa-solid fa-star"></i>
                    </div>

                    <hr>
                    <div class="text-start">
                        <div class="d-flex justify-content-between align-items-center">
                            <h6 class="fw-bold mb-0"><i class="fa-solid fa-circle-info"></i> Giới thiệu</h6>
                        </div>
                        <p class="small text-secondary fst-italic mt-2">
                            ${not empty u.gioiThieu ? u.gioiThieu : "Người dùng chưa cập nhật giới thiệu."}
                        </p>
                        
                        <div class="d-flex justify-content-between align-items-center mt-3">
                            <h6 class="fw-bold mb-0"><i class="fa-solid fa-screwdriver-wrench"></i> Kỹ năng</h6>
                        </div>
                        <div class="mt-2">
                            <c:if test="${not empty u.kyNang}">
                                <c:forEach items="${u.kyNang.split(',')}" var="skill">
                                    <span class="badge bg-light text-dark border me-1 mb-1">${skill.trim()}</span>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty u.kyNang}">
                                <span class="text-muted small">Chưa cập nhật</span>
                            </c:if>
                        </div>
                    </div>
                </div>

                <div class="card border-0 shadow-sm mt-3">
                    <div class="card-body">
                        <h6 class="fw-bold border-bottom pb-2">Thống kê hoạt động</h6>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Công việc đã xong:</span>
                            <span class="fw-bold text-success">${jobsDone}</span>
                        </div>
                        <div class="d-flex justify-content-between">
                            <span>Tỷ lệ hoàn thành:</span>
                            <span class="fw-bold text-primary">${completionRate}%</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-8">
                <div class="card border-0 shadow-sm">
                    <div class="card-header bg-white fw-bold py-3">
                        <i class="fa-solid fa-comments"></i> Đánh giá từ cộng đồng (${reviews.size()})
                    </div>
                    <div class="card-body">
                        <c:forEach items="${reviews}" var="r">
                            <div class="d-flex border-bottom pb-3 mb-3">
                                <div class="me-3">
                                    <img src="https://ui-avatars.com/api/?name=${r.tenNguoiDanhGia}&background=random&color=fff" class="rounded-circle" width="40" height="40">
                                </div>
                                <div class="flex-grow-1">
                                    <div class="d-flex justify-content-between">
                                        <strong><a href="profile?id=${r.nguoiDanhGiaId}" class="text-decoration-none text-dark">${r.tenNguoiDanhGia}</a></strong>
                                        <small class="text-muted"><fmt:formatDate value="${r.ngayDanhGia}" pattern="dd/MM/yyyy"/></small>
                                    </div>
                                    <div class="text-warning small mb-1">
                                        <c:forEach begin="1" end="${r.soSao}"><i class="fa-solid fa-star"></i></c:forEach>
                                    </div>
                                    <p class="mb-0 text-secondary">"${r.nhanXet}"</p>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty reviews}">
                            <div class="text-center text-muted py-5">
                                <i class="fa-regular fa-face-meh fa-2x mb-3"></i><br>
                                Chưa có đánh giá nào.
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <c:if test="${sessionScope.acc.maNguoiDung == u.maNguoiDung}">
        <div class="modal fade" id="editProfileModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title fw-bold"><i class="fa-solid fa-user-pen"></i> Cập nhật hồ sơ</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="update-profile" method="post">
                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label fw-bold">Giới thiệu bản thân:</label>
                                <textarea name="bio" class="form-control" rows="4" placeholder="Ví dụ: Sinh viên Bách Khoa, nhiệt tình, có xe máy...">${u.gioiThieu}</textarea>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Kỹ năng (Ngăn cách bởi dấu phẩy):</label>
                                <input type="text" name="skills" class="form-control" value="${u.kyNang}" placeholder="Ví dụ: Bưng bê, IT, Tiếng Anh, Sửa điện">
                                <div class="form-text small">Nhập các kỹ năng cách nhau bằng dấu phẩy (,)</div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-primary fw-bold">Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </c:if>

    <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>