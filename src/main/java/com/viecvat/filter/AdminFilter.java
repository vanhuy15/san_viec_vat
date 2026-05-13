package com.viecvat.filter;

import com.viecvat.model.NguoiDung;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Chặn tất cả các truy cập vào thư mục /admin/*
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        NguoiDung user = (NguoiDung) session.getAttribute("acc");

        // Logic: Nếu chưa đăng nhập HOẶC vai trò không phải ADMIN -> Đá về trang chủ
        if (user == null || !"ADMIN".equals(user.getVaiTro())) {
            res.sendRedirect(req.getContextPath() + "/home");
        } else {
            // Nếu là Admin -> Cho qua
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig fConfig) throws ServletException {}
    public void destroy() {}
}