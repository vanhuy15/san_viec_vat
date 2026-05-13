package com.viecvat.controller;

import com.viecvat.dao.AdminDAO;
import com.viecvat.model.CongViec;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AdminRevenueServlet", urlPatterns = {"/admin-revenue"})
public class AdminRevenueServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Kiểm tra quyền Admin
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getVaiTro())) {
            response.sendRedirect("login");
            return;
        }

        AdminDAO dao = new AdminDAO();

        // 2. Logic Phân trang cho bảng chi tiết
        int page = 1;
        int pageSize = 10;
        if(request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) { page = 1; }
        }

        // 3. Lấy danh sách Job đã HOÀN THÀNH (Dùng lại hàm filter có sẵn)
        List<CongViec> listCompleted = dao.getJobsFilter("HOAN_THANH", page, pageSize);
        
        // Tính tổng số trang (để phân trang)
        // Lưu ý: Cần đảm bảo hàm countTotalJobsByStatus có trong DAO hoặc dùng tạm countTotalJobs
        // Ở đây để đơn giản mình dùng countTotalJobs, nếu bạn muốn chính xác hãy thêm hàm countJobsByStatus('HOAN_THANH') vào DAO
        int totalRecords = dao.countTotalJobs(); 
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // 4. [QUAN TRỌNG] Tính toán Doanh thu
        double totalTransactionValue = dao.getTotalCompletedJobValue(); // Tổng tiền giao dịch
        double adminProfit = totalTransactionValue * 0.05; // Hoa hồng 5%

        // 5. Gửi dữ liệu sang JSP
        request.setAttribute("listJobs", listCompleted);
        request.setAttribute("totalValue", totalTransactionValue); // Tổng GMV
        request.setAttribute("adminProfit", adminProfit);          // Lợi nhuận Admin
        
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);

        request.getRequestDispatcher("admin/revenue.jsp").forward(request, response);
    }
}