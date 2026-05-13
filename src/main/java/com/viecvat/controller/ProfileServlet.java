package com.viecvat.controller;

import com.viecvat.dao.DanhGiaDAO;
import com.viecvat.dao.NguoiDungDAO;
import com.viecvat.model.DanhGia;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            // Lấy ID từ tham số URL (ví dụ: /profile?id=5)
            String idStr = request.getParameter("id");
            int userId;

            // Nếu không có id trên URL, kiểm tra xem có user đang đăng nhập không
            if (idStr == null) {
                NguoiDung currentUser = (NguoiDung) request.getSession().getAttribute("acc");
                if (currentUser != null) {
                    userId = currentUser.getMaNguoiDung();
                } else {
                    response.sendRedirect("login.jsp"); // Không tìm thấy ID và chưa đăng nhập -> Login
                    return;
                }
            } else {
                userId = Integer.parseInt(idStr);
            }

            // Khởi tạo DAO
            NguoiDungDAO userDAO = new NguoiDungDAO();
            DanhGiaDAO reviewDAO = new DanhGiaDAO();

            // 1. Lấy thông tin cá nhân (bao gồm Bio, Kỹ năng)
            NguoiDung profileUser = userDAO.getUserById(userId);

            // Nếu không tìm thấy user (ID bậy), về home
            if (profileUser == null) {
                response.sendRedirect("home");
                return;
            }

            // 2. Lấy danh sách đánh giá
            List<DanhGia> reviews = reviewDAO.getReviewsByUserId(userId);

            // 3. Tính toán thống kê
            int jobsDone = 0;
            int totalJobs = 0;
            
            try {
                jobsDone = userDAO.countCompletedJobs(userId); 
                totalJobs = userDAO.countTotalJobs(userId);
            } catch (Exception e) {
                // Nếu UserDAO chưa có hàm này, tạm thời để 0 để không lỗi code
                System.out.println("Warning: UserDAO chưa có hàm countCompletedJobs/countTotalJobs");
            }

            // Tính % hoàn thành
            int completionRate = (totalJobs > 0) ? (int) (((double) jobsDone / totalJobs) * 100) : 100;

            // 4. Đẩy dữ liệu sang JSP
            request.setAttribute("u", profileUser);          // Đối tượng người dùng chủ profile
            request.setAttribute("reviews", reviews);        // List đánh giá
            request.setAttribute("jobsDone", jobsDone);      // Số việc đã làm
            request.setAttribute("completionRate", completionRate); // Tỷ lệ hoàn thành

            request.getRequestDispatcher("profile.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // ID trên URL không phải số
            response.sendRedirect("home");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); // Trang lỗi chung nếu có
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}