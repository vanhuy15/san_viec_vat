package com.viecvat.dao;

import com.viecvat.context.DBContext;
import com.viecvat.model.NguoiDung;
import com.viecvat.utils.MD5; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NguoiDungDAO {

    // ================================================================
    // 1. ĐĂNG NHẬP (CHECK LOGIN) - [ĐÃ FIX LỖI]
    // ================================================================
    public NguoiDung checkLogin(String username, String password) {
        String passwordHash = MD5.getMd5(password);
        
        // Cần đảm bảo SELECT * để lấy được cột trang_thai
        String sql = "SELECT * FROM NguoiDung WHERE ten_dang_nhap = ? AND mat_khau = ?";
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, passwordHash); 
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                NguoiDung nd = new NguoiDung();
                nd.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
                nd.setTenDangNhap(rs.getString("ten_dang_nhap"));
                nd.setMatKhau(rs.getString("mat_khau"));
                nd.setHoTen(rs.getString("ho_ten"));
                nd.setSoDienThoai(rs.getString("so_dien_thoai"));
                nd.setEmail(rs.getString("email"));
                nd.setSoDuVi(rs.getDouble("so_du_vi"));
                nd.setDiemUyTin(rs.getFloat("diem_uy_tin"));
                nd.setVaiTro(rs.getString("vai_tro"));
                nd.setNgayTao(rs.getTimestamp("ngay_tao"));

                // [FIX QUAN TRỌNG] Lấy trạng thái để check Login
                String status = rs.getString("trang_thai");
                nd.setTrangThai((status == null || status.isEmpty()) ? "HOAT_DONG" : status);
                
                // Map thông tin ngân hàng
                try {
                    nd.setTenNganHang(rs.getString("ten_ngan_hang"));
                    nd.setSoTaiKhoanNganHang(rs.getString("so_tai_khoan_ngan_hang"));
                    nd.setTenChuTaiKhoan(rs.getString("ten_chu_tai_khoan"));
                } catch (SQLException e) {}

                // Map thông tin Profile
                try {
                    nd.setGioiThieu(rs.getString("gioi_thieu"));
                    nd.setKyNang(rs.getString("ky_nang"));
                } catch (SQLException e) {}
                
                return nd;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================================================================
    // 2. ĐĂNG KÝ (SIGNUP)
    // ================================================================
    public boolean signup(String user, String pass, String name, String phone, String email) {
        String passwordHash = MD5.getMd5(pass);
        
        String sql = "INSERT INTO NguoiDung (ten_dang_nhap, mat_khau, ho_ten, so_dien_thoai, email, vai_tro, so_du_vi, diem_uy_tin, ngay_tao, trang_thai) " +
                     "VALUES (?, ?, ?, ?, ?, 'USER', 0, 0, NOW(), 'HOAT_DONG')";
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, passwordHash);
            ps.setString(3, name);
            ps.setString(4, phone);
            ps.setString(5, email);
            
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("-> LỖI INSERT DB: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ================================================================
    // 3. KIỂM TRA TỒN TẠI (CHECK EXIST)
    // ================================================================
    public boolean checkExist(String username) {
        String sql = "SELECT * FROM NguoiDung WHERE ten_dang_nhap = ?";
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return true;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ================================================================
    // 4. QUÊN MẬT KHẨU (RESET PASSWORD)
    // ================================================================
    public boolean resetPassword(String username, String email, String phone, String newPassword) {
        String newPassHash = MD5.getMd5(newPassword);
        
        String sql = "UPDATE NguoiDung SET mat_khau = ? WHERE ten_dang_nhap = ? AND email = ? AND so_dien_thoai = ?";
        
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newPassHash);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, phone);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ================================================================
    // 5. CẬP NHẬT NGÂN HÀNG (UPDATE BANK)
    // ================================================================
    public void updateBankInfo(int userId, String bankName, String bankAcc, String accHolder) {
        String sql = "UPDATE NguoiDung SET ten_ngan_hang = ?, so_tai_khoan_ngan_hang = ?, ten_chu_tai_khoan = ? WHERE ma_nguoi_dung = ?";
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, bankName);
            ps.setString(2, bankAcc);
            ps.setString(3, accHolder);
            ps.setInt(4, userId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ================================================================
    // 6. LẤY INFO ADMIN
    // ================================================================
    public NguoiDung getAdminInfo() {
        String sql = "SELECT * FROM NguoiDung WHERE vai_tro = 'ADMIN' LIMIT 1";
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NguoiDung admin = new NguoiDung();
                admin.setTenNganHang(rs.getString("ten_ngan_hang"));
                admin.setSoTaiKhoanNganHang(rs.getString("so_tai_khoan_ngan_hang"));
                admin.setTenChuTaiKhoan(rs.getString("ten_chu_tai_khoan"));
                return admin;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // ================================================================
    // 7. CẬP NHẬT SỐ DƯ VÍ
    // ================================================================
    public boolean updateSoDuVi(int userId, double newBalance) {
        String sql = "UPDATE NguoiDung SET so_du_vi = ? WHERE ma_nguoi_dung = ?";
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, newBalance);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ================================================================
    // 8. LẤY SỐ DƯ HIỆN TẠI TỪ DB
    // ================================================================
    public double getSoDuHienTai(int userId) {
        String sql = "SELECT so_du_vi FROM NguoiDung WHERE ma_nguoi_dung = ?";
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("so_du_vi");
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    } 

    // ================================================================
    // 9. LẤY USER THEO ID (FULL INFO CHO PROFILE)
    // ================================================================
    public NguoiDung getUserById(int userId) {
        String sql = "SELECT * FROM NguoiDung WHERE ma_nguoi_dung = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NguoiDung n = new NguoiDung();
                n.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
                n.setTenDangNhap(rs.getString("ten_dang_nhap"));
                n.setHoTen(rs.getString("ho_ten"));
                n.setSoDienThoai(rs.getString("so_dien_thoai"));
                n.setEmail(rs.getString("email"));
                n.setSoDuVi(rs.getDouble("so_du_vi"));
                n.setDiemUyTin(rs.getFloat("diem_uy_tin"));
                n.setVaiTro(rs.getString("vai_tro"));
                
                // [FIX] Cũng lấy luôn trạng thái ở đây cho chắc
                String status = rs.getString("trang_thai");
                n.setTrangThai((status == null || status.isEmpty()) ? "HOAT_DONG" : status);
                
                try {
                    n.setTenNganHang(rs.getString("ten_ngan_hang"));
                    n.setSoTaiKhoanNganHang(rs.getString("so_tai_khoan_ngan_hang"));
                    n.setTenChuTaiKhoan(rs.getString("ten_chu_tai_khoan"));
                } catch(Exception e) {}
                
                try {
                    n.setGioiThieu(rs.getString("gioi_thieu"));
                    n.setKyNang(rs.getString("ky_nang"));
                } catch(Exception e) {}
                
                return n;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // ================================================================
    // 10. THỐNG KÊ (CHO PROFILE)
    // ================================================================
    public int countCompletedJobs(int workerId) {
        String sql = "SELECT COUNT(*) FROM CongViec WHERE nguoi_lam_id = ? AND trang_thai = 'HOAN_THANH'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countTotalJobs(int workerId) {
        String sql = "SELECT COUNT(*) FROM CongViec WHERE nguoi_lam_id = ? AND trang_thai IN ('HOAN_THANH', 'DA_HUY')";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    } 

    // ================================================================
    // 11. CẬP NHẬT PROFILE (BIO & SKILLS)
    // ================================================================
    public boolean updateProfile(int userId, String bio, String skills) {
        String sql = "UPDATE NguoiDung SET gioi_thieu = ?, ky_nang = ? WHERE ma_nguoi_dung = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bio);
            ps.setString(2, skills);
            ps.setInt(3, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}