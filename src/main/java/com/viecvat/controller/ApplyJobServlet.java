package com.viecvat.controller;

import com.viecvat.dao.CongViecDAO;
import com.viecvat.dao.UngTuyenDAO;
import com.viecvat.model.CongViec;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import java.util.Date; // [MỚI] Import Date
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/apply-job")
public class ApplyJobServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        // 1. Kiểm tra đăng nhập
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idStr = request.getParameter("id");

        try {
            if (idStr == null || idStr.isEmpty()) {
                response.sendRedirect("home");
                return;
            }
            int jobId = Integer.parseInt(idStr);
            String message = request.getParameter("message");
            if (message == null) message = "";

            // 2. Lấy thông tin Job để kiểm tra
            CongViecDAO cvDao = new CongViecDAO();
            CongViec job = cvDao.getJobById(jobId);
            
            // Check tồn tại
            if (job == null) {
                response.sendRedirect("home");
                return;
            }

            // Check: Chủ bài đăng không được tự ứng tuyển
            if (job.getNguoiThueId() == user.getMaNguoiDung()) {
                session.setAttribute("msg", "Bạn không thể tự ứng tuyển công việc của mình!");
                session.setAttribute("msgType", "error");
                response.sendRedirect("job-detail?id=" + jobId);
                return;
            }
            
            // [MỚI] CHECK HẠN CHÓT (Option 2)
            // Nếu hạn chót < thời gian hiện tại -> Chặn luôn
            Date now = new Date();
            if (job.getHanChot() != null && job.getHanChot().before(now)) {
                session.setAttribute("msg", "Rất tiếc! Công việc này đã quá hạn ứng tuyển.");
                session.setAttribute("msgType", "error");
                response.sendRedirect("job-detail?id=" + jobId);
                return; // Dừng xử lý
            }

            // 3. Gọi DAO lưu vào Database
            UngTuyenDAO dao = new UngTuyenDAO();
            boolean success = dao.insertUngTuyen(jobId, user.getMaNguoiDung(), message);

            // 4. ĐIỀU HƯỚNG
            if (success) {
                session.setAttribute("msg", "Ứng tuyển thành công! Vui lòng chờ chủ việc duyệt.");
                session.setAttribute("msgType", "success");
                response.sendRedirect("my-applications"); 
            } else {
                session.setAttribute("msg", "Bạn đã ứng tuyển công việc này rồi!");
                session.setAttribute("msgType", "error");
                response.sendRedirect("job-detail?id=" + jobId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home");
        }
    }
}