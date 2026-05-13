package com.viecvat.dao;

import com.viecvat.context.DBContext;
import com.viecvat.model.*;
import java.sql.*;
import java.util.*;

public class AdminDAO {

    // ==========================================================
    // --- 1. THỐNG KÊ DASHBOARD ---
    // ==========================================================
    public int countTotalUsers() { return count("SELECT COUNT(*) FROM nguoidung WHERE vai_tro = 'USER'"); }
    
    public int countTotalJobs() { return count("SELECT COUNT(*) FROM congviec"); }
    
    public int countPendingTransactions() { return count("SELECT COUNT(*) FROM giaodich WHERE trang_thai = 'CHO_DUYET'"); }
    public int countPendingReports() { return count("SELECT COUNT(*) FROM khieunai WHERE trang_thai_xu_ly = 'CHO_XU_LY'"); }

    // ==========================================================
    // --- 2. BIỂU ĐỒ (CHART DATA) ---
    // ==========================================================
    public Map<String, Integer> getNewJobsByDate(int days) {
        String sql = "SELECT DATE_FORMAT(ngay_dang, '%d/%m') as ngay, COUNT(*) as sl FROM congviec WHERE ngay_dang >= DATE(NOW()) - INTERVAL ? DAY GROUP BY DATE_FORMAT(ngay_dang, '%d/%m')";
        return getCountMap(sql, days);
    }
    public Map<String, Integer> getCompletedJobsByDate(int days) {
        String sql = "SELECT DATE_FORMAT(ngay_dang, '%d/%m') as ngay, COUNT(*) as sl FROM congviec WHERE trang_thai = 'HOAN_THANH' AND ngay_dang >= DATE(NOW()) - INTERVAL ? DAY GROUP BY DATE_FORMAT(ngay_dang, '%d/%m')";
        return getCountMap(sql, days);
    }

    // ==========================================================
    // --- 3. QUẢN LÝ GIAO DỊCH (FILTER & PAGING) ---
    // ==========================================================
    public int countAllTransactions() { return count("SELECT COUNT(*) FROM giaodich"); }

    public int countTransactionsByType(String type) {
        String sql = "SELECT COUNT(*) FROM giaodich";
        if (type != null && !type.isEmpty() && !type.equals("ALL")) {
            sql += " WHERE loai_giao_dich = '" + type + "'";
        }
        return count(sql);
    }

