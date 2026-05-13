package com.viecvat.dao;

import com.viecvat.context.DBContext;
import com.viecvat.model.UngTuyen;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UngTuyenDAO {

    // 1. Nộp đơn ứng tuyển (Giữ nguyên)
    public boolean insertUngTuyen(int jobId, int userId, String message) {
        if(checkApplied(jobId, userId)) return false; 
        
        String sql = "INSERT INTO UngTuyen (ma_cong_viec, nguoi_ung_tuyen_id, loi_nhan, trang_thai, thoi_gian) VALUES (?, ?, ?, 'CHO_DUYET', NOW())";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            ps.setInt(2, userId);
            ps.setString(3, message);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. Kiểm tra (Giữ nguyên)
    public boolean checkApplied(int jobId, int userId) {
        String sql = "SELECT * FROM UngTuyen WHERE ma_cong_viec = ? AND nguoi_ung_tuyen_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            ps.setInt(2, userId);
            return ps.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Lấy danh sách ứng viên (Giữ nguyên)
    public List<UngTuyen> getApplicantsByJobId(int jobId) {
        List<UngTuyen> list = new ArrayList<>();
        String sql = "SELECT ut.*, nd.ho_ten, nd.so_dien_thoai, nd.diem_uy_tin " +
                     "FROM UngTuyen ut " +
                     "JOIN NguoiDung nd ON ut.nguoi_ung_tuyen_id = nd.ma_nguoi_dung " +
                     "WHERE ut.ma_cong_viec = ? " +
                     "ORDER BY ut.thoi_gian DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UngTuyen ut = new UngTuyen();
                ut.setMaUngTuyen(rs.getInt("ma_ung_tuyen"));
                ut.setMaCongViec(rs.getInt("ma_cong_viec"));
                ut.setNguoiUngTuyenId(rs.getInt("nguoi_ung_tuyen_id"));
                ut.setLoiNhan(rs.getString("loi_nhan"));
                ut.setTrangThai(rs.getString("trang_thai"));
                ut.setThoiGian(rs.getTimestamp("thoi_gian"));
                ut.setTenNguoiUngTuyen(rs.getString("ho_ten"));
                ut.setSdtNguoiUngTuyen(rs.getString("so_dien_thoai"));
                try { ut.setDiemUyTin(rs.getFloat("diem_uy_tin")); } catch (Exception e) {}
                list.add(ut);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 4. [QUAN TRỌNG] Lấy danh sách việc TÔI ĐÃ ỨNG TUYỂN
    public List<UngTuyen> getMyApplications(int userId) {
        List<UngTuyen> list = new ArrayList<>();
        // Sửa SQL để lấy thêm cv.gia_tien
        String sql = "SELECT ut.*, cv.tieu_de, cv.gia_tien, cv.trang_thai AS job_status, nd.ho_ten AS ten_chu_viec " +
                     "FROM UngTuyen ut " +
                     "JOIN CongViec cv ON ut.ma_cong_viec = cv.ma_cong_viec " +
                     "JOIN NguoiDung nd ON cv.nguoi_thue_id = nd.ma_nguoi_dung " +
                     "WHERE ut.nguoi_ung_tuyen_id = ? " +
                     "ORDER BY ut.thoi_gian DESC";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UngTuyen ut = new UngTuyen();
                ut.setMaUngTuyen(rs.getInt("ma_ung_tuyen"));
                ut.setMaCongViec(rs.getInt("ma_cong_viec"));
                ut.setTenCongViec(rs.getString("tieu_de"));     
                ut.setTenNguoiThue(rs.getString("ten_chu_viec")); 
                ut.setLoiNhan(rs.getString("loi_nhan"));
                ut.setTrangThai(rs.getString("trang_thai")); // Trạng thái đơn (DA_CHON)
                ut.setThoiGian(rs.getTimestamp("thoi_gian"));
                
                // [QUAN TRỌNG] Set trạng thái công việc (DA_GIAO) để JSP check hiển thị nút
                ut.setTrangThaiCongViec(rs.getString("job_status"));
                try { ut.setGiaTien(rs.getDouble("gia_tien")); } catch (Exception e) {}
                
                list.add(ut);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}