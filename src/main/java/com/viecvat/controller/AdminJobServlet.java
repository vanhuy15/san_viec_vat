package com.viecvat.controller;

import com.viecvat.dao.AdminDAO;
import com.viecvat.model.CongViec;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AdminJobServlet", urlPatterns = {"/admin-jobs"})
public class AdminJobServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        AdminDAO dao = new AdminDAO();
        
        int page = 1;
        int pageSize = 10;
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) { page = 1; }
        }

        String status = request.getParameter("status");
        if (status == null || status.isEmpty()) {
            status = "ALL";
        }

        // [FIX] Đếm chính xác số lượng job theo trạng thái lọc
        int totalRecords = dao.countJobsByStatus(status);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        
        if (page > totalPages && totalPages > 0) page = 1;

        List<CongViec> list = dao.getJobsFilter(status, page, pageSize);

        request.setAttribute("listJobs", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("status", status);

        request.getRequestDispatcher("admin/jobs.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                new AdminDAO().deleteJob(id);
            } catch (Exception e) { e.printStackTrace(); }
        }
        
        String status = request.getParameter("status");
        if(status == null) status = "ALL";
        response.sendRedirect("admin-jobs?status=" + status);
    }
}