package com.viecvat.controller;

import com.viecvat.dao.NguoiDungDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        // 1. Lấy dữ liệu từ form
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
        String rePass = request.getParameter("re-pass");
        String name = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // [UX] Giữ lại thông tin đã nhập để nếu lỗi thì không phải nhập lại
        request.setAttribute("user", user);
        request.setAttribute("fullname", name);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);

        // 2. Validate mật khẩu
        if (!pass.equals(rePass)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        NguoiDungDAO dao = new NguoiDungDAO();
        
        // 3. Kiểm tra tên đăng nhập đã tồn tại chưa
        if (dao.checkExist(user)) {
            request.setAttribute("error", "Tên đăng nhập này đã được sử dụng!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 4. Gọi hàm Đăng ký (Hàm này trả về true/false)
        boolean isSuccess = dao.signup(user, pass, name, phone, email);
        
        if (isSuccess) {
            // Thành công -> Gửi tín hiệu 'success' về JSP để hiện SweetAlert đẹp
            request.setAttribute("status", "success");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        } else {
            // Thất bại (Do lỗi DB, ví dụ cột mat_khau quá ngắn)
            request.setAttribute("error", "Đăng ký thất bại! Lỗi hệ thống, vui lòng thử lại sau.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}