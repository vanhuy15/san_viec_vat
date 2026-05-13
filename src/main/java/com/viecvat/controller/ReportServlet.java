package com.viecvat.controller;

import com.viecvat.dao.KhieuNaiDAO;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Dòng này rất quan trọng, nó định nghĩa đường dẫn trùng với action="report-job" bên JSP
@WebServlet("/report-job")
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // 1. Kiểm tra đăng nhập
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc"); // Đảm bảo session key là "acc"

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 2. Lấy dữ liệu từ form gửi lên
            String jobIdStr = request.getParameter("jobId");
            String lyDo = request.getParameter("reason"); // name="reason" bên JSP

            if (jobIdStr != null && lyDo != null) {
                int maCongViec = Integer.parseInt(jobIdStr);

                // 3. Gọi DAO để lưu khiếu nại
                KhieuNaiDAO dao = new KhieuNaiDAO();
                boolean isSuccess = dao.taoKhieuNai(maCongViec, user.getMaNguoiDung(), lyDo);

                if (isSuccess) {
                    session.setAttribute("msg", "Đã gửi khiếu nại thành công. Admin sẽ sớm xử lý.");
                } else {
                    session.setAttribute("error", "Gửi khiếu nại thất bại! Vui lòng thử lại.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }
        
        // 4. Quay về trang danh sách việc
        response.sendRedirect("my-posted-jobs");
    }
}