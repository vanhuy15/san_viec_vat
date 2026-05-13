package com.viecvat.controller;

import com.viecvat.dao.AdminDAO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin-dashboard"})
public class AdminDashboardServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        AdminDAO dao = new AdminDAO();
        
        // 1. Lấy số liệu tổng quan (Cards)
        request.setAttribute("cntUsers", dao.countTotalUsers());
        request.setAttribute("cntJobs", dao.countTotalJobs());
        request.setAttribute("cntTrans", dao.countPendingTransactions());
        request.setAttribute("cntReports", dao.countPendingReports());

        // 2. XỬ LÝ DỮ LIỆU BIỂU ĐỒ (7 NGÀY GẦN NHẤT)
        // Lấy Map dữ liệu từ DAO
        Map<String, Integer> mapNew = dao.getNewJobsByDate(7);
        Map<String, Integer> mapDone = dao.getCompletedJobsByDate(7);
        
        List<String> labels = new ArrayList<>();
        List<Integer> dataNew = new ArrayList<>();
        List<Integer> dataDone = new ArrayList<>();

        // Vòng lặp 7 ngày từ quá khứ đến hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -6); // Lùi lại 6 ngày trước

        for (int i = 0; i < 7; i++) {
            String keyDate = sdf.format(cal.getTime());
            
            // Thêm vào Label (có dấu nháy đơn để JS đọc được)
            labels.add("'" + keyDate + "'"); 
            
            // Lấy dữ liệu từ Map, nếu không có thì bằng 0
            dataNew.add(mapNew.getOrDefault(keyDate, 0));
            dataDone.add(mapDone.getOrDefault(keyDate, 0));
            
            cal.add(Calendar.DAY_OF_YEAR, 1); // Tăng thêm 1 ngày
        }

        // Gửi dữ liệu dạng String List: [1, 2, 0, 5...]
        request.setAttribute("chartLabels", labels.toString());
        request.setAttribute("chartDataNew", dataNew.toString());
        request.setAttribute("chartDataDone", dataDone.toString());

        request.getRequestDispatcher("admin/dashboard.jsp").forward(request, response);
    }
}