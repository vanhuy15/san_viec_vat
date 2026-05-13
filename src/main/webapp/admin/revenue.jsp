<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="admin-header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="content-wrapper">
    <div class="container-fluid">
        <h2 class="mt-3 mb-4 text-primary fw-bold">
            <i class="fa-solid fa-sack-dollar"></i> Quản lý Doanh thu & Hoa hồng
        </h2>
        
        <div class="row mb-4">
            <div class="col-md-6">
                <div class="card shadow border-0 h-100" style="background: linear-gradient(45deg, #4e73df, #224abe); color: white;">
                    <div class="card-body">
                        <div class="row no-gutters align-items-center">
                            <div class="col mr-2">
                                <div class="text-uppercase fw-bold mb-1" style="font-size: 0.9rem; opacity: 0.8;">Tổng giá trị giao dịch (GMV)</div>
                                <div class="h2 mb-0 fw-bold"><fmt:formatNumber value="${totalValue}" pattern="#,###"/> đ</div>
                                <small>Tổng tiền Job đã hoàn thành</small>
                            </div>
                            <div class="col-auto"><i class="fas fa-calendar fa-3x text-gray-300" style="opacity: 0.3;"></i></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card shadow border-0 h-100" style="background: linear-gradient(45deg, #1cc88a, #13855c); color: white;">
                    <div class="card-body">
                        <div class="row no-gutters align-items-center">
                            <div class="col mr-2">
                                <div class="text-uppercase fw-bold mb-1" style="font-size: 0.9rem; opacity: 0.8;">Doanh thu sàn (Hoa hồng 5%)</div>
                                <div class="h2 mb-0 fw-bold text-white">+ <fmt:formatNumber value="${adminProfit}" pattern="#,###"/> đ</div>
                                <small>Tiền thực nhận về túi Admin</small>
                            </div>
                            <div class="col-auto"><i class="fas fa-dollar-sign fa-3x" style="opacity: 0.3;"></i></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="card shadow-sm border-0">
            <div class="card-header bg-white py-3">
                <h6 class="m-0 fw-bold text-primary">Chi tiết Hoa hồng từng Job (Đã hoàn thành)</h6>
            </div>
            <div class="card-body">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>STT</th>
                            <th>Tiêu đề công việc</th>
                            <th>Người thuê</th>
                            <th>Giá trị Job</th>
                            <th>Người làm nhận (95%)</th>
                            <th>Admin nhận (5%)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listJobs}" var="j" varStatus="loop">
                            <tr>
                                <%-- Công thức STT: (Trang hiện tại - 1) * Số lượng/trang + Số thứ tự vòng lặp --%>
                                <td>${(currentPage - 1) * pageSize + loop.count}</td>
                                <td>
                                    <span class="fw-bold text-dark">${j.tieuDe}</span><br>
                                    <small class="text-muted"><fmt:formatDate value="${j.ngayDang}" pattern="dd/MM/yyyy"/></small>
                                </td>
                                <td>${j.moTa}</td>
                                <td class="fw-bold"><fmt:formatNumber value="${j.giaTien}" pattern="#,###"/> đ</td>
                                <td class="text-muted"><fmt:formatNumber value="${j.giaTien * 0.95}" pattern="#,###"/> đ</td>
                                <td class="text-success fw-bold">+ <fmt:formatNumber value="${j.giaTien * 0.05}" pattern="#,###"/> đ</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listJobs}"><tr><td colspan="6" class="text-center text-muted py-4">Chưa có công việc nào hoàn thành.</td></tr></c:if>
                    </tbody>
                </table>
            </div>
            <div class="card-footer bg-white d-flex justify-content-center py-3">
                <nav><ul class="pagination mb-0">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="admin-revenue?page=${i}">${i}</a>
                        </li>
                    </c:forEach>
                </ul></nav>
            </div>
        </div>
    </div>
</div>
<script src="assets/js/admin-script.js"></script>
</body>
</html>