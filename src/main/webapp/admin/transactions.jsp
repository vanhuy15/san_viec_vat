<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="admin-header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="assets/css/transaction-style.css" rel="stylesheet">

<div class="content-wrapper">
    <div class="container-fluid">
        <h2 class="mt-3 mb-4 fw-bold text-primary"><i class="fa-solid fa-money-bill-transfer"></i> Quản lý Giao dịch</h2>
        
        <div class="mb-4">
            <a href="admin-transactions?type=ALL" class="btn-filter ${param.type=='ALL' || empty param.type ? 'active-all' : ''}">
                Tất cả
            </a>
            <a href="admin-transactions?type=NAP_TIEN" class="btn-filter ${param.type=='NAP_TIEN'?'active-success':''}">
                Nạp tiền
            </a>
            <a href="admin-transactions?type=RUT_TIEN" class="btn-filter ${param.type=='RUT_TIEN'?'active-warning':''}">
                Rút tiền
            </a>
            <a href="admin-transactions?type=THANH_TOAN_VIEC" class="btn-filter ${param.type=='THANH_TOAN_VIEC'?'active-info':''}">
                Thanh toán việc
            </a>
            <a href="admin-transactions?type=NHAN_LUONG" class="btn-filter ${param.type=='NHAN_LUONG'?'active-primary':''}">
                Nhận lương
            </a>
            <a href="admin-transactions?type=HOAN_TIEN" class="btn-filter ${param.type=='HOAN_TIEN'?'active-danger':''}">
                Hoàn tiền
            </a>
        </div>

        <c:if test="${not empty param.status}">
            <div class="alert alert-${param.status == 'success' ? 'success' : 'danger'} alert-dismissible fade show shadow-sm">
                <i class="fa-solid ${param.status == 'success' ? 'fa-check-circle' : 'fa-triangle-exclamation'}"></i>
                <c:choose>
                    <c:when test="${param.status == 'success'}">Thao tác thành công!</c:when>
                    <c:otherwise>Có lỗi xảy ra.</c:otherwise>
                </c:choose>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="ps-3">STT</th>
                                <th>Người dùng</th>
                                <th>Loại GD</th>
                                <th>Số tiền</th>
                                <th>Trạng thái</th>
                                <th class="text-center" style="min-width: 150px;">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${listTrans}" var="t" varStatus="loop">
                                <tr>
                                    <td class="ps-3 fw-bold text-secondary">${(currentPage - 1) * pageSize + loop.count}</td>
                                    <td>
                                        <div class="fw-bold text-dark">${t.moTa}</div>
                                        <small class="text-muted"><i class="fa-regular fa-clock"></i> <fmt:formatDate value="${t.ngayTao}" pattern="dd/MM/yy HH:mm"/></small>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.loaiGiaoDich == 'NAP_TIEN'}"><span class="badge bg-success bg-opacity-75">Nạp tiền</span></c:when>
                                            <c:when test="${t.loaiGiaoDich == 'RUT_TIEN'}"><span class="badge bg-warning text-dark">Rút tiền</span></c:when>
                                            <c:when test="${t.loaiGiaoDich == 'NHAN_LUONG'}"><span class="badge bg-primary bg-opacity-75">Nhận lương</span></c:when>
                                            <c:when test="${t.loaiGiaoDich == 'THANH_TOAN_VIEC'}"><span class="badge bg-info text-dark">Thanh toán</span></c:when>
                                            <c:when test="${t.loaiGiaoDich == 'HOAN_TIEN'}"><span class="badge bg-danger bg-opacity-75">Hoàn tiền</span></c:when>
                                            <c:otherwise><span class="badge bg-secondary">${t.loaiGiaoDich}</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="fw-bold font-monospace">
                                        <c:choose>
                                            <c:when test="${t.loaiGiaoDich == 'NAP_TIEN' || t.loaiGiaoDich == 'HOAN_TIEN' || t.loaiGiaoDich == 'NHAN_LUONG'}">
                                                <span class="text-success">+<fmt:formatNumber value="${t.soTien}" pattern="#,###"/> đ</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-danger">-<fmt:formatNumber value="${t.soTien}" pattern="#,###"/> đ</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.trangThai == 'CHO_DUYET'}"><span class="badge bg-warning text-dark border border-warning"><i class="fa-regular fa-hourglass"></i> Chờ duyệt</span></c:when>
                                            <c:when test="${t.trangThai == 'THANH_CONG'}"><span class="badge bg-success border border-success"><i class="fa-solid fa-check"></i> Thành công</span></c:when>
                                            <c:otherwise><span class="badge bg-danger border border-danger"><i class="fa-solid fa-xmark"></i> Đã hủy</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <div class="action-group">
                                            <c:if test="${t.trangThai == 'CHO_DUYET'}">
                                                <button onclick="handleTransaction(${t.maGiaoDich}, 'approve', '${t.loaiGiaoDich}')" 
                                                        class="btn btn-sm btn-success shadow-sm fw-bold" title="Duyệt đơn">
                                                    <i class="fa-solid fa-check"></i>
                                                </button>
                                                
                                                <button onclick="handleTransaction(${t.maGiaoDich}, 'reject', '${t.loaiGiaoDich}')" 
                                                        class="btn btn-sm btn-danger shadow-sm fw-bold" title="Hủy đơn">
                                                    <i class="fa-solid fa-xmark"></i>
                                                </button>
                                            </c:if>

                                            <c:if test="${t.loaiGiaoDich == 'RUT_TIEN'}">
                                                <button onclick="showWithdrawQR('${t.tenNganHang}', '${t.soTaiKhoanNganHang}', '${t.tenChuTaiKhoan}', ${t.soTien}, 'THANH TOAN RUT TIEN #${t.maGiaoDich}')" 
                                                        class="btn btn-sm btn-light border shadow-sm text-dark" 
                                                        title="Lấy mã QR VietQR">
                                                    <i class="fa-solid fa-qrcode text-primary"></i>
                                                </button>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listTrans}">
                                <tr><td colspan="6" class="text-center py-5 text-muted">Không có dữ liệu giao dịch.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <c:if test="${totalPages > 1}">
                <div class="card-footer bg-white d-flex justify-content-center py-3">
                    <nav><ul class="pagination mb-0">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link" href="admin-transactions?type=${param.type}&page=${i}">${i}</a>
                            </li>
                        </c:forEach>
                    </ul></nav>
                </div>
            </c:if>
        </div>
    </div>
