package com.viecvat.controller;

import com.viecvat.dao.AdminDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AdminUserServlet", urlPatterns = {"/admin-users"})
public class AdminUserServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AdminDAO dao = new AdminDAO();
        request.setAttribute("listUsers", dao.getAllUsers());
        request.getRequestDispatcher("admin/users.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        try {
            String action = request.getParameter("action");
            int id = Integer.parseInt(request.getParameter("id"));
            AdminDAO dao = new AdminDAO();
            
            // Xử lý action lock/unlock
            if ("lock".equals(action)) {
                dao.toggleUserLock(id, "BI_KHOA"); 
            } else if ("unlock".equals(action)) {
                dao.toggleUserLock(id, "HOAT_DONG");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Redirect lại trang danh sách để load lại trạng thái mới
        response.sendRedirect("admin-users");
    }
}