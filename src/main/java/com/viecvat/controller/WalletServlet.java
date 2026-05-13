package com.viecvat.controller;

import com.viecvat.dao.GiaoDichDAO;
import com.viecvat.dao.NguoiDungDAO;
import com.viecvat.model.GiaoDich;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/wallet")
public class WalletServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public static String removeAccent(String s) {
        if (s == null) return "";
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D').toUpperCase();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc");
        if (user == null) { response.sendRedirect("login.jsp"); return; }
        
        // --- CẬP NHẬT SESSION (để số dư luôn mới nhất) ---
        NguoiDungDAO userDAO = new NguoiDungDAO();
        NguoiDung latestUser = userDAO.getUserById(user.getMaNguoiDung());
        if (latestUser != null) {
            session.setAttribute("acc", latestUser);
            user = latestUser;
        }

        // --- XỬ LÝ PHÂN TRANG & BỘ LỌC ---
        GiaoDichDAO dao = new GiaoDichDAO();
        
        // 1. Lấy tham số
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) type = "ALL"; // Mặc định lấy tất cả
        
        int page = 1;
        int pageSize = 10; // Số dòng trên 1 trang
        if (request.getParameter("page") != null) {
            try { page = Integer.parseInt(request.getParameter("page")); } catch (Exception e) {}
        }

        // 2. Tính toán phân trang
        int totalRecords = dao.countHistoryFilter(user.getMaNguoiDung(), type);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (page > totalPages && totalPages > 0) page = totalPages; 

        // 3. Gọi DAO lấy list
        List<GiaoDich> history = dao.getHistoryFilter(user.getMaNguoiDung(), type, page, pageSize);
        
        // 4. Lấy thông tin Admin (ĐỂ HIỂN THỊ BANK)
        NguoiDung adminInfo = userDAO.getAdminInfo();
        
        // 5. Đẩy dữ liệu ra JSP
        request.setAttribute("adminBank", adminInfo); // Thông tin này sẽ hiện lên thẻ Card màu xanh
        request.setAttribute("history", history);
        
        // Các thuộc tính cho phân trang/bộ lọc
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentType", type);

        request.getRequestDispatcher("wallet.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc");
        if (user == null) { response.sendRedirect("login.jsp"); return; }

        String action = request.getParameter("action");
        GiaoDichDAO dao = new GiaoDichDAO();
        String message = "";
        String msgType = "info";

        try {
            if ("confirm_deposit".equals(action)) {
                double amount = Double.parseDouble(request.getParameter("amount"));
                boolean check = dao.taoYeuCauNapTien(user.getMaNguoiDung(), amount, "NAPTIEN " + user.getTenDangNhap());
                message = check ? "Đã gửi yêu cầu nạp tiền! Vui lòng chờ Admin duyệt." : "Lỗi hệ thống!";
                msgType = check ? "success" : "danger";
            }
            else if ("request_withdraw".equals(action)) {
                // Nhận số tiền từ request
                double amount = Double.parseDouble(request.getParameter("amount")); 
                // Gọi DAO thực hiện trừ tiền và ghi log
                String res = dao.taoYeuCauRutTien(user.getMaNguoiDung(), amount, user.getSoDuVi());
                
                // Thiết lập thông báo hiển thị cho User
                message = "SUCCESS".equals(res) ? "Đã gửi yêu cầu rút tiền!" : "Thất bại: " + res;
                msgType = "SUCCESS".equals(res) ? "success" : "danger";
            }
            else if ("update_bank".equals(action)) {
                String bankName = request.getParameter("bankName");
                String bankAcc = request.getParameter("bankAcc");
                String accHolder = removeAccent(request.getParameter("accHolder"));
                
                NguoiDungDAO ndDao = new NguoiDungDAO();
                ndDao.updateBankInfo(user.getMaNguoiDung(), bankName, bankAcc, accHolder);
                
                user.setTenNganHang(bankName);
                user.setSoTaiKhoanNganHang(bankAcc);
                user.setTenChuTaiKhoan(accHolder);
                session.setAttribute("acc", user);
                
                message = "Cập nhật thông tin ngân hàng thành công!";
                msgType = "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Lỗi xử lý: " + e.getMessage();
            msgType = "danger";
        }

        request.setAttribute("message", message);
        request.setAttribute("msgType", msgType);
        doGet(request, response);
    }
}