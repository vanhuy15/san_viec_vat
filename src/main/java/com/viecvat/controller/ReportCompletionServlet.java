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

@WebServlet("/report-completion")
public class ReportCompletionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Cấu hình tiếng Việt
        request.setCharacterEncoding("UTF-8");
        
        // 2. Kiểm tra đăng nhập
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc"); 

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 3. Lấy ID công việc từ form ẩn gửi sang
            String jobIdStr = request.getParameter("jobId");
            int jobId = Integer.parseInt(jobIdStr);
            
            // 4. Gọi DAO cập nhật trạng thái -> CHO_XAC_NHAN
            CongViecDAO dao = new CongViecDAO();
            boolean success = dao.workerReportCompletion(jobId, user.getMaNguoiDung());

            // 5. Kiểm tra kết quả và lưu thông báo vào Session
            if (success) {
                session.setAttribute("msg", "Đã báo cáo hoàn thành! Vui lòng đợi chủ việc xác nhận.");
                session.setAttribute("msgType", "success");
            } else {
                session.setAttribute("msg", "Lỗi: Không thể cập nhật trạng thái (Có thể công việc đã bị hủy hoặc không đúng trạng thái).");
                session.setAttribute("msgType", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msg", "Lỗi hệ thống: " + e.getMessage());
            session.setAttribute("msgType", "error");
        }
        
        // 6. Chuyển hướng quay lại trang danh sách việc làm
        response.sendRedirect("my-applications");
    }
}