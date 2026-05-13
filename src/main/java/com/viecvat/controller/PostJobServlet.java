package com.viecvat.controller;

import com.viecvat.dao.CongViecDAO;
import com.viecvat.model.NguoiDung;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/post-job")
public class PostJobServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("post-job.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("acc");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String tieuDe = request.getParameter("tieuDe");
            int maDanhMuc = Integer.parseInt(request.getParameter("maDanhMuc"));
            double giaTien = Double.parseDouble(request.getParameter("giaTien"));
            String diaDiem = request.getParameter("diaDiem");
            String hanChot = request.getParameter("hanChot"); 
            String moTa = request.getParameter("moTa");
            
            // [MỚI] Lấy số lượng cần tuyển (mặc định là 1 nếu lỗi)
            int soLuong = 1;
            try {
                soLuong = Integer.parseInt(request.getParameter("soLuong"));
                if (soLuong < 1) soLuong = 1;
            } catch (Exception e) { soLuong = 1; }

            String deadlineSQL = hanChot.replace("T", " ") + ":00";

            CongViecDAO dao = new CongViecDAO();
            // [MỚI] Truyền thêm tham số soLuong vào hàm insertJob
            boolean check = dao.insertJob(user.getMaNguoiDung(), maDanhMuc, tieuDe, moTa, diaDiem, giaTien, deadlineSQL, soLuong);

            if (check) {
                request.setAttribute("message", "Đăng việc thành công! (Cần tuyển: " + soLuong + " người)");
                request.setAttribute("msgType", "success");
            } else {
                request.setAttribute("message", "Lỗi hệ thống, vui lòng thử lại!");
                request.setAttribute("msgType", "danger");
            }
            request.getRequestDispatcher("post-job.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Vui lòng kiểm tra lại thông tin nhập!");
            request.setAttribute("msgType", "danger");
            request.getRequestDispatcher("post-job.jsp").forward(request, response);
        }
    }
}