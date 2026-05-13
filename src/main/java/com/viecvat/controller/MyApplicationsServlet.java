package com.viecvat.controller;

import com.viecvat.dao.UngTuyenDAO;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/my-applications")
public class MyApplicationsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NguoiDung user = (NguoiDung) req.getSession().getAttribute("acc");
        if(user == null) { 
            resp.sendRedirect("login.jsp"); 
            return; 
        }
        
        UngTuyenDAO dao = new UngTuyenDAO();
        // Gửi danh sách đã cập nhật đầy đủ thông tin sang JSP
        req.setAttribute("myApps", dao.getMyApplications(user.getMaNguoiDung()));
        req.getRequestDispatcher("my-applications.jsp").forward(req, resp);
    }
    
    // Thêm doPost để hỗ trợ redirect
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}