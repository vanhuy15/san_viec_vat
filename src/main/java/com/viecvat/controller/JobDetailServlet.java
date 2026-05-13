package com.viecvat.controller;

import com.viecvat.dao.CongViecDAO;
import com.viecvat.dao.DanhGiaDAO;
import com.viecvat.model.CongViec;
import com.viecvat.model.DanhGia;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/job-detail")
public class JobDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String idRaw = request.getParameter("id");
            if (idRaw == null || idRaw.isEmpty()) {
                response.sendRedirect("home");
                return;
            }

            int id = Integer.parseInt(idRaw);
            
            // 1. Lấy thông tin Job
            CongViecDAO jobDao = new CongViecDAO();
            CongViec job = jobDao.getJobById(id);
            
            if (job != null) {
                request.setAttribute("job", job);
                
                // 2. [CẬP NHẬT] Chỉ lấy đánh giá nếu job đã HOÀN THÀNH
                if ("HOAN_THANH".equals(job.getTrangThai())) {
                    DanhGiaDAO reviewDao = new DanhGiaDAO();
                    // Lấy đúng review của công việc này
                    DanhGia review = reviewDao.getReviewByJobId(job.getMaCongViec());
                    request.setAttribute("review", review);
                }

                request.getRequestDispatcher("job-detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("home");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home");
        }
    }
}