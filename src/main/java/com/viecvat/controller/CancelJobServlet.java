package com.viecvat.controller;

import com.viecvat.dao.CongViecDAO;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/cancel-job")
public class CancelJobServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        NguoiDung worker = (NguoiDung) session.getAttribute("acc");
        
        if (worker == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int jobId = Integer.parseInt(request.getParameter("jobId"));
            
            CongViecDAO dao = new CongViecDAO();
            boolean success = dao.workerCancelJob(jobId, worker.getMaNguoiDung());

            if (success) {
                session.setAttribute("msg", "Đã hủy công việc và hoàn tiền lại cho chủ nhà.");
                session.setAttribute("msgType", "success");
            } else {
                session.setAttribute("msg", "Lỗi: Không thể hủy! Job không tồn tại hoặc không ở trạng thái đang làm.");
                session.setAttribute("msgType", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msg", "Lỗi hệ thống.");
            session.setAttribute("msgType", "error");
        }
        
        // Quay về trang danh sách việc đang làm của tôi
        response.sendRedirect("my-applications"); 
    }
}