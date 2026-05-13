package com.viecvat.controller;

import com.viecvat.dao.NguoiDungDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String newPass = request.getParameter("newpass");
        String confirmPass = request.getParameter("confirmpass");

        // 1. Check mật khẩu nhập lại
        if (!newPass.equals(confirmPass)) {
            request.setAttribute("mess", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        // 2. Gọi DAO để reset
        NguoiDungDAO dao = new NguoiDungDAO();
        boolean success = dao.resetPassword(username, email, phone, newPass);

        if (success) {
            // Thành công -> Chuyển về trang login và báo thành công
            request.setAttribute("mess", "Lấy lại mật khẩu thành công! Hãy đăng nhập.");
            // Dùng cái này để hiện thông báo xanh (nếu login.jsp có support)
            // Hoặc đơn giản là forward về login
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            // Thất bại -> Báo lỗi
            request.setAttribute("mess", "Thông tin xác thực không chính xác!");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
        }
    }
}