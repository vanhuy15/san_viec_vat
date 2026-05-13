package com.viecvat.dao;

import com.viecvat.context.DBContext;
import com.viecvat.model.DanhGia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DanhGiaDAO {

    // 1. [HÀM MỚI] Lấy đánh giá cụ thể của một công việc (dành cho popup xem chi tiết)
    public DanhGia getReviewByJobId(int jobId) {
        String sql = "SELECT dg.*, nd.ho_ten " +
                     "FROM danhgia dg " +
                     "JOIN nguoidung nd ON dg.nguoi_danh_gia_id = nd.ma_nguoi_dung " +
                     "WHERE dg.ma_cong_viec = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                DanhGia d = new DanhGia();
                d.setMaDanhGia(rs.getInt("ma_danh_gia"));
                d.setMaCongViec(rs.getInt("ma_cong_viec"));
                d.setNguoiDanhGiaId(rs.getInt("nguoi_danh_gia_id"));
                d.setNguoiDuocDanhGiaId(rs.getInt("nguoi_duoc_danh_gia_id"));
                d.setSoSao(rs.getInt("so_sao"));
                d.setNhanXet(rs.getString("nhan_xet"));
                d.setNgayDanhGia(rs.getTimestamp("ngay_danh_gia"));
                d.setTenNguoiDanhGia(rs.getString("ho_ten")); 
                return d;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 2. [HÀM MỚI - SỬA LỖI] Lấy danh sách đánh giá của một người dùng (để hiện ở trang Profile)
    public List<DanhGia> getReviewsByUserId(int userId) {
        List<DanhGia> list = new ArrayList<>();
        // Query: Lấy đánh giá MÀ người này được nhận (nguoi_duoc_danh_gia_id)
        // Join để lấy tên người ĐÃ viết đánh giá (nguoi_danh_gia_id)
        String sql = "SELECT dg.*, nd.ho_ten " +
                     "FROM danhgia dg " +
                     "JOIN nguoidung nd ON dg.nguoi_danh_gia_id = nd.ma_nguoi_dung " +
                     "WHERE dg.nguoi_duoc_danh_gia_id = ? " +
                     "ORDER BY dg.ngay_danh_gia DESC"; 

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                DanhGia d = new DanhGia();
                d.setMaDanhGia(rs.getInt("ma_danh_gia"));
                d.setMaCongViec(rs.getInt("ma_cong_viec"));
                d.setNguoiDanhGiaId(rs.getInt("nguoi_danh_gia_id"));
                d.setNguoiDuocDanhGiaId(rs.getInt("nguoi_duoc_danh_gia_id"));
                d.setSoSao(rs.getInt("so_sao"));
                d.setNhanXet(rs.getString("nhan_xet"));
                d.setNgayDanhGia(rs.getTimestamp("ngay_danh_gia"));
                d.setTenNguoiDanhGia(rs.getString("ho_ten")); // Tên người viết review
                
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Bạn có thể thêm các hàm insert/update khác ở dưới đây nếu cần
}