package com.viecvat.controller;

import com.viecvat.dao.GiaoDichDAO;
import com.viecvat.model.GiaoDich;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AdminTransactionServlet", urlPatterns = {"/admin-transactions"})
public class AdminTransactionServlet extends HttpServlet {
    
    // XỬ LÝ GET: Hiển thị danh sách
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Kiểm tra quyền Admin
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getVaiTro())) {
            response.sendRedirect("login.jsp");
            return;
        }

        // [SỬ DỤNG GiaoDichDAO] Thay vì AdminDAO để đảm bảo lấy đủ thông tin Bank
        GiaoDichDAO dao = new GiaoDichDAO();
        
        int page = 1;
        int pageSize = 10;
        if(request.getParameter("page") != null) {
            try { page = Integer.parseInt(request.getParameter("page")); } catch (Exception e) {}
        }

        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) type = "ALL";
        
        // Tính phân trang
        int totalRecords = dao.countTransactionsByType(type);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (page > totalPages && totalPages > 0) page = 1;

        // Lấy danh sách (Có Join Bank Info)
        List<GiaoDich> list = dao.getTransactionsForAdmin(type, page, pageSize);
        
        request.setAttribute("listTrans", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize); 
        request.setAttribute("type", type);
        
        request.getRequestDispatcher("admin/transactions.jsp").forward(request, response);
    }

    // XỬ LÝ POST: Duyệt / Hủy giao dịch
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getVaiTro())) return;

        request.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        String currentType = request.getParameter("type"); // Để redirect về đúng tab
        if(currentType == null) currentType = "ALL";

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            GiaoDichDAO dao = new GiaoDichDAO();
            boolean result = false;
            
            if ("approve".equals(action)) {
                result = dao.duyetGiaoDich(id); // Logic Duyệt
            } else if ("reject".equals(action)) {
                result = dao.huyGiaoDich(id);   // Logic Hủy (Hoàn tiền nếu là Rút)
            }
            
            response.sendRedirect("admin-transactions?type=" + currentType + "&status=" + (result ? "success" : "fail"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin-transactions?status=error");
        }
    }
}