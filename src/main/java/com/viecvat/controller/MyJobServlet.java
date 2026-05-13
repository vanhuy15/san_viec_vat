package com.viecvat.controller;

import com.viecvat.dao.CongViecDAO;
import com.viecvat.model.CongViec;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/my-posted-jobs") // Link: Việc tôi đã đăng
public class MyJobServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Kiểm tra đăng nhập
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Gọi DAO lấy danh sách việc CỦA NGƯỜI NÀY
        CongViecDAO dao = new CongViecDAO();
        List<CongViec> myJobs = dao.getJobsByOwnerId(user.getMaNguoiDung());
        
        // 3. Đẩy sang JSP
        request.setAttribute("myJobs", myJobs);
        request.getRequestDispatcher("my-posted-jobs.jsp").forward(request, response);
    }
}