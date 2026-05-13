<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="admin-header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="content-wrapper">
    <div class="container-fluid">
        <h2 class="mt-3 mb-4 fw-bold text-primary"><i class="fa-solid fa-gavel"></i> Giải quyết Khiếu nại</h2>
        
        <c:if test="${not empty sessionScope.msg}">
            <div class="alert alert-${sessionScope.msgType == 'success' ? 'success' : 'danger'} alert-dismissible fade show shadow-sm" role="alert">
                <i class="fa-solid ${sessionScope.msgType == 'success' ? 'fa-circle-check' : 'fa-triangle-exclamation'} me-2"></i>
                <strong>Thông báo:</strong> ${sessionScope.msg}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% 
                session.removeAttribute("msg"); 
                session.removeAttribute("msgType"); 
            %>
        </c:if>

        <div class="alert alert-light border shadow-sm mb-4">
            <i class="fa-solid fa-circle-info text-primary"></i> 
            <strong>Hướng dẫn xử lý:</strong> 
            <ul class="mb-0 mt-1 small text-muted">
                <li><span class="badge bg-success">Hoàn tiền</span> : Tiền sẽ được trả lại ví của <strong>Người đăng việc (Chủ)</strong>. Job sẽ bị Hủy.</li>
                <li><span class="badge bg-danger">Trả lương</span> : Tiền được chuyển cho <strong>Người làm (95%)</strong> và <strong>Admin (5%)</strong>. Job chuyển sang Hoàn thành.</li>
            </ul>
        </div>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light text-secondary">
                            <tr>
                                <th class="ps-4">STT</th>
                                <th>Nội dung khiếu nại</th>
                                <th>Lý do chi tiết</th>
                                <th>Ngày tạo</th>
                                <th>Trạng thái</th>
                                <th class="text-center">Hành động của Admin</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${listReports}" var="r" varStatus="loop">
                                <tr>
                                    <td class="ps-4 fw-bold text-secondary">${loop.count}</td>
                                    <td>
                                        <%-- Hiển thị thông tin job liên quan --%>
                                        <div class="d-flex flex-column">
                                            <span class="fw-bold text-dark">Job #${r.maCongViec}</span>
                                            <small class="text-muted text-wrap" style="max-width: 250px;">
                                                <i class="fa-solid fa-note-sticky"></i> ${r.ghiChu != null ? r.ghiChu : 'Không có ghi chú job'}
                                            </small>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="fst-italic text-danger bg-light p-2 rounded border border-danger-subtle" style="max-width: 300px;">
                                            "${r.lyDo}"
                                        </div>
                                    </td>
                                    <td><fmt:formatDate value="${r.ngayTao}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${r.trangThaiXuLy == 'CHO_XU_LY'}">
                                                <span class="badge bg-warning text-dark"><i class="fa-regular fa-clock"></i> Chờ xử lý</span>
                                            </c:when>
                                            <c:when test="${r.trangThaiXuLy == 'DA_GIAI_QUYET'}">
                                                <span class="badge bg-success"><i class="fa-solid fa-check-double"></i> Đã xong</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">${r.trangThaiXuLy}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${r.trangThaiXuLy == 'CHO_XU_LY'}">
                                                <div class="d-flex justify-content-center gap-2">
                                                    <%-- NÚT HOÀN TIỀN (Type = 1) --%>
                                                    <button onclick="resolveReport(${r.maKhieuNai}, 1)" class="btn btn-sm btn-outline-success fw-bold" title="Hủy job, trả tiền chủ">
                                                        <i class="fa-solid fa-rotate-left"></i> Hoàn tiền
                                                    </button>
                                                    
                                                    <%-- NÚT TRẢ LƯƠNG (Type = 2) --%>
                                                    <button onclick="resolveReport(${r.maKhieuNai}, 2)" class="btn btn-sm btn-outline-danger fw-bold" title="Job xong, trả lương thợ">
                                                        <i class="fa-solid fa-hand-holding-dollar"></i> Trả lương
                                                    </button>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="small text-muted border-top pt-1 mt-1">
                                                    <strong>Kết quả:</strong><br>
                                                    ${r.ketQuaXuLy}
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listReports}">
                                <tr>
                                    <td colspan="6" class="text-center py-5 text-muted">
                                        <i class="fa-solid fa-clipboard-check fa-3x mb-3 text-secondary opacity-25"></i>
                                        <p>Hiện tại không có khiếu nại nào cần giải quyết.</p>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script src="assets/js/admin-script.js?v=<%=System.currentTimeMillis()%>"></script>

</body>
</html>