    public List<GiaoDich> getTransactionsFilter(String type, int page, int pageSize) {
        List<GiaoDich> list = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder("SELECT gd.*, nd.ho_ten, nd.ten_ngan_hang, nd.so_tai_khoan_ngan_hang, nd.ten_chu_tai_khoan " + 
                                              "FROM giaodich gd JOIN nguoidung nd ON gd.ma_nguoi_dung = nd.ma_nguoi_dung WHERE 1=1");
        
        if (type != null && !type.isEmpty() && !type.equals("ALL")) {
            sql.append(" AND gd.loai_giao_dich = ?");
        }
        sql.append(" ORDER BY gd.ngay_tao DESC LIMIT ? OFFSET ?");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int index = 1;
            if (type != null && !type.isEmpty() && !type.equals("ALL")) {
                ps.setString(index++, type);
            }
            ps.setInt(index++, pageSize);
            ps.setInt(index++, (page - 1) * pageSize);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GiaoDich gd = new GiaoDich();
                gd.setMaGiaoDich(rs.getInt("ma_giao_dich"));
                gd.setLoaiGiaoDich(rs.getString("loai_giao_dich"));
                gd.setSoTien(rs.getDouble("so_tien"));
                gd.setTrangThai(rs.getString("trang_thai"));
                gd.setNgayTao(rs.getTimestamp("ngay_tao"));
                gd.setMoTa(rs.getString("ho_ten")); 
                
                gd.setTenNganHang(rs.getString("ten_ngan_hang") != null ? rs.getString("ten_ngan_hang") : ""); 
                gd.setSoTaiKhoanNganHang(rs.getString("so_tai_khoan_ngan_hang") != null ? rs.getString("so_tai_khoan_ngan_hang") : "");
                gd.setTenChuTaiKhoan(rs.getString("ten_chu_tai_khoan") != null ? rs.getString("ten_chu_tai_khoan") : "");
                
                list.add(gd);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ==========================================================
    // --- 4. QUẢN LÝ NGƯỜI DÙNG (USER MANAGEMENT) ---
    // ==========================================================
    public List<NguoiDung> getAllUsers() {
        List<NguoiDung> list = new ArrayList<>();
        // Lấy tất cả user trừ ADMIN ra
        String sql = "SELECT * FROM nguoidung WHERE vai_tro != 'ADMIN' ORDER BY ngay_tao DESC";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NguoiDung n = new NguoiDung();
                n.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
                n.setTenDangNhap(rs.getString("ten_dang_nhap"));
                n.setHoTen(rs.getString("ho_ten"));
                n.setEmail(rs.getString("email"));
                n.setSoDienThoai(rs.getString("so_dien_thoai"));
                n.setVaiTro(rs.getString("vai_tro"));
                n.setSoDuVi(rs.getDouble("so_du_vi"));
                
                // Lấy trạng thái, xử lý null
                String status = rs.getString("trang_thai");
                n.setTrangThai((status == null || status.isEmpty()) ? "HOAT_DONG" : status);
                
                list.add(n);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean toggleUserLock(int userId, String status) {
        String sql = "UPDATE nguoidung SET trang_thai = ? WHERE ma_nguoi_dung = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ==========================================================
    // --- 5. QUẢN LÝ CÔNG VIỆC (JOB MANAGEMENT) ---
    // ==========================================================

    public int countJobsByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM congviec";
        if (status != null && !status.isEmpty() && !status.equals("ALL")) {
            sql += " WHERE trang_thai = '" + status + "'";
        }
        return count(sql);
    }

    public List<CongViec> getJobsFilter(String status, int page, int pageSize) {
        List<CongViec> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT cv.*, nd.ho_ten FROM congviec cv JOIN nguoidung nd ON cv.nguoi_thue_id = nd.ma_nguoi_dung WHERE 1=1");
        if (status != null && !status.isEmpty() && !status.equals("ALL")) {
            sql.append(" AND cv.trang_thai = ?");
        }
        sql.append(" ORDER BY cv.ngay_dang DESC LIMIT ? OFFSET ?");
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int index = 1;
            if (status != null && !status.isEmpty() && !status.equals("ALL")) {
                ps.setString(index++, status);
            }
            ps.setInt(index++, pageSize);
            ps.setInt(index++, (page - 1) * pageSize);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CongViec cv = new CongViec();
                cv.setMaCongViec(rs.getInt("ma_cong_viec"));
                cv.setTieuDe(rs.getString("tieu_de"));
                cv.setGiaTien(rs.getDouble("gia_tien")); 
                cv.setTrangThai(rs.getString("trang_thai"));
                cv.setNgayDang(rs.getTimestamp("ngay_dang"));
                cv.setMoTa(rs.getString("ho_ten"));
                list.add(cv);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean deleteJob(int jobId) {
        String sql = "DELETE FROM congviec WHERE ma_cong_viec = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ==========================================================
    // --- 6. XỬ LÝ KHIẾU NẠI (REPORTS) ---
    // ==========================================================
    public List<KhieuNai> getAllReports() {
        List<KhieuNai> list = new ArrayList<>();
        String sql = "SELECT kn.*, nd.ho_ten, cv.tieu_de FROM khieunai kn " +
                     "JOIN nguoidung nd ON kn.nguoi_khieu_nai_id = nd.ma_nguoi_dung " +
                     "JOIN congviec cv ON kn.ma_cong_viec = cv.ma_cong_viec " +
                     "ORDER BY kn.ngay_tao DESC";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhieuNai kn = new KhieuNai();
                kn.setMaKhieuNai(rs.getInt("ma_khieu_nai"));
                kn.setLyDo(rs.getString("ly_do"));
                kn.setTrangThaiXuLy(rs.getString("trang_thai_xu_ly"));
                kn.setKetQuaXuLy(rs.getString("ket_qua_xu_ly"));
                kn.setNgayTao(rs.getTimestamp("ngay_tao"));
                kn.setGhiChu(rs.getString("ho_ten") + " (Job: " + rs.getString("tieu_de") + ")"); 
                list.add(kn);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean resolveReport(int reportId, int action, String adminNote) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); 

            // Lấy thông tin khiếu nại & công việc
            String sqlGet = "SELECT kn.ma_cong_viec, cv.gia_tien, cv.nguoi_lam_id, cv.nguoi_thue_id " +
                            "FROM khieunai kn JOIN congviec cv ON kn.ma_cong_viec = cv.ma_cong_viec " +
                            "WHERE kn.ma_khieu_nai = ?";
            PreparedStatement psGet = conn.prepareStatement(sqlGet);
            psGet.setInt(1, reportId);
            ResultSet rs = psGet.executeQuery();
            
            if (rs.next()) {
                int jobId = rs.getInt("ma_cong_viec");
                double amount = rs.getDouble("gia_tien");
                int nguoiLamId = rs.getInt("nguoi_lam_id");
                int nguoiThueId = rs.getInt("nguoi_thue_id");

                if (action == 1) { 
                    // Đồng ý khiếu nại: Hoàn tiền về ví NGƯỜI THUÊ
                    String sqlRefund = "UPDATE nguoidung SET so_du_vi = so_du_vi + ? WHERE ma_nguoi_dung = ?";
                    PreparedStatement psRefund = conn.prepareStatement(sqlRefund);
                    psRefund.setDouble(1, amount);
                    psRefund.setInt(2, nguoiThueId);
                    psRefund.executeUpdate();
                    
                    conn.createStatement().executeUpdate("UPDATE congviec SET trang_thai = 'DA_HUY' WHERE ma_cong_viec = " + jobId);
                    
                } else if (action == 2) {
                    // Từ chối khiếu nại: Tiền chuyển cho NGƯỜI LÀM
                    String sqlPay = "UPDATE nguoidung SET so_du_vi = so_du_vi + ? WHERE ma_nguoi_dung = ?";
                    PreparedStatement psPay = conn.prepareStatement(sqlPay);
                    psPay.setDouble(1, amount);
                    psPay.setInt(2, nguoiLamId);
                    psPay.executeUpdate();

                    conn.createStatement().executeUpdate("UPDATE congviec SET trang_thai = 'HOAN_THANH' WHERE ma_cong_viec = " + jobId);
                }

                String sqlUpdateReport = "UPDATE khieunai SET trang_thai_xu_ly = 'DA_GIAI_QUYET', ket_qua_xu_ly = ? WHERE ma_khieu_nai = ?";
                PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateReport);
                psUpdate.setString(1, adminNote);
                psUpdate.setInt(2, reportId);
                psUpdate.executeUpdate();

                conn.commit(); 
                return true;
            }
        } catch (Exception e) {
            try { if(conn != null) conn.rollback(); } catch(Exception ex){}
            e.printStackTrace();
        } finally {
            try { if(conn != null) conn.close(); } catch(Exception ex){}
        }
        return false;
    }

    // ==========================================================
    // --- 7. TÍNH TOÁN DOANH THU (REVENUE) ---
    // ==========================================================
    public double getTotalCompletedJobValue() {
        String sql = "SELECT SUM(gia_tien) FROM congviec WHERE trang_thai = 'HOAN_THANH'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ==========================================================
    // --- 8. HÀM HỖ TRỢ CHUNG (HELPER) ---
    // ==========================================================
    private int count(String sql) {
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {}
        return 0;
    }
    
    private Map<String, Integer> getCountMap(String sql, int days) {
        Map<String, Integer> map = new HashMap<>();
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, days);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) map.put(rs.getString("ngay"), rs.getInt("sl"));
        } catch (Exception e) {}
        return map;
    }
}