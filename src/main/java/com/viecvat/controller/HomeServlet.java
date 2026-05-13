package com.viecvat.controller;

import com.viecvat.dao.CongViecDAO;
import com.viecvat.dao.DanhMucDAO;
import com.viecvat.model.CongViec;
import com.viecvat.model.DanhMuc;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        // 1. LẤY THAM SỐ TỪ URL (Để phục vụ Tìm kiếm & Lọc)
        String keyword = request.getParameter("q");          // Từ khóa
        String cateParam = request.getParameter("cid");      // ID Danh mục
        String pageParam = request.getParameter("page");     // Trang hiện tại
        
        int cateId = (cateParam != null && !cateParam.isEmpty()) ? Integer.parseInt(cateParam) : 0;
        int page = (pageParam != null && !pageParam.isEmpty()) ? Integer.parseInt(pageParam) : 1;
        int pageSize = 6; // Hiển thị 6 việc / trang (đúng yêu cầu của bạn)

        // 2. GỌI DAO LẤY DỮ LIỆU
        CongViecDAO jobDAO = new CongViecDAO();
        
        // Tính toán phân trang
        int totalJobs = jobDAO.countJobs(keyword, cateId);
        int totalPage = (int) Math.ceil((double) totalJobs / pageSize);
        
        // Lấy danh sách việc (Kết hợp Tìm kiếm + Lọc + Phân trang)
        List<CongViec> listJobs = jobDAO.searchAndFilterJobs(keyword, cateId, page, pageSize);

        // Lấy danh mục để hiển thị cột bên trái
        DanhMucDAO cateDAO = new DanhMucDAO();
        List<DanhMuc> listCate = cateDAO.getAll();

        // 3. GỬI DỮ LIỆU SANG JSP
        request.setAttribute("listJobs", listJobs);
        request.setAttribute("categories", listCate);
        
        // Gửi các biến này để JSP biết đang ở trang nào, tìm từ khóa gì
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentPage", page);
        request.setAttribute("currentKeyword", keyword);
        request.setAttribute("currentCateId", cateId);

        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}