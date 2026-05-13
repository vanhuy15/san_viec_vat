<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="admin-header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="assets/css/transaction-style.css" rel="stylesheet">

<div class="content-wrapper">
    <div class="container-fluid">
        <h2 class="mt-3 mb-4 fw-bold text-primary">Quản lý Công việc</h2>
        
        <div class="mb-4">
            <a href="admin-jobs?status=ALL" class="btn-filter ${param.status=='ALL' || empty param.status ? 'active-all' : ''}">
                Tất cả
            </a>
            
            <a href="admin-jobs?status=CHO_DUYET" class="btn-filter ${param.status=='CHO_DUYET'?'active-danger':''}">
                Đang khiếu nại
            </a>
            
            <a href="admin-jobs?status=MO" class="btn-filter ${param.status=='MO'?'active-primary':''}">
                Đang tìm người làm
            </a>
            <a href="admin-jobs?status=DANG_LAM" class="btn-filter ${param.status=='DANG_LAM'?'active-warning':''}">
                Đang thực hiện
            </a>
            <a href="admin-jobs?status=HOAN_THANH" class="btn-filter ${param.status=='HOAN_THANH'?'active-success':''}">
                Hoàn thành
            </a>
            <a href="admin-jobs?status=DA_HUY" class="btn-filter ${param.status=='DA_HUY'?'active-danger':''}">
                Đã hủy
            </a>
        </div>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="ps-3">STT</th>
                                <th>Tiêu đề</th>
                                <th>Người đăng</th>
                                <th>Ngân sách</th>
                                <th>Ngày đăng</th>
                                <th>Trạng thái</th>
                                <th class="text-center">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${listJobs}" var="j" varStatus="loop">
                                <tr>
                                    <td class="ps-3 fw-bold text-secondary">${(currentPage - 1) * pageSize + loop.count}</td>
                                    <td class="fw-bold text-primary text-truncate" style="max-width: 250px;">${j.tieuDe}</td>
                                    <td>${j.moTa}</td> 
                                    <td class="text-success fw-bold"><fmt:formatNumber value="${j.giaTien}" pattern="#,###"/> đ</td>
                                    <td><fmt:formatDate value="${j.ngayDang}" pattern="dd/MM"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${j.trangThai=='MO'}"><span class="badge bg-primary bg-opacity-75">Đang tìm người</span></c:when>
                                            <c:when test="${j.trangThai=='DANG_LAM'}"><span class="badge bg-warning text-dark">Đang thực hiện</span></c:when>
                                            <c:when test="${j.trangThai=='CHO_DUYET'}"><span class="badge bg-danger text-white"><i class="fa-solid fa-triangle-exclamation"></i> Khiếu nại</span></c:when>
                                            <c:when test="${j.trangThai=='HOAN_THANH'}"><span class="badge bg-success">Hoàn thành</span></c:when>
                                            <c:when test="${j.trangThai=='DA_HUY'}"><span class="badge bg-secondary">Đã hủy</span></c:when>
                                            <c:otherwise><span class="badge bg-light text-dark border">${j.trangThai}</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${j.trangThai=='CHO_DUYET'}">
                                                <a href="admin-reports" class="btn btn-sm btn-warning text-dark fw-bold shadow-sm">
                                                    <i class="fa-solid fa-gavel"></i> Xử lý
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="admin-jobs" method="post" class="d-inline">
                                                    <input type="hidden" name="id" value="${j.maCongViec}">
                                                    <input type="hidden" name="status" value="${param.status}">
                                                    <button name="action" value="delete" class="btn btn-sm btn-outline-danger border-0" onclick="return confirm('Xóa bài đăng này?')">
                                                        <i class="fa-solid fa-trash"></i>
                                                    </button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listJobs}">
                                <tr><td colspan="7" class="text-center py-5 text-muted">Không có công việc nào.</td></tr>
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
                                <a class="page-link" href="admin-jobs?status=${param.status}&page=${i}">${i}</a>
                            </li>
                        </c:forEach>
                    </ul></nav>
                </div>
            </c:if>
        </div>
    </div>
</div>
<script src="assets/js/admin-script.js"></script>
</body>
</html>