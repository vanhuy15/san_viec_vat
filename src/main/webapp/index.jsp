<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Khi vào trang chủ, lập tức chuyển hướng sang Servlet /home
    // Để Servlet lo việc lấy dữ liệu và hiển thị
    response.sendRedirect("home");
%>