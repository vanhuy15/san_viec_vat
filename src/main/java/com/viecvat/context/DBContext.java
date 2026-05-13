package com.viecvat.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    // 1. Cấu hình kết nối-ho tro VIETNAMESE
    private static final String DB_URL = "jdbc:mysql://localhost:3306/san_viec_vat?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASS = "";

    // 2. Hàm lấy kết nối (Static để gọi trực tiếp từ DAO)
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Khai báo Driver cho MySQL 8.x (com.mysql.cj.jdbc.Driver)
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // Kết nối thành công thì trả về đối tượng conn
        } catch (ClassNotFoundException e) {
            System.out.println("Lỗi: Không tìm thấy thư viện MySQL ConnectorJ!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Lỗi: Sai thông tin kết nối (URL, User hoặc Pass)!");
            e.printStackTrace();
        }
        return conn;
    }
    
    // 3. Hàm Main để chạy thử (Test) kết nối
    public static void main(String[] args) {
        System.out.println("Đang thử kết nối đến Database...");
        Connection conn = DBContext.getConnection();
        
        if (conn != null) {
            System.out.println("KẾT NỐI THÀNH CÔNG!");
            System.out.println("-> Database: san_viec_vat");
        } else {
            System.out.println("KẾT NỐI THẤT BẠI! Vui lòng kiểm tra lại XAMPP hoặc MySQL Workbench.");
        }
    }
}