package com.viecvat.controller;

import com.viecvat.dao.CongViecDAO;
import com.viecvat.dao.UngTuyenDAO;
import com.viecvat.model.CongViec;
import com.viecvat.model.NguoiDung;
import com.viecvat.model.UngTuyen;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/manage-job")
public class ManageJobServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Kiểm tra đăng nhập
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc"); // Lưu ý: Tên biến session của bạn là "acc" hay "user"?
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 2. Lấy ID công việc từ đường dẫn (URL)
            String idParam = request.getParameter("id");
            if (idParam == null) {
                response.sendRedirect("my-posted-jobs");
                return;
            }
            int jobId = Integer.parseInt(idParam);

            // 3. Gọi các công cụ làm việc với Database (DAO)
            CongViecDAO jobDao = new CongViecDAO();
            UngTuyenDAO applyDao = new UngTuyenDAO();

            // 4. Lấy thông tin chi tiết của Công Việc
            CongViec job = jobDao.getJobById(jobId);
            
            // [Bảo mật] Kiểm tra xem người đang xem có phải là chủ nhân bài đăng không
            if (job == null || job.getNguoiThueId() != user.getMaNguoiDung()) {
                response.sendRedirect("home"); 
                return;
            }

            // ==================================================================
            // 5. [QUAN TRỌNG NHẤT] LẤY DANH SÁCH NGƯỜI ỨNG TUYỂN
            // (Đây là đoạn code bạn đang thiếu)
            // ==================================================================
            List<UngTuyen> applicants = applyDao.getApplicantsByJobId(jobId);

            // 6. Gửi dữ liệu sang trang giao diện (JSP) để hiển thị
            request.setAttribute("job", job);
            request.setAttribute("applicants", applicants); // Biến 'applicants' này sẽ giúp hiện danh sách người ứng tuyển
            
            // 7. Mở trang giao diện quản lý
            request.getRequestDispatcher("manage-job.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("my-posted-jobs");
        }
    }
}