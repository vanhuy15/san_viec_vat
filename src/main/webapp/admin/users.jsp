<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="admin-header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="content-wrapper">
    <div class="container-fluid">
        <h2 class="mt-3 mb-4">Quản lý Thành viên</h2>
        
        <div class="card shadow-sm border-0">
            <div class="card-body">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>STT</th>
                            <th>Tài khoản</th>
                            <th>Liên hệ</th>
                            <th>Số dư</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listUsers}" var="u" varStatus="loop">
                            <tr>
                                <td>${loop.count}</td>
                                <td>
                                    <div class="fw-bold">${u.hoTen}</div>
                                    <small class="text-muted">@${u.tenDangNhap}</small>
                                </td>
                                <td>${u.soDienThoai}<br><small>${u.email}</small></td>
                                <td class="text-success fw-bold">
                                    <fmt:formatNumber value="${u.soDuVi}" pattern="#,###"/> đ
                                </td>
                                <td>
                                    <%-- HIỂN THỊ TRẠNG THÁI --%>
                                    <c:choose>
                                        <c:when test="${u.vaiTro == 'ADMIN'}">
                                            <span class="badge bg-primary">Quản trị viên</span> 
                                        </c:when>
                                        <c:when test="${u.trangThai == 'BI_KHOA'}">
                                            <span class="badge bg-danger">Bị khóa</span> 
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-success">Hoạt động</span> 
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="${root}/profile?id=${u.maNguoiDung}" target="_blank" class="btn btn-sm btn-info text-white" title="Xem hồ sơ">
                                        <i class="fa-solid fa-eye"></i>
                                    </a>
                                    
                                    <%-- NÚT KHÓA / MỞ KHÓA --%>
                                    <c:if test="${u.vaiTro != 'ADMIN'}">
                                        <form action="admin-users" method="post" class="d-inline">
                                            <input type="hidden" name="id" value="${u.maNguoiDung}">
                                            
                                            <c:choose>
                                                <c:when test="${u.trangThai == 'BI_KHOA'}">
                                                    <%-- Nếu đang khóa -> Hiện nút Mở --%>
                                                    <button name="action" value="unlock" class="btn btn-sm btn-success" 
                                                            title="Mở khóa tài khoản" 
                                                            onclick="return confirm('Mở khóa cho tài khoản ${u.tenDangNhap}?')">
                                                        <i class="fa-solid fa-lock-open"></i> Mở
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <%-- Nếu đang hoạt động -> Hiện nút Khóa --%>
                                                    <button name="action" value="lock" class="btn btn-sm btn-outline-danger" 
                                                            title="Khóa tài khoản" 
                                                            onclick="return confirm('CẢNH BÁO: User sẽ không thể đăng nhập.\nBạn chắc chắn muốn KHÓA tài khoản ${u.tenDangNhap}?')">
                                                        <i class="fa-solid fa-lock"></i> Khóa
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>