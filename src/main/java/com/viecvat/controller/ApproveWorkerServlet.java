package com.viecvat.controller;

import com.viecvat.dao.CongViecDAO;
import com.viecvat.model.CongViec;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/approve-worker")
public class ApproveWorkerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Sử dụng doPost để bảo mật và xử lý AJAX
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Thiết lập trả về dạng Text Plain (UTF-8)
        response.setContentType("text/plain; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc"); // Kiểm tra lại tên biến session "acc" hay "user"

        // 1. Check đăng nhập
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.getWriter().write("Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.");
            return;
        }

        try {
            // 2. Lấy dữ liệu từ Request (AJAX gửi lên)
            String jobIdStr = request.getParameter("id"); 
            String workerIdStr = request.getParameter("workerId");
            
            if (jobIdStr == null || workerIdStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Dữ liệu không hợp lệ.");
                return;
            }

            int jobId = Integer.parseInt(jobIdStr);
            int workerId = Integer.parseInt(workerIdStr);

            CongViecDAO dao = new CongViecDAO();
            
            // Check quyền sở hữu: Có phải chủ job không?
            CongViec job = dao.getJobById(jobId);
            if (job == null || job.getNguoiThueId() != user.getMaNguoiDung()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                response.getWriter().write("Bạn không có quyền quản lý công việc này.");
                return;
            }

            // 3. Gọi Transaction xử lý trong DAO
            // Hàm này sẽ: Trừ tiền chủ -> Update Job (gán thợ) -> Update Ứng tuyển -> Ghi Log
            String result = dao.approveWorker(jobId, workerId, user.getMaNguoiDung());

            if ("SUCCESS".equals(result)) {
                // --- Cập nhật lại Session User để hiển thị số dư mới trên Header ngay lập tức ---
                // Tính số dư mới: Cũ - Giá tiền cọc
                double newBalance = user.getSoDuVi() - job.getGiaTien();
                user.setSoDuVi(newBalance);
                session.setAttribute("acc", user); // Lưu lại vào session
                
                // Trả về mã 200 OK
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Đã giao việc thành công và trừ tiền cọc: " + (long)job.getGiaTien() + "đ");
            } else {
                // Trả về mã 400 Bad Request kèm thông báo lỗi từ DAO (VD: Số dư không đủ)
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Lỗi Server: " + e.getMessage());
        }
    }
}