package com.viecvat.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.viecvat.dao.CongViecDAO;
import com.viecvat.model.NguoiDung;

@WebServlet("/complete-job")
public class CompleteJobServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Chuyển sang doPost để xử lý Form gửi lên
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Xử lý tiếng Việt
        
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc"); // Đổi thành "acc" theo code của bạn

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Lấy dữ liệu từ Form (name="jobId", name="rating", name="review")
            String idStr = request.getParameter("jobId");
            String rateStr = request.getParameter("rating");
            String review = request.getParameter("review");

            if (idStr == null || rateStr == null) {
                response.sendRedirect("my-posted-jobs.jsp");
                return;
            }

            int jobId = Integer.parseInt(idStr);
            int stars = Integer.parseInt(rateStr);

            CongViecDAO dao = new CongViecDAO();
            
            // Gọi hàm mới trong DAO để xử lý tất cả (Tiền + Đánh giá)
            String result = dao.confirmAndRateWorker(jobId, user.getMaNguoiDung(), stars, review);

            if ("SUCCESS".equals(result)) {
                session.setAttribute("msg", "Đã hoàn thành đơn và thanh toán cho thợ!");
            } else {
                session.setAttribute("error", result);
            }
            
            // Quay lại trang quản lý việc
            response.sendRedirect("my-posted-jobs.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home.jsp");
        }
    }
    
    // Giữ lại doGet nếu lỡ có ai gọi nhầm, chuyển hướng về trang chủ
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("my-posted-jobs.jsp");
    }
}