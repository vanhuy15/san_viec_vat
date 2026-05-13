package com.viecvat.controller;

import com.viecvat.dao.NguoiDungDAO;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "UpdateProfileServlet", urlPatterns = {"/update-profile"})
public class UpdateProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        try {
            HttpSession session = request.getSession();
            NguoiDung currentUser = (NguoiDung) session.getAttribute("acc");
            
            if (currentUser != null) {
                String bio = request.getParameter("bio");
                String skills = request.getParameter("skills");
                int userId = currentUser.getMaNguoiDung();
                
                NguoiDungDAO dao = new NguoiDungDAO();
                boolean isUpdated = dao.updateProfile(userId, bio, skills);
                
                if (isUpdated) {
                    // Cập nhật session để giao diện đổi ngay lập tức
                    currentUser.setGioiThieu(bio);
                    currentUser.setKyNang(skills);
                    session.setAttribute("acc", currentUser);
                    session.setAttribute("succMsg", "Cập nhật hồ sơ thành công!");
                } else {
                    session.setAttribute("failedMsg", "Có lỗi xảy ra, vui lòng thử lại.");
                }
                
                // Redirect kèm ID cụ thể để chắc chắn về đúng trang
                response.sendRedirect("profile?id=" + userId);
            } else {
                // Nếu mất session thì về login
                response.sendRedirect("login.jsp");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home");
        }
    }
}