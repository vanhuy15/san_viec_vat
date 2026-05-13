<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="admin-header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="content-wrapper">
    <div class="container-fluid">
        <h2 class="mt-3 mb-4">Tổng quan hệ thống</h2>
        
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="card text-white bg-primary mb-3 h-100 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">THÀNH VIÊN</h5>
                        <p class="card-text display-6 fw-bold">${cntUsers}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white bg-success mb-3 h-100 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">CÔNG VIỆC</h5>
                        <p class="card-text display-6 fw-bold">${cntJobs}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-dark bg-warning mb-3 h-100 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">GIAO DỊCH CHỜ</h5>
                        <p class="card-text display-6 fw-bold">${cntTrans}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white bg-danger mb-3 h-100 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">KHIẾU NẠI</h5>
                        <p class="card-text display-6 fw-bold">${cntReports}</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="card shadow-sm border-0">
            <div class="card-header bg-white fw-bold">
                <i class="fa-solid fa-chart-column text-primary"></i> Thống kê Việc làm (7 ngày)
            </div>
            <div class="card-body">
                <div style="height: 350px;">
                    <canvas id="jobChart"
                            data-labels="${chartLabels}" 
                            data-new="${chartDataNew}" 
                            data-done="${chartDataDone}">
                    </canvas>
                </div>
            </div>
        </div>
        
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script src="assets/js/admin-script.js"></script>

</body>
</html>