</div>

<template id="qr-popup-template">
    <div class="mb-3 text-center">
        <img id="tpl-qr-img" src="" style="max-width: 100%; width: 380px; border-radius: 10px; border: 1px solid #eee; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
    </div>
    <div class="text-start alert alert-primary small border-0 shadow-sm" style="background-color: #f0f8ff;">
        <div class="d-flex justify-content-between mb-1">
            <span class="text-muted">Ngân hàng:</span> <strong class="text-dark" id="tpl-bank-name"></strong>
        </div>
        <div class="d-flex justify-content-between mb-1">
            <span class="text-muted">Số tài khoản:</span> <span id="tpl-bank-acc" class="font-monospace text-primary fw-bold fs-6"></span>
        </div>
        <div class="d-flex justify-content-between mb-2">
            <span class="text-muted">Chủ tài khoản:</span> <strong class="text-uppercase text-danger" id="tpl-acc-name"></strong>
        </div>
        <hr class="my-1">
        <div class="d-flex justify-content-between align-items-center mt-2">
            <span class="text-muted">Số tiền:</span> <span id="tpl-amount" class="text-success fw-bold fs-5"></span>
        </div>
    </div>
    <p class="small text-center text-muted fst-italic mb-0">
        Admin sử dụng App Ngân hàng quét mã trên để chuyển tiền cho User.
    </p>
</template>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="assets/js/admin-script.js?v=<%=System.currentTimeMillis()%>"></script>
</body>
</html>