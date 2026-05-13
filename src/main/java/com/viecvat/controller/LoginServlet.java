package com.viecvat.controller;

import com.viecvat.dao.NguoiDungDAO;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Nếu đã đăng nhập thì chuyển hướng luôn
        if (request.getSession().getAttribute("acc") != null) {
            NguoiDung user = (NguoiDung) request.getSession().getAttribute("acc");
            if ("ADMIN".equals(user.getVaiTro())) {
                response.sendRedirect("admin-dashboard");
            } else {
                response.sendRedirect("home");
            }
            return;
        }
        
        // Xử lý Cookie ghi nhớ mật khẩu
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("userC")) request.setAttribute("username", c.getValue());
                if (c.getName().equals("passC")) request.setAttribute("password", c.getValue());
            }
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        String remember = request.getParameter("remember");

        NguoiDungDAO dao = new NguoiDungDAO();
        NguoiDung acc = dao.checkLogin(user, pass);

        if (acc != null) {
            // ==================================================================
            // LOGIC PHÂN QUYỀN VÀ KHÓA TÀI KHOẢN (Đã sửa)
            // ==================================================================
            
            // 1. Ưu tiên ADMIN: Nếu là Admin, cho vào luôn bất kể trạng thái
            if ("ADMIN".equalsIgnoreCase(acc.getVaiTro())) {
                createSessionAndCookie(request, response, acc, user, pass, remember);
                response.sendRedirect("admin-dashboard"); // Trang Admin
                return;
            }

            // 2. Nếu là USER thường: Kiểm tra xem có bị KHÓA không
            // Lưu ý: Đảm bảo trong DB lưu đúng chuỗi 'BI_KHOA'
            if ("BI_KHOA".equalsIgnoreCase(acc.getTrangThai())) {
                request.setAttribute("mess", "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ Admin!");
                request.setAttribute("username", user); // Giữ lại username để user đỡ nhập lại
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // 3. User bình thường và trạng thái tốt -> Cho vào
            createSessionAndCookie(request, response, acc, user, pass, remember);
            response.sendRedirect("home"); // Trang chủ User
            
        } else {
            // Đăng nhập thất bại (Sai user hoặc pass)
            request.setAttribute("mess", "Tài khoản hoặc mật khẩu không đúng!");
            request.setAttribute("username", user);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    // Hàm phụ trợ để tạo session và cookie cho gọn code
    private void createSessionAndCookie(HttpServletRequest request, HttpServletResponse response, 
                                      NguoiDung acc, String user, String pass, String remember) {
        HttpSession session = request.getSession();
        session.setAttribute("acc", acc);
        
        Cookie uC = new Cookie("userC", user);
        Cookie pC = new Cookie("passC", pass);
        
        if (remember != null) {
            uC.setMaxAge(60 * 60 * 24 * 7); // 7 ngày
            pC.setMaxAge(60 * 60 * 24 * 7);
        } else {
            uC.setMaxAge(0);
            pC.setMaxAge(0);
        }
        response.addCookie(uC);
        response.addCookie(pC);
    }
}