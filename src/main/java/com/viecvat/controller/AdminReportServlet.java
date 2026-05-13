package com.viecvat.controller;

import com.viecvat.dao.AdminDAO;
import com.viecvat.dao.KhieuNaiDAO;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AdminReportServlet", urlPatterns = {"/admin-reports"})
public class AdminReportServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        // 1. Kiểm tra quyền Admin
        HttpSession session = request.getSession();
        NguoiDung admin = (NguoiDung) session.getAttribute("acc");
        if (admin == null || !"ADMIN".equalsIgnoreCase(admin.getVaiTro())) {
            response.sendRedirect("login.jsp");
            return;
        }

        // ==================================================================
        // PHẦN LOGIC MỚI: XỬ LÝ KHIẾU NẠI (KHI ẤN NÚT TỪ JS)
        // ==================================================================
        String action = request.getParameter("action");
        if (action != null && "resolve".equals(action)) {
            try {
                int reportId = Integer.parseInt(request.getParameter("id"));
                int type = Integer.parseInt(request.getParameter("type")); // 1: Hoàn tiền, 2: Trả lương
                String note = request.getParameter("note"); // Ghi chú của admin
                
                KhieuNaiDAO knDao = new KhieuNaiDAO();
                String result = knDao.giaiQuyetKhieuNai(reportId, type, note);
                
                if ("SUCCESS".equals(result)) {
                    session.setAttribute("msg", "Xử lý thành công!");
                    session.setAttribute("msgType", "success");
                } else {
                    session.setAttribute("msg", "Lỗi: " + result);
                    session.setAttribute("msgType", "error");
                }
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("msg", "Lỗi tham số: " + e.getMessage());
            }
            // Quay lại trang chính để load lại danh sách sau khi xử lý
            response.sendRedirect("admin-reports");
            return;
        }

        // ==================================================================
        // PHẦN CODE CŨ: LOAD DANH SÁCH BÁO CÁO
        // ==================================================================
        AdminDAO dao = new AdminDAO();
        request.setAttribute("listReports", dao.getAllReports());
        request.getRequestDispatcher("admin/reports.jsp").forward(request, response);
    }